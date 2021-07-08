package com.oup.eac.dto;

import java.io.Serializable;

public class DivisionDto implements Serializable {
	
	private static final long serialVersionUID = 6627721705571444117L;
	
	private Integer id;
	private String divisionType;
	
	public DivisionDto(final Integer id, final String divisionType) {
		super();
		this.id = id;
		this.divisionType = divisionType;
	}
	
	public DivisionDto() {
		super();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDivisionType() {
		return divisionType;
	}
	public void setDivisionType(String divisionType) {
		this.divisionType = divisionType;
	}
	
	@Override
	public String toString() {
		return "Division [id=" + id 
				+ ", divisionType=" + divisionType 
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionType == null) ? 0 : divisionType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DivisionDto other = (DivisionDto) obj;
		if (divisionType == null) {
			if (other.divisionType != null)
				return false;
		} else if (!divisionType.equals(other.divisionType))
			return false;
		return true;
	}
	
}
