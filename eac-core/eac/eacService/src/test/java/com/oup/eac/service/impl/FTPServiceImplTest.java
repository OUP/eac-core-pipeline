package com.oup.eac.service.impl;

import java.io.ByteArrayInputStream;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.data.AbstractDBTest;
import com.oup.eac.domain.FTPCriteria;
import com.oup.eac.service.FTPService;

@Ignore
public class FTPServiceImplTest extends AbstractDBTest {

    @Autowired
    private FTPService ftpService;
    

    /**
     * @throws Exception
     *             Sets up data ready for test.
     */
    @Before
    public final void setUp() throws Exception {
        ftpService = new FTPServiceImpl();
    }
    
    @Test
    public final void testActivationCodeBatchGeneration() throws Exception {
        
        String txt = "hi there how are you";
        FTPCriteria ftpCriteria = FTPCriteria.valueOf("test.txt", new ByteArrayInputStream(txt.getBytes()), "localhost", "harlandd", "", "dir");
        ftpService.ftp(ftpCriteria);
    }

}
