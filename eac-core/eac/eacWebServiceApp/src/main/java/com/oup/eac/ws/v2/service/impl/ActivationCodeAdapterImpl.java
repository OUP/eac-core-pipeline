package com.oup.eac.ws.v2.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.exolab.castor.types.Date;
import org.joda.time.DateTime;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.ActivationCodeSearchDto;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.LinkedProductNew;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.entitlement.ProductEntitlementInfoDto;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.dto.ExternalProductIdDto;
import com.oup.eac.dto.GuestRedeemActivationCodeDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.UserEntitlementsService;
import com.oup.eac.service.exceptions.ActivationCodeServiceLayerException;
import com.oup.eac.ws.v2.binding.access.GuestRedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.RedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.SearchActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.ValidateActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.ValidateActivationCodeResponseSequence;
import com.oup.eac.ws.v2.binding.access.ValidateActivationCodeResponseSequenceItem;
import com.oup.eac.ws.v2.binding.common.ActivationCodeInfo;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.GuestProductEntitlement;
import com.oup.eac.ws.v2.binding.common.Identifiers;
import com.oup.eac.ws.v2.binding.common.ProductDetails;
import com.oup.eac.ws.v2.binding.common.ProductEntitlement;
import com.oup.eac.ws.v2.binding.common.RedeemedInfo;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.ActivationCodeAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.WsExternalSystemLookup;
import com.oup.eac.ws.v2.service.entitlements.EntitlementsAdapterHelper;
import com.oup.eac.ws.v2.service.entitlements.ExternalCustomerIdDtoSource;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;
import com.oup.eac.ws.v2.service.utils.IdUtils;

public class ActivationCodeAdapterImpl implements ActivationCodeAdapter {

	private final RegistrationService registrationService;

	private final WsCustomerLookup customerLookup;

	private final WsExternalSystemLookup externalSystemLookup;

	private final ExternalIdService externalIdService;

	private final ActivationCodeService activationCodeService;

	private final UserEntitlementsService userEntitlementsService;

	private final EntitlementsAdapterHelper entitlementsHelper;
	
	private final ExternalCustomerIdDtoSource externalCustomerIdDtoSource;
	
	private final ProductService productService;
	
	private final LicenceService licenceService;
	
	private final ErightsFacade erightsFacade;
	
	public ActivationCodeAdapterImpl(
			final WsCustomerLookup customerLookup,
			final RegistrationService registrationService,						
			final WsExternalSystemLookup externalSystemLookup,
			final ExternalCustomerIdDtoSource externalCustomerIdDtoSource,
			final ActivationCodeService activationCodeService,
			final UserEntitlementsService userEntitlementsService,
			final EntitlementsAdapterHelper entitlementsHelper,
			final ExternalIdService externalIdService,
			final ProductService productService,
			final LicenceService licenceService,
			final ErightsFacade erightsFacade
			) {
		Assert.notNull(customerLookup);
		Assert.notNull(registrationService);
		Assert.notNull(externalSystemLookup);
		Assert.notNull(externalCustomerIdDtoSource);
		Assert.notNull(activationCodeService);
		Assert.notNull(entitlementsHelper);
		Assert.notNull(userEntitlementsService);
		Assert.notNull(externalIdService);
		Assert.notNull(productService);
		Assert.notNull(licenceService);
		Assert.notNull(erightsFacade);
		this.customerLookup = customerLookup;
		this.registrationService = registrationService;				
		this.externalSystemLookup = externalSystemLookup;
		this.activationCodeService = activationCodeService;
		this.userEntitlementsService = userEntitlementsService;
		this.entitlementsHelper = entitlementsHelper;
		this.externalCustomerIdDtoSource = externalCustomerIdDtoSource;
		this.externalIdService = externalIdService;
		this.productService = productService;
		this.licenceService = licenceService;
		this.erightsFacade = erightsFacade;
	}

	private ProductEntitlement[] getEntitlements(List<ProductEntitlementInfoDto> infoDtos) {
		ProductEntitlement[] result = new ProductEntitlement[infoDtos.size()];
		for(int i=0;i<result.length;i++){
			result[i] = this.entitlementsHelper.getProductEntitlement(infoDtos.get(i).getEntitlement());
		}
		return result;
	}

