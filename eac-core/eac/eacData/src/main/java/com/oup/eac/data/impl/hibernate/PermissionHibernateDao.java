package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.PermissionDao;
import com.oup.eac.domain.Permission;
import com.oup.eac.domain.Role;

@Repository(value="permissionDao")
public class PermissionHibernateDao extends HibernateBaseDao<Permission, String> implements PermissionDao {

	@Autowired
    public PermissionHibernateDao(final SessionFactory sf) {
        super(sf);
    }  
	
    @SuppressWarnings("unchecked")
    public final List<Permission> getAllPermissions() {
        return getSession().createQuery("select p from Permission p order by p.name asc").list();
    }	
	
	@SuppressWarnings("unchecked")
    public final List<Permission> getPermissionsByRole(final Role role) {
	    return getSession().createQuery("select p from Permission p join p.roles r where r = :role order by p.name asc").setParameter("role", role).list();
	}
	
    @SuppressWarnings("unchecked")
    public final List<String> getPermissionIdsByRole(final Role role) {
        return getSession().createQuery("select distinct(p.id) from Permission p join p.roles r where r = :role").setParameter("role", role).list();
    }
	
}
