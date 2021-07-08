package com.oup.eac.service;

import java.util.Set;

import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.EacGroupsSearchCriteria;
import com.oup.eac.dto.ProductGroupDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;

public interface EacGroupService {

	Paging<EacGroups> searchProductGroups(final EacGroupsSearchCriteria searchCriteria, final PagingCriteria pagingCriteria) throws ServiceLayerException;
	
	EacGroups getEacGroupById(final String id) throws ServiceLayerException;
	
	void updateEacGroup(final EacGroups eacGroup, Set<String> productIdsToAdd, Set<String> productIdsToRemove, String currentEacGroupName)throws ServiceLayerException, ProductNotFoundException, ErightsException;
	
	void createEacGroup(final EacGroups eacGroup) throws ServiceLayerException, ProductNotFoundException, ErightsException;
	
	boolean deleteUnUsedEacGroup(final String eacGroupId) throws ServiceLayerException, ProductNotFoundException, ErightsException;
	
	boolean isEacGroupUsed(final String eacGroupId) throws ServiceLayerException;
	
	void updateIsEditable(final EacGroups eacGroup) throws ServiceLayerException;
	
	EacGroups getEacGroupByName(final String name) throws ServiceLayerException;
	
	String getEacGroupIdByName(final String name) throws ServiceLayerException;

	EacGroups getEacGroupByProductGroupDto(ProductGroupDto productGroupDto)
			throws ServiceLayerException;

	ProductGroupDto getProductGroupDtoByErightsId(String groupId)
			throws ErightsException;

	
}
