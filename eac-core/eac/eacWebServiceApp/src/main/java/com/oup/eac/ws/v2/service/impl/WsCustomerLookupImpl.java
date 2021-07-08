package com.oup.eac.ws.v2.service.impl;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Customer;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.WsCustomerLookup;

public class WsCustomerLookupImpl extends BaseWsLookupImpl implements WsCustomerLookup {

    private CustomerService customerService;

    public WsCustomerLookupImpl(CustomerService customerService) {
    	Assert.notNull(customerService);
        this.customerService = customerService;
    }

    @Override
    //@Cacheable(cacheName = "wsCustomerCache", keyGeneratorName="wsUserIdKeyGenerator")
    public Customer getCustomerByWsUserId(WsUserId wsUserId) throws WebServiceValidationException {
        Customer result = getCustomer(wsUserId);
        if (result == null) {
            throw new WebServiceValidationException(ERR_CUSTOMER_NOT_FOUND);
        }
        return result;
    }

    @Override
    public  Set<Answer> getCustomerWithAnswers(String customerId) throws WebServiceValidationException {
        Set<Answer> result = this.customerService.getCustomerWithAnswersByCustomerId(customerId);
        if (result == null) {
            throw new WebServiceValidationException(ERR_CUSTOMER_NOT_FOUND);
        }
        return result;
    }

    
    private Customer getCustomer(WsUserId wsUserId) throws WebServiceValidationException {
        Customer result = null;
        if (wsUserId == null) {
            return result;
        }
        String sessionKey = wsUserId.getSessionToken();
        String username = wsUserId.getUserName();
        String ip = wsUserId.getIP();
        if (sessionKey != null) {
            result = getCustomerFromSession(sessionKey);
        } else if (username != null) {
            result = getCustomerFromUsername(username);
        } else if (ip != null) {
            result = getCustomerFromIP(ip);
        } else {
            Identifier id = wsUserId.getUserId();
            if (id != null) {
                result = getCustomerFromIdentifier(id);
            }
        }
        return result;
    }


    private interface CustomerLookupFromSession {
        public Customer getCustomer(String sesionKey) throws CustomerNotFoundServiceLayerException;
    }

    
    private Customer getCustomerFromSession(String sessionKey) throws WebServiceValidationException {
        CustomerLookupFromSession lookup = new CustomerLookupFromSession() {
            @Override
            public Customer getCustomer(String sessionKey) throws CustomerNotFoundServiceLayerException {
                return customerService.getCustomerFromSession(sessionKey);
            }
        };
        Customer result = lookupCustomer(sessionKey, lookup);
        return result;
    }


    private Customer lookupCustomer(String sessionKey, CustomerLookupFromSession lookup) throws WebServiceValidationException {
        checkNotBlank(ERR_SESSION_CANNOT_BE_BLANK, sessionKey);
        Customer result = null;
        try {
            result = lookup.getCustomer(sessionKey);
        } catch (CustomerNotFoundServiceLayerException sle) {
            throw new WebServiceValidationException(ERR_CUSTOMER_NOT_FOUND_FOR_SESSION + sessionKey, sle);
        }
        if (result == null) {
            throw new WebServiceValidationException(ERR_CUSTOMER_NOT_FOUND_FOR_SESSION + sessionKey);
        }
        return result;
    }

    private Customer getCustomerFromUsername(String username) throws WebServiceValidationException {
        checkNotBlank(ERR_USERNAME_CANNOT_BE_BLANK, username);
        Customer result = null;
		try {
			result = this.customerService.getCustomerByUsername(username);
		} catch (ErightsException e) {
			if ( e.getErrorCode() != null ) {
				if (e.getErrorCode() == 2057) {
					throw new WebServiceValidationException("No customer found for User Name : " + username);
				}
			}
			 throw new WebServiceValidationException("No customer found for User Name : " + username);
		}

        if (result == null) {
            throw new WebServiceValidationException(ERR_CUSTOMER_NOT_FOUND_FOR_USERNAME + username);
        }
        return result;
    }
    
