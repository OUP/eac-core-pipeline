package com.oup.eac.service;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.AdminAccountSearchBean;

public interface AccountSearchService {
 
	Paging<AdminUser> searchAdminCustomers(final AdminAccountSearchBean customerSearchCriteria, final PagingCriteria pagingCriteria)throws ServiceLayerException;
}