	@Override	                             
    @PreAuthorize("hasRole('ROLE_WS_SEARCH_ACTIVATION_CODE')")
	public final SearchActivationCodeResponse searchActivationCode(
			final String systemId, final String activationCode,
			final boolean likeSearch) throws WebServiceException {
		long startTime = System.currentTimeMillis();
		SearchActivationCodeResponse result = new SearchActivationCodeResponse();
		try {
			validateActivationCode(activationCode);

			try {
				this.externalSystemLookup.validateExternalSystem(systemId);
			} catch (AccessDeniedException | ErightsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<ActivationCodeSearchDto> codes = activationCodeService.searchActivationCodeByCode(systemId, activationCode, likeSearch);
			if (codes.size() > 0) {
				for (ActivationCodeSearchDto code : codes) {
					result.addActivationCodeInfo(getActivationCodeInfo(code,
							systemId));
				}
			}

		} catch (WebServiceValidationException ex2) {
			setErrorStatus(ex2, result);// CLIENT ERROR
		}
		AuditLogger.logEvent(":: Time to SearchActivationCodeRequest :: " + (System.currentTimeMillis() - startTime));
		return result;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_WS_VALIDATE_ACTIVATION_CODE')")
	public final ValidateActivationCodeResponse validateActivationCode(
			final String systemId, final String activationCode)
			throws WebServiceException {
		long startTime = System.currentTimeMillis();
		ValidateActivationCodeResponse result = new ValidateActivationCodeResponse();
		try {
			String trimmedActivationCode = trimActivationCode(activationCode);

			validateActivationCodeExists(trimmedActivationCode);

			
			List<RegisterableProduct> regProducts = new ArrayList<RegisterableProduct>() ;
			regProducts = erightsFacade.validateActivationCode(trimmedActivationCode, systemId) ;
			List<Product> products = new ArrayList<Product>() ;
			for (RegisterableProduct regProduct : regProducts){
				products.add((Product)regProduct) ;
			}
			ExternalProductIdDto dto = externalIdService.getExternalProductIds(products, systemId);			
			List<ExternalProductId> externalProductIds = dto.getExternalProductIds(products.get(0));
			
			result.addValidateActivationCodeResponseSequence(getValidateActivationCodeResponseSequence(
					activationCode, regProducts.get(0), externalProductIds));
		} catch (WebServiceValidationException ex2) {
			setErrorStatus(ex2, result);// CLIENT ERROR
		} catch (ErightsException e) {
			/*if (e.getErrorCode() == 2153 ) {
				setErrorStatus(new Exception(""), result);
			} else if (e.getErrorCode() == 2154 ) {
				setErrorStatus(new Exception(""), result);
			} else if (e.getErrorCode() == 2155 ) {
				setErrorStatus(new Exception(""), result);
			} else if (e.getErrorCode() == 2156 ) {
				setErrorStatus(new Exception(""), result);
			} else {*/
			setErrorStatus(e, result);
			/*}*/
		}
		AuditLogger.logEvent(":: Time to validateActivationCode :: " + (System.currentTimeMillis() - startTime));
		return result;
	}

	private final void validateActivationCode(final String activationCode)
			throws WebServiceValidationException {
		if (StringUtils.isBlank(activationCode)) {
			throw new WebServiceValidationException(
					"No Activation Code supplied");
		}
	}

	private final void validateActivationCodeExists(final String activationCode)
			throws WebServiceValidationException {
		validateActivationCode(activationCode);

		/*try {
			this.activationCodeService
					.getActivationCodeByCode(activationCode);
		} catch (ServiceLayerException e) {
			throw new WebServiceValidationException(
					"This is not a valid activation code");
		}*/
		/*if (acCode == null) {
			throw new WebServiceValidationException(
					"This is not a valid activation code");
		}*/
	}

	private final ActivationCodeInfo getActivationCodeInfo(
			final ActivationCodeSearchDto activationCode, final String systemId) {
		ActivationCodeInfo activationCodeInfo = new ActivationCodeInfo();
		activationCodeInfo.setActivationCode(activationCode.getCode());
		activationCodeInfo.setActualUsages(activationCode.getActualUsage());
		activationCodeInfo.setAllowedUsages(activationCode.getAllowedUsage());
		activationCodeInfo.setValidStart(getDate(activationCode
				.getActivationCodeBatch().getStartDate()));
		activationCodeInfo.setValidEnd(getDate(activationCode
				.getActivationCodeBatch().getEndDate()));
		activationCodeInfo.setProductId(getProductIdentifiers(activationCode));
		activationCodeInfo.setActivationCodeLicence(getRedeemedInfo(activationCode, systemId));

		return activationCodeInfo;
	}
	
	private final RedeemedInfo[] getRedeemedInfo(final ActivationCodeSearchDto activationCode, final String systemId) {
		List<RedeemedInfo> redeemedInfos = new ArrayList<RedeemedInfo>();
		List<ActivationCodeRegistration> registrations = activationCodeService.getRedeemActivationCodeInfo(activationCode, systemId);
		Map<String, ExternalCustomerIdDto> customerMap = getExternalCustomerIdDtoMap(externalCustomerIdDtoSource.getExternalCustomersId(systemId, registrations));
		for(ActivationCodeRegistration registration : registrations) {
			redeemedInfos.add(getRedeemedInfo(registration, customerMap.get(registration.getCustomer().getId())));
		}		
		return redeemedInfos.toArray(new RedeemedInfo[redeemedInfos.size()]);
	}
	
	private final RedeemedInfo[] getRedeemedInfo(final ActivationCode activationCode, final String systemId) {
		List<RedeemedInfo> redeemedInfos = new ArrayList<RedeemedInfo>();
		List<ActivationCodeRegistration> registrations = registrationService.getActivationCodeRegistrationByCode(activationCode);
		Map<String, ExternalCustomerIdDto> customerMap = getExternalCustomerIdDtoMap(externalCustomerIdDtoSource.getExternalCustomersId(systemId, registrations));
		for(ActivationCodeRegistration registration : registrations) {
			redeemedInfos.add(getRedeemedInfo(registration, customerMap.get(registration.getCustomer().getId())));
		}		
		return redeemedInfos.toArray(new RedeemedInfo[redeemedInfos.size()]);
	}
	
	private final RedeemedInfo getRedeemedInfo(ActivationCodeRegistration registration, final ExternalCustomerIdDto externalCustomerIdDto) {
		RedeemedInfo redeemedInfo = new RedeemedInfo();
		redeemedInfo.setRedeemedDate(getDateTime(registration.getCreatedDate()));
		redeemedInfo.setUserId(IdUtils.getIds(externalCustomerIdDto.getCustomer().getId(), externalCustomerIdDto.getExternalCustomerIds()));
		return redeemedInfo;
	}
	
	private Map<String, ExternalCustomerIdDto> getExternalCustomerIdDtoMap(
			List<ExternalCustomerIdDto> customerDtos) {
		Map<String, ExternalCustomerIdDto> map = new HashMap<String, ExternalCustomerIdDto>();
		for (ExternalCustomerIdDto externalCustomerIdDto : customerDtos) {
			map.put(externalCustomerIdDto.getCustomer().getId(),
					externalCustomerIdDto);
		}
		return map;
	}
	
	private final Identifiers getProductIdentifiers(final ActivationCodeSearchDto activationCode) { 
		return IdUtils.getIds(activationCode.getProduct().getId(), activationCode.getProduct().getExternalIds());
	}

	private final Identifiers getProductIdentifiers(
			final ActivationCode activationCode, final String systemId) {
        //product de-duplication(chnages done for remove compilation error)
		RegisterableProduct product = (RegisterableProduct)activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getProduct();		
		List<Product> products = Arrays.asList(new Product[] { product });
		ExternalProductIdDto externalProductIdDto = externalIdService.getExternalProductIds(products, systemId);
		return IdUtils.getIds(product.getId(), externalProductIdDto.getExternalProductIds(product));
	}

	private final ValidateActivationCodeResponseSequence getValidateActivationCodeResponseSequence(
			final String activationCode, final RegisterableProduct product,
			final List<ExternalProductId> externalProductIds) {
		ValidateActivationCodeResponseSequence response = new ValidateActivationCodeResponseSequence();
		response.addValidateActivationCodeResponseSequenceItem(getValidateActivationCodeResponseSequenceItem(
				activationCode, product, externalProductIds));
		return response;
	}

	private final ValidateActivationCodeResponseSequenceItem getValidateActivationCodeResponseSequenceItem(
			final String activationCode, final RegisterableProduct product,
			List<ExternalProductId> externalProductIds) {
		ValidateActivationCodeResponseSequenceItem item = new ValidateActivationCodeResponseSequenceItem();
		item.setActivationCode(activationCode);
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProductName(product.getProductName());
		/*EnforceableProductDto enforceableProductDto = null;
		try {
			enforceableProductDto = productService.getEnforceableProductByErightsId(product.getId());
		} catch (ErightsException e) {
			e.printStackTrace();
		}*/
	//	if (enforceableProductDto != null) {
			productDetails.setProductIds(IdUtils.getIds(product.getId(), externalProductIds));			
		//}
        item.setProduct(productDetails);
		return item;
	}

	private void setErrorStatus(Exception ex, SearchActivationCodeResponse resp) {
		ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(ex.getMessage());
		resp.setErrorStatus(errorStatus);
	}

	private static String trimActivationCode(final String activationCode) {
		StringTrimmerEditor editor = new StringTrimmerEditor("- ", true);
		editor.setAsText(activationCode);
		String trimmedActivationCode = editor.getAsText();
		return trimmedActivationCode;
	}

	private void setErrorStatus(Exception ex,
			ValidateActivationCodeResponse resp) {
		ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(ex
				.getMessage());
		resp.setErrorStatus(errorStatus);
	}

	private void setErrorStatus(Exception ex, RedeemActivationCodeResponse resp) {
		ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(ex
				.getMessage());
		resp.setErrorStatus(errorStatus);
	}
	
	private void setErrorStatus(String exception, RedeemActivationCodeResponse resp) {
		ErrorStatus errorStatus = ErrorStatusUtils.getServerErrorStatus(exception);
		resp.setErrorStatus(errorStatus);
	}

	private Date getDate(final DateTime date) {
		if (date == null) {
			return null;
		}
		return new Date(date.toDate());
	}

	private java.util.Date getDateTime(final DateTime date) {
		if (date == null) {
			return null;
		}
		return date.toDate();
	}
	
	@Override
	@PreAuthorize("hasRole('ROLE_WS_REDEEM_ACTIVATION_CODE')")
	public final RedeemActivationCodeResponse redeemActivationCode(
			final String systemId, final WsUserId wsUserId,
			final String code, final Locale locale)
			throws WebServiceException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException, ErightsException {
		RedeemActivationCodeResponse result = new RedeemActivationCodeResponse();
		//ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
		ActivationCode activationCode = null ;
		try {
			if (StringUtils.isBlank(code)) {
				throw new WebServiceValidationException("No Activation Code supplied");
			}

			long startTime = System.currentTimeMillis();
			this.externalSystemLookup.validateExternalSystem(systemId);
			AuditLogger.logEvent("Time to validateExternalSystem :: " + (System.currentTimeMillis() - startTime));
			Customer customer = this.customerLookup.getCustomerByWsUserId(wsUserId);
			String trimmedActivationCode = trimActivationCode(code);
			startTime = System.currentTimeMillis();
			/*try {
				activationCodeBatchDto = erightsFacade.getActivationCodeDetailsByActivationCode(trimmedActivationCode);
			} catch (ErightsException e) {
				if(e.getErrorCode().equals(1123) || e.getErrorCode().equals(2004))
					throw new ServiceLayerException("The activation code  ["+trimmedActivationCode+"] does not exist");
				}*/
			try {
				activationCode =  activationCodeService.getActivationCodeWithDetailsWS(trimmedActivationCode);
			} catch (ErightsException e) {
				throw new WebServiceValidationException("The activation code  [" + trimmedActivationCode + "] does not exist ");
			}
			AuditLogger.logEvent("Time to getActivationCodeWithDetails :: " + (System.currentTimeMillis() - startTime));
					//De-duplication
					//getActivationCodeFullDetails(trimmedActivationCode);
			
			if (activationCode == null) {
				throw new WebServiceValidationException("The activation code  [" + trimmedActivationCode + "] does not exist ");
			}
			
			ActivationCodeRegistrationDefinition acrd = activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition();
			ProductPageDefinition pageDef = acrd.getPageDefinition();
			if (pageDef != null) {
				throw new WebServiceValidationException(
						"You cannot redeem this type of activation code : it requires the capture of product registration information");
			}
			
			List<Registration<? extends ProductRegistrationDefinition>> pRegDefList = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
			
			EacGroups eacGroup = activationCode.getActivationCodeBatch().getActivationCodeRegistrationDefinition().getEacGroup();
			Product product = activationCode.getActivationCodeBatch()
					.getActivationCodeRegistrationDefinition().getProduct();		
			CustomerRegistrationsDto dto = null;
			List<LicenceDto> licenses =null; 
			if(eacGroup != null && product == null){
				//De-duplication
				//activationCode = this.registrationService.incrementActivationCodeUsage(activationCode);
				startTime = System.currentTimeMillis();
				activationCodeService.validateActivationCode(activationCode);
				AuditLogger.logEvent("Time to validateActivationCode :: " + (System.currentTimeMillis() - startTime));
				startTime = System.currentTimeMillis();
				licenses = this.registrationService.createRegistrationAndAddLicenceForEacGroup(customer, activationCode, locale);
				AuditLogger.logEvent("Time to getActivationCodeWithDetails :: " + (System.currentTimeMillis() - startTime));
				/*for (ActivationCodeRegistration activationCodeRegistration : (List<ActivationCodeRegistration>)dto.getRegistrations()) {
					activationCodeRegistration.setRegistrationDefinition(acrd);
					Registration<? extends ProductRegistrationDefinition> pRegDef = activationCodeRegistration;
					pRegDefList.add(pRegDef);
				}*/
//				pRegDefList.addAll(dto.getRegistrations());
			}else if(eacGroup == null && product != null){
				//De-duplication
				//activationCode = this.registrationService.incrementActivationCodeUsage(activationCode);
				startTime = System.currentTimeMillis();
				activationCodeService.validateActivationCode(activationCode);
				AuditLogger.logEvent("Time to validateActivationCode :: " + (System.currentTimeMillis() - startTime));
				startTime = System.currentTimeMillis();
				licenses =  this.registrationService.createRegistrationAndAddLicence(customer, activationCode, locale);
				AuditLogger.logEvent("Time to createRegistrationAndAddLicence :: " + (System.currentTimeMillis() - startTime));
//				pRegDefList.addAll(dto.getRegistrations());
			}else{
				throw new WebServiceValidationException("The activation code details are not valid.");
			}
			List<LicenceDto> licenceDtos = new ArrayList<LicenceDto>() ;
			for (LicenceDto licenceDto : licenses) {
				if (null != licenceDto.getProducts().getLinkedProducts() && licenceDto.getProducts().getLinkedProducts().size() > 0 ) {
					List<LinkedProductNew> linkedProducts = licenceDto.getProducts().getLinkedProducts() ;
					for (LinkedProductNew linkedProductNew : linkedProducts ) {
						Product linkedProduct = new RegisterableProduct() ;
						linkedProduct.setId(linkedProductNew.getProductId());
						linkedProduct.setProductName(linkedProductNew.getName());
						LicenceDto linkedLicense = licenseClone(licenceDto, linkedProduct) ;
						licenceDtos.add(linkedLicense);
					}
				}
			}
			if (licenceDtos.size() > 0) {
				licenses.addAll(licenceDtos);
			}
			//CustomerRegistrationsDto dto = this.registrationService.getEntitlementsForCustomerRegistrations(customer,null); 
					//EAC-WEB service redeemACB changes after de-duplication 
					//this.registrationService.getEntitlementsForCustomerRegistrationForAfterRedeem(customer, pRegDefList);
			/*List<Registration<? extends ProductRegistrationDefinition>> regs = dto.getRegistrations();
			if (regs.size() < 1) {
				throw new WebServiceException("Problem getting entitlements after activation code redemption");
			}
			
			List<LicenceDto> licences =dto.getLicences(); 
					//licenceService.getLicencesForCustomer(customer);
			List<LicenceDto> actualLicences = new ArrayList<LicenceDto>();
			int licenseCount = 0;
			boolean flag = false;
			List<String> prodIds = new ArrayList<String>();
			List<String> tempProdIds = new ArrayList<String>();
			for (LicenceDto license : licences) {
				if(license.getActivationCode() != null 
						&& license.getActivationCode().equals(trimmedActivationCode)) {
					if (eacGroup == null) {
						tempProdIds.addAll(license.getProductIds());
						actualLicences.add(license);
						licenseCount = licenseCount + 1;
					} else if (eacGroup != null && licenseCount <= eacGroup.getProducts().size()) {
						if (prodIds.size() == 0) {
							actualLicences.add(license);
							prodIds.addAll(license.getProductIds());
							licenseCount = licenseCount + 1;
						} else {
							for (String prodId : license.getProductIds()) {
								for (String id : prodIds) {
									if (!id.equals(prodId)) {
										for (String tempProdId : tempProdIds) {
											if (tempProdId.equals(prodId)) {
												flag = true;	
											}
										}
										if (flag == false) {
											actualLicences.add(license);
											tempProdIds.add(prodId);
										}
										licenseCount = licenseCount + 1;
									}
								}
							}
						}
					}
				}
			}
			prodIds.addAll(tempProdIds);
			List<Registration<? extends ProductRegistrationDefinition>> actRegs = 
					new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
			for (Registration reg : regs) {
				if (reg.getRegistrationType().equals(RegistrationType.ACTIVATION_CODE)) {
					actRegs.add(reg);
				}
			}
			CustomerRegistrationsDto custDto = new CustomerRegistrationsDto(customer, actRegs, actualLicences);
			*/
			List<ProductEntitlementInfoDto> infoDtos = this.userEntitlementsService.getProductEntitlementInfo(licenses);
			ProductEntitlement[] ents = getEntitlements(infoDtos);
			result.setEntitlement(ents);
		} catch (ActivationCodeServiceLayerException ex1) {
			setErrorStatus(ex1, result);// CLIENT ERROR
		} catch (WebServiceValidationException ex2) {
			setErrorStatus(ex2, result);// CLIENT ERROR
		} catch (ServiceLayerException ex3) {
			if (ex3.getMessage().contains("Erights problem adding licence.")) {
				setErrorStatus(ex3.getMessage(),result); 
			} else {
				setErrorStatus(ex3, result);
			}
//			throw new WebServiceException(ex3.getMessage(), ex3);
		} catch (ErightsException e){
			if (e.getMessage().contains("can not be redeem more than allowed usage")) {
				String[] actualUsage = e.getMessage().split("\\[|\\]") ;
				String errMsg = "Activation code: " + activationCode.getCode() + " Allowed usage is: " + actualUsage[1] + 
			            " Actual usage is: " + actualUsage[1] ;
				setErrorStatus(errMsg,result); 
			} else {
				setErrorStatus(e, result);
			}
			
		} catch (Exception e) {
			setErrorStatus(e, result);
		}
		return result;
	}

	@Override
    @PreAuthorize("hasRole('ROLE_WS_REDEEM_ACTIVATION_CODE')")
    public final GuestRedeemActivationCodeResponse guestRedeemActivationCode(final String code)throws WebServiceException {
        GuestRedeemActivationCodeResponse result = new GuestRedeemActivationCodeResponse();
        int allowedUsage = 5 ;
        try {
            if (StringUtils.isBlank(code)) {
                throw new WebServiceValidationException("No Activation Code supplied");
            }
            List<GuestRedeemActivationCodeDto> guestRedeemActivationCodeDtos = activationCodeService.guestRedeemActivationCode(code) ;
            GuestProductEntitlement[] ents = getEntitlementsforGuest(guestRedeemActivationCodeDtos);
            result.setEntitlement(ents);
            
            
        } catch (WebServiceValidationException ex1) {
            setErrorStatus(ex1, result);// CLIENT ERROR
        } catch (ErightsException e) {
        	ErrorStatus errorStatus = new ErrorStatus() ;
        	if (e.getErrorCode() == 2053) {
        		errorStatus.setStatusCode(StatusCode.CLIENT_ERROR);
        		errorStatus.setStatusReason("Activation code: "+ code + " Allowed usage is: "+ e.getMessage() +" Actual usage is: "+e.getMessage()); //e.getMessage() return allowedusage which we set from erightsfacade
        	} else if (e.getErrorCode() == 2004 ) {
        		errorStatus.setStatusCode(StatusCode.CLIENT_ERROR);
        		errorStatus.setStatusReason("The activation code  ["+ code + "] does not exist.");
        	} else if (e.getErrorCode() == 2054 ) {
        		errorStatus.setStatusCode(StatusCode.CLIENT_ERROR);
        		//errorStatus.setStatusReason(e.getMessage().substring(0, e.getMessage().lastIndexOf(" ")).replace("[", "")+"T00:00:00.000Z");
        		errorStatus.setStatusReason(e.getMessage().replaceAll(" ([0-9]+):+([0-9]+):+([0-9]+)\\.+([0-9]+)]", "T00:00:00.000Z").replace("[ ", ""));
        	} else if( e.getErrorCode() == 2007){
        		errorStatus.setStatusCode(StatusCode.CLIENT_ERROR);
        		errorStatus.setStatusReason(e.getMessage().replaceAll(" ([0-9]+):+([0-9]+):+([0-9]+)\\.+([0-9]+)]", "T00:00:00.000Z").replace("[ ", ""));
        	}else {
        		
            		errorStatus.setStatusCode(StatusCode.SERVER_ERROR);
            		errorStatus.setStatusReason(e.getMessage());
        	}
        	result.setErrorStatus(errorStatus);
        }
        return result;
    }
	
	private void setErrorStatus(Exception ex, GuestRedeemActivationCodeResponse resp) {
        ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(ex.getMessage());
        resp.setErrorStatus(errorStatus);
    }
	
	private GuestProductEntitlement[] getEntitlementsforGuest(List<GuestRedeemActivationCodeDto> infoDtos) {
	    GuestProductEntitlement[] result = new GuestProductEntitlement[infoDtos.size()];
	    int i = 0 ;
	    int j=0;
        for (GuestRedeemActivationCodeDto guestRedeemActivationCode : infoDtos) {
        	GuestProductEntitlement guestProductEntitlement = new GuestProductEntitlement() ;
        	guestProductEntitlement.setActivationCode(guestRedeemActivationCode.getActivationCode());
        	ProductDetails productDetails = new ProductDetails() ;
        	ProductDetails[] vProductArray=new ProductDetails[1];
        	Identifiers identifiers = new Identifiers() ;
        	identifiers.setId(guestRedeemActivationCode.getProductId());
        	ExternalIdentifier[] externalIdentifiers = new ExternalIdentifier[guestRedeemActivationCode.getExternalProductId().size()] ;
        	int cnt = 0 ;
        	for (ExternalProductId externalProduct : guestRedeemActivationCode.getExternalProductId() ) {
        		
        		ExternalIdentifier externalIdentifier = new ExternalIdentifier() ;
        		externalIdentifier.setId(externalProduct.getExternalId());
        		externalIdentifier.setSystemId(externalProduct.getExternalSystemIdType().getExternalSystem().getName());
        		externalIdentifier.setTypeId(externalProduct.getExternalSystemIdType().getName());
        		externalIdentifiers[cnt] = externalIdentifier ;
        		cnt++;
        	}
        	if ( externalIdentifiers.length > 0 ){
        		identifiers.setExternal(externalIdentifiers);
        	}
        	productDetails.setProductIds(identifiers);
        	productDetails.setProductName(guestRedeemActivationCode.getProductName());
        	
        	vProductArray[j]=productDetails;
        	//guestProductEntitlement.setProduct(productDetails);
        	guestProductEntitlement.setProduct(vProductArray);
        	result[i] = guestProductEntitlement ;
        	i++;
        }
        return result;
    }
	
	private LicenceDto licenseClone(final LicenceDto licenceDto, Product linkedProduct) {
		
		LicenceDto license = new LicenceDto(linkedProduct.getId(), licenceDto.getExpiryDateAndTime(), licenceDto.isExpired(), licenceDto.isActive(), licenceDto.isCompleted(), licenceDto.isAwaitingValidation(), licenceDto.isDenied()) ;
		license.setProductIds(Arrays.asList(linkedProduct.getId()));
		EnforceableProductDto products = new EnforceableProductDto() ;
		products.setProductId(linkedProduct.getId());
		products.setName(linkedProduct.getProductName());
		license.setProducts(products);
		
		license.setActivationCode(licenceDto.getActivationCode());

		license.setCreatedDate(licenceDto.getCreatedDate());

		license.setEnabled(licenceDto.isEnabled());
		license.setEndDate(licenceDto.getEndDate());
		license.setEndDateTime(licenceDto.getEndDateTime());
		license.setLicenceDetail(licenceDto.getLicenceDetail());
		license.setLicenceTemplateId(licenceDto.getLicenceTemplateId());
		license.setStartDate(licenceDto.getStartDate());
		license.setStartDateTime(licenceDto.getStartDateTime());
		license.setUpdatedDate(licenceDto.getUpdatedDate());
		return license;
	}
}
