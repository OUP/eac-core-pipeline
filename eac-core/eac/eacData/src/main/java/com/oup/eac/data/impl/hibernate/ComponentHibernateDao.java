package com.oup.eac.data.impl.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.ComponentDao;
import com.oup.eac.domain.Component;

@Repository(value="componentDao")
public class ComponentHibernateDao extends HibernateBaseDao<Component, String> implements ComponentDao {

	@Autowired
    public ComponentHibernateDao(final SessionFactory sf) {
        super(sf);
    }

}
