package com.oup.eac.service;

import java.util.List;

import org.joda.time.DateTime;

/**
 * Services for exporting registration data.
 * 
 */
public interface DataExportService {

    /**
     * Get user data by owner and date range.
     *
     * @param fromDT
     *            starting export period
     * @param toDT
     *            ending export period
     * @return list of customer data
     */
    List<String[]> getUserDataByOwnerAndDateRange(final String divisionType, final DateTime fromDT, final DateTime toDT);

    /**
     * Email user data by owner and date range to recipient.
     *
     * @param fromDT
     *            starting export period
     * @param toDT
     *            ending export period
     * @param emailTo
     *            recipient
     * @throws ServiceLayerException
     *             service layer exception
     */
    void eMailDataExport(final String divisionType, final DateTime fromDT, final DateTime toDT, final String emailTo,
    					final String eacEmailBcc) throws ServiceLayerException;

}
