package com.oup.eac.service;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

/**
 * Services for exporting registration data.
 * 
 */
public interface ELTDataExportService {
	
	List<String[]> getUserDataByOwnerAndDateRange(final String divisionType, final DateTime fromDT, final DateTime toDT, final String[] headers, final int batchSize, int firstRecord, Map<String, Map<String, Map<String, String>>> pageDefinitionExportNameMap);

    void ftpCMDPData(final String divisionType, final DateTime fromDT, final DateTime toDT, final String cmdpSupportEmail) throws ServiceLayerException;

}
