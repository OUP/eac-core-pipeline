package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.Checkbox;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.ElementCountryRestriction;
import com.oup.eac.domain.HiddenField;
import com.oup.eac.domain.Label;
import com.oup.eac.domain.MultiSelect;
import com.oup.eac.domain.OptionsTag;
import com.oup.eac.domain.PasswordField;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Radio;
import com.oup.eac.domain.Select;
import com.oup.eac.domain.Tag;
import com.oup.eac.domain.Tag.TagType;
import com.oup.eac.domain.TagOption;
import com.oup.eac.domain.TextField;
import com.oup.eac.domain.UrlLink;

public class ElementBean implements Serializable {

	private List<Element> elements;
	private List<Question> questions;
	private Element selectedElement;
	private boolean newElement;
	private TagType tagType;
	private List<LocaleBean> existingRestrictions = new ArrayList<LocaleBean>();
	private List<LocaleBean> newRestrictions = new ArrayList<LocaleBean>();
	private String restrictionsToRemove;
	private String url;
	private boolean newWindow;
	private List<TagOption> newOptions = new ArrayList<TagOption>();
	private String seq;
	private String optionsToRemove;
	private boolean emptyOption;

	public ElementBean(final List<Element> elements, final List<Question> questions) {
		this.elements = elements;
		this.questions = questions;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(final List<Element> elements) {
		this.elements = elements;
	}
	
	public List<Question> getQuestions() {
		return questions;
	}

	public Element getSelectedElement() {
		return selectedElement;
	}

	public void setSelectedElement(final Element selectedElement) {
		this.selectedElement = selectedElement;
		if (selectedElement != null) {
			Tag tag = selectedElement.getTag();
			if (tag != null) {
				tagType = tag.getTagType();
				if (tagType == TagType.URLLINK) {
					UrlLink urlLink = (UrlLink) tag;
					url = urlLink.getUrl();
					newWindow = urlLink.isNewWindow();
				} else if (tagType == TagType.SELECT) {
					Select select = (Select) tag;
					emptyOption = select.isEmptyOption();
				}
			}
			for (ElementCountryRestriction restriction : selectedElement.getElementCountryRestrictionsAsList()) {
				existingRestrictions.add(new LocaleBean(restriction));
			}
		}
	}
	
	public void clearSelectedElement() {
		selectedElement = null;
	}
	
	public boolean isSelectedElementDeletable() {
		boolean deletable = false;
		if (selectedElement != null) {
			deletable = selectedElement.getFields().isEmpty();
		}
		return deletable;
	}
	
	public boolean isSelectedElementReferencingOptionsTag() {
		boolean referencingOptionsTag = false;
		if (selectedElement != null && selectedElement.getTag() instanceof OptionsTag) {
			referencingOptionsTag = true;
		}
		return referencingOptionsTag;
	}

	public boolean isNewElement() {
		return newElement;
	}

	public void setNewElement(final boolean newElement) {
		this.newElement = newElement;
	}
	
	public Element getUpdatedElement() {
		for (LocaleBean localeBean : existingRestrictions) {
			ElementCountryRestriction restriction = selectedElement.getElementCountryRestriction(localeBean.getId());
			restriction.setLocale(localeBean.toLocale());
		}
		
		for (LocaleBean localeBean : newRestrictions) {
			ElementCountryRestriction restriction = new ElementCountryRestriction();
			restriction.setLocale(localeBean.toLocale());
			selectedElement.addElementCountryRestriction(restriction);
		}
		
		if (StringUtils.isNotBlank(restrictionsToRemove)) {
			for (String id : restrictionsToRemove.split(",")) {
				if (StringUtils.isNotBlank(id)) {
					selectedElement.removeElementCountryRestriction(id.trim());
				}
			}
		}
		
		Tag tag = selectedElement.getTag();
		
		if (tag == null || tag.getTagType() != tagType) {
			tag = createTag();
			selectedElement.getTags().clear();
			selectedElement.addTag(tag);
		}
		
		if (tag instanceof OptionsTag) {
			resequenceOptions((OptionsTag) tag);
			removeOptions((OptionsTag) tag);
			removeTemporaryOptionIds();
			if (tag instanceof Select) {
				((Select) tag).setEmptyOption(emptyOption);
			}
		} else if (tag instanceof UrlLink) {
			((UrlLink) tag).setNewWindow(newWindow);
			((UrlLink) tag).setUrl(url);
		}
		
		return selectedElement;
	}

	private Tag createTag() {
		Tag tag = null;
		switch (tagType) {
		case CHECKBOX:
			tag = new Checkbox();
			break;
		case LABEL:
			tag = new Label();
			break;
		case MULTISELECT:
			tag = new MultiSelect();
			break;
		case SELECT:
			tag = new Select();
			break;
		case RADIO:
			tag = new Radio();
			break;
		case PASSWORDFIELD:
			tag = new PasswordField();
			break;
		case TEXTFIELD:
			tag = new TextField();
			break;
		case URLLINK:
			tag = new UrlLink();
			break;
		case HIDDENFIELD:
			tag = new HiddenField();
			break;
		}
		return tag;
	}

	private void resequenceOptions(final OptionsTag optionsTag) {
		Set<TagOption> resequencedOptions = new HashSet<TagOption>(optionsTag.getOptions());
		resequencedOptions.addAll(newOptions);
		for (TagOption tagOption : resequencedOptions) {
			if (tagOption.getTag() == null) {
				tagOption.setTag(optionsTag);
			}
			tagOption.setSequence(findSequenceFor(tagOption));
		}
		optionsTag.getOptions().clear();
		optionsTag.getOptions().addAll(resequencedOptions);
	}
	
	private int findSequenceFor(final TagOption tagOption) {
		int sequence = 0;
		if (StringUtils.isNotBlank(seq)) {
			String[] orderedSeqKeys = seq.split(",");
			for (int i = 0; i < orderedSeqKeys.length; i++) {
				if (orderedSeqKeys[i].equals(tagOption.getId())) {
					sequence = i;
					break;
				}
			}
		}
		return sequence;
	}
	
	private void removeOptions(final OptionsTag optionsTag) {
		if (StringUtils.isNotBlank(optionsToRemove)) {
			for (String id : optionsToRemove.split(",")) {
				if (StringUtils.isNotBlank(id)) {
					optionsTag.removeOption(id);
				}
			}
		}
	}
	
	private void removeTemporaryOptionIds() {
		for (TagOption option : newOptions) {
			if (StringUtils.isNotBlank(option.getId())) {
				option.setId(null);
			}
		}
	}

	public TagType getTagType() {
		return tagType;
	}

	public void setTagType(final TagType tagType) {
		this.tagType = tagType;
	}

	public List<LocaleBean> getNewRestrictions() {
		return newRestrictions;
	}

	public void setNewRestrictions(final List<LocaleBean> newRestrictions) {
		this.newRestrictions = newRestrictions;
	}
	
	public List<LocaleBean> getExistingRestrictions() {
		return existingRestrictions;
	}

	public void setExistingRestrictions(final List<LocaleBean> existingRestrictions) {
		this.existingRestrictions = existingRestrictions;
	}

	public String getRestrictionsToRemove() {
		return restrictionsToRemove;
	}

	public void setRestrictionsToRemove(final String restrictionsToRemove) {
		this.restrictionsToRemove = restrictionsToRemove;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public boolean isNewWindow() {
		return newWindow;
	}

	public void setNewWindow(final boolean newWindow) {
		this.newWindow = newWindow;
	}

	public List<TagOption> getNewOptions() {
		return newOptions;
	}

	public void setNewOptions(final List<TagOption> newOptions) {
		this.newOptions = newOptions;
	}
	
	public String getOptionsToRemove() {
		return optionsToRemove;
	}

	public void setOptionsToRemove(final String optionsToRemove) {
		this.optionsToRemove = optionsToRemove;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(final String seq) {
		this.seq = seq;
	}

	public boolean isEmptyOption() {
		return emptyOption;
	}

	public void setEmptyOption(final boolean emptyOption) {
		this.emptyOption = emptyOption;
	}

	public static class LocaleBean {

		private String id;
		private String language;
		private String country;
		
		public LocaleBean() {
		}
		
		public LocaleBean(final ElementCountryRestriction restriction) {
			id = restriction.getId();
			language = restriction.getLocale().getLanguage();
			country = restriction.getLocale().getCountry();
		}

		public String getId() {
			return id;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(final String language) {
			this.language = language;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(final String country) {
			this.country = country;
		}

		public Locale toLocale() {
			if (StringUtils.isNotBlank(language) && StringUtils.isNotBlank(country)) {
				return new Locale(language, country);
			}
			return new Locale(language);
		}
	}
}
