package com.oup.eac.ws.v2.service.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalProductId;
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
import com.oup.eac.ws.v2.binding.access.GetFullUserEntitlementsResponse;
import com.oup.eac.ws.v2.binding.access.GetFullUserEntitlementsResponseSequence;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.FullProductEntitlement;
import com.oup.eac.ws.v2.binding.common.FullProductEntitlementGroup;
import com.oup.eac.ws.v2.binding.common.User;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.FullUserEntitlementsAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.WsExternalSystemLookup;
import com.oup.eac.ws.v2.service.WsProductLookup;
import com.oup.eac.ws.v2.service.entitlements.CustomerConverter;
import com.oup.eac.ws.v2.service.entitlements.EntitlementsAdapterHelper;

/**
 * The Class UserEntitlementsAdapterImpl.
 */
public class FullUserEntitlementsAdapterImpl implements FullUserEntitlementsAdapter {

    /** The Constant LOG. */
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(FullUserEntitlementsAdapterImpl.class);

    /** The registration service. */
    private final RegistrationService registrationService;

    /** The customer converter. */
    private final CustomerConverter customerConverter;

    /** The customer lookup. */
    private final WsCustomerLookup customerLookup;
    
    /** The external system lookup */
    private final WsExternalSystemLookup externalSystemLookup;
    
    private final ExternalIdService externalIdService;
    
    private final WsProductLookup productLookup;
    
    private UserEntitlementsService userEntitlementsService;
    
    private EntitlementsAdapterHelper entitlementsHelper;

    public FullUserEntitlementsAdapterImpl(
            final WsCustomerLookup customerLookup1,
            final RegistrationService registrationService1,
            final CustomerConverter customerConverter1, 
            final ExternalIdService externalIdService,
            final WsExternalSystemLookup externalSystemLookup,
            final UserEntitlementsService userEntitlementsService, 
            final EntitlementsAdapterHelper entitlementsHelper,
            final WsProductLookup productLookup) {       
        Assert.notNull(customerLookup1);
        Assert.notNull(registrationService1);
        Assert.notNull(customerConverter1);
        Assert.notNull(externalSystemLookup);
        Assert.notNull(externalIdService);
        Assert.notNull(productLookup);
        this.customerLookup = customerLookup1;
        this.registrationService = registrationService1;
        this.customerConverter = customerConverter1;
        this.externalIdService = externalIdService;
        this.externalSystemLookup = externalSystemLookup;
        this.userEntitlementsService = userEntitlementsService;
        this.entitlementsHelper = entitlementsHelper;
        this.productLookup = productLookup;
    }

    /**
     * {@inheritDoc}
     * @throws ErightsException 
     * @throws AccessDeniedException 
     */
    @Override
    @PreAuthorize("hasRole('ROLE_WS_GET_USER_ENTITLEMENTS')")
    public GetFullUserEntitlementsResponse getFullUserEntitlementGroups(WsUserId wsUserId, Set<String> systemIdSet, Set<String> productSystemIdSet, Set<String> productOrgUnitSet, String licenceState) throws WebServiceException, AccessDeniedException, ErightsException {
    	long startTime = System.currentTimeMillis();
    	GetFullUserEntitlementsResponse response = new GetFullUserEntitlementsResponse();
        try {
            Customer customer = customerLookup.getCustomerByWsUserId(wsUserId);
            
            this.externalSystemLookup.validateMultipleExternalSystem(systemIdSet);
            this.externalSystemLookup.validateMultipleExternalSystem(productSystemIdSet);
            this.productLookup.validateProductOrgUnit(productOrgUnitSet);
            this.productLookup.validateLicenceState(licenceState);
            
            //the data in the dto has the correct external ids set for products and users
            /*
             * Gaurav Soni : Performance improvements CR
             * below call has been removed to apply filtering at the query level
             * 
             * --CustomerRegistrationsDto custRegistrationsDto = this.registrationService.getEntitlementsForCustomerRegistrations(customer);
             * */
            //
            CustomerRegistrationsDto custRegistrationsDto = this.registrationService.getEntitlementsForCustomerRegistrationsFiltered(customer,  productSystemIdSet,  productSystemIdSet, licenceState);
            customer = custRegistrationsDto.getUser();
           
            ExternalCustomerIdDto extCustIdDto = this.externalIdService.getFullExternalCustomerIdDto(customer);            
            extCustIdDto = filterSystemId(extCustIdDto, systemIdSet);
           
           
            //seq
            GetFullUserEntitlementsResponseSequence seq = new GetFullUserEntitlementsResponseSequence();            
            
            //seq-user
            User user = this.customerConverter.convertCustomerToUser(extCustIdDto);
            seq.setUser(user);
            seq.setUserConcurrency(customer.getCustomerType().getConcurrency());

            //seq-groups
            List<ProductEntitlementGroupDto> groups = this.userEntitlementsService.getFullUserEntitlementGroups(custRegistrationsDto);            
            /*groups = filterGroupsByProductOrgUnit(groups, productOrgUnitSet);
            groups = filterGroupsByProductSystemId(groups, productSystemIdSet);
            groups = filterGroupByLicenceState(groups, licenceState); */
            
            /*
             * Gaurav Soni : Performance improvements CR
             * below call has been removed to apply filtering at the query level
             * 
             * --groups = filterGroups(groups, productOrgUnitSet, productSystemIdSet, licenceState);
             * */
            //
            
            groups = filterGroups(groups, productOrgUnitSet, productSystemIdSet, licenceState);
            
            FullProductEntitlementGroup[] entitlementGroups = getGroupArray(groups);
            seq.setEntitlementGroup(entitlementGroups);
            response.setGetFullUserEntitlementsResponseSequence(seq);
            
        } catch (WebServiceValidationException wsve) {
            setErrorStatus(wsve, response);
        } catch (ServiceLayerException sle) {
            throw new WebServiceException(sle.getMessage(),sle);
        }
        AuditLogger.logEvent(":: Time to GetFullUserEntitlementsRequest :: " + (System.currentTimeMillis() - startTime));
        return response;
    }

