package com.oup.eac.data.impl.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.ExternalIdDao;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalId;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.Product;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.dto.ExternalProductIdDto;

/**
 * The Class ExternalIdHibernateDao.
 * 
 * @author David Hay
 * @author Ian Packard
 */
@Repository(value="externalIdDao")
public class ExternalIdHibernateDao extends HibernateBaseDao<ExternalId<?>, String> implements ExternalIdDao {

    private static final List<ExternalProductId> EMPTY_LIST = Collections.unmodifiableList(Collections.<ExternalProductId> emptyList());
    private static final List<Product> EMPTY_PRODUCT_LIST = Collections.unmodifiableList(Collections.<Product> emptyList());

    public static final String FILTER_SQL = " and extSys.name = :systemId ";
    
    public static final String CUSTOMER_SQL = "select distinct extid from " 
            + " ExternalCustomerId extid"
            + " join fetch extid.customer as c" //eagerly fetch external system type ids
            + " join fetch extid.externalSystemIdType as extSysIdType" //eagerly fetch external system type ids 
            + " join fetch extSysIdType.externalSystem as extSys" //eagerly fetch external systems
            + " where c.id = :cid "
            ;

    public static final String CUSTOMER_SQL_WITH_FILTER = CUSTOMER_SQL + FILTER_SQL;
    
    public static final String PRODUCT_SQL = "select distinct extid from " 
            + " ExternalProductId extid"
            + " join fetch extid.product as p" //eagerly fetch external system type ids
            + " join fetch extid.externalSystemIdType as extSysIdType" //eagerly fetch external system type ids 
            + " join fetch extSysIdType.externalSystem as extSys" //eagerly fetch external systems                
            + " where p in (:products) "
            ;
    
    /* Start Rishit: Added for getfullUserEntitlement */
    public static final String REGISTERABLE_PRODUCT_SQL = "select distinct p from " 
    		+ "RegisterableProduct as p where p.id in (:productIDs) "; 
    		
    
    public static final String REGISTERABLE_PRODUCT_SQL_WITH_FILTER = REGISTERABLE_PRODUCT_SQL + FILTER_SQL;
    
    /* End Rishit: Added for getfullUserEntitlement */
    public static final String PRODUCT_SQL_WITH_FILTER = PRODUCT_SQL + FILTER_SQL;
    
    
    /**
     * Instantiates a new external id hibernate dao.
     *
     * @param sf the sf
     */
    @Autowired
    public ExternalIdHibernateDao(final SessionFactory sf) {
        super(sf);
    }
    
    private List<ExternalCustomerId> getExternalCustomerIds(Customer customer, String systemId){
        boolean useSystemId = StringUtils.isNotBlank(systemId);        
        
        String querySQL = useSystemId ? CUSTOMER_SQL_WITH_FILTER : CUSTOMER_SQL;
        
        Query q = getSession().createQuery(querySQL);
        q.setParameter("cid", customer.getId());
        if(useSystemId){
            q.setParameter("systemId", systemId);
        }
        @SuppressWarnings("unchecked")
        List<ExternalCustomerId> result = (List<ExternalCustomerId>)q.list();
        return result;
    }

    private List<ExternalProductId> getExternalProductIds(List<? extends Product> products, String systemId){
        
        if(products == null || products.isEmpty()){
            return EMPTY_LIST;
        }
        
        boolean useSystemId = StringUtils.isNotBlank(systemId);        
        
        String querySQL = useSystemId ? PRODUCT_SQL_WITH_FILTER : PRODUCT_SQL;
        
        Query q = getSession().createQuery(querySQL);
        q.setParameterList("products", products);
        if(useSystemId){
            q.setParameter("systemId", systemId);
        }
        @SuppressWarnings("unchecked")
        List<ExternalProductId> result = q.list();
        return result;
    }

