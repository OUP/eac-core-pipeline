package com.oup.eac.ws.v2.service.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.ExternalId;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.binding.common.Identifiers;
import com.oup.eac.ws.v2.binding.common.InternalIdentifier;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;

/**
 * The Class IdUtils. Utility class for Identifiers.
 * 
 * @author David Hay
 */
public final class IdUtils {

    /**
     * Instantiates a new id utils.
     */
    private IdUtils() {
    }

    public static Identifiers getIds(String id, Collection<? extends ExternalId<?>> extIds) {
        Identifiers result = new Identifiers();
        if(StringUtils.isNotBlank(id)){
            result.setId(id);
        }
        if(extIds != null){
            List<ExternalIdentifier> temp = new ArrayList<ExternalIdentifier>();
            for(ExternalId<?> extId : extIds){
                ExternalIdentifier external = getExternalIdentifier(extId);
                temp.add(external);                
            }
            ExternalIdentifier[] ext = new ExternalIdentifier[temp.size()];
            temp.toArray(ext);
            result.setExternal(ext);
        }
        
        return result;
    }
    
    private static ExternalIdentifier getExternalIdentifier(ExternalId<?> extId) {
        ExternalIdentifier result = new ExternalIdentifier();
        result.setId(extId.getExternalId());
        ExternalSystemIdType externalSystemTypeId = extId.getExternalSystemIdType();
        result.setSystemId(externalSystemTypeId.getExternalSystem().getName());
        result.setTypeId(externalSystemTypeId.getName());
        return result;
    }
    
    public static Identifier getInternalIdentifier(String internalId){
        InternalIdentifier intId = new InternalIdentifier();
        intId.setId(internalId);
        Identifier result = new Identifier();
        result.setInternalId(intId);
        return result;
    }

    public static void validateSetExternalIds(final String systemId, final ExternalIdentifier[] externals) throws WebServiceValidationException {
        if (StringUtils.isBlank(systemId)) {
            throw new WebServiceValidationException("The systemId cannot be blank");
        }
        for (ExternalIdentifier external : externals) {
            if (StringUtils.isBlank(external.getId())) {
                throw new WebServiceValidationException("The externalIds cannot be blank");
            }
            if (StringUtils.isBlank(external.getTypeId())) {
                throw new WebServiceValidationException("The typeIds cannot be blank");
            }
            if (systemId.equalsIgnoreCase(external.getSystemId()) == false) {
                throw new WebServiceValidationException("Different systemIds are present in the request");
            }
        }
    }

    public static List<ExternalIdDto> getExternalIdDtos(ExternalIdentifier[] externals) throws WebServiceValidationException {
        List<ExternalIdDto> result = new ArrayList<ExternalIdDto>();
        for (ExternalIdentifier external : externals) {
            ExternalIdDto dto = new ExternalIdDto(external.getSystemId(), external.getTypeId(), external.getId());
            result.add(dto);
        }
        return result;
    }

}
