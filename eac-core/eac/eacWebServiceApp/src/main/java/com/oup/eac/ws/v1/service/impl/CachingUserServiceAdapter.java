package com.oup.eac.ws.v1.service.impl;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;

import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Customer;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v1.service.UserServiceAdapter;
import com.oup.eac.ws.v1.userdata.binding.RegistrationInformation;
import com.oup.eac.ws.v1.userdata.binding.RegistrationInformationResponse;
import com.oup.eac.ws.v1.userdata.binding.User;
import com.oup.eac.ws.v1.userdata.binding.UserNameResponse;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.WsCustomerLookup;

/**
 * User services adapter that provides caching.
 * 
 */
public class CachingUserServiceAdapter implements UserServiceAdapter {

    private static final Logger LOG = Logger.getLogger(CachingUserServiceAdapter.class);
    private WsCustomerLookup wsCustomerLookup;

    /**
     * Default Constructor.
     */
    public CachingUserServiceAdapter() {
        super();
    }

    /**
     * Constructor.
     *
     * @param wsCustomerLookup the ws customer lookup
     */
    public CachingUserServiceAdapter(final WsCustomerLookup wsCustomerLookup) {
        super();
        Assert.notNull(wsCustomerLookup);
        this.wsCustomerLookup = wsCustomerLookup;
    }

    /**
     * @see com.oup.eac.ws.v1.service.UserServiceAdapter#getUserName(java.lang.String)
     * @param sessionToken
     *            session token
     * @return UserNameResponse
     * @throws ServiceLayerException
     *             the exception
     */
    @PreAuthorize("hasRole('ROLE_WS_USER_NAME')")
    public final UserNameResponse getUserName(final String sessionToken) throws ServiceLayerException {
        LOG.info("getUserName from sessiontoken:" + sessionToken);
        UserNameResponse response = new UserNameResponse();
        Customer customer = getCustomerFromSessionToken(sessionToken);
        if (customer != null) {
            response.setUserName(customer.getFullName());
        } else {
            throw new ServiceLayerException("No customer found for session " + sessionToken);
        }
        return response;
    }

    private Customer getCustomerFromSessionToken(String sessionToken) throws ServiceLayerException {
        Customer customer = null;
        try{
            WsUserId wsUserId = new WsUserId();
            wsUserId.setSessionToken(sessionToken);
            customer = wsCustomerLookup.getCustomerByWsUserId(wsUserId);
        }catch(WebServiceValidationException ex){
            Throwable cause = ex.getCause();
            if(cause instanceof ServiceLayerException){
                throw (ServiceLayerException)cause;
            }
        }
        return customer;
    }
    private Set<Answer> getCustomerWithAns(Customer param){
        Set<Answer> answer = null;
        try{
            if(param != null){
                answer = wsCustomerLookup.getCustomerWithAnswers(param.getId());
            }
        }catch(WebServiceValidationException ex){
        	answer = null;
        }
        return answer;
    }
    /**
     * @see com.oup.eac.ws.v1.service.UserServiceAdapter#getRegistrationInformation(java.lang.String)
     * @param sessionToken
     *            session token
     * @return RegistrationInformationResponse
     * @throws ServiceLayerException
     *             the exception
     */
    @PreAuthorize("hasRole('ROLE_WS_REGISTRATION_INFORMATION')")
    public final RegistrationInformationResponse getRegistrationInformation(final String sessionToken) throws ServiceLayerException {
        LOG.info("getRegistrationInformation from sessiontoken:" + sessionToken);

        final RegistrationInformationResponse response = new RegistrationInformationResponse();
        final User user = new User();
        response.setUser(user);

        WsUserId wsUserId = new WsUserId();
        wsUserId.setSessionToken(sessionToken);
        
        Customer customerNoAns = getCustomerFromSessionToken(sessionToken);
        final Set<Answer> answers = getCustomerWithAns(customerNoAns);
                
        if (customerNoAns != null) {
            user.setEmailAddress(customerNoAns.getEmailAddress());
            user.setFirstName(customerNoAns.getFirstName());
            user.setLastName(customerNoAns.getFamilyName());
            user.setUserName(customerNoAns.getUsername());
            //final Set<Answer> answers = customerNoAns.getAnswers();
            RegistrationInformation registrationItem = null;
            for (Answer answer : answers) {
                //TODO This must be fixed once a final decision has been made
                //     about the export name.
               String exportName = answer.getQuestion().getDescription();
                if (StringUtils.isNotBlank(exportName)) {
                    registrationItem = new RegistrationInformation();
                    registrationItem.setRegistrationKey(exportName);
                    registrationItem.setRegistrationValue(answer.getAnswerText());
                    response.addRegistrationInformation(registrationItem);
                } else {
                    LOG.warn("No export name defined for field id:" + answer.getQuestion().getId());
                }                
            }
        } else {
            throw new ServiceLayerException("No customer found for session " + sessionToken);
        }
        return response;
    }
    
}
