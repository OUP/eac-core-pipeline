package com.oup.eac.ws.v2.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;

import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.dto.WsUserIdDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ErightsSessionNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.exceptions.SessionNotFoundServiceLayerException;
import com.oup.eac.ws.v2.binding.access.KillUserSessionRequest;
import com.oup.eac.ws.v2.binding.access.KillUserSessionResponse;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.KillUserSessionAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;

/**
 * The Class KillUserSessionAdapterImpl.
 */
public class KillUserSessionAdapterImpl extends BaseUserAdapter implements KillUserSessionAdapter {

    private static final Logger LOG = Logger.getLogger(KillUserSessionAdapterImpl.class);
    public static final String FMT_CUSTOMER_NOT_FOUND_FOR_EXTERNAL_ID = "No customer found for External Id : systemId[%s] typeId[%s] id[%s]";
    public static final String ERR_CUSTOMER_NOT_FOUND_FOR_USERNAME = "No customer found for User Name : ";
    public static final String ERR_CUSTOMER_NOT_FOUND_FOR_USERID = "No customer found for User Id : ";
    public static final String ERR_CUSTOMER_NOT_FOUND_FOR_SESSION = "No customer found for Session Token : ";
    public static final String ERR_FURTHER_SESSION_AVAILABLE = "There are further concurrent login sessions available for this user. A login session will be killed once all concurrent login sessions are in use." ;
    protected final CustomerService customerService;
    private final WsCustomerLookup customerLookup;
    
    public KillUserSessionAdapterImpl(MessageSource messageSource, CustomerService customerService, WsCustomerLookup customerLookup, UsernameValidator usernameValidator) {
        super(messageSource,customerService, usernameValidator);
        Assert.notNull(customerLookup);
        this.customerService = customerService;
        this.customerLookup = customerLookup;
    }
    

    @Override
    @PreAuthorize("hasRole('ROLE_WS_KILL_USER_SESSION')")
    public KillUserSessionResponse killUserSession(KillUserSessionRequest request) throws WebServiceException {
        
        KillUserSessionResponse response = new KillUserSessionResponse();
        WsUserId wsUserId = request.getWsUserId();
        try {
            this.customerLookup.checkNotEmpty(wsUserId);
            WsUserIdDto wsUserIdDto = convertWsUserIdtoDto(wsUserId) ;
            customerService.killUserSession(wsUserIdDto);
        } catch (WebServiceValidationException wsve) {
            LOG.debug("Web service validation exception occured", wsve);
            ErrorStatus status = ErrorStatusUtils.getClientErrorStatus(wsve.getMessage());
            response.setErrorStatus(status);
        } catch (ErightsException e) {
        	if (e.getErrorCode() == 2026 || e.getErrorCode() == 2057 || e.getErrorCode() == 2129 ) {
        		if (e.getErrorCode() == 2129) {
	        		ErrorStatus status = ErrorStatusUtils.getClientErrorStatus(String.format(FMT_CUSTOMER_NOT_FOUND_FOR_EXTERNAL_ID, wsUserId.getUserId().getExternalId().getSystemId(), wsUserId.getUserId().getExternalId().getTypeId(), wsUserId.getUserId().getExternalId().getId()));
	                response.setErrorStatus(status);
        		} else if ( e.getErrorCode() == 2057 ) {
        			ErrorStatus status = ErrorStatusUtils.getClientErrorStatus(ERR_CUSTOMER_NOT_FOUND_FOR_USERNAME + wsUserId.getUserName());
	                response.setErrorStatus(status);
        		} else if (e.getErrorCode() == 2026) {
        			ErrorStatus status = ErrorStatusUtils.getClientErrorStatus(ERR_CUSTOMER_NOT_FOUND_FOR_USERID + wsUserId.getUserId().getInternalId().getId());
	                response.setErrorStatus(status);
        		}
        	} else if (e.getErrorCode() == 2151){
        		ErrorStatus status = ErrorStatusUtils.getClientErrorStatus(ERR_FURTHER_SESSION_AVAILABLE);
                response.setErrorStatus(status);
        	} else if (e.getErrorCode() == 2152 ) {
        		ErrorStatus status = ErrorStatusUtils.getClientErrorStatus(ERR_CUSTOMER_TYPE_INVALID + "SHARED");
                response.setErrorStatus(status);
        	} else if (e.getErrorCode() == 1045 ) {
        		ErrorStatus status = ErrorStatusUtils.getClientErrorStatus(ERR_CUSTOMER_NOT_FOUND_FOR_SESSION + request.getWsUserId().getSessionToken());
                response.setErrorStatus(status);
        	} else if (e.getErrorCode() == 2010 ) {
        		ErrorStatus status = ErrorStatusUtils.getServerErrorStatus("Session not found");
                response.setErrorStatus(status);
        	} else {
        		ErrorStatus status = ErrorStatusUtils.getServerErrorStatus(e.getMessage());
                response.setErrorStatus(status);
        	}
			// TODO Auto-generated catch block
        	LOG.debug("Erights exception occured", e);
		} catch (Exception e) {
			LOG.debug("problem killing user session", e);
            throw new WebServiceException("problem killing user session", e);
		}
        return response;
    }
    
    private WsUserIdDto convertWsUserIdtoDto(WsUserId wsUserId) {
    	WsUserIdDto wsUserIdDto = new WsUserIdDto() ;
    	wsUserIdDto.setSessionToken(wsUserId.getSessionToken());
    	wsUserIdDto.setUserName(wsUserId.getUserName());
    	if (wsUserId.getUserId() != null && wsUserId.getUserId().getInternalId() != null) {
    		wsUserIdDto.setUserId(wsUserId.getUserId().getInternalId().getId());
    	}
    	else if (wsUserId.getUserId() != null && wsUserId.getUserId().getExternalId() != null) {
    		ExternalIdentifier extId = wsUserId.getUserId().getExternalId() ;
    		wsUserIdDto.setExternalId( new ExternalIdDto(extId.getSystemId(), extId.getTypeId(), extId.getId()));
    	}
    	
    	return wsUserIdDto ;
    }


	
}
