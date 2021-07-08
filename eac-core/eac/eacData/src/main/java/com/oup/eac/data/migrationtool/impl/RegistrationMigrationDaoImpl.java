package com.oup.eac.data.migrationtool.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.impl.hibernate.HibernateBaseDao;
import com.oup.eac.data.migrationtool.RegistrationMigrationDao;
import com.oup.eac.domain.migrationtool.RegistrationMigration;
import com.oup.eac.domain.migrationtool.RegistrationMigrationState;

@Repository(value="registrationMigrationDao")
public class RegistrationMigrationDaoImpl extends HibernateBaseDao<RegistrationMigration, String> implements RegistrationMigrationDao{

private static final Logger LOG = Logger.getLogger(RegistrationMigrationDaoImpl.class);
	 
    @Autowired 
    public RegistrationMigrationDaoImpl(SessionFactory sf) {
        super(sf);
    }
    
    @Override
    public List<RegistrationMigration> getInState(RegistrationMigrationState state, int fetchSize) {
    	Session session = getSession();
        @SuppressWarnings("unchecked")
        List<RegistrationMigration> migrations =
        		session.createQuery(
        		"select rm from RegistrationMigration rm " +
        		" where rm.state = :state " +        		
        		" order by rm.id"
        		)
        		.setMaxResults(fetchSize)
        		.setParameter("state", state)
        		.list();
        return migrations;
    }
    
    @Override
    public List<String> getIdsInState(RegistrationMigrationState state, int fetchSize) {
    	Session session = getSession();
		@SuppressWarnings("unchecked")
		List<String> migrations =
        		session.createQuery(
        		"select rm.id from RegistrationMigration rm " +
        		" where rm.state = :state " +        		
        		" order by rm.id"
        		)
        		.setMaxResults(fetchSize)
        		.setParameter("state", state)        		
        		.list();
        return migrations;
    }
    
    @Override
	public long getCount() {
		long result = (Long)getSession().createQuery("select count(rm) from RegistrationMigration rm ").uniqueResult();
		return result;
	}

    @Override
	public RegistrationMigration getRegistrationMigrationToProcess(String id) {
    	System.out.println("Insidet the getRegistrationMigrationToProcess ");
    	Session session = getSession();
    	RegistrationMigration migration = (RegistrationMigration)
        		session.createQuery(
        		" select rm from RegistrationMigration rm " +
        		" join fetch rm.data rmData" +
        		" join fetch rm.customerMigration cm " +
        		" join fetch cm.data cmData " +
        	    " left outer join fetch rm.registration registration" +
        		" where rm.id = :id "
        		)
        .setParameter("id", id)
        .uniqueResult();
        return migration;
	}
    
    @Override
	public long getCount(RegistrationMigrationState state) {
		long result = (Long)getSession()
		        .createQuery("select count(rm) from RegistrationMigration rm where rm.state = :state")
		        .setParameter("state",state)
		        .uniqueResult();
		return result;
	}
    
    
    @Override
    public List<RegistrationMigration> findAllForOrcsCustomerMigrationId(String orcsCustomerMigrationId) {
        @SuppressWarnings("unchecked")
        List<RegistrationMigration> result = (List<RegistrationMigration>)getSession()
                .createQuery(                
                        " select rm" +
                        " from RegistrationMigration rm" +
                        " join fetch rm.data rmd" +
                        " where rm.customerMigration.id = :cmId")
                .setParameter("cmId", orcsCustomerMigrationId)
                .list();
        return result;
    }
    
    @Override
    public long getNotFinishedCount() {
        List<RegistrationMigrationState> endStates = new ArrayList<RegistrationMigrationState>();
        for(RegistrationMigrationState state : RegistrationMigrationState.values()){
            if(state.isEndState() || state.isErrorState()){
                endStates.add(state);
            }
        }
        Session session = getSession();
        Long count = (Long)
                session.createQuery(
                        " select count(rm) " +
                        " from RegistrationMigration rm " +
                        " where rm.state not in (:endStates)"
                        )
                .setParameterList("endStates",endStates)
                .uniqueResult();
        return count;
    }

}