    private void setErrorStatus(Exception ex, GetFullUserEntitlementsResponse resp) {
        ErrorStatus errorStatus = new ErrorStatus();
        errorStatus.setStatusCode(StatusCode.CLIENT_ERROR);
        errorStatus.setStatusReason(ex.getMessage());
        resp.setErrorStatus(errorStatus);
    }
    
    private FullProductEntitlementGroup[] getGroupArray(List<ProductEntitlementGroupDto> groupDtos){
        FullProductEntitlementGroup[] result = new FullProductEntitlementGroup[groupDtos.size()];
        for(int i=0;i<result.length;i++){
            FullProductEntitlementGroup group = getGroup(groupDtos.get(i));
            result[i] = group;
        }
        return result;
    }

    private FullProductEntitlementGroup getGroup(ProductEntitlementGroupDto productEntitlementGroupDto) {
        FullProductEntitlementGroup result = new FullProductEntitlementGroup();
        result.setEntitlement(this.entitlementsHelper.getFullProductEntitlement(productEntitlementGroupDto.getEntitlement()));
        result.setLinkedEntitlement(getLinkedProdEntitlements(productEntitlementGroupDto.getLinkedEntitlements()));
        return result;
    }

    private FullProductEntitlement[] getLinkedProdEntitlements(
            List<ProductEntitlementDto> linkedEntitlements) {
        FullProductEntitlement[] result = new FullProductEntitlement[linkedEntitlements.size()];
        for(int i=0;i<result.length;i++){
            result[i] = this.entitlementsHelper.getFullProductEntitlement(linkedEntitlements.get(i));
        }
        return result;
    }

    private ExternalCustomerIdDto filterSystemId(ExternalCustomerIdDto extCustIdDto, Set<String> systemIdSet ){        
        List<ExternalCustomerId> externalCustomerIds = new ArrayList<ExternalCustomerId>(Collections.<ExternalCustomerId>emptyList());
        if(!systemIdSet.isEmpty()){            
            for(int i=0; i<extCustIdDto.getExternalCustomerIds().size(); i++){
                for(String systemId : systemIdSet){
                    if(extCustIdDto.getExternalCustomerIds().get(i).getExternalSystemIdType().getExternalSystem().getName().equalsIgnoreCase(systemId) ){
                        externalCustomerIds.add(extCustIdDto.getExternalCustomerIds().get(i));                
                    }
                }
            }
            extCustIdDto.setExternalCustomerIds(externalCustomerIds);  
        }
        return extCustIdDto;
    }
    
