package com.oup.eac.ws.rest.v1;

import java.io.Serializable;
import java.util.Map;

public class GenerateReportRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** The Report name to be generated. */

    protected String reportName;

    /** input request parameters */
    protected Map<String, Object> requestParams;
    
    
    /*** The requestorEmail. */
    protected String requestorEmail;
    
    /**getReportName.
     * @return String
     */
    public String getReportName() {
		return reportName;
	}

	/**setReportName.
	 * @param reportName
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**getRequestParams.
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getRequestParams() {
		return requestParams;
	}

	/**setRequestParams.
	 * @param requestParams
	 */
	public void setRequestParams(Map<String, Object> requestParams) {
		this.requestParams = requestParams;
	}

	/**getRequestorEmail.
	 * @return String
	 */
	public String getRequestorEmail() {
		return requestorEmail;
	}

	/**setRequestorEmail.
	 * @param requestorId
	 */
	public void setRequestorEmail(String requestorId) {
		this.requestorEmail = requestorId;
	}

	
	public enum ReportName {
		RIGHT_TO_SEE_MY_DATA
	}

}
