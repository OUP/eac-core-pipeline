package com.oup.eac.service.exceptions;

import com.oup.eac.dto.ExternalIdDto;

public class ExternalIdTakenException extends ServiceLayerValidationException {
	
	private ExternalIdDto dto;

	public ExternalIdTakenException(String msg, ExternalIdDto dto){
		super(msg);
		this.dto = dto;
	}

	public ExternalIdDto getDto() {
		return dto;
	}

	
	
}
