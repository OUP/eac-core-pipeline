package com.oup.eac.data.migrationtool;

import java.util.List;

import com.oup.eac.data.BaseDao;
import com.oup.eac.domain.migrationtool.RegistrationMigration;
import com.oup.eac.domain.migrationtool.RegistrationMigrationState;

public interface RegistrationMigrationDao extends BaseDao<RegistrationMigration, String>{


    /**
     * Gets the in state.
     *
     * @param initial the initial
     * @param fetchSize the fetch size
     * @return the in state
     */
    List<RegistrationMigration> getInState(RegistrationMigrationState initial, int fetchSize);
    
    /**
     * Gets the ids in state.
     *
     * @param initial the initial
     * @param fetchSize the fetch size
     * @return the ids in state
     */
    List<String> getIdsInState(RegistrationMigrationState initial, int fetchSize);

    /**
     * Gets the registration migration to process.
     *
     * @param id the id
     * @return the registration migration to process
     */
    RegistrationMigration getRegistrationMigrationToProcess(String id);

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
    long getCount(RegistrationMigrationState state);


    
    /**
     * Creates the registration migration records.
     *
     * @param customerMigrationId the customer migration id
     * @param status the status
     * @return the int
     */
//    int createRegistrationMigrationRecords(String customerMigrationId, Status status);

    List<RegistrationMigration> findAllForOrcsCustomerMigrationId(String orcsCustomerMigrationId);
    
    long getNotFinishedCount();

}
