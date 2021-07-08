package com.oup.eac.data.impl.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.QuartzLogEntryDao;
import com.oup.eac.domain.QuartzLogEntry;

/**
 * Hibernate implementation of QuartzLogEntryDao interface.
 * 
 * @author David Hay.
 * @author Ian Packard
 */
@Repository(value="quartzLogEntryDao")
public class QuartzLogEntryHibernateDao extends HibernateBaseDao<QuartzLogEntry, String> implements QuartzLogEntryDao{

	@Autowired
    public QuartzLogEntryHibernateDao(SessionFactory sf) {
        super(sf);
    }

}
