package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.ActivationCodeBatchReportCriteria;
import com.oup.eac.dto.ActivationCodeBatchSearchCriteria;
import com.oup.eac.dto.ActivationCodeDto;

public interface ActivationCodeBatchDao extends BaseDao<ActivationCodeBatch, String> {
    
    Long getActivationCodeReportCount(ActivationCodeBatchReportCriteria reportCriteria, String rsDBSchema);
    
    List<Object[]> getActivationCodeReport(ActivationCodeBatchReportCriteria activationCodeReportCriteria, String rsDBSchema);
	
	ActivationCodeBatch getActivationCodeBatchByBatchId(String batchId);

	//List<ActivationCodeBatch> getActivationCodeBatchByDivision(List<DivisionAdminUser> divisionAdminUsers);
	
	List<ActivationCodeBatch> searchActivationCodeBatches(ActivationCodeBatchSearchCriteria searchCriteria, PagingCriteria pagingCriteria);
	
	int countSearchActivationCodeBatches(ActivationCodeBatchSearchCriteria searchCriteria, PagingCriteria pagingCriteria);

    ActivationCodeBatch getActivationCodeBatchByBatchDbId(String batchDbId);
    
    boolean isBatchUsed(String batchDbId);

    boolean archiveBatch(ActivationCodeBatch batch);
    
    int getNumberOfTokensInBatch(String batchDbId);

    boolean doesArchivedBatchExist(String batchName);
     
}
