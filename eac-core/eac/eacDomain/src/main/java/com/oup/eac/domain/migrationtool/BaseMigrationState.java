package com.oup.eac.domain.migrationtool;

/**
 * 
 * @author Chirag Joshi
 *
 */
public interface BaseMigrationState {

    BaseMigrationState next();
    
    boolean isEndState();
    
    boolean isErrorState();
    
    String name();

}
