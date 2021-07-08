package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.PageDefinitionDao;
import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.Question;

/**
 * The PageDefinition dao hibernate implementation.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="accountPageDefinitionDao")
public class AccountPageDefinitionHibernateDao extends HibernateBaseDao<AccountPageDefinition, String> implements PageDefinitionDao<AccountPageDefinition> {

    /**
     * @param sf
     *            the session factory
     */
	@Autowired
    public AccountPageDefinitionHibernateDao(final SessionFactory sf) {
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
    public final AccountPageDefinition getPageDefinitionById(final String id) {
        return (AccountPageDefinition) getSession().createQuery("select distinct pd from AccountPageDefinition pd " 
        												+ "join fetch pd.pageComponents pc "
														+ "join fetch pc.component c "
														+ "join fetch c.fields f " 
														+ "join fetch f.element e "
														+ "join fetch e.question q " 
														+ "join fetch e.tags t " 
														+ "left join fetch e.elementCountryRestrictions ecr "
														+ "where pd.id = :id "
        												+ "order by pc.sequence, f.sequence")
        												.setParameter("id", id)
        												.uniqueResult();
    }

    //division de-duplication
	/*@SuppressWarnings("unchecked")
	@Override
	public List<AccountPageDefinition> getAvailablePageDefinitions(final AdminUser adminUser) {
		return getSession().createQuery("select distinct pd from AccountPageDefinition pd " +
				//"join pd.division div " +
				"join div.divisionAdminUsers dau " +
				"where dau.adminUser = :adminUser " +
				"order by pd.name asc")
				.setParameter("adminUser", adminUser)
				.list();
	}*/
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AccountPageDefinition> getAvailablePageDefinitions(final AdminUser adminUser) {
		return getSession().createQuery("select distinct pd from AccountPageDefinition pd, DivisionAdminUser dau " +
										"where dau.adminUser = :adminUser " +
										"and pd.divisionErightsId = dau.divisionErightsId " +
										"order by pd.name asc" ) 
										.setParameter("adminUser", adminUser)
										.list();
	}
	

    @Override
    public List<Question> getPageDefinitionQuestions(PageDefinition pageDefinition) {
        // TODO Auto-generated method stub
        return null;
    }
}
