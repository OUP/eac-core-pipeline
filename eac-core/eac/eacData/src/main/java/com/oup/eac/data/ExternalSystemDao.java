package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.ExternalSystem;


public interface ExternalSystemDao extends BaseDao<ExternalSystem, String> {
    public ExternalSystem findByName(String name);

	public List<ExternalSystem> findAllExternalSystemsOrderedByName();
}