    public List<ProductEntitlementGroupDto> filterGroups(List<ProductEntitlementGroupDto> groups, Set<String> productOrgUnitSet,  Set<String> productSystemIdSet, String licenceState){
        List<ProductEntitlementGroupDto> newGroups = new ArrayList<ProductEntitlementGroupDto>(Collections.<ProductEntitlementGroupDto>emptyList());
        if(!groups.isEmpty()){
        	for(int i=0; i<groups.size(); i++){
        		if(groups.get(i) !=  null && groups.get(i).getEntitlement().getProductList().get(0) != null){
        			 boolean pOrgUnitFlag=true;
                     boolean pSysIdFlag=true;
                     
                   //filtering based on productOrgUnit
                     if(!productOrgUnitSet.isEmpty()){
                         for(String pOrgUnit : productOrgUnitSet){
                             if(groups.get(i).getEntitlement().getProductList().get(0).getProduct().getDivision().getDivisionType().equalsIgnoreCase(pOrgUnit) ){
                                 pOrgUnitFlag=true;
                                 break;
                             }else{
                                 pOrgUnitFlag=false;
                             }
                         }                
                     }
                                          
                     //filtering based on productSystemId
                     if(pOrgUnitFlag == true){
                         if(!productSystemIdSet.isEmpty()){
                             boolean extSystemExists = false;
                             List<ExternalProductId> productExtSysIds=groups.get(i).getEntitlement().getProductList().get(0).getExternalProductIds();
                             List<ExternalProductId> productExtSysExist = new ArrayList<ExternalProductId>();
                             if(productExtSysIds != null){
                             	for(String pSysId : productSystemIdSet){
                             		productExtSysIds=groups.get(i).getEntitlement().getProductList().get(0).getExternalProductIds();
                                     for(int j=0; j<productExtSysIds.size();j++){
                                         if(groups.get(i).getEntitlement().getProductList().get(0).getExternalProductIds().get(j).getExternalSystemIdType().getExternalSystem().getName().equalsIgnoreCase(pSysId) ){
                                             extSystemExists = true;
                                             productExtSysExist.add(groups.get(i).getEntitlement().getProductList().get(0).getExternalProductIds().get(j)) ;
                                             //break;
                                         }   
                                     }
                                     if(extSystemExists){
                                         pSysIdFlag=true;
                                         //break;
                                     }else{
                                         pSysIdFlag=false;
                                     }
                                 }  
                             }else{
                             	pSysIdFlag=false;
                             }    
                             if (productExtSysExist.size() > 0) {
                            	 groups.get(i).getEntitlement().getProductList().get(0).getExternalProductIds().clear();
                            	 groups.get(i).getEntitlement().getProductList().get(0).getExternalProductIds().addAll(productExtSysExist);
                             }
                         }
                     }

                     //filtering based on licenceState
                     if(pOrgUnitFlag == true && pSysIdFlag == true){
                         if(licenceState==null){
                             newGroups.add(groups.get(i));
                         }else if(licenceState.equalsIgnoreCase("active")){                                
                             if(groups.get(i).getEntitlement().getLicence().isActive()){
                                 newGroups.add(groups.get(i));                
                             }     
                         }else if(licenceState.equalsIgnoreCase("expired")){
                             if(groups.get(i).getEntitlement().getLicence().isExpired()){
                                 newGroups.add(groups.get(i));                
                             }    
                         }

                     }
        		}
            }
        	
        }
        return newGroups;
    }
    
    /*
     * Gaurav Soni : Performance improvements CR
     * Filtering has been applied at query level instead of code level
     * 
     * 
     *     
    private List<ProductEntitlementGroupDto> filterGroupsByProductSystemId(List<ProductEntitlementGroupDto> groups,  Set<String> productSystemIdSet){
        List<ProductEntitlementGroupDto> newGroups = new ArrayList<ProductEntitlementGroupDto>(Collections.<ProductEntitlementGroupDto>emptyList());
        int count=0;
        if(productSystemIdSet.isEmpty()){
            newGroups = groups;
        } else{
            for(int i=0; i<groups.size(); i++){
                List<ExternalProductId> productExtSysIds = groups.get(i).getEntitlement().getProductList().get(0).getExternalProductIds();
                for(String pSysId : productSystemIdSet){
                    for(int j=0; j<productExtSysIds.size();j++){
                        if(groups.get(i).getEntitlement().getProductList().get(0).getExternalProductIds().get(j).getExternalSystemIdType().getExternalSystem().getName().equalsIgnoreCase(pSysId) ){
                            count++;
                        }
                    }                        
                }
                if(count>0){ 
                    newGroups.add(groups.get(i));
                    count=0;
                }
            }                
        }
        return newGroups;
    }
    
    private List<ProductEntitlementGroupDto> filterGroupByLicenceState(List<ProductEntitlementGroupDto> groups, String licenceState){
        List<ProductEntitlementGroupDto> newGroups = new ArrayList<ProductEntitlementGroupDto>(Collections.<ProductEntitlementGroupDto>emptyList());
        
        if(licenceState==null){
            newGroups = groups;
        }else if(licenceState.equalsIgnoreCase("active")){
            for(int i=0; i<groups.size(); i++){                
                if(groups.get(i).getEntitlement().getLicence().isActive()){
                    newGroups.add(groups.get(i));                
                }            
            }            
        }else if(licenceState.equalsIgnoreCase("expired")){
            for(int i=0; i<groups.size(); i++){                
                if(groups.get(i).getEntitlement().getLicence().isExpired()){
                    newGroups.add(groups.get(i));                
                }            
            }            
        }        
        return newGroups;
    }
    
    */
    
}
