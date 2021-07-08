package com.oup.eac.admin.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.SkinBean;
import com.oup.eac.common.utils.url.URLUtils;
import com.oup.eac.domain.UrlSkin;

@Component
public class SkinBeanValidator implements Validator {

	@Override
	public void validate(final Object target, final Errors errors) {
		SkinBean skinBean = (SkinBean) target;

		validateSiteName(skinBean, errors);
		validateSiteUrl(skinBean, errors);
		validateContactUrl(skinBean, errors);
		validateSkinUrl(skinBean, errors);
	}

	private void validateSiteName(final SkinBean skinBean, final Errors errors) {
		if (StringUtils.isBlank(skinBean.getSelectedSkin().getSiteName())) {
			errors.rejectValue("selectedSkin.siteName", "error.skin.siteName.empty");
		}
	}

	private void validateSiteUrl(final SkinBean skinBean, final Errors errors) {
		UrlSkin urlSkin = skinBean.getSelectedSkin();
		if (StringUtils.isBlank(urlSkin.getUrl())) {
			errors.rejectValue("selectedSkin.url", "error.skin.siteUrl.empty");
		} else {
			if (!isValidUrl(urlSkin.getUrl())) {
				errors.rejectValue("selectedSkin.url", "error.skin.siteUrl.malformed");
			}
			for (UrlSkin existingSkin : skinBean.getSkins()) {
				if (StringUtils.equals(urlSkin.getUrl(), existingSkin.getUrl()) && !StringUtils.equals(urlSkin.getId(), existingSkin.getId())) {
					errors.rejectValue("selectedSkin.url", "error.skin.siteUrl.duplicate");
				}
			}
		}
	}

	private void validateContactUrl(final SkinBean skinBean, final Errors errors) {
		String contactPath = skinBean.getSelectedSkin().getContactPath();
		if (StringUtils.isBlank(contactPath)) {
			errors.rejectValue("selectedSkin.contactPath", "error.skin.contactUrl.empty");
		} else if (!isValidUrl(contactPath)) {
			errors.rejectValue("selectedSkin.contactPath", "error.skin.contactUrl.malformed");
		}
	}
	
	private boolean isValidUrl(final String url) {
		boolean valid = true;
		try {
			if (!StringUtils.startsWithIgnoreCase(url, "mailto:")) {
				URLUtils.safeEncode(url);
			}
		} catch (Exception e) {
			valid = false;
		}
		return valid;
	}

	private void validateSkinUrl(final SkinBean skinBean, final Errors errors) {
		if (StringUtils.isBlank(skinBean.getSelectedSkin().getSkinPath())) {
			errors.rejectValue("selectedSkin.skinPath", "error.skin.skinUrl.empty");
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		final boolean supports = SkinBean.class.isAssignableFrom(clazz);
		return supports;
	}

}
