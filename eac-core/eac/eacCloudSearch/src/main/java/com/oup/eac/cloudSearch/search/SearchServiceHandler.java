package com.oup.eac.cloudSearch.search;


import java.util.List;

import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.dto.ActivationCodeBatchSearchCriteria;
import com.oup.eac.dto.LicenceDto;

public interface SearchServiceHandler {
	
	/**
	 * @param SearchRequest
	 * @return
	 */
	public AmazonSearchResponse searchUser(AmazonSearchRequest SearchRequest, 
			boolean isArchivedRequired, boolean filterOutTrusted);
	
	/**
	 * searchActivationCode
	 * @param SearchRequest
	 * @return
	 * AmazonSearchResponse
	 * @author Developed by TCS
	 */ 
	public AmazonSearchResponse searchActivationCode(AmazonSearchRequest SearchRequest);

	/**
	 * searchActivationCodeBatch
	 * @param searchRequest
	 * @return
	 * AmazonSearchResponse
	 * @author Developed by TCS
	 */ 
	public AmazonSearchResponse searchActivationCodeBatch(AmazonSearchRequest searchRequest, 
			ActivationCodeBatchSearchCriteria searchCriteria);
	
	/**
	 * searchProductGroup
	 * @param searchRequest
	 * @return
	 * AmazonSearchResponse
	 * @author Developed by TCS
	 */ 

	public AmazonSearchResponse searchProductGroups(AmazonSearchRequest searchRequest);

	public AmazonSearchResponse searchProduct(AmazonSearchRequest searchRequest);

	public AmazonSearchResponse searchLicense(AmazonSearchRequest searchRequest);

	public AmazonSearchResponse searchProductsDivisions(AmazonSearchRequest searchRequest, List<Integer> divisionIds);

	public AmazonSearchResponse searchAllLicenses(AmazonSearchRequest searchRequest);

	public AmazonSearchResponse searchActivationCode(AmazonSearchRequest searchRequest, boolean likeSearch);
	
	public AmazonSearchResponse searchGroup(AmazonSearchRequest searchRequest);
	
	public AmazonSearchResponse searchUserGroupMembership(AmazonSearchRequest searchRequest);

	AmazonSearchResponse searchUserByUserIdList(List<String> userIdList);

	boolean searchUserGroupMembershipForBritanico(String userId);

	List<LicenceDto> searchAllLicensesForBritanico(String userId);
	
	
}
