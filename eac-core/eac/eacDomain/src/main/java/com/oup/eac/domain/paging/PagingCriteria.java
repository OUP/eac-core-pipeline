package com.oup.eac.domain.paging;

import java.io.Serializable;

public class PagingCriteria implements Serializable {

    private static final long serialVersionUID = -2649406055576224075L;

    public static enum SortDirection {
        ASC,
        DESC;
        
        public String getName() {
            return name();
        }
        
        public String getNameLowerCase() {
            return name().toLowerCase();
        }
        
        public String getOpposite() {
            if(this == ASC) {
                return DESC.getName();
            } else {
                return ASC.getName();
            }
        }
    }
    
    private int itemsPerPage = 10;
    
    private int requestedPage = 1;
    
    private SortDirection sortDirection = SortDirection.ASC;
    
    private String sortColumn;
    
    public PagingCriteria() {
        super();
    }

    public PagingCriteria(int itemsPerPage, int requestedPage, SortDirection sortDirection, String sortColumn) {
        super();
        this.itemsPerPage = itemsPerPage;
        this.requestedPage = requestedPage;
        this.sortDirection = sortDirection;
        this.sortColumn = sortColumn;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getRequestedPage() {
        return requestedPage;
    }

    public void setRequestedPage(int requestedPage) {
        this.requestedPage = requestedPage;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }
    
    public int getMaxResults() {
        return getItemsPerPage();
    }
    
    public int getFirstResult() {
        return (getRequestedPage() - 1) * getItemsPerPage();
    }

    public static PagingCriteria valueOf(int itemsPerPage, int requestedPage, SortDirection sortDirection, String sortColumn) {
        return new PagingCriteria(itemsPerPage, requestedPage, sortDirection, sortColumn);
    }
    
    
}

