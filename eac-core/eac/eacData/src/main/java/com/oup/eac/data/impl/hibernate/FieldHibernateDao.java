package com.oup.eac.data.impl.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.FieldDao;
import com.oup.eac.domain.Field;

/**
 * The Element DAO hibernate implementation.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="fieldDao")
public class FieldHibernateDao extends HibernateBaseDao<Field, String> implements FieldDao {

    /**
     * Constructor.
     * 
     * @param sf
     *            the session factory
     */
	@Autowired
    public FieldHibernateDao(final SessionFactory sf) {
        super(sf);
    }

}
