package com.oup.eac.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oup.eac.data.PageDefinitionDao;
import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.Question;
import com.oup.eac.service.PageDefinitionService;

/**
 * Service to manage page definitions.
 * 
 * @author Ian Packard
 *
 */
@Service(value="pageDefinitionService")
public class PageDefinitionServiceImpl implements PageDefinitionService {

	private final PageDefinitionDao<ProductPageDefinition> productPageDefinitionDao;
    
    private final PageDefinitionDao<AccountPageDefinition> accountPageDefinitionDao;
    
    @Autowired
	public PageDefinitionServiceImpl(final PageDefinitionDao<ProductPageDefinition> productPageDefinitionDao, 
									 final PageDefinitionDao<AccountPageDefinition> accountPageDefinitionDao) {
		super();
		this.productPageDefinitionDao = productPageDefinitionDao;
		this.accountPageDefinitionDao = accountPageDefinitionDao;
	}

    /**
     * Get a fully fetched product page definition. 
     */
	@Override
	public ProductPageDefinition getFullyFetchedProductPageDefinitionById(String id) {
		return productPageDefinitionDao.getPageDefinitionById(id);
	}

	/**
	 * Get a fully fetched account page definition.
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public AccountPageDefinition getFullyFetchedAccountPageDefinitionById(String id) {
		return accountPageDefinitionDao.getPageDefinitionById(id);
	}

	@Override
	public List<ProductPageDefinition> getAvailableProductPageDefinitions(final AdminUser adminUser) {
		return productPageDefinitionDao.getAvailablePageDefinitions(adminUser);
	}

	@Override
	public List<AccountPageDefinition> getAvailableAccountPageDefinitions(final AdminUser adminUser) {
		return accountPageDefinitionDao.getAvailablePageDefinitions(adminUser);
	}
	
    @Override
    public List<Question> getPageDefinitionQuestions(final PageDefinition pageDefinition) {
        return productPageDefinitionDao.getPageDefinitionQuestions(pageDefinition);
    }

	@Override
	public void savePageDefinition(final PageDefinition pageDefinition) {
		if (pageDefinition instanceof AccountPageDefinition) {
			accountPageDefinitionDao.saveOrUpdate((AccountPageDefinition) pageDefinition);
		} else {
			productPageDefinitionDao.saveOrUpdate((ProductPageDefinition) pageDefinition);
		}
	}

	@Override
	public void deletePageDefinition(final PageDefinition pageDefinition) {
		if (pageDefinition instanceof AccountPageDefinition) {
			accountPageDefinitionDao.delete((AccountPageDefinition) pageDefinition);
		} else {
			productPageDefinitionDao.delete((ProductPageDefinition) pageDefinition);
		}
		
	}
    
    
}
