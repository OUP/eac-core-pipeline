package com.oup.eac.web.tags;

import com.oup.eac.service.EacGroupService;

public class IsEacGroupUsedTag extends BaseGetValueTag{
	
	private String eacGroupId;

	public String getEacGroupId() {
		return eacGroupId;
	}

	public void setEacGroupId(String eacGroupId) {
		this.eacGroupId = eacGroupId;
	}

	@Override
	public Object getValue() throws Exception {
		EacGroupService eacGroupService= getService("eacGroupService", EacGroupService.class);
		 Boolean value =eacGroupService.isEacGroupUsed(eacGroupId);
		return value;
	}

}
