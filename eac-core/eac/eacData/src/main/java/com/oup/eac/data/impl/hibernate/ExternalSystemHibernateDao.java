package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.ExternalSystemDao;
import com.oup.eac.domain.ExternalSystem;

/**
 * The Class ExternalSystemHibernateDao.
 * 
 * @author David Hay
 * @author Ian Packard
 */
@Repository(value="externalSystemDao")
public class ExternalSystemHibernateDao extends HibernateBaseDao<ExternalSystem, String> implements ExternalSystemDao{

	@Autowired
    public ExternalSystemHibernateDao(SessionFactory sf) {
        super(sf);
    }

    @Override
    /**
     * The findByName is case insensitive it assumes that the external system names are stored in lower case.
     */
    public ExternalSystem findByName(String name) {
        //external system names are stored in lower case
        return (ExternalSystem) getSession().createQuery("select es from ExternalSystem es where es.name = :name").setParameter("name",name).uniqueResult();
    }

    /**
     * Find external systems ordered by name.
     */
	@Override
	@SuppressWarnings("unchecked")
	public List<ExternalSystem> findAllExternalSystemsOrderedByName() {
		return getSession().createQuery("select es from ExternalSystem es order by es.name").list();
	}

}
