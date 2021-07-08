package com.oup.eac.data.impl.hibernate;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.UrlSkinDao;
import com.oup.eac.domain.UrlSkin;

/**
 * Hibernate Dao class for the UrlSkin entity.
 * 
 * @author David Hay
 * @author Ian Packard
 */
@Repository(value="urlSkinDao")
public class UrlSkinHibernateDao extends HibernateBaseDao<UrlSkin, String> implements UrlSkinDao {

    private static final Logger LOG = Logger.getLogger(UrlSkinHibernateDao.class);
    
    @Autowired
    public UrlSkinHibernateDao(SessionFactory sf) {
        super(sf);
    }

    @PostConstruct
    public void init(){
        LOG.debug("UrlSkinHibernateDao initialised");
    }
}
