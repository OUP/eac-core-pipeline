package com.oup.eac.domain;


public class UnmatchedCountryMatchRegistrationActivationVisitor extends CountryMatchRegistrationActivationVisitor {

	@Override
	public void visit(final CountryMatchRegistrationActivation registrationActivation) throws CircularCountryMatchException {

		if (path.contains(registrationActivation)) {
			throw new CircularCountryMatchException("Circular reference detected in unmatched path: " + getPathAsString(registrationActivation));
		}

		RegistrationActivation unmatched = registrationActivation.getUnmatchedActivation();
		if (unmatched instanceof CountryMatchRegistrationActivation) {
			CountryMatchRegistrationActivation unmatchedCountryMatch = (CountryMatchRegistrationActivation) unmatched;
			path.add(registrationActivation);
			unmatchedCountryMatch.checkCircularReferences(this);
		}

	}

}
