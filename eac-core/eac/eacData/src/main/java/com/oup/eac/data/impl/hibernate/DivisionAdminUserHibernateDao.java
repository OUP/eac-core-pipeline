package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.DivisionAdminUserDao;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.DivisionAdminUser;

/**
 * The Answer dao hibernate implementation.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="divisionAdminUserDao")
public class DivisionAdminUserHibernateDao extends HibernateBaseDao<DivisionAdminUser, String> implements DivisionAdminUserDao {

    /**
     * @param sf
     *            the session factory
     */
	@Autowired
    public DivisionAdminUserHibernateDao(final SessionFactory sf) {
        super(sf);
    }  
    
    @SuppressWarnings("unchecked")
	public final List<DivisionAdminUser> getDivisionAdminUserByDivision(final Division division) {
    	return getSession().createQuery("select dau from DivisionAdminUser dau " +
    			"join fetch dau.adminUser au " +
    			"where dau.division = :division")
    			.setParameter("division", division)
    			.list();
    }

    @Override
    public DivisionAdminUser getDivisionAdminUserByDivisionAndAdmin(Division division, AdminUser adminUser) {
        DivisionAdminUser result = (DivisionAdminUser) getSession().createQuery(
                " select dau from DivisionAdminUser dau " +
                " join   fetch dau.adminUser au " +
                //" join   fetch dau.division div " +
                //" where  dau.division  = :division" +
                " where    dau.adminUser = :adminUser" +
                " and dau.divisionErightsId = :divisionErightsId"
                )                
                .setParameter("adminUser", adminUser)
                .setParameter("divisionErightsId",  division.getErightsId())
                .uniqueResult();
        return result;
    }
    
	@Override
	public List<DivisionAdminUser> getDivisionAdminUserByAdmin(AdminUser adminUser) {
		return getSession().createQuery(
                " select dau from DivisionAdminUser dau " +
                " join   fetch dau.adminUser au " +
                " where  dau.adminUser = :adminUser")                
                .setParameter("adminUser", adminUser)
                .list();
	}
    
}
