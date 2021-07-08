package com.oup.eac.domain.migrationtool;

import org.junit.Assert;
import org.junit.Test;

import static com.oup.eac.domain.migrationtool.CustomerMigrationState.*;

/**
 * 
 * @author Chirag Joshi
 *
 */
public class CustomerMigrationStateTest  {

    @Test
    public void testNext(){
        checkNextState(INITIAL, CREATING_CUSTOMER);
        checkNextState(CREATING_CUSTOMER, CUSTOMER_CREATED);        
        
        checkNoNextState(ERROR_CREATING_CUSTOMER);
        
    }
    
    private void checkNoNextState(CustomerMigrationState current){
        checkNextState(current, null);
    }
    
    private void checkNextState(CustomerMigrationState current, CustomerMigrationState next){
        try{
            Assert.assertEquals(next, current.next());
            Assert.assertNotNull(next);
            Assert.assertFalse(current.isEndState());
            Assert.assertFalse(current.isErrorState());
        }catch(Exception ex){
            Assert.assertNull(next);
            Assert.assertTrue(current.isEndState() || current.isErrorState());
        }
    }
    
}