    /* Start Rishit: Added for getfullUserEntitlement
     */
    private List<Product> getExternalProductIdsForLinkedProduct(List<? extends Product> products, String systemId){
        if(products == null || products.isEmpty()){
            return EMPTY_PRODUCT_LIST;
        }
        
        boolean useSystemId = StringUtils.isNotBlank(systemId);
        
        String querySQL = useSystemId ? REGISTERABLE_PRODUCT_SQL_WITH_FILTER : REGISTERABLE_PRODUCT_SQL;
        
        Query q = getSession().createQuery(querySQL);
        
        List<String> productIDs = new ArrayList<String>();
        for(Product p: products){
            if(p!=null){
            	productIDs.add(p.getId());
            }
        }
        
        q.setParameterList("productIDs", productIDs);
        if(useSystemId){
            q.setParameter("systemId", systemId);
        }
        @SuppressWarnings("unchecked")
        List<Product> result = q.list();
        return result;
    }
    /* End Rishit: Added for getfullUserEntitlement
     */
    
    public ExternalId<?> getIdOnly(String id){
        return (ExternalId<?>) getSession().createQuery("select extId from ExternalCustomerId extId where extId.id = :id").setParameter("id",id).uniqueResult();
    }
    
    public ExternalId<?> getIdAndExtSysIdTypeOnly(String id){
        ExternalId<?> result = (ExternalId<?>) getSession().createQuery("select extId from ExternalCustomerId extId " +
                "join fetch extId.externalSystemIdType extSysIdType " +
                "where extId.id = :id").setParameter("id",id).uniqueResult();
        return result;
    }

    @Override
    public ExternalCustomerId findExternalCustomerIdByCustomerAndTypeAndExternalId(Customer customer,
            ExternalSystemIdType idType, String externalId) {
        ExternalCustomerId result = (ExternalCustomerId) getSession().createQuery(
                " select extId " +
                " from   ExternalCustomerId extId " +
                " where  extId.customer.id = :customerId " +
                " and    extId.externalSystemIdType.id = :typeId" +
                " and    extId.externalId = :externalId"
                )
                .setParameter("customerId",customer.getId())
                .setParameter("typeId",idType.getId())
                .setParameter("externalId",externalId)
                .uniqueResult();
        return result;
    }

    @Override
    public List<ExternalCustomerId> findExternalCustomerIdByCustomerAndSystem(Customer customer, ExternalSystem extSys) {
        @SuppressWarnings("unchecked")
        List<ExternalCustomerId> result = (List<ExternalCustomerId>) getSession().createQuery(
                " select extId " +
                " from   ExternalCustomerId extId " +
                " where  extId.customer = :customer " +
                " and    extId.externalSystemIdType.externalSystem = :externalSystem"
                )
                .setParameter("customer",customer)                
                .setParameter("externalSystem",extSys)
                .list();
        return result;
    }
    
