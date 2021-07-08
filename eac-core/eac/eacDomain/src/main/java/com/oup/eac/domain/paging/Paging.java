package com.oup.eac.domain.paging;

import java.io.Serializable;
import java.util.List;

import com.oup.eac.domain.paging.PagingCriteria.SortDirection;

public class Paging<T> implements Serializable {
    
    private static final long serialVersionUID = 3633846462051495128L;

    public static int DEFAULT_NUMBER_PAGE_LINKS = 10;
    
    private int numberOfPageLinks;

    private List<T> items;
    
    private PagingCriteria pagingCriteria;
    
    private int totalItems;
    
    private int numberOfPages;
    
    private int[] pages;
    
    public Paging(PagingCriteria pagingCriteria, List<T> items, int totalItems) {
        this(pagingCriteria, DEFAULT_NUMBER_PAGE_LINKS, items, totalItems);
    }
    
    public Paging(PagingCriteria pagingCriteria, int numberOfPageLinks, List<T> items, int totalItems) {
        super();
        this.pagingCriteria = pagingCriteria;
        this.numberOfPageLinks = numberOfPageLinks;
        this.items = items;
        this.totalItems = totalItems;
        this.init();
    }

    private void init() {
        if(totalItems == 0) {
            numberOfPages = 1;
        } else {
            numberOfPages = (int) Math.ceil(Double.valueOf(totalItems)/Double.valueOf(getItemsPerPage()));
        }
        if(getRequestedPage() < 1) {
            throw new AssertionError("The requested page must be greater than 0.");
        }
        if(getRequestedPage() > numberOfPages) {
            throw new AssertionError("The requested page can not be greater than the maximum number of pages.");
        }
        int firstLink = 1;
        int lastLink = numberOfPageLinks;
        if(numberOfPages > numberOfPageLinks) {
            if(getRequestedPage() > (numberOfPageLinks/2)) {
                firstLink = (getRequestedPage() - (numberOfPageLinks/2)) + 1;
                lastLink = getRequestedPage() + (numberOfPageLinks/2);
                if(getRequestedPage() > (numberOfPages - (numberOfPageLinks/2))) {
                    firstLink = (numberOfPages - numberOfPageLinks) + 1;
                }
                if(lastLink > numberOfPages) {
                    lastLink = numberOfPages;
                }
            }   
        } else {
            lastLink = numberOfPages;
        }
        pages = new int[(lastLink - firstLink) + 1];
        for(int i=0;i<pages.length;i++) {
            pages[i] = firstLink++;
        }
    }
    
    public int getLastPage() {
        return numberOfPages;
    }
    
    public boolean isLastPage() {
    	return getRequestedPage() == numberOfPages;
    }
    
    public int getFirstPage() {
        return 1;
    }

    public List<T> getItems() {
        return items;
    }

    public int getRequestedPage() {
        return pagingCriteria.getRequestedPage();
    }

    public int getItemsPerPage() {
        return pagingCriteria.getItemsPerPage();
    }
    
    public int getFirstItem() {
        return ((pagingCriteria.getRequestedPage() - 1) * pagingCriteria.getItemsPerPage()) + 1;
    }
    
    public int getLastItem() {
        return getFirstItem() + items.size() - 1;
    }

    public int getTotalItems() {
        return totalItems;
    }
    
    public int getNumberOfPageLinks() {
        return numberOfPageLinks;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int[] getPages() {
        return pages;
    }

    public Integer getPreviousPage() {
        if(getRequestedPage() == 1) {
            return null;
        }
        return getRequestedPage() - 1;
    }
    
    public Integer getNextPage() {
        if(getRequestedPage() == getLastPage()) {
            return null;
        }
        return getRequestedPage() + 1;
    }
    
    public SortDirection getSortDirection() {
        return pagingCriteria.getSortDirection();
    }

    public String getSortColumn() {
        return pagingCriteria.getSortColumn();
    }
    
    public static <T> Paging<T> valueOf(PagingCriteria pagingCriteria, List<T> items, int totalItems) {
        return new Paging<T>(pagingCriteria, items, totalItems);
    }
}
