package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.RegistrationActivation;

public interface RegistrationActivationDao extends BaseDao<RegistrationActivation, String> {

	List<RegistrationActivation> findAllRegistrationActivationsOrderedByType();
}
