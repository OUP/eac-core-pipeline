package com.oup.eac.data;

import java.util.List;

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
 * The Interface ExternalIdDao.
 */
public interface ExternalIdDao extends BaseDao<ExternalId<?>, String> {

    public ExternalId<?> getIdOnly(String id);
    
    public ExternalId<?> getIdAndExtSysIdTypeOnly(String id);

    public ExternalCustomerId findExternalCustomerIdByCustomerAndTypeAndExternalId(Customer customer, ExternalSystemIdType idType, String externalId);

    public List<ExternalCustomerId> findExternalCustomerIdByCustomerAndSystem(Customer customer, ExternalSystem extSys);
    
    public List<ExternalProductId> findExternalProductIdByProductAndSystem(Product product, ExternalSystem extSys);
    
    /**
     * Gets the external ids for customer filtered by systemId.
     *
     * @param customer the customer to get the external ids for
     * @param systemId the system id - used to filter the external customer ids - a null systemId means no filter.
     * @return the external ids for customer
     */
    public ExternalCustomerIdDto getExternalIdsForCustomer(Customer customer, String systemId);
    
    /**
     * Gets the external ids for products filtered by systemId.
     *
     * @param products the products to get the external ids for
     * @param systemId the system id - used to filter the external product ids - a null systemId means no filter.
     * @return the external ids for products
     */
    public ExternalProductIdDto getExternalIdsForProducts(List<Product> products, String systemId);
    /*
     * Start Rishit : getFullUserentitlement
     */
    public ExternalProductIdDto getExternalIdsForProductsAndLinkedProducts(List<Product> products, String systemId);
    /*
     * End Rishit : getFullUserentitlement
     */
    /**
     * Find external customer id by system and type and external id.
     *
     * @param systemId the system id
     * @param systemTypeId the system type id
     * @param externalId the external id
     * @return the external customer id
     */
    public ExternalCustomerId findExternalCustomerIdBySystemAndTypeAndExternalId(String systemId, String systemTypeId, String externalId);
    
    /**
     * Find external product id by system and type and external id.
     *
     * @param systemId the system id
     * @param systemTypeId the system type id
     * @param externalId the external id
     * @return the external product id
     */
    public ExternalProductId findExternalProductIdBySystemAndTypeAndExternalId(String systemId, String systemTypeId, String externalId);
}
