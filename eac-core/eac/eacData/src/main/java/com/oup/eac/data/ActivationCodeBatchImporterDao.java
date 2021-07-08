package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.ActivationCodeBatchImporter;
import com.oup.eac.domain.ActivationCodeBatchImporterStatus.StatusCode;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.ActivationCodeBatchImporterSearchCriteria;

public interface ActivationCodeBatchImporterDao extends BaseDao<ActivationCodeBatchImporter, String> {
	
	List<String> getUniqueActivationCodesForGrouping(StatusCode status);
	
	List<String> getProductsForActivationCode(String code);
	
	List<ActivationCodeBatchImporter> getActivationCodeBatchImportorByCode(String code);
	
	public List<ActivationCodeBatchImporter> getActivationCodes(ActivationCodeBatchImporterSearchCriteria searchCriteria, PagingCriteria pagingCriteria);
	
    /*public List<ActivationCodeBatchImporterExtra> getActivationCodeReadyBatches(ActivationCodeBatchExtraSearchCriteria searchCriteria, PagingCriteria pagingCriteria);
    */
    public List<ActivationCodeBatchImporter> getActivationCodesImporters(ActivationCodeBatchImporterSearchCriteria searchCriteria, PagingCriteria pagingCriteria);
}
