package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.RoleDao;
import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;

@Repository(value="roleDao")
public class RoleHibernateDao extends HibernateBaseDao<Role, String> implements RoleDao {

	@Autowired
    public RoleHibernateDao(final SessionFactory sf) {
        super(sf);
    }  
	
	public Long getCountUsersWithRole(final String roleId) {
	    return (Long)getSession().createQuery("select count(u) from User u join u.roles r where r.id = :roleId").setParameter("roleId", roleId).uniqueResult();
	}
	
    @Override
    public Role getRoleWithPermissions(final String id) {
        return (Role)getSession().createQuery("select r from Role r " +
                                                "join fetch r.permissions p where r.id = :id")
                                                .setParameter("id", id)
                                                .uniqueResult();
    }
    
    @Override
    public Long getRoleCountByNameAndNotId(final String name, String id) {
        return (Long)getSession().createQuery("select count(r) from Role r " +
                                                "where r.id != :id and r.name = :name")
                                                .setParameter("id", id)
                                                .setParameter("name", name)
                                                .uniqueResult();
    }
	
    @SuppressWarnings("unchecked")
    @Override
    public final List<Role> getRolesWithPermission(final Permission permission) {
        return getSession().createQuery("select r from Role r " +
                "join r.permissions p " +
                "where p = :permission")
                .setParameter("permission", permission)
                .list();
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRolesOrderedByNameWithFullyFetchedPermissions() {
        List<Role> roles = getSession().createQuery("select r from Role r " +
                "order by r.name")
                .list();
        for (Role role : roles) {
        	Hibernate.initialize(role.getPermissions());
        }
        return roles;
	}
}
