package com.oup.eac.domain;


public class MatchedCountryMatchRegistrationActivationVisitor extends CountryMatchRegistrationActivationVisitor {
	
	@Override
	public void visit(final CountryMatchRegistrationActivation registrationActivation) throws CircularCountryMatchException {

		if (path.contains(registrationActivation)) {
			throw new CircularCountryMatchException("Circular reference detected in matched path: " + getPathAsString(registrationActivation));
		}

		RegistrationActivation matched = registrationActivation.getMatchedActivation();
		if (matched instanceof CountryMatchRegistrationActivation) {
			CountryMatchRegistrationActivation matchedCountryMatch = (CountryMatchRegistrationActivation) matched;
			path.add(registrationActivation);
			matchedCountryMatch.checkCircularReferences(this);
		}
	}

}
