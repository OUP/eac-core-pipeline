package com.oup.eac.dto;

import java.io.Serializable;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public abstract class LicenceDetailDto implements Serializable {
	public enum LicenceType {
		STANDARD,
		ROLLING,
		CONCURRENT,
		USAGE;
	}
	
	public abstract LicenceType getLicenceType();
	
	@DateTimeFormat (pattern="dd/MM/yyyy")
	private LocalDate startDate;
	
	@DateTimeFormat (pattern="dd/MM/yyyy")
	private LocalDate endDate;

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}
