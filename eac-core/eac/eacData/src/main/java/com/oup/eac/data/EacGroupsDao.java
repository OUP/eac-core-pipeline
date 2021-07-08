package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.EacGroupsSearchCriteria;

public interface EacGroupsDao extends BaseDao<EacGroups, String>{
	
	List<EacGroups> searchProductGroups(final EacGroupsSearchCriteria searchCriteria, final PagingCriteria pagingCriteria);

	int countSearchProductGroups(final EacGroupsSearchCriteria searchCriteria);
	
	EacGroups getEacGroupById(String id);
	
	boolean isEacGroupUsed(String id);
	
	EacGroups getEacGroupByName(String name);
	
	boolean deleteRegistrationDefinitionOfEacGroup(String id);
	
	String getEacGroupIdByName(String name);
}
