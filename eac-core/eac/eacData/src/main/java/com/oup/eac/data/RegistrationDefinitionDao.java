/**
 * 
 */
package com.oup.eac.data;

import java.util.List;

import org.hibernate.Session;

import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;

/**
 * @author harlandd
 * 
 */
public interface RegistrationDefinitionDao extends BaseDao<RegistrationDefinition , String> {

	<T extends RegistrationDefinition> T getRegistrationDefinitionByProduct(Class<T> aClass, Product product);
	
	ProductRegistrationDefinition getProductRegistrationDefinitionWithLicence(String id);
	
	List<ActivationCodeRegistrationDefinition> getActivationCodeRegistrationDefinitionsByAdminUser(AdminUser adminUser);
	
	AccountRegistrationDefinition getAccountRegistrationDefinitionByProduct(final Product product);
	
	ActivationCodeRegistrationDefinition getActivationCodeRegistrationDefinitionByProduct(final Product product);

    ProductRegistrationDefinition getProductRegistrationDefinitionFromCustomerAndRegistrationId(Customer customer, String registrationId);
    
    List<RegistrationDefinition> searchRegistrationDefinitions(final RegistrationDefinitionSearchCriteria searchCriteria, final PagingCriteria pagingCriteria);
    
    int countSearchRegistrationDefinitions(final RegistrationDefinitionSearchCriteria searchCriteria);
    
    ActivationCodeRegistrationDefinition getActivationCodeRegistrationDefinitionForEacGroup(final EacGroups eacGroup);
    
    List<RegistrationDefinition> getRegistrationDefinitionsForProduct(final Product product);
    
    //void saveRegistrationDefinition(ProductRegistrationDefinition productRegistrationDefinition);
    
    Session getSessionFromDao();

    List<ProductRegistrationDefinition> getRegistrationDefinitionsForPageDefinition(
			String pageDefId);

	List<AccountRegistrationDefinition> getRegistrationDefinitionsForPageDefinitionForAccount(
			String pageDefId);
}