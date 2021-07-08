package com.oup.eac.ws.v2.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.domain.entitlement.ProductEntitlementGroupDto;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.UserEntitlementsService;
import com.oup.eac.ws.v2.binding.access.GetUserEntitlementsResponse;
import com.oup.eac.ws.v2.binding.access.GetUserEntitlementsResponseSequence;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.ProductEntitlement;
import com.oup.eac.ws.v2.binding.common.ProductEntitlementGroup;
import com.oup.eac.ws.v2.binding.common.User;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.UserEntitlementsAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.WsExternalSystemLookup;
import com.oup.eac.ws.v2.service.entitlements.CustomerConverter;
import com.oup.eac.ws.v2.service.entitlements.EntitlementsAdapterHelper;

/**
 * The Class UserEntitlementsAdapterImpl.
 */
public class UserEntitlementsAdapterImpl implements UserEntitlementsAdapter {

    /** The Constant LOG. */
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(UserEntitlementsAdapterImpl.class);

    /** The registration service. */
    private final RegistrationService registrationService;

    /** The customer converter. */
    private final CustomerConverter customerConverter;

    /** The customer lookup. */
    private final WsCustomerLookup customerLookup;
    
    /** The external system lookup */
    private final WsExternalSystemLookup externalSystemLookup;
    
    private final ExternalIdService externalIdService;
    
    private UserEntitlementsService userEntitlementsService;
    
    private EntitlementsAdapterHelper entitlementsHelper;

    public UserEntitlementsAdapterImpl(
    		final WsCustomerLookup customerLookup1,
    		final RegistrationService registrationService1,
            final CustomerConverter customerConverter1, 
            final ExternalIdService externalIdService,
            final WsExternalSystemLookup externalSystemLookup,
            final UserEntitlementsService userEntitlementsService, 
            final EntitlementsAdapterHelper entitlementsHelper) {    	
    	Assert.notNull(customerLookup1);
    	Assert.notNull(registrationService1);
    	Assert.notNull(customerConverter1);
    	Assert.notNull(externalSystemLookup);
    	Assert.notNull(externalIdService);
        this.customerLookup = customerLookup1;
        this.registrationService = registrationService1;
        this.customerConverter = customerConverter1;
        this.externalIdService = externalIdService;
        this.externalSystemLookup = externalSystemLookup;
        this.userEntitlementsService = userEntitlementsService;
        this.entitlementsHelper = entitlementsHelper;
    }

    /**
     * {@inheritDoc}
     * @throws ErightsException 
     * @throws AccessDeniedException 
     */
    @Override
    @PreAuthorize("hasRole('ROLE_WS_GET_USER_ENTITLEMENTS')")
    public GetUserEntitlementsResponse getUserEntitlementGroups(String systemId, WsUserId wsUserId) throws WebServiceException{
    	long startTime = System.currentTimeMillis();
    	GetUserEntitlementsResponse response = new GetUserEntitlementsResponse();
        try {
            Customer customer = customerLookup.getCustomerByWsUserId(wsUserId);

            this.externalSystemLookup.validateExternalSystem(systemId);
            
            //the data in the dto has the correct external ids set for products and users
            CustomerRegistrationsDto registrationsDto = this.registrationService.getEntitlementsForCustomerRegistrations(customer,null, false);
            customer = registrationsDto.getUser();
            ExternalCustomerIdDto extCustIdDto = this.externalIdService.getExternalCustomerIds(customer,systemId);
            
            //seq
            GetUserEntitlementsResponseSequence seq = new GetUserEntitlementsResponseSequence();
            response.setGetUserEntitlementsResponseSequence(seq);
            
            //seq-user
            User user = this.customerConverter.convertCustomerToUser(extCustIdDto);
            seq.setUser(user);

            //seq-groups
            List<ProductEntitlementGroupDto> groups = this.userEntitlementsService.getUserEntitlementGroups(registrationsDto, systemId);
            ProductEntitlementGroup[] entitlementGroups = getGroupArray(groups);
            //groups.toArray(entitlementGroups);
            seq.setEntitlementGroup(entitlementGroups);
            
        } catch (WebServiceValidationException wsve) {
            setErrorStatus(wsve, response);
        } catch (ServiceLayerException sle) {
            throw new WebServiceException(sle.getMessage(),sle);
        } catch (AccessDeniedException e) {
        	setErrorStatus(e, response);
		} catch (ErightsException e) {
			setErrorStatus(e, response);
		}
        AuditLogger.logEvent(":: Time to getUserEntitlementGroups :: " + (System.currentTimeMillis() - startTime));
        return response;
    }

    private void setErrorStatus(Exception ex, GetUserEntitlementsResponse resp) {
        ErrorStatus errorStatus = new ErrorStatus();
        errorStatus.setStatusCode(StatusCode.CLIENT_ERROR);
        errorStatus.setStatusReason(ex.getMessage());
        resp.setErrorStatus(errorStatus);
    }
    
    private ProductEntitlementGroup[] getGroupArray(List<ProductEntitlementGroupDto> groupDtos){
    	ProductEntitlementGroup[] result = new ProductEntitlementGroup[groupDtos.size()];
    	for(int i=0;i<result.length;i++){
    		ProductEntitlementGroup group = getGroup(groupDtos.get(i));
    		result[i] = group;
    	}
    	return result;
    }

	private ProductEntitlementGroup getGroup(
			ProductEntitlementGroupDto productEntitlementGroupDto) {
		ProductEntitlementGroup result = new ProductEntitlementGroup();
		result.setEntitlement(this.entitlementsHelper.getProductEntitlement(productEntitlementGroupDto.getEntitlement()));
		result.setLinkedEntitlement(getLinkedProdEntitlements(productEntitlementGroupDto.getLinkedEntitlements()));
		return result;
	}

	private ProductEntitlement[] getLinkedProdEntitlements(
			List<ProductEntitlementDto> linkedEntitlements) {
		ProductEntitlement[] result = new ProductEntitlement[linkedEntitlements.size()];
		for(int i=0;i<result.length;i++){
			result[i] = this.entitlementsHelper.getProductEntitlement(linkedEntitlements.get(i));
		}
		return result;
	}



}
