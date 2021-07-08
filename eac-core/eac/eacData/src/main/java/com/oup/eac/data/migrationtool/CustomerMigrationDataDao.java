package com.oup.eac.data.migrationtool;

import java.util.List;

import org.hibernate.Session;

import com.oup.eac.data.BaseDao;
import com.oup.eac.domain.migrationtool.CustomerMigrationData;


public interface CustomerMigrationDataDao extends BaseDao<CustomerMigrationData, String> {

	
	/**
     * Gets the count.
     *
     * @return the count
     */
    long getCount();
    
    List<CustomerMigrationData> findByUserId(String userId);
    
    Session fetchSession();
}
