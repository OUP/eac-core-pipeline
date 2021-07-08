package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.PageDefinitionDao;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.Question;

/**
 * The PageDefinition dao hibernate implementation.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="productPageDefinitionDao")
public class ProductPageDefinitionHibernateDao extends HibernateBaseDao<ProductPageDefinition, String> implements PageDefinitionDao<ProductPageDefinition> {

    /**
     * @param sf
     *            the session factory
     */
	@Autowired
    public ProductPageDefinitionHibernateDao(final SessionFactory sf) {
        super(sf);
    }

    /**
     * @param division
     *            the product
     * @param registrationType
     *            the registrationType
     * @return the PageDefinition
     */
	/* Gaurav Soni:
	 * #EAC Performance
	 * Added setCacheable and setCacheRegion
	 * */
    @Override
    public final ProductPageDefinition getPageDefinitionById(final String id) {
        return (ProductPageDefinition) getSession().createQuery("select distinct pd from ProductPageDefinition pd " 
        												+ "join fetch pd.pageComponents pc "
        												+ "join fetch pc.component c "
        												+ "join fetch c.fields f " 
        												+ "join fetch f.element e "
        												+ "left join fetch e.elementCountryRestrictions ecr "
        												+ "join fetch e.question q " 
        												+ "join fetch e.tags t " 
        												+ "where pd.id = :id "
        												+ "order by pc.sequence, f.sequence")
        												.setParameter("id", id)
        												.uniqueResult();
    }
    
    
    /**
     * @param division
     *            the product
     * @param registrationType
     *            the registrationType
     * @return the PageDefinition
     */
    @SuppressWarnings("unchecked")
    @Override
    public final List<Question> getPageDefinitionQuestions(final PageDefinition pageDefinition) {
        return  getSession().createQuery("select q from Element e " 
                                                + "join e.question q " 
                                                + "join e.fields f " 
                                                + "join f.component c " 
                                                + "join c.pageComponents pc " 
                                                + "join pc.pageDefinition pd "
                                                + "where pd = :pageDefinition")
                                                .setParameter("pageDefinition", pageDefinition)
                                                .list();
    }

    //division de-duplication
	/*@SuppressWarnings("unchecked")
	@Override
	public List<ProductPageDefinition> getAvailablePageDefinitions(final AdminUser adminUser) {
		return getSession().createQuery("select distinct pd from ProductPageDefinition pd " +
										"join pd.division div " +
										"join div.divisionAdminUsers dau " +
										"where dau.adminUser = :adminUser " +
										"order by pd.name asc")
										.setParameter("adminUser", adminUser)									
										.list();
	}*/
    
    @SuppressWarnings("unchecked")
	@Override
	public List<ProductPageDefinition> getAvailablePageDefinitions(final AdminUser adminUser) {
		return getSession().createQuery("select distinct pd from ProductPageDefinition pd, DivisionAdminUser dau " +
										"where dau.adminUser = :adminUser " +
										"and pd.divisionErightsId = dau.divisionErightsId " +
										"order by pd.name asc" )
										.setParameter("adminUser", adminUser)									
										.list();
	}
    
}
