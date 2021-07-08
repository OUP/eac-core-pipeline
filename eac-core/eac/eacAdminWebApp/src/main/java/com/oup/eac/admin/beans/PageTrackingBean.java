package com.oup.eac.admin.beans;

import java.io.Serializable;

public class PageTrackingBean implements Serializable {
	
	private static final int DEFAULT_PAGE_NUMBER = 1;
	private static final int DEFAULT_RESULTS_PER_PAGE = 10;

    private int pageNumber = DEFAULT_PAGE_NUMBER;
    private int resultsPerPage = DEFAULT_RESULTS_PER_PAGE;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }
    
	public void incrementPageNumber() {
		setPageNumber(getPageNumber() + 1);
	}
	
	public void decrementPageNumber() {
		if (getPageNumber() > 0) {
			setPageNumber(getPageNumber() - 1);
		}
	}
}
