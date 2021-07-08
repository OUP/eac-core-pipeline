package com.oup.eac.log4j;

import org.apache.log4j.Logger;
import org.apache.log4j.net.MySmtpAppender;
import org.junit.Test;

public class MyEventEvaluatorTest {

    private static final Logger LOG = Logger.getLogger(MyEventEvaluatorTest.class);
    
    @Test
    public void test() {
        for(int i=0;i<200;i++){
            LOG.error("event " + i);
        }        
    }
    
    @Test
    public void testSmtp(){
        Logger logger = Logger.getLogger(MySmtpAppender.class);
        logger.debug("This is a test!");
    }
}
