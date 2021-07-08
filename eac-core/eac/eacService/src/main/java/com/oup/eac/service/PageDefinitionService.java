package com.oup.eac.service;

import java.util.List;

import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.Question;

public interface PageDefinitionService {

	ProductPageDefinition getFullyFetchedProductPageDefinitionById(String id);
	
	AccountPageDefinition getFullyFetchedAccountPageDefinitionById(String id);
	
	List<ProductPageDefinition> getAvailableProductPageDefinitions(AdminUser adminUser);
	
	List<AccountPageDefinition> getAvailableAccountPageDefinitions(AdminUser adminUser);
	
	List<Question> getPageDefinitionQuestions(PageDefinition pageDefinition);
	
	void savePageDefinition(final PageDefinition pageDefinition);
	
	void deletePageDefinition(final PageDefinition pageDefinition);
}
