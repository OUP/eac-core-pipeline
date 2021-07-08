package com.oup.eac.admin.binding;

import java.beans.PropertyEditorSupport;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.domain.Division;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.DivisionService;

public class DivisionTypePropertyEditor extends PropertyEditorSupport {

	private final DivisionService divisionService;

	public DivisionTypePropertyEditor(final DivisionService divisionService) {
		this.divisionService = divisionService;
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return (value != null ? ((Division) value).getId() : "");
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		if (text == null) {
			setValue(null);
		} else {
			List<Division> divisions = null;
			try {
				divisions = divisionService.getAllDivisions();
			} catch (AccessDeniedException | ErightsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (Division division : divisions) {
				if (division.getErightsId().toString().equals(text)) {
					setValue(division);
				}
			}
			//De-duplication
			/*Division division = divisionService.getDivisionById(text);
			setValue(division);*/
		}
	}
}
