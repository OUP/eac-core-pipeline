package com.oup.eac.data.impl.hibernate;

import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.ElementDao;
import com.oup.eac.domain.Element;

/**
 * The Element DAO hibernate implementation.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="elementDao")
public class ElementHibernateDao extends HibernateBaseDao<Element, String> implements ElementDao {

    /**
     * Constructor.
     * 
     * @param sf
     *            the session factory
     */
	@Autowired
    public ElementHibernateDao(final SessionFactory sf) {
        super(sf);
    }

	@Override
	public Element getElementByIdWithFullyFetchedQuestion(final String id) {
		Query query = getSession().createQuery("select e from Element e " +
												"join fetch e.question q " +
												"where e.id = :id");
		query.setParameter("id", id);
		return (Element) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Locale> getOrderedElementCountryRestrictionLocales() {
		return getSession().createQuery("select distinct(ecr.locale) from ElementCountryRestriction ecr order by ecr.locale").list();
	}
	
}