    private Customer getCustomerFromIP(String ip) throws WebServiceValidationException {        
        throw new WebServiceValidationException(ERR_IP_ADDRESS_UNSUPPORTED);
    }

    private Customer getCustomerFromIdentifier(Identifier id) throws WebServiceValidationException {
        Customer result = null;
        ExternalIdentifier external = id.getExternalId();
        if (external != null) {
            String systemId = external.getSystemId();
            String typeId = external.getTypeId();
            String externalId = external.getId();

            checkNotBlank(ERR_MSG_EXTERNAL, systemId, typeId, externalId);
            try {
				result = this.customerService.getCustomerByExternalCustomerId(systemId, typeId, externalId);
			} catch ( ErightsException e) {
				// TODO Auto-generated catch block
				String msg = String.format(FMT_CUSTOMER_NOT_FOUND_FOR_EXTERNAL_ID, systemId, typeId, externalId);
                throw new WebServiceValidationException(msg);
			}
            
            if (result == null) {
                String msg = String.format(FMT_CUSTOMER_NOT_FOUND_FOR_EXTERNAL_ID, systemId, typeId, externalId);
                throw new WebServiceValidationException(msg);
            }
        }
        if (id.getInternalId() != null && id.getInternalId().getId() != null) {
            String userId = id.getInternalId().getId();
            checkNotBlank(ERR_USERID_CANNOT_BE_BLANK, userId);
            checkOupIdPattern(userId);
            try {
				String regexe = "[{]?[a-fA-F0-9]{8}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{12}[}]?";
				//String input  = userId;
				Pattern pattern = Pattern.compile(regexe);
				Matcher matcher = pattern.matcher(userId);
				 if (matcher.matches()) {
					 result = this.customerService.getCustomerByIdWs(userId);
			      } else {
			    	  throw new WebServiceValidationException(ERR_CUSTOMER_NOT_FOUND_FOR_USERID + userId);
			      }
			}  catch (UserNotFoundException unfe) {
	           
				throw new WebServiceValidationException(ERR_CUSTOMER_NOT_FOUND_FOR_USERID + userId);
	        } catch (ErightsException ee) { 
	            
	        	throw new WebServiceValidationException(ERR_CUSTOMER_NOT_FOUND_FOR_USERID + userId);
	        }

            if (result == null) {
                throw new WebServiceValidationException(ERR_CUSTOMER_NOT_FOUND_FOR_USERID + userId);
            }
        }
        return result;
    }

	private void checkOupIdPattern(String userId) throws WebServiceValidationException {
		String pattern = "[{]?[a-fA-F0-9]{8}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{4}[-]?[a-fA-F0-9]{12}[}]?";
		if(!userId.matches(pattern)){
			throw new WebServiceValidationException(ERR_CUSTOMER_NOT_FOUND_FOR_USERID + userId);
		}
	}

	@Override
	public void checkNotEmpty(WsUserId wsUserId) throws WebServiceValidationException {
		if (wsUserId.getSessionToken() != null ){
			checkNotBlank(ERR_SESSION_CANNOT_BE_BLANK, wsUserId.getSessionToken());
		} else if (wsUserId.getUserName() != null) {
			checkNotBlank(ERR_USERNAME_CANNOT_BE_BLANK, wsUserId.getUserName());
		} else if (wsUserId.getUserId() != null ) {
			if (wsUserId.getUserId().getInternalId() != null ) {
				checkNotBlank(ERR_USERID_CANNOT_BE_BLANK, wsUserId.getUserId().getInternalId().getId());
				checkOupIdPattern(wsUserId.getUserId().getInternalId().getId());
			} else if (wsUserId.getUserId().getExternalId() != null ) {
				checkNotBlank(ERR_MSG_EXTERNAL, wsUserId.getUserId().getExternalId().getSystemId(), wsUserId.getUserId().getExternalId().getTypeId(), wsUserId.getUserId().getExternalId().getId());
			}
		} else if (wsUserId.getIP() != null) {
			throw new WebServiceValidationException(ERR_IP_ADDRESS_UNSUPPORTED);
		}
	}
}
