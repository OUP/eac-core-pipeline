package com.oup.eac.data.impl.hibernate;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.ProductDao;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.Product.ProductType;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.utils.audit.AuditLogger;

/**
 * Product dao hibernate implementation.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="productDao")
public class ProductHibernateDao extends OUPHibernateBaseDao<Product, String> implements ProductDao {

    private static final String PRODUCT_ID = "productId";
    private static Logger LOG = Logger.getLogger(ProductHibernateDao.class);
    
    /**
     * @param sf
     *            the session factory
     */
	@Autowired
    public ProductHibernateDao(final SessionFactory sf) {
        super(sf);
    }
	
	public void saveProduct(final Product product) {
	    SQLQuery sqlQuery = getSession().createSQLQuery(buildInsertStatement(product));
	    addParameters(product, sqlQuery);         
	    sqlQuery.executeUpdate();
	    
	}

    public final RegisterableProduct getRegisterableProductById(final String id) {
        return (RegisterableProduct) getSession().createQuery("select pd from RegisterableProduct pd " +
                                                        "where pd.id = :id")
                                                        .setParameter("id", id)
                                                        .uniqueResult();
    }
	
	
    /**
     * Gets the registerable products by erights id.
     *
     * @param erightsId the erightsId
     * @param states the states
     * @return the list of RegisterableProducts
     */
    @SuppressWarnings("unchecked")
	@Override
    public final List<RegisterableProduct> getRegisterableProductsByErightsId(final Integer erightsId, Set<Product.ProductState> states) {
        return  getSession().createQuery("select pd from RegisterableProduct pd " +        												
        												" where pd.erightsId = :erightsId " +
        												" and pd.state in (:states) "
        												)
        												.setParameter("erightsId", erightsId)
        												.setParameterList("states", states)
        												.list();
    }
    
    /**
     * @param erightsId
     *            the erightsId
     * @return the Product
     */
    @SuppressWarnings("unchecked")
	@Override
    public final List<Product> getProductByErightsId(final String erightsId) {
        return getSession().createQuery("select pd from Product pd " +
                "where pd.id = :id")
                .setParameter("id", erightsId)
                .list();
        /*return getSession().createQuery("select p from Product p " +
                                                      "where p.erightsId = :erightsId")
                                                      .setParameter("erightsId", erightsId)
                                                      .list();*/
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public final List<Product> getProductsByErightsIdsAndAdminUser(final List<String> ids, final AdminUser adminUser) {
    	List<Product> products = getSession().createQuery("select distinct a.erightsId, a.productName from Product p " +
                "join p.division pd " +
                "join pd.divisionAdminUsers dau " +
                "where p.id in (:ids) " +
                "and dau.adminUser = :adminUser")
                .setParameterList("id", ids)
                .setParameter("adminUser", adminUser)
                .list();
    	return products;
    }
    
    @Override
    public final Product getProductById(final String id) {
        return (Product) getSession().createQuery("select pd from Product pd " +
										                "where pd.id = :id")
										                .setParameter("id", id)
										                .uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Product> getProductsByErightsIds(final List<Integer> erightsIds) {
    	return (List<Product>) getSession().createQuery("select pd from Product pd " +    			
                "where pd.erightsId in (:erightsIds)")
                .setParameterList("erightsIds", erightsIds)
                .list();
    }
        
    
    private final String buildQuery(final String divisionType, final String productName, final Integer erightsId) {
    		
    	StringBuilder query = new StringBuilder("select distinct pd from Product pd ");
    	query.append("join fetch pd.division d where 1=1 ");
    	
    	if (divisionType != null) {
    		query.append("and d.divisionType = :divisionType ");
    	}
    	if(StringUtils.isNotBlank(productName)) {
    		query.append("and pd.productName like :productName ");
    	}
    	if(erightsId != null) {
    		query.append("and pd.erightsId = :erightsId ");
    	}
    	
    	query.append("order by pd.productName");
    	
    	return query.toString();
    }

    @Override
    public Product getProductByExternalProductId(String systemId, String typeId, String externalId) {
        Product product = (Product) getSession().createQuery(
                " select pd from Product pd " +
                " join fetch pd.externalIds extid   " +                
                " join fetch extid.externalSystemIdType typeid   " +
                " join fetch typeid.externalSystem system   " +
                " where extid.class = com.oup.eac.domain.ExternalProductId" +
                " and   extid.externalId = :externalId" +
                " and   typeid.name = :typeId" +
                " and   system.name = :systemId")
                .setParameter("typeId", typeId)
                .setParameter("systemId", systemId)
                .setParameter("externalId", externalId)
                .uniqueResult();
        return product;
    }

    @Override
    public Product getProductAndExternalIdsById(String productId) {
        return (Product) getSession().createQuery(
                " select pd from Product pd " +
                " left outer join fetch pd.externalIds extid " +
                " where pd.id = :id"
                )                
                .setParameter("id", productId)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<Product> getProductAndExternalIdsByErightsId(String eRightsProductId) {
        return  getSession().createQuery(
                "select pd from Product pd " +                
                "left outer join fetch pd.externalIds extid " +
                "where pd.id = :id"
                )                
                .setParameter("id", eRightsProductId)
                .list();
    }
    
    private void addParameters(final Product product, SQLQuery sqlQuery) {
        sqlQuery.setParameter("id", product.getId());        
        sqlQuery.setParameter("productType", product.getProductType().toString());
        //product De-duplication
        /*if(StringUtils.isNotBlank(product.getEmail())) {
            sqlQuery.setParameter("email", product.getEmail());
        }
        if(StringUtils.isNotBlank(product.getHomePage())) {
            sqlQuery.setParameter("homePage", product.getHomePage());
        }
        if(StringUtils.isNotBlank(product.getLandingPage())) {
            sqlQuery.setParameter("landingPage", product.getLandingPage());
        }
        if(StringUtils.isNotBlank(product.getServiceLevelAgreement())) {
            sqlQuery.setParameter("serviceLevelAgreement", product.getServiceLevelAgreement());
        }
        if(product.getProductType() == ProductType.REGISTERABLE) {
            sqlQuery.setParameter("registerableType", ((RegisterableProduct)product).getRegisterableType().toString());
        } 
        if(product.getProductType() == ProductType.LINKED) {
            sqlQuery.setParameter("activationMethod", ((LinkedProduct)product).getActivationMethod().toString());
            sqlQuery.setParameter("registerableProductId", ((LinkedProduct)product).getRegisterableProduct().getId());
        }*/
    }

    private String buildInsertStatement(final Product product) {
        StringBuilder insert = new StringBuilder("insert into product(id, obj_version, product_type");
        StringBuilder values = new StringBuilder("values(:id, 0, :productType");
        //product De-duplication
        /*if(StringUtils.isNotBlank(product.getEmail())) {
            insert.append(", email");
            values.append(", :email");
        }
        if(StringUtils.isNotBlank(product.getHomePage())) {
            insert.append(", home_page");
            values.append(", :homePage");
        }
        if(StringUtils.isNotBlank(product.getLandingPage())) {
            insert.append(", landing_page");
            values.append(", :landingPage");
        }
        if(StringUtils.isNotBlank(product.getServiceLevelAgreement())) {
            insert.append(", service_level_agreement");
            values.append(", :serviceLevelAgreement");
        }*/
        /*if(product.getProductType() == ProductType.REGISTERABLE) {
            insert.append(", registerable_type");
            values.append(", :registerableType");
        } 
        if(product.getProductType() == ProductType.LINKED) {
            insert.append(", activation_method, registerable_product_id");
            values.append(", :activationMethod, :registerableProductId");
        }   */    
        return insert.append(") ").append(values.toString()).append(")").toString();
    }
    
    @Override
    public boolean isProductUsed(String productId) {
        boolean result = false;
        try {

            if (!result) {
                //PRODUCT IS REGISTERABLE PRODUCT - affects product search
                long countRegistrations = (Long) getSession()
                        .createQuery("select count(*) from Registration R where R.registrationDefinition.product.id = :productId")
                        .setParameter(PRODUCT_ID, productId).uniqueResult();            
                result = countRegistrations > 0;
            }
            
            if (!result) {
                //PRODUCT IS REGISTERABLE PRODUCT - affects product search
            	long countLinkedProducts = (Long) getSession()
                        .createQuery(
                                "select count(*) from LinkedProduct LP where LP.registerableProduct.id = :productId")
                        .setParameter(PRODUCT_ID, productId).uniqueResult();
                result = countLinkedProducts > 0;
            }
            
            if (!result) {
                //PRODUCT IS REGISTERABLE PRODUCT - affects product search
                long countACBs = (Long) getSession()
                        .createQuery("select count(*) from ActivationCodeBatch ACB where ACB.activationCodeRegistrationDefinition.product.id = :productId")
                        .setParameter(PRODUCT_ID, productId).uniqueResult();
                result = countACBs > 0;
            }

            if (!result) {
                //PRODUCT IS LINKED PRODUCT - affects Associated Product List in EAC ADMIN
                long countLinkedRegistrations = (Long) getSession()
                        .createQuery(
                                "select count(*) from LinkedRegistration LR where LR.linkedProduct.id = :productId")
                        .setParameter(PRODUCT_ID, productId).uniqueResult();
                result = countLinkedRegistrations > 0;
            }
            
            if (!result) {
                long countRegistrations = (Long) getSession()
                        .createQuery("select count(*) from EacGroups eg join eg.products pr where pr.id = :productId")
                        .setParameter(PRODUCT_ID, productId).uniqueResult();            
                result = countRegistrations > 0;
            }

        } finally {
            if (LOG.isDebugEnabled()) {
                String msg = String.format("isProduct [%s] used [%b]", productId, result);
                LOG.debug(msg);
            }
        }
        return result;
    }
    
    @Override
    public boolean deleteUnusedProduct(String productId) {
        boolean result = false;
        try {
            
            //delete any external product ids
            getSession().createQuery("delete from ExternalProductId epid where epid.product.id = :productId").setParameter(PRODUCT_ID, productId).executeUpdate();
            
            int deleted1 = getSession().createQuery("delete from RegistrationDefinition rd where rd.product.id = :productId").setParameter(PRODUCT_ID, productId).executeUpdate();
            
            int deleted2 = getSession().createQuery("delete from Product p where p.id = :productId").setParameter(PRODUCT_ID, productId).executeUpdate();

            result = deleted1 >= 0 && deleted2 == 1;
            
        } catch (HibernateException ex) {
            String msg = String.format("unexpected exception attempting to delete unused product " + productId);
            LOG.error(msg,ex);
        }
        if (result) {
            String msg = String.format("Deleted Unused Product : Id [%s]", productId);
            AuditLogger.logEvent(msg);
            LOG.info(msg);
        }
        return result;
    }
    
    @Override
    public Product getProductByName(String name) {
    	//product de-duplication
        return null ;//(Product) getSession().createQuery("select pd from RegisterableProduct pd " +                
                //"where pd.productName = :name)")
                //.setParameter("name", name)
                //.uniqueResult();
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<LinkedProduct> getProductsLinkedDirectToProduct(Product product) {
        return  getSession().createQuery(
                "select lp from LinkedProduct lp " +
                "where lp.id = :productId"
                )                
                .setParameter("productId", product.getId())
                .list();
    }
    
    @Override
	public Product getProductByErightsIdAndAdminUser(final Integer erightsId, final AdminUser adminUser) {
        return (Product) getSession().createQuery("select p from Product p " +
        		"join p.division div " +
        		"join div.divisionAdminUsers dau " +
                "where p.erightsId = :erightsId " +
                "and dau.adminUser = :adminUser ")
                .setParameter("erightsId", erightsId)
                .setParameter("adminUser", adminUser)
                .uniqueResult();
	}
}
