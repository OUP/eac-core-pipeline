package com.oup.eac.domain.migrationtool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Chirag Joshi
 */
public enum CustomerMigrationState implements BaseMigrationState{

    INITIAL,
    CREATING_CUSTOMER,
    CUSTOMER_CREATED,
    ERROR_CREATING_CUSTOMER,;    
    
    private static Set<CustomerMigrationState> errors = new HashSet<CustomerMigrationState>();

    static {
        errors.add(ERROR_CREATING_CUSTOMER);        
    }

    @Override
    public CustomerMigrationState next() {
        CustomerMigrationState result = null;
        switch (this) {
        case INITIAL:
            result =  CREATING_CUSTOMER;
            break;
        case CREATING_CUSTOMER:            
            result =  CUSTOMER_CREATED;
            break;
        case CUSTOMER_CREATED:            
            throw getEndStateException();
        case ERROR_CREATING_CUSTOMER:
            throw getErrorStateException();
        }
        return result;
    }

    private IllegalArgumentException getErrorStateException(){
        return new IllegalArgumentException("No Next State : In Error State : " + this);
    }

    private IllegalArgumentException getEndStateException() {
        return new IllegalArgumentException("No Next State : In End State : " + this);
    }


    @Override
    public boolean isEndState() {
        return this == CUSTOMER_CREATED;
    }

    @Override
    public boolean isErrorState() {
        return this == ERROR_CREATING_CUSTOMER;
    }

}
