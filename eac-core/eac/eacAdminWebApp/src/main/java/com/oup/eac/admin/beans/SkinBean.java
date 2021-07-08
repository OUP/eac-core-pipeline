package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.List;

import com.oup.eac.domain.UrlSkin;

public class SkinBean implements Serializable {

	private List<UrlSkin> skins;
	private UrlSkin selectedSkin;

	public SkinBean(final List<UrlSkin> skins) {
		this.skins = skins;
	}

	public List<UrlSkin> getSkins() {
		return skins;
	}
	
	public void setSkins(final List<UrlSkin> skins) {
		this.skins = skins;
	}

	public UrlSkin getSelectedSkin() {
		return selectedSkin;
	}

	public void setSelectedSkin(final UrlSkin selectedSkin) {
		this.selectedSkin = selectedSkin;
	}
	
	public void clearSelectedSkin() {
		this.selectedSkin = null;
	}
	
	public UrlSkin getUpdatedSkin() {
		// Consistent with other *beans we provide this method
		// even though this implementation does nothing other
		// than return the selected skin (as form data will already 
		// have been bound to it).
		return selectedSkin;
	}
}