    @Override
    public List<ExternalProductId> findExternalProductIdByProductAndSystem(Product product, ExternalSystem extSys) {
        @SuppressWarnings("unchecked")
        List<ExternalProductId> result = (List<ExternalProductId>) getSession().createQuery(
                " select extId " +
                " from   ExternalProductId extId " +
                " where  extId.product = :product " +
                " and    extId.externalSystemIdType.externalSystem = :externalSystem"
                )
                .setParameter("product",product)                
                .setParameter("externalSystem",extSys)
                .list();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override    
    public ExternalCustomerIdDto getExternalIdsForCustomer(Customer customer, String systemId) {
        List<ExternalCustomerId> ids = getExternalCustomerIds(customer, systemId);
        ExternalCustomerIdDto result = new ExternalCustomerIdDto(customer,ids);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalProductIdDto getExternalIdsForProducts(List<Product> products, String systemId) {
        List<ExternalProductId> ids = this.getExternalProductIds(products, systemId);
        Map<String,List<ExternalProductId>> productMap = new HashMap<String, List<ExternalProductId>>();
        if(ids != null){
            for(ExternalProductId id : ids){
                String productId = id.getProduct().getId();
                List<ExternalProductId> exts = productMap.get(productId);
                if(exts == null){
                    exts = new ArrayList<ExternalProductId>();
                    productMap.put(productId,exts);
                }
                exts.add(id);
            }
        }
        ExternalProductIdDto result = new ExternalProductIdDto(products, productMap);
        return result;
    }

    /* Start Rishit: Added for getfullUserEntitlement
     * (non-Javadoc)
     */
    
    public ExternalProductIdDto getExternalIdsForProductsAndLinkedProducts(List<Product> products, String systemId){
        List<Product> registerdProducts = new ArrayList<Product>();
        List<Product> linkedProducts = new ArrayList<Product>();
        Map<String,List<ExternalProductId>> productMap = new HashMap<String, List<ExternalProductId>>();
        
        for(Product product: products){
            if(product!=null){
                if(product.getProductType().equals(Product.ProductType.REGISTERABLE)){
                    registerdProducts.add(product);
                }
                if(product.getProductType().equals(Product.ProductType.LINKED)){
                    linkedProducts.add(product);
                }
            }
        }
        
        //process registered product
        List<ExternalProductId> ids = this.getExternalProductIds(registerdProducts, systemId);
        if(ids != null){
            for(ExternalProductId id : ids){
                String productId = id.getProduct().getId();
                List<ExternalProductId> exts = productMap.get(productId);
                if(exts == null){
                    exts = new ArrayList<ExternalProductId>();
                    productMap.put(productId,exts);
                }
                exts.add(id);
            }
        }
       
        //process linked product
        if(linkedProducts!=null && linkedProducts.size()>0){
            List<Product> productIds  = this.getExternalProductIdsForLinkedProduct(linkedProducts, systemId);
            List<ExternalProductId> linkedProductIds = this.getExternalProductIds(productIds, systemId);
            
            if(linkedProductIds != null){
                for(ExternalProductId id : linkedProductIds){
                    for(Product p: linkedProducts){
                        if(id.getProduct().getId().equals(p.getId())){
                            String productId = p.getId();
                            List<ExternalProductId> exts = productMap.get(productId);
                            if(exts == null){
                                exts = new ArrayList<ExternalProductId>();
                                productMap.put(productId,exts);
                            }
                            exts.add(id);
                        }
                    }
                }
            }
        }
        ExternalProductIdDto result = new ExternalProductIdDto(products, productMap);
        return result;
    }
    
    /*
     * End Rishit: Added for getfullUserEntitlement
     * (non-Javadoc)
     */
    @Override
    public ExternalCustomerId findExternalCustomerIdBySystemAndTypeAndExternalId(String systemId, String systemTypeId, String externalId) {        
        ExternalCustomerId result = (ExternalCustomerId) getSession().createQuery(
                " select extId "
                + " from   ExternalCustomerId extId "
                + " join fetch extId.customer "
                + " where  extId.externalId = :externalId"
                + " and    extId.externalSystemIdType.name = :systemTypeId "
                + " and    extId.externalSystemIdType.externalSystem.name = :externalSystem"
                )
                .setParameter("externalId",externalId)
                .setParameter("systemTypeId",systemTypeId)
                .setParameter("externalSystem",systemId)
                .uniqueResult();
        return result;
    }
    
    @Override
    public ExternalProductId findExternalProductIdBySystemAndTypeAndExternalId(String systemId, String systemTypeId, String externalId) {
        ExternalProductId result = (ExternalProductId) getSession().createQuery(
                " select extId "
                + " from   ExternalProductId extId "
                + " join fetch extId.product "
                + " where  extId.externalId = :externalId"
                + " and    extId.externalSystemIdType.name = :systemTypeId "
                + " and    extId.externalSystemIdType.externalSystem.name = :externalSystem"
                )
                .setParameter("externalId",externalId)
                .setParameter("systemTypeId",systemTypeId)
                .setParameter("externalSystem",systemId)
                .uniqueResult();
        return result;
    }
    

}
