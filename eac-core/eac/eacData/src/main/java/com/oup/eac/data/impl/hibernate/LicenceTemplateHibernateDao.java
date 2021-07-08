/**
 * 
 */
package com.oup.eac.data.impl.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.LicenceTemplateDao;
import com.oup.eac.domain.LicenceTemplate;

/**
 * The Class LicenceTemplateHibernateDao.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="licenceTemplateDao")
public class LicenceTemplateHibernateDao extends HibernateBaseDao<LicenceTemplate, String> implements LicenceTemplateDao {

    /**
     * @param sf
     *            the session factory
     */
	@Autowired
    public LicenceTemplateHibernateDao(final SessionFactory sf) {
        super(sf);
    }
}
