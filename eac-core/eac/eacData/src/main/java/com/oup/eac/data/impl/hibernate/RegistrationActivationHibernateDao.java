package com.oup.eac.data.impl.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.RegistrationActivationDao;
import com.oup.eac.domain.RegistrationActivation;

@Repository(value="registrationActivationDao")
public class RegistrationActivationHibernateDao extends HibernateBaseDao<RegistrationActivation, String> implements RegistrationActivationDao {

    @Autowired
    public RegistrationActivationHibernateDao(final SessionFactory sf) {
        super(sf);
    }

    @SuppressWarnings("unchecked")
	@Override
	public List<RegistrationActivation> findAllRegistrationActivationsOrderedByType() {
		return getSession().createQuery("select ra from RegistrationActivation ra order by activation_type").list();
	}
    
}
