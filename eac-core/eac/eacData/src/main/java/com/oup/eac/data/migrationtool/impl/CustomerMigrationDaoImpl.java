package com.oup.eac.data.migrationtool.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.impl.hibernate.HibernateBaseDao;
import com.oup.eac.data.migrationtool.CustomerMigrationDao;
import com.oup.eac.domain.migrationtool.CustomerMigration;
import com.oup.eac.domain.migrationtool.CustomerMigrationState;

@Repository(value="customerMigrationDao")
public class CustomerMigrationDaoImpl extends HibernateBaseDao<CustomerMigration, String> implements CustomerMigrationDao {

	private static final Logger LOG = Logger.getLogger(CustomerMigrationDaoImpl.class);
	
	@Autowired
    public CustomerMigrationDaoImpl(SessionFactory sf) {
        super(sf);
    }

    @Override
    public List<CustomerMigration> getInState(CustomerMigrationState state, int fetchSize) {
        Session session = getSession();
        @SuppressWarnings("unchecked")
        List<CustomerMigration> migrations =
                session.createQuery(
                "select cm from CustomerMigration cm " +
                " where cm.state = :state " +
                " order by cm.id"
                )
                .setMaxResults(fetchSize)
                .setParameter("state", state)
                .list();
        return migrations;
    }
    
    @Override
    public List<String> getIdsInState(CustomerMigrationState state, int fetchSize) {
        Session session = getSession();
        @SuppressWarnings("unchecked")
        List<String> migrations =
                session.createQuery(
                "select cm.id from CustomerMigration cm " +
                " where cm.state = :state " +
                " order by cm.id"
                )
                .setMaxResults(fetchSize)
                .setParameter("state", state)
                .list();
        return migrations;
    }


    @Override
    public CustomerMigration getCustomersMigrationToProcess(String id) {
        Session session = getSession();
        CustomerMigration migration = (CustomerMigration)
                session.createQuery(
                " select cm from CustomerMigration cm " +
                " join fetch cm.data data" +
                " left outer join fetch cm.customer customer" +
                " where cm.id = :id "
                )
        .setParameter("id", id)
        .uniqueResult();
        return migration;
    }
    

    @Override
    public long getCount() {
        long result = (Long)getSession().createQuery("select count(cm) from CustomerMigration cm ").uniqueResult();
        return result;
    }

    @Override
    public long getCount(CustomerMigrationState state) {
        long result = (Long)getSession().createQuery("select count(cm) from CustomerMigration cm where cm.state = :state").setParameter("state",state).uniqueResult();
        return result;
    }

    

    @Override
    public long getNotFinishedCount() {
        List<CustomerMigrationState> endStates = new ArrayList<CustomerMigrationState>();
        for(CustomerMigrationState state : CustomerMigrationState.values()){
            if(state.isEndState() || state.isErrorState()){
                endStates.add(state);
            }
        }
        Session session = getSession();
        Long count = (Long)
                session.createQuery(
                        " select count(cm) " +
                        " from CustomerMigration cm " +
                        " where cm.state not in (:endStates)"
                        )
                .setParameterList("endStates",endStates)
                .uniqueResult();
        return count;
    }

}
