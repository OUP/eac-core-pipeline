package com.oup.eac.domain.migrationtool;
import java.util.HashSet;
import java.util.Set;

import com.oup.eac.domain.migrationtool.BaseMigrationState;
import com.oup.eac.domain.migrationtool.CustomerMigrationState;


public enum RegistrationMigrationState implements BaseMigrationState {


    INITIAL,
    CREATING_REGISTRATION,
    REGISTRATION_CREATED,    
    ERROR_CREATING_REGISTRATION,;    
    
    private static Set<RegistrationMigrationState> errors = new HashSet<RegistrationMigrationState>();

    static {
        errors.add(ERROR_CREATING_REGISTRATION);        
    }

    @Override
    public RegistrationMigrationState next() {
    	RegistrationMigrationState result = null;
        switch (this) {
        case INITIAL:
            result =  CREATING_REGISTRATION;
            break;
        case CREATING_REGISTRATION:            
            result =  REGISTRATION_CREATED;
            break;
        case ERROR_CREATING_REGISTRATION:
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
        return this == REGISTRATION_CREATED;
    }

    @Override
    public boolean isErrorState() {
        return this == ERROR_CREATING_REGISTRATION;
    }



}
