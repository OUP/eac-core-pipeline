package com.oup.eac.data.impl.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.LinkedRegistrationDao;
import com.oup.eac.domain.LinkedRegistration;

/**
 * The Class LinkedRegistrationHibernateDao.
 * 
 * @author David Hay
 * @author Ian Packard
 */
@Repository(value="linkedRegistrationDao")
public class LinkedRegistrationHibernateDao extends HibernateBaseDao<LinkedRegistration, String> implements LinkedRegistrationDao{

	@Autowired
    public LinkedRegistrationHibernateDao(SessionFactory sf) {
        super(sf);
    }
}
