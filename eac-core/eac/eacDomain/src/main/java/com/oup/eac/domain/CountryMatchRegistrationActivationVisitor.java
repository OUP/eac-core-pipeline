package com.oup.eac.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class CountryMatchRegistrationActivationVisitor {
	
	protected final List<CountryMatchRegistrationActivation> path = new ArrayList<CountryMatchRegistrationActivation>();

	public abstract void visit(final CountryMatchRegistrationActivation registrationActivation) throws CircularCountryMatchException;

	protected String getPathAsString(final CountryMatchRegistrationActivation registrationActivation) {
		StringBuilder message = new StringBuilder();
		
		for (CountryMatchRegistrationActivation pathComponent : path) {
			message.append(pathComponent.getDescription());
			message.append(" -> ");
		}
		
		message.append(registrationActivation.getDescription());
		
		return message.toString();
	}
}
