package com.oup.eac.service;

import java.util.List;

import com.oup.eac.domain.UserDetailsReport;
import com.oup.eac.dto.CustomerSearchCriteria;

public interface UserDetailsReportService {

	List<String> generateUserDetailsReport(UserDetailsReport userDetailsReport);

	String getUserIdFromUsername(CustomerSearchCriteria customerSearchCriteria);

}
