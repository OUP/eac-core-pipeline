package com.oup.eac.data.impl.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.ProgressBarDao;
import com.oup.eac.domain.ProgressBar;
import com.oup.eac.domain.ProgressState;

/**
 * The PageDefinition dao hibernate implementation.
 * 
 * @author harlandd
 */
@Repository(value="progressBarDao")
public class ProgressBarHibernateDao extends HibernateBaseDao<ProgressBar, String> implements ProgressBarDao {

    /**
     * @param sf
     *            the session factory
     */
	@Autowired
    public ProgressBarHibernateDao(final SessionFactory sf) {
        super(sf);
    }

	@Override
	public ProgressBar getProgressBar(ProgressState state, String page) {
		return (ProgressBar)getSession().createQuery("select p from ProgressBar p " +
										"join fetch p.elements e " +
										"where p.page = :page " +
										"and p.userState = :userState " +
										"and p.registrationState = :registrationState " +
										"and p.activationState = :activationState " +
										"and p.tokenState = :tokenState " +
										"and p.registrationType = :registrationType " +
										"and p.activationStrategy = :activationStrategy " +
										"and p.accountValidated = :accountValidated " +
										"order by e.sequence")
										.setParameter("page", page)
										.setParameter("userState", state.getUserState())
										.setParameter("registrationState", state.getRegistrationState())
										.setParameter("activationState", state.getActivationState())
										.setParameter("tokenState", state.getTokenState())
										.setParameter("registrationType", state.getRegistrationType())
										.setParameter("activationStrategy", state.getActivationStrategy())
										.setParameter("accountValidated", state.getAccountValidated())
										.uniqueResult();
	}


}
