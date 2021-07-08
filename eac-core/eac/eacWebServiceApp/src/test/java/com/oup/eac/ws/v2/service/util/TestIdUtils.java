package com.oup.eac.ws.v2.service.util;

import com.oup.eac.domain.ExternalId;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.binding.common.Identifiers;
import com.oup.eac.ws.v2.binding.common.InternalIdentifier;

public class TestIdUtils {

    private TestIdUtils() {
    }

    /**
     * Gets the Identifier structure where we have both and internal and external id.
     * 
     * @param internal
     *            the internal
     * @param external
     *            the external
     * @param systemId
     *            the systemId
     * @return the id
     */
    public static Identifiers getIds(final String internal, final String external, final String systemId, final String systemTypeId) {
        Identifiers result = new Identifiers();
        result.setId(internal);
        if(external != null){
            ExternalIdentifier ext = new ExternalIdentifier();
            ext.setId(external);
            ext.setSystemId(systemId);
            ext.setTypeId(systemTypeId);
            result.setExternal(new ExternalIdentifier[]{ext});
        }
        return result;
    }

    public static Identifier getExternalId(final String external, final String systemId, final String systemTypeId) {
        if (external == null) {
            return null;
        }
        Identifier extID = new Identifier();
        ExternalIdentifier ext = new ExternalIdentifier();
        ext.setId(external);
        ext.setSystemId(systemId);
        ext.setTypeId(systemTypeId);
        extID.setExternalId(ext);
        extID.setInternalId(null);
        return extID;
    }

    public static Identifier getInternalId(final String internal) {
        Identifier intID = new Identifier();
        InternalIdentifier idInt = new InternalIdentifier();
        idInt.setId(internal);
        intID.setInternalId(idInt);
        return intID;
    }

    protected static ExternalIdentifier getExternalIdentifier(ExternalId<?> extId) {
        ExternalIdentifier result = new ExternalIdentifier();
        result.setId(extId.getExternalId());
        ExternalSystemIdType externalSystemTypeId = extId.getExternalSystemIdType();
        result.setSystemId(externalSystemTypeId.getExternalSystem().getName());
        result.setTypeId(externalSystemTypeId.getName());
        return result;
    }

}
