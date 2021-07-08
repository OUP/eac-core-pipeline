package com.oup.eac.service;


import java.util.List;

import com.oup.eac.domain.ActivationCodeBatchImporter;
import com.oup.eac.domain.ActivationCodeBatchImporterStatus.StatusCode;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.ActivationCodeBatchImporterSearchCriteria;

public interface ActivationCodeBatchImporterService {
	
    boolean importActivationCodeBatch(final ActivationCodeBatchImporter obj) throws ServiceLayerException;
    
    public List<ActivationCodeBatchImporter> getActivationCodeBatchList() throws ServiceLayerException;
    
    List<String> getUniqueActivationCodesForGrouping(StatusCode status) throws ServiceLayerException;
    
    List<String> getProductsForActivationCode(String code) throws ServiceLayerException;
    
    boolean updateStatusofCodeForGroup(String code, String eacGroupId, StatusCode statusCode) throws ServiceLayerException;
    
    public List<ActivationCodeBatchImporter> getActivationCodesImporters(ActivationCodeBatchImporterSearchCriteria searchCriteria, PagingCriteria pagingCriteria)throws ServiceLayerException;
    
    boolean updateStatus(ActivationCodeBatchImporter obj, StatusCode statusCode, StatusCode currentStatus)throws ServiceLayerException;
}
