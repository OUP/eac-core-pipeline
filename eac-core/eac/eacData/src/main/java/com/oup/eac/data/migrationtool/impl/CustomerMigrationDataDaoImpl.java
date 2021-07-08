package com.oup.eac.data.migrationtool.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.impl.hibernate.HibernateBaseDao;
import com.oup.eac.data.migrationtool.CustomerMigrationDataDao;
import com.oup.eac.domain.migrationtool.CustomerMigrationData;

@Repository(value="customerMigrationDataDao")
public class CustomerMigrationDataDaoImpl extends HibernateBaseDao<CustomerMigrationData, String> implements CustomerMigrationDataDao {

    @Autowired
    public CustomerMigrationDataDaoImpl(SessionFactory sf) {
        super(sf);
    }

    @Override
    public long getCount() {
        long result = (Long)getSession().createQuery("select count(cmd) from CustomerMigrationData cmd ").uniqueResult();
        return result;
    }

    @Override
    public List<CustomerMigrationData> findByUserId(String username) {
        @SuppressWarnings("unchecked")
        List<CustomerMigrationData> result = 
                (List<CustomerMigrationData>)getSession().createQuery(
                        " select cmd from CustomerMigrationData cmd " +                        
                        " where cmd.username = :username")
                .setParameter("username", username)
                .list();
        return result;
    }

    public Session fetchSession(){
        return super.getSession();
    }
}
