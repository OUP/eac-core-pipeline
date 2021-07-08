/**
 * 
 */
package com.oup.eac.data.impl.hibernate;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.RegistrationDefinitionDao;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;

/**
 * The RegistrationDefinition dao hibernate implementation.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="registrationDefinitionDao")
public class RegistrationDefinitionHibernateDao extends HibernateBaseDao<RegistrationDefinition, String> implements RegistrationDefinitionDao {
	
    /**
     * @param sf
     *            the session factory
     */
	@Autowired
    public RegistrationDefinitionHibernateDao(final SessionFactory sf) {
        super(sf);
    }
	
    /**
     * We eagerly load the licenceTemplate only if it's a produtRegistrationDefinition
     */
	@SuppressWarnings("unchecked")
	@Override
	public final <T extends RegistrationDefinition> T getRegistrationDefinitionByProduct(Class<T> aClass, Product product) {
		//product de-duplication
		/*if (ProductRegistrationDefinition.class.isAssignableFrom(aClass)) {
	        return (T) getSession().createQuery("select rd from " + aClass.getSimpleName() + " rd " +
	                "join fetch rd.product p " +
	                "join fetch rd.registrationActivation ra " +
	                "left join fetch rd.licenceTemplate lt " +
	                "left join fetch rd.pageDefinition pd " +
				    "where p = :product")
				    .setParameter("product", product)
				    //TODO Not sure if this is working.
				    .uniqueResult();
	    }*/
        return (T) getSession().createQuery("select rd from " + aClass.getSimpleName() + " rd " +
        		//"join fetch rd.product p " +
                "left join fetch rd.pageDefinition pd " +
                "where rd.productId = :productId")
                .setParameter("productId", product.getId())
                .uniqueResult();
	}
	
	@Override
	public final ProductRegistrationDefinition getProductRegistrationDefinitionWithLicence(String id) {
		return (ProductRegistrationDefinition) getSession().createQuery("select rd from ProductRegistrationDefinition rd " +
				//"join fetch rd.registrationActivation ra " +
				//"join fetch rd.licenceTemplate lt " +
				"left join fetch rd.pageDefinition pd " +
				//"join fetch rd.product p " +
				//"join fetch p.division d " +
				"where rd.id = :id")
				.setParameter("id", id)
				.uniqueResult();		
	}

    @SuppressWarnings("unchecked")
    @Override
    public final List<ActivationCodeRegistrationDefinition> getActivationCodeRegistrationDefinitionsByAdminUser(AdminUser adminUser) {
        return getSession().createQuery("select rd from ActivationCodeRegistrationDefinition rd " +
                "join fetch rd.product p " +
                "join fetch rd.registrationActivation ra " +
                "join fetch rd.licenceTemplate lt " +
                "join fetch p.division pd " +
                "join pd.divisionAdminUsers dau " +
                "where dau.adminUser = :adminUser " +
                "order by p.productName asc")
                .setParameter("adminUser", adminUser)
                .list();
    }
    
    public final AccountRegistrationDefinition getAccountRegistrationDefinitionByProduct(final Product product) {
    	return (AccountRegistrationDefinition)getSession().createQuery("select ard from AccountRegistrationDefinition ard " +
    			"join fetch ard.pageDefinition pd " +
    			"where ard.productId = :productId")
    			.setParameter("productId", product.getId())
    			.uniqueResult();
    }

    @Override
    public ActivationCodeRegistrationDefinition getActivationCodeRegistrationDefinitionByProduct(final Product product) {
        return (ActivationCodeRegistrationDefinition)getSession().createQuery("select acrd from ActivationCodeRegistrationDefinition acrd " +
                "where acrd.productId = :productId")
                .setParameter("productId", product.getId())
                .uniqueResult();
    }

    @Override
    public ProductRegistrationDefinition getProductRegistrationDefinitionFromCustomerAndRegistrationId(Customer customer, String registrationId){
        String sql = "select reg.registrationDefinition from com.oup.eac.domain.Registration reg "
                +" join fetch reg.registrationDefinition.pageDefinition"
                +" where reg.id = :registrationId"
                +" and reg.customer = :customer"
                +" and reg.activated = true "
                +" and reg.registrationDefinition.class != com.oup.eac.domain.AccountRegistrationDefinition"
                ;
        ProductRegistrationDefinition result = (ProductRegistrationDefinition)getSession().createQuery(sql)
        .setParameter("customer", customer)
        .setParameter("registrationId", registrationId)
        .uniqueResult();
        return result;
    }
    
	@Override
	public int countSearchRegistrationDefinitions(final RegistrationDefinitionSearchCriteria searchCriteria) {
    	Query query = getQueryForCountRegistrationDefinitions(searchCriteria);
    	setQueryParamsForSearchRegistrationDefinitions(query, searchCriteria);
    	
    	Long count = (Long) query.uniqueResult();

    	return count.intValue();
	}
	
	private Query getQueryForCountRegistrationDefinitions(final RegistrationDefinitionSearchCriteria searchCriteria) {
		final StringBuilder builder = new StringBuilder();
		builder.append("select count(distinct reg) from RegistrationDefinition reg ");
		builder.append("join reg.product pr ");
		builder.append("join pr.division div ");
		builder.append("join div.divisionAdminUsers dau ");
		builder.append("left join pr.externalIds extIds ");
		builder.append("join div.divisionAdminUsers dau ");
		builder.append(getBaseQueryStringForSearchRegistrationDefinitions(searchCriteria));

		return getSession().createQuery(builder.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegistrationDefinition> searchRegistrationDefinitions(final RegistrationDefinitionSearchCriteria searchCriteria, final PagingCriteria pagingCriteria) {
		Query query = getQueryForSearchRegistrationDefinitions(searchCriteria);
		setQueryParamsForSearchRegistrationDefinitions(query, searchCriteria);
		
        query.setFirstResult((pagingCriteria.getRequestedPage() - 1) * pagingCriteria.getItemsPerPage());
        query.setMaxResults(pagingCriteria.getItemsPerPage());
        
		return query.list();
	}
	
	private Query getQueryForSearchRegistrationDefinitions(final RegistrationDefinitionSearchCriteria searchCriteria) {
		//de-duplication
		/*StringBuilder builder = new StringBuilder("select distinct reg from RegistrationDefinition reg ");
		builder.append("join fetch reg.registrationActivation act ");
		builder.append("join fetch reg.product pr ");
		builder.append("left join fetch pr.externalIds extIds ");
		builder.append("join fetch pr.division div ");
		builder.append("join div.divisionAdminUsers dau ");
		builder.append(getBaseQueryStringForSearchRegistrationDefinitions(searchCriteria));
		builder.append("order by pr.productName asc");
		*/
		StringBuilder builder = new StringBuilder("select distinct reg from RegistrationDefinition reg ");
		builder.append("join fetch reg.registrationActivation act ");
		builder.append("join fetch reg.product pr ");
		builder.append("join div.divisionAdminUsers dau ");
		builder.append(getBaseQueryStringForSearchRegistrationDefinitions(searchCriteria));
		//builder.append("order by pr.productName asc");
		
		return getSession().createQuery(builder.toString());
	}
	
	private String getBaseQueryStringForSearchRegistrationDefinitions(final RegistrationDefinitionSearchCriteria searchCriteria) {
		
		StringBuilder builder = new StringBuilder();
		
		if (searchCriteria.getRegistrationDefinitionType() != null) {
			builder.append("where reg.class = :regDefType ");
		} else {
			builder.append("where reg.class <> :regDefType ");
		}
		
		if (searchCriteria.getAdminUser() != null) {
			builder.append("and dau.adminUser = :adminUser ");
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getProductName())) {
			builder.append("and lower(pr.productName) like :productName ");
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getProductId())) {
			builder.append("and lower(pr.id) like :productId ");
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getExternalId())) {
			builder.append("and lower(extIds.externalId) like :externalId ");
		}
			
		if (searchCriteria.getDivision() != null) {
			builder.append("and div = :div ");
		}
		
        Set<ProductState> states = searchCriteria.getProductStates();
        if (states != null && states.isEmpty() == false) {
            builder.append("and pr.state in (:states) ");
        }
		
		return builder.toString();
	}

    private void setQueryParamsForSearchRegistrationDefinitions(final Query query, final RegistrationDefinitionSearchCriteria searchCriteria) {
    	
    	if (searchCriteria.getRegistrationDefinitionType() != null) {
    		query.setParameter("regDefType", searchCriteria.getRegistrationDefinitionType() + "");
    	} else {
    		query.setParameter("regDefType", RegistrationDefinitionType.ACCOUNT_REGISTRATION + "");
    	}
    	
    	if (searchCriteria.getAdminUser() != null) {
    		query.setParameter("adminUser", searchCriteria.getAdminUser());
    	}
    	
    	if (StringUtils.isNotBlank(searchCriteria.getProductName())) {
    		query.setParameter("productName", "%" + searchCriteria.getProductName().toLowerCase() + "%");
    	}
    	
		if (StringUtils.isNotBlank(searchCriteria.getProductId())) {
			query.setParameter("productId", "%" +  searchCriteria.getProductId().toLowerCase() + "%");
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getExternalId())) {
			query.setParameter("externalId", "%" + searchCriteria.getExternalId().toLowerCase() + "%");
		}
		
		if (searchCriteria.getDivision() != null) {
			query.setParameter("div", searchCriteria.getDivision());
		}
				
        Set<ProductState> states = searchCriteria.getProductStates();
        if (states != null && states.isEmpty() == false) {
            query.setParameterList("states", states);
        }
    }
    
      
    @SuppressWarnings("unchecked")
    @Override
    public List<RegistrationDefinition> getRegistrationDefinitionsForProduct(final Product product) {
        return getSession().createQuery("select rd from RegistrationDefinition rd " +
                "join fetch rd.product p " +
                "join fetch rd.registrationActivation ra " + 
                "left outer join fetch p.linkedProducts linked  " +
                "join fetch p.division pd " +
                "where p.id = :productId ")
                .setString("productId", product.getId())
                .list();
    }
    
    @SuppressWarnings("unchecked")
    @Override   
    public final List<ProductRegistrationDefinition> getRegistrationDefinitionsForPageDefinition(String pageDefId) {
		return getSession().createQuery("select rd from ProductRegistrationDefinition rd " +
				"left join fetch rd.pageDefinition pd " +
				"where pd.id = :id")
				.setParameter("id", pageDefId) 
				.list();		
	}
    
    @SuppressWarnings("unchecked")
    @Override   
    public final List<AccountRegistrationDefinition> getRegistrationDefinitionsForPageDefinitionForAccount(String pageDefId) {
		return getSession().createQuery("select rd from AccountRegistrationDefinition rd " +
				"left join fetch rd.pageDefinition pd " +
				"where pd.id = :id")
				.setParameter("id", pageDefId) 
				.list();		
	}

	@Override
	public ActivationCodeRegistrationDefinition getActivationCodeRegistrationDefinitionForEacGroup(EacGroups eacGroup) {
		 return (ActivationCodeRegistrationDefinition)getSession().createQuery("select acrd from ActivationCodeRegistrationDefinition acrd " +
	                "where acrd.eacGroup = :eacGroup")
	                .setParameter("eacGroup", eacGroup)
	                .uniqueResult();
	}
	
	@Override
	public Session getSessionFromDao() {
		return getSession();
	}

	/*@Override
	public void saveRegistrationDefinition(
			ProductRegistrationDefinition productRegistrationDefinition) {
		 	SQLQuery sqlQuery = getSession().createSQLQuery(buildInsertStatement(productRegistrationDefinition));
		    addParameters(productRegistrationDefinition, sqlQuery);         
		    sqlQuery.executeUpdate();
	}

	private String buildInsertStatement(
			ProductRegistrationDefinition productRegistrationDefinition) {
	        StringBuilder insert = new StringBuilder("insert into registration_definition(registration_definition_type, id, obj_version, product_id, page_definition_id, validation_required");
	        StringBuilder values = new StringBuilder("values(:registration_definition_type, 0, :product_id, :page_definition_id, 0");
	        return insert.append(") ").append(values.toString()).append(")").toString();
	}
	
	 private void addParameters(final ProductRegistrationDefinition productRegistrationDefinition, SQLQuery sqlQuery) {
	        sqlQuery.setParameter("registration_definition_type", productRegistrationDefinition.getRegistrationDefinitionType());        
	        //sqlQuery.setParameter("id", productRegistrationDefinition.getId());
	        sqlQuery.setParameter("product_id", productRegistrationDefinition.getProductId());
	        sqlQuery.setParameter("page_definition_id", productRegistrationDefinition.getPageDefinition().getId());
	 }
*/
}
