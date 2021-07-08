package com.oup.eac.data;

import java.util.List;
import java.util.Set;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;

/**
 * @author harlandd Product dao interface
 */
public interface ProductDao extends OUPBaseDao<Product, String> {
    
    void saveProduct(Product product);
    
    RegisterableProduct getRegisterableProductById(String id);

	Product getProductById(String id);
	Product getProductByErightsIdAndAdminUser(final Integer erightsId, final AdminUser adminUser) ;
	List<Product> getProductsByErightsIdsAndAdminUser(List<String> erightsIds, AdminUser adminUser);
    /**
     * Get a {@link List} of {@link RegisterableProduct}s by their erights id.
     *
     * @param erightsId the erightsId
     * @param states the states
     * @return the list of RegisterableProducts.
     */
	List<RegisterableProduct> getRegisterableProductsByErightsId(Integer erightsId, Set<Product.ProductState> states);
   
    /**
     * Get a list of product definitions filtered by product owner.
     * 
     * @param productOwners
     *      The product owners for the product definitions to return
     * @return
     *      The list of filtered product definitions.
     */
	
	//division removal
   /* List<Product> getProductByDivision(String divisionType, 
			String productName, Integer erightsId, 
			int pageSize, int page);*/

    
    Product getProductByExternalProductId(String systemId, String typeId, String externalId);

    /**
     * Get a product by its erights id.
     * 
     * @param erightsId the erightsId
     * @return the Product
     */
    List<Product> getProductByErightsId(String erightsId);
    
    /**
     * Gets the product and external ids by erights id.
     *
     * @param eRightsProductId the erights id
     * @return the product and external ids by erights id
     */
    List<Product> getProductAndExternalIdsByErightsId(String eRightsProductId);

    /**
     * Get a list of product definitions when provided with a list of
     * erights ids.
     * 
     * @param erightsIds
     * @return
     */
	List<Product> getProductsByErightsIds(List<Integer> erightsIds);
	
	
	/**
	 * Gets the product and external ids by id.
	 *
	 * @param productId the product id
	 * @return the product and external ids by id
	 */
	Product getProductAndExternalIdsById(String productId);

    /**
     * Checks if is product used.
     *
     * @param productId the product id
     * @return true, if is product used
     */
    boolean isProductUsed(String productId);

    /**
     * Delete unused product.
     *
     * @param productId the product id
     * @return true, if successful
     */
    boolean deleteUnusedProduct(String productId);

    List<LinkedProduct> getProductsLinkedDirectToProduct(Product product);
    Product getProductByName(String name);
}
