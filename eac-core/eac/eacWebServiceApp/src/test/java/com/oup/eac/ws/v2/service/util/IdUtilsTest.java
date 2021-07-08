package com.oup.eac.ws.v2.service.util;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.binding.common.Identifiers;

/**
 * Tests the IdUtils class.
 * @author David Hay
 *
 */
public class IdUtilsTest {

    @Test
    public void testIdInternalOnly() {
        String internalid = UUID.randomUUID().toString();
        Identifier id = TestIdUtils.getInternalId(internalid);
        Assert.assertNull(id.getExternalId());
        Assert.assertEquals(internalid,id.getInternalId().getId());
    }
    
    @Test
    public void testId(){
        String internalid = UUID.randomUUID().toString();
        String externalid = UUID.randomUUID().toString();
        String systemId = UUID.randomUUID().toString();
        String systemTypeId = UUID.randomUUID().toString();
        Identifiers ids = TestIdUtils.getIds(internalid, externalid, systemId, systemTypeId);
        ExternalIdentifier extID = ids.getExternal()[0];
        Assert.assertEquals(internalid, ids.getId());
        Assert.assertEquals(externalid, extID.getId());
        Assert.assertEquals(systemId, extID.getSystemId());
        Assert.assertEquals(systemTypeId, extID.getTypeId());
    }
    
}


