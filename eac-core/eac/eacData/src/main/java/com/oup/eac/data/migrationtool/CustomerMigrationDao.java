package com.oup.eac.data.migrationtool;

import java.util.List;

import com.oup.eac.data.BaseDao;
import com.oup.eac.domain.migrationtool.CustomerMigration;
import com.oup.eac.domain.migrationtool.CustomerMigrationState;

public interface CustomerMigrationDao extends BaseDao<CustomerMigration, String> {

    /**
     * Gets the in state.
     *
     * @param initial the initial
     * @param fetchSize the fetch size
     * @return the in state
     */
    List<CustomerMigration> getInState(CustomerMigrationState initial, int fetchSize);
    
    /**
     * Gets the ids in state.
     *
     * @param initial the initial
     * @param fetchSize the fetch size
     * @return the ids in state
     */
    List<String> getIdsInState(CustomerMigrationState initial, int fetchSize);

    /**
     * Gets the customers migration to process.
     *
     * @param id the id
     * @return the customers migration to process
     */
    CustomerMigration getCustomersMigrationToProcess(String id);

    /**
     * Gets the count.
     *
     * @return the count
     */
    long getCount();

    /**
     * Gets the count.
     *
     * @param state the state
     * @return the count
     */
    long getCount(CustomerMigrationState state);

    /**
     * Gets the not finished count.
     *
     * @return the not finished count
     */
    long getNotFinishedCount();
    
    
}
