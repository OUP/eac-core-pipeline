package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.AdminAccountSearchBean;

public interface AdminAccountSearchDao extends OUPBaseDao<AdminUser, String> {
	List<AdminUser> searchCustomers(final AdminAccountSearchBean customerSearchCriteria, final PagingCriteria pagingCriteria);
	int countSearchCustomers(AdminAccountSearchBean customerSearchCriteria);

}
