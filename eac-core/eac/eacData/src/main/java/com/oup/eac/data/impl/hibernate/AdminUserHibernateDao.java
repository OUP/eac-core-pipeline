package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.AdminUserDao;
import com.oup.eac.domain.AdminUser;

/**
 * Load admin users.
 * 
 * @author Ian Packard
 *
 */
@Repository(value="adminUserDao")
public class AdminUserHibernateDao extends OUPHibernateBaseDao<AdminUser, String> implements AdminUserDao {

    @Autowired
    public AdminUserHibernateDao(SessionFactory sf) {
        super(sf);
    }

    @Override
    public AdminUser getAdminUserById(final String id) {
        System.out.println("Inside the getAdminUserById method");
        AdminUser adminUser=(AdminUser) getSession().createQuery("select u from AdminUser u " +
        		"left join fetch u.divisionAdminUsers dau " +
        		"left join fetch u.roles r " +
        		"left join fetch r.permissions " +
        		//"join fetch dau.division " +
                "where u.id = :id")
                .setParameter("id",id)
                .uniqueResult(); 
        return adminUser; 
    }
    
    @Override
    public AdminUser getAdminUserWithOutRoleAndPermission(final String id) {
        System.out.println("Inside the getAdminUserById method");
        AdminUser adminUser=(AdminUser) getSession().createQuery("select u from AdminUser u " +
        		"where u.id = :id")
                .setParameter("id",id)
                .uniqueResult(); 
        return adminUser; 
    }
    
    @Override
    public AdminUser getAdminUserByUsername(final String username) {
        
        return (AdminUser) getSession().createQuery("select u from AdminUser u " +
        		"left join fetch u.divisionAdminUsers dau " +
        		"left join fetch u.roles r " +
        		"left join fetch r.permissions " +
        		//"join fetch dau.division " +
                "where u.username = :username")
                .setParameter("username", username)
                .uniqueResult();    
    }

	@Override
	public AdminUser getAdminUserByUsernameUnInitialised(String username) {
        return (AdminUser) getSession().createQuery("select u from AdminUser u " +
                "where u.username = :username")
                .setParameter("username", username)
                .uniqueResult();    
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdminUser> getAllAdminUsersOrderedByName() {
		return getSession().createQuery("select u from AdminUser u order by u.firstName asc").list();
	}

    @Override
    public boolean canAdminDeleteCustomer(String adminId, String customerId) {
        
        // validate customer
        /*long customerCount = (Long) getSession()
                .createQuery("select count(*) from Customer C where C.id = :customerId")
                .setParameter("customerId", customerId).uniqueResult();
        if (customerCount == 0) {
            return false;
        }*/

        // validate admin
        long adminCount = (Long) getSession().createQuery("select count(*) from AdminUser A where A.id = :adminId")
                .setParameter("adminId", adminId).uniqueResult();
        if (adminCount == 0) {
            return false;
        }

        /*long diffDivRegCount =  (Long)getSession()
                .createQuery("select count(*) from Registration R " +
                		" where R.customer.id = :customerId" + 
                		" and R.registrationDefinition.product.division in" +
                        " (" +
                        "   select DAU.division from DivisionAdminUser DAU where DAU.adminUser.id = :adminId " +
                        " )")
                .setParameter("adminId", adminId)
                .setParameter("customerId", customerId)
                .uniqueResult();*/
        boolean result = true;
        return result;
    }
    
}
