package com.oup.eac.data;

import java.util.List;

import org.hibernate.Session;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.ActivationCodeReportDto;
import com.oup.eac.dto.ActivationCodeSearchCriteria;

public interface ActivationCodeDao extends BaseDao<ActivationCode, String> {
    
    ActivationCode getActivationCodeAndBatchBy(ActivationCodeRegistration activationCodeRegistration);
	
	List<ActivationCode> searchActivationCodeByCode(String code, boolean likeSearch);
	
	ActivationCode getActivationCodeAndDefinitionByCode(String code);
	
	ActivationCode getActivationCodeAndDefinitionByCodeForEacGroup(String code);

	List<ActivationCode> getActivationCodeByBatch(ActivationCodeBatch activationCodeBatch);
	
    ActivationCode getActivationCodeByCode(String code);
    
    ActivationCode getActivationCodeWithDetails(String id);
    
    ActivationCode getActivationCodeByCodeAndBatch(String code, ActivationCodeBatch activationCodeBatch);
    
    boolean checkActivationCodeIsUnique(String code);
    
    ActivationCode getActivationCodeAndProductByCode(String activationCode);
    
    ActivationCode getActivationCodeAndProductAndEacGroupByCode(String activationCode);
    
    List<ActivationCodeReportDto> getActivationCodeSearch(ActivationCodeSearchCriteria criteria, PagingCriteria pagingCriteria, AdminUser adminUser);
    
    Long getActivationCodeReportCount(ActivationCodeSearchCriteria criteria, AdminUser adminUser);
    
    Session getSessionFromDao();
    
    ActivationCode getActivationCodeFullDetails(String activationCode);
}
