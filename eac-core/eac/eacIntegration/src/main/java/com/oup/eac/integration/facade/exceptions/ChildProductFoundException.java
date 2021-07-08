package com.oup.eac.integration.facade.exceptions;

import java.util.List;

public class ChildProductFoundException extends ErightsException {

	private final List<String> childIds;
	
    public ChildProductFoundException(final String errorMessage, final List<String> childIds) {
        super(errorMessage);
        this.childIds = childIds;
    }

	public List<String> getChildIds() {
		return childIds;
	}
}
