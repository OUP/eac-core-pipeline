package com.oup.eac.data;

import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.data.message.DbMessageSourceReloader;
import com.oup.eac.data.util.SampleDataCreator;
import com.oup.eac.domain.Message;

/**
 * Base Class which extends AbstractDbTest with ability to create DbMessages in the 
 * test database and have them available through DbMessageSources.
 * 
 * @author David Hay
 *
 */
public abstract class AbstractDbMessageTest extends AbstractDBTest {
    
    private SampleDataCreator insertOnlyDataCreator;
    
    @Autowired
    private DbMessageSourceReloader reloader;
    
    public AbstractDbMessageTest() {
        this.insertOnlyDataCreator = new SampleDataCreator();//this is for insert only!
    }

    protected final Message createDbMessage(String basename, String messageKey, String messageText) throws Exception {        
        Message message = this.insertOnlyDataCreator.createMessage(basename, null, messageKey, messageText);
        return message;
    }

    /**
     * Loads all test data into the database for each test
     * where we clean the table and then insert test data.
     * 
     * @throws Exception
     *             checked exception thrown by method
     */
    @Override
    protected final void loadAllDataSets() throws Exception {
        super.loadAllDataSets();
        loadAllDataSets(DatabaseOperation.INSERT, this.insertOnlyDataCreator);
        reloader.onMessageSourceReload();
    }

}
