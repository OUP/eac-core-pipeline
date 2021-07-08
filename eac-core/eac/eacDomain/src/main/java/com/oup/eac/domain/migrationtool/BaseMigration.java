package com.oup.eac.domain.migrationtool;

/**
 * 
 * @author Chirag Joshi
 *
 */
public interface BaseMigration<S extends BaseMigrationState> {

    S getState();
    
    String getId();
}
