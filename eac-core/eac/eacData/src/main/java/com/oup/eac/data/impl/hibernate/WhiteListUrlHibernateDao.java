package com.oup.eac.data.impl.hibernate;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.WhiteListUrlDao;
import com.oup.eac.domain.WhiteListUrl;

@Repository(value="whiteListUrlDao")
public class WhiteListUrlHibernateDao extends HibernateBaseDao<WhiteListUrl, String> implements WhiteListUrlDao {

	 private static final Logger LOG = Logger.getLogger(WhiteListUrlHibernateDao.class);
	    
	    @Autowired
	    public WhiteListUrlHibernateDao(SessionFactory sf) {
	        super(sf);
	    }

	    @PostConstruct
	    public void init(){
	        LOG.debug("whiteListUrlHibernateDao initialised");
	    }
	    
		@SuppressWarnings("unchecked")
		@Override
		public List<WhiteListUrl> getAllUrls() {
		       return getSession().createQuery("from WhiteListUrl a " 
		        								+ "where a.date_deleted is NULL ")
		        								.list();
	}
	
}
