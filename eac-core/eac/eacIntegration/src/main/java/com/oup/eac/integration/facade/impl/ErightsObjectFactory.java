package com.oup.eac.integration.facade.impl;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

import com.oup.eac.common.date.utils.DateUtils;
import com.oup.eac.domain.ActivationCodeBatch.ActivationCodeFormat;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalId;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.LinkedProductNew;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.User.EmailVerificationState;
import com.oup.eac.domain.User.UserType;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.dto.AuthenticationResponseDto;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.DivisionDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.EnforceableProductDto.ActivationStrategy;
import com.oup.eac.dto.EnforceableProductUrlDto;
import com.oup.eac.dto.GroupDto;
import com.oup.eac.dto.GuestRedeemActivationCodeDto;
import com.oup.eac.dto.HashedLoginPasswordCredentialDto;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.LicenceDtoDateTime;
import com.oup.eac.dto.LoginPasswordCredentialDto;
import com.oup.eac.dto.ProductGroupDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.StandardLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;
import com.oup.eac.dto.WsUserIdDto;
import com.oup.eac.integration.erights.ActivationCodeBatch;
import com.oup.eac.integration.erights.ActivationDetail;
import com.oup.eac.integration.erights.ActivationType;
import com.oup.eac.integration.erights.AuthenticationResponse;
import com.oup.eac.integration.erights.BulkActivationCodeLicense;
import com.oup.eac.integration.erights.CodeFormatEnum;
import com.oup.eac.integration.erights.ExternalIdentifier;
import com.oup.eac.integration.erights.ExternalSystemWS;
import com.oup.eac.integration.erights.GetActivationCodeBatchByActivationCodeResponse;
import com.oup.eac.integration.erights.GetActivationCodeBatchByBatchIdResponse;
import com.oup.eac.integration.erights.GetActivationCodeDetailsByActivationCodeResponse;
import com.oup.eac.integration.erights.GetGroupResponse;
import com.oup.eac.integration.erights.LocaleType;
import com.oup.eac.integration.erights.MatchMODE;
import com.oup.eac.integration.erights.OupActivationCodeLicenseWS;
import com.oup.eac.integration.erights.OupAuthProfileWS;
import com.oup.eac.integration.erights.OupCredentialLoginPasswordWS;
import com.oup.eac.integration.erights.OupCredentialWS;
import com.oup.eac.integration.erights.OupDivisions;
import com.oup.eac.integration.erights.OupGetAuthProfileWS;
import com.oup.eac.integration.erights.OupGetCredentialLoginPasswordWS;
import com.oup.eac.integration.erights.OupGetCredentialWS;
import com.oup.eac.integration.erights.OupGetLicenseWS;
import com.oup.eac.integration.erights.OupGetProductWS;
import com.oup.eac.integration.erights.OupGetUserWS;
import com.oup.eac.integration.erights.OupGroupWS;
import com.oup.eac.integration.erights.OupGuestRedeemProduct;
import com.oup.eac.integration.erights.OupLicenseDetail;
import com.oup.eac.integration.erights.OupLicenseInfoWS;
import com.oup.eac.integration.erights.OupLicenseWS;
import com.oup.eac.integration.erights.OupLinkedProductDetails;
import com.oup.eac.integration.erights.OupProductGroupWS;
import com.oup.eac.integration.erights.OupProductIds;
import com.oup.eac.integration.erights.OupProductLicenseWS;
import com.oup.eac.integration.erights.OupProductUrlWS;
import com.oup.eac.integration.erights.OupProductWS;
import com.oup.eac.integration.erights.OupProducts;
import com.oup.eac.integration.erights.OupRollingLicenseData;
import com.oup.eac.integration.erights.OupRollingLicenseInfoWS;
import com.oup.eac.integration.erights.OupStandardConcurrencyLicenseData;
import com.oup.eac.integration.erights.OupUpdateProductWS;
import com.oup.eac.integration.erights.OupUsageLicenseData;
import com.oup.eac.integration.erights.OupUsageLicenseInfoWS;
import com.oup.eac.integration.erights.OupUserIdentifier;
import com.oup.eac.integration.erights.OupUserWS;
import com.oup.eac.integration.erights.PlatformIdentifier;
import com.oup.eac.integration.erights.RedeemActivatoinCodeUserDetails;
import com.oup.eac.integration.erights.RollingBEGINON;
import com.oup.eac.integration.erights.RollingUNITTYPE;
import com.oup.eac.integration.erights.SystemTypeId;
import com.oup.eac.integration.erights.ValidatorEmails;

/**
 * Object factory to assist with marshalling atypon web service request/response objects
 * to eac domain entities and dto's.
 * 
 * @author Ian Packard
 *
 */
public class ErightsObjectFactory {
	private static DateUtils dateConverter  ;
	/*static OupUserWS getOupUserWS(final CustomerDto customerDto) {		
        OupUserWS oupUserWS = new OupUserWS();
        oupUserWS.setId(customerDto.getErightsId());
        oupUserWS.setName(customerDto.getUsername());
        oupUserWS.setConcurrency(customerDto.getConcurrency());        
        oupUserWS.getAuthProfile().addAll(getOupAuthProfilesWS(customerDto.getLoginPasswordCredential()));
        oupUserWS.getGroupIds().addAll(customerDto.getGroupIds());
        oupUserWS.setSuspended(customerDto.isSuspended());
        return oupUserWS;
    }*/

	static OupUserWS getOupUserWS(final CustomerDto customerDto) {		
		OupUserWS oupUserWS = new OupUserWS();
		oupUserWS.setId(customerDto.getUserId());
		oupUserWS.setName(customerDto.getUsername());
		oupUserWS.setConcurrency(customerDto.getConcurrency());        
		oupUserWS.getAuthProfile().addAll(getOupAuthProfilesWS(customerDto.getLoginPasswordCredential()));
		oupUserWS.getGroupIds().addAll(customerDto.getGroupIds());
		oupUserWS.setSuspended(customerDto.isSuspended());
		oupUserWS.setForceResetPassword(customerDto.isResetPassword());

		/*Added for externalId*/
		if(customerDto.getExternalIds()!=null && !customerDto.getExternalIds().isEmpty()){
			List<ExternalIdentifier> externalIdent = new ArrayList<ExternalIdentifier>();
			for(ExternalCustomerId extId: customerDto.getExternalIds()){
				ExternalIdentifier external = getExternalIdentifier(extId);
				externalIdent.add(external);                
			}
			oupUserWS.getExternalUserId().addAll(externalIdent);
		}

		if(customerDto.getUserType().equals(UserType.CUSTOMER))
			oupUserWS.setUserType("CUSTOMER");
		else if(customerDto.getUserType().equals(UserType.ADMIN))
			oupUserWS.setUserType("ADMIN");

		oupUserWS.setEmailAddress(customerDto.getEmailAddress());
		oupUserWS.setFirstName(customerDto.getFirstName());
		oupUserWS.setLastName(customerDto.getFamilyName());

		if(customerDto.getEmailVerificationState().equals(EmailVerificationState.UNKNOWN))
			oupUserWS.setEmailVerificationState("UNKNOWN");
		else if(customerDto.getEmailVerificationState().equals(EmailVerificationState.VERIFIED))
			oupUserWS.setEmailVerificationState("VERIFIED");
		else if(customerDto.getEmailVerificationState().equals(EmailVerificationState.EMAIL_SENT))
			oupUserWS.setEmailVerificationState("EMAIL_SENT");

		if(null != customerDto.getTimeZone())
			oupUserWS.setTimeZone(customerDto.getTimeZone());

		LocaleType localeType=new LocaleType();
		if(null != customerDto.getLocale()){
			String lang = customerDto.getLocale().getLanguage();
			String country = customerDto.getLocale().getCountry();
			String variant = customerDto.getLocale().getVariant();
			if(!country.isEmpty())
				localeType.setCountry(country);
			localeType.setLanguage(lang);
			if(!variant.isEmpty())
				localeType.setVariant(variant);
			oupUserWS.setLocale(localeType);
		}
		//oupUserWS.setResetPassword(customerDto.isResetPassword());

		return oupUserWS;
	}

	/*static CustomerDto getCustomerDto(final OupUserWS oupUserWS) {
		CustomerDto customerDto = new CustomerDto(oupUserWS.getId(),
				oupUserWS.getName(),
				oupUserWS.getGroupIds(), 
				oupUserWS.getConcurrency(),
				getLoginPasswordCredential(oupUserWS), 
				oupUserWS.isSuspended());
		return customerDto;		
	}*/

	/*static CustomerDto getCustomerDto(final OupUserWS oupUserWS) {

		UserType userType= UserType.CUSTOMER;
		if(oupUserWS.getUserType().equalsIgnoreCase("CUSTOMER"))
			userType= UserType.CUSTOMER;
		else if(oupUserWS.getUserType().equalsIgnoreCase("ADMIN"))
			userType= UserType.ADMIN;

		EmailVerificationState emailVerificationState = EmailVerificationState.UNKNOWN;
		if(oupUserWS.getEmailVerificationState().equalsIgnoreCase("UNKNOWN"))
			emailVerificationState = EmailVerificationState.UNKNOWN;
		else if(oupUserWS.getEmailVerificationState().equalsIgnoreCase("EMAIL_SENT"))
			emailVerificationState = EmailVerificationState.EMAIL_SENT;
		else if(oupUserWS.getEmailVerificationState().equalsIgnoreCase("VERIFIED"))
			emailVerificationState = EmailVerificationState.VERIFIED;

		Locale locale = null;
	    if(null != oupUserWS.getLocale())
	    	if(oupUserWS.getLocale().getLanguage() != null && oupUserWS.getLocale().getCountry() == null && oupUserWS.getLocale().getVariant() == null)
	    		locale = new Locale(oupUserWS.getLocale().getLanguage());
	    	else if(oupUserWS.getLocale().getLanguage() != null && oupUserWS.getLocale().getCountry() != null && oupUserWS.getLocale().getVariant() == null)
	    		locale=new Locale(oupUserWS.getLocale().getLanguage(), oupUserWS.getLocale().getCountry());
	    	else if(oupUserWS.getLocale().getLanguage() != null && oupUserWS.getLocale().getCountry() != null && oupUserWS.getLocale().getVariant() != null)
	    		locale=new Locale(oupUserWS.getLocale().getLanguage(), oupUserWS.getLocale().getCountry(), oupUserWS.getLocale().getVariant());

		CustomerDto customerDto = new CustomerDto(
				oupUserWS.getId(),
				oupUserWS.getName(),
				oupUserWS.getGroupIds(),
				oupUserWS.getConcurrency(),
				getLoginPasswordCredential(oupUserWS), 
				oupUserWS.isSuspended(),
				userType,
				oupUserWS.getEacId(),
				oupUserWS.getEmailAddress(),
				oupUserWS.getFirstName(),
				oupUserWS.getLastName(),
				emailVerificationState,
				oupUserWS.getTimeZone(),
				locale);
				//oupUserWS.isResetPassword());
		return customerDto;		
	}*/

	static LoginPasswordCredentialDto getLoginPasswordCredential(final OupGetUserWS oupGetUserWS) {		
		OupGetAuthProfileWS authProfile = oupGetUserWS.getAuthProfile().get(0);
		OupGetCredentialWS credential = authProfile.getCredentials().get(0);
		OupGetCredentialLoginPasswordWS login = credential.getGetLoginPasswordCredential();	
		return new HashedLoginPasswordCredentialDto(login.getUsername());
	}

	static List<OupAuthProfileWS> getOupAuthProfilesWS(final LoginPasswordCredentialDto loginPasswordCredential) {
		List<OupAuthProfileWS> oupAuthProfilesWS = new ArrayList<OupAuthProfileWS>();
		OupAuthProfileWS oupAuthProfileWS = new OupAuthProfileWS();
		oupAuthProfileWS.setMatchMode(MatchMODE.ANY);
		if (loginPasswordCredential != null) {
			oupAuthProfileWS.getCredentials().addAll(getOupCredentialsWS(loginPasswordCredential));
		}
		oupAuthProfilesWS.add(oupAuthProfileWS);     
		return oupAuthProfilesWS;
	}

	static List<OupCredentialWS> getOupCredentialsWS(final LoginPasswordCredentialDto loginPasswordCredential) {    	
		List<OupCredentialWS> oupCredentialsWS = new ArrayList<OupCredentialWS>();
		oupCredentialsWS.add(getOupCredentialWS(loginPasswordCredential));
		return oupCredentialsWS;
	}    

	static OupCredentialWS getOupCredentialWS(final LoginPasswordCredentialDto loginPasswordCredential) {
		OupCredentialWS oupCredentialWS = new OupCredentialWS(); 
		oupCredentialWS.setLoginPasswordCredential(getOupCredentialLoginPasswordWS(loginPasswordCredential));
		return oupCredentialWS;
	}



	static OupCredentialLoginPasswordWS getOupCredentialLoginPasswordWS(final LoginPasswordCredentialDto loginPasswordCredential) {
		OupCredentialLoginPasswordWS oupCredentialLoginPasswordWS = new OupCredentialLoginPasswordWS();
		oupCredentialLoginPasswordWS.setUsername(loginPasswordCredential.getUsername());

		if (!(loginPasswordCredential instanceof HashedLoginPasswordCredentialDto)) {
			oupCredentialLoginPasswordWS.setPassword(loginPasswordCredential.getPassword());
		}
		return oupCredentialLoginPasswordWS;
	}


	static AuthenticationResponseDto getAuthenticationResponse(final AuthenticationResponse response) {
		AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
		authenticationResponseDto.setSessionKey(response.getSessionKey());
		authenticationResponseDto.addUserIds(response.getUserIds());
		authenticationResponseDto.setCustomerDto(getCustomerDto(response.getUser()));
		return authenticationResponseDto;
	}

	static OupStandardConcurrencyLicenseData getOupStandardConcurrencyLicenseData(final ConcurrentLicenceTemplate concurrentLicenceTemplate) {
		OupStandardConcurrencyLicenseData oupStandardConcurrencyLicenseData = new OupStandardConcurrencyLicenseData();
		oupStandardConcurrencyLicenseData.setTotalConcurrency(concurrentLicenceTemplate.getTotalConcurrency());
		oupStandardConcurrencyLicenseData.setUserConcurrency(concurrentLicenceTemplate.getUserConcurrency());
		return oupStandardConcurrencyLicenseData;
	}

	static OupStandardConcurrencyLicenseData getOupStandardConcurrencyLicenseData(final StandardConcurrentLicenceDetailDto standardConcurrentLicenceDetail) {
		OupStandardConcurrencyLicenseData oupStandardConcurrencyLicenseData = new OupStandardConcurrencyLicenseData();
		oupStandardConcurrencyLicenseData.setTotalConcurrency(standardConcurrentLicenceDetail.getTotalConcurrency());
		oupStandardConcurrencyLicenseData.setUserConcurrency(standardConcurrentLicenceDetail.getUserConcurrency());
		return oupStandardConcurrencyLicenseData;
	}

	static OupRollingLicenseData getOupRollingLicenseData(final RollingLicenceTemplate rollingLicenceTemplate) {
		OupRollingLicenseData oupRollingLicenseData = new OupRollingLicenseData();
		oupRollingLicenseData.setPeriodValue(rollingLicenceTemplate.getTimePeriod());
		oupRollingLicenseData.setPeriodUnit(getRollingUNITTYPE(rollingLicenceTemplate.getUnitType()));
		oupRollingLicenseData.setBeginOn(getRollingBEGINON(rollingLicenceTemplate.getBeginOn()));
		return oupRollingLicenseData;
	}

	static OupRollingLicenseData getOupRollingLicenseData(final RollingLicenceDetailDto rollingLicenceDetail) {
		OupRollingLicenseData oupRollingLicenseData = new OupRollingLicenseData();
		oupRollingLicenseData.setPeriodValue(rollingLicenceDetail.getTimePeriod());
		oupRollingLicenseData.setPeriodUnit(getRollingUNITTYPE(rollingLicenceDetail.getUnitType()));
		oupRollingLicenseData.setBeginOn(getRollingBEGINON(rollingLicenceDetail.getBeginOn()));
		return oupRollingLicenseData;
	}

	static RollingUNITTYPE getRollingUNITTYPE(final RollingUnitType rollingUnitType) {
		if (rollingUnitType == null) return RollingUNITTYPE.MINUTE;
		switch (rollingUnitType) {
		case YEAR:
			return RollingUNITTYPE.YEAR;
		case MONTH:
			return RollingUNITTYPE.MONTH;
		case WEEK:
			return RollingUNITTYPE.WEEK;
		case DAY:
			return RollingUNITTYPE.DAY;
		case HOUR:
			return RollingUNITTYPE.HOUR;
		case MINUTE:
			return RollingUNITTYPE.MINUTE;
		case SECOND:
			return RollingUNITTYPE.SECOND;
		case MILLISECOND:
			return RollingUNITTYPE.MILLISECOND;
		default:
			throw new IllegalArgumentException("Unknown Rolling Unit Type");
		}
	}

	static RollingUnitType getRollingUnitType(final RollingUNITTYPE rollingUnitType) {
		if (rollingUnitType == null) return RollingUnitType.MINUTE;
		switch (rollingUnitType) {
		case YEAR:
			return RollingUnitType.YEAR;
		case MONTH:
			return RollingUnitType.MONTH;
		case WEEK:
			return RollingUnitType.WEEK;
		case DAY:
			return RollingUnitType.DAY;
		case HOUR:
			return RollingUnitType.HOUR;
		case MINUTE:
			return RollingUnitType.MINUTE;
		case SECOND:
			return RollingUnitType.SECOND;
		case MILLISECOND:
			return RollingUnitType.MILLISECOND;
		default:
			throw new IllegalArgumentException("Unknown Rolling Unit Type");
		}
	}

	static RollingBEGINON getRollingBEGINON(final RollingBeginOn rollingBeginOn) {
		if (rollingBeginOn == null) return RollingBEGINON.FIRST_USE;
		switch (rollingBeginOn) {
		case FIRST_USE:
			return RollingBEGINON.FIRST_USE;
		case CREATION:
			return RollingBEGINON.CREATION;
		default:
			throw new IllegalArgumentException("Unknown Rolling Begin On");
		}
	}

	static RollingBeginOn getRollingBeginOn(final RollingBEGINON rollingBeginOn) {
		if (rollingBeginOn == null) return RollingBeginOn.FIRST_USE;
		switch (rollingBeginOn) {
		case FIRST_USE:
			return RollingBeginOn.FIRST_USE;
		case CREATION:
			return RollingBeginOn.CREATION;
		default:
			throw new IllegalArgumentException("Unknown Rolling Begin On");
		}
	}

	/**
	 * Get oup licence data.
	 * 
	 * @param usageLicenceTemplate
	 *            The licence template
	 * @return the licence data
	 */
	static OupUsageLicenseData getOupUsageLicenseData(final UsageLicenceTemplate usageLicenceTemplate) {
		OupUsageLicenseData oupUsageLicenseData = new OupUsageLicenseData();
		oupUsageLicenseData.setQuantityLimit(usageLicenceTemplate.getAllowedUsages());
		return oupUsageLicenseData;
	}

	/**
	 * Get oup licence data.
	 * 
	 * @param usageLicenceTemplate
	 *            The licence template
	 * @return the licence data
	 */
	static OupUsageLicenseData getOupUsageLicenseData(final UsageLicenceDetailDto usageLicenceDetail) {
		OupUsageLicenseData oupUsageLicenseData = new OupUsageLicenseData();
		oupUsageLicenseData.setQuantityLimit(usageLicenceDetail.getAllowedUsages());
		return oupUsageLicenseData;
	}

	static GroupDto getGroupDto(final GetGroupResponse response) {    	
		GroupDto groupDto = new GroupDto(response.getOupGroupWS().getId(),response.getOupGroupWS().getName(), response.getOupGroupWS().getParentIds());   	
		return groupDto;
	}

	static OupGroupWS getGroup(final GroupDto groupDto) {
		OupGroupWS groupWS = new OupGroupWS();
		groupWS.setId(groupDto.getErightsId());
		groupWS.setName(groupDto.getName());
		groupWS.getParentIds().addAll(groupDto.getParentIds());
		return groupWS;
	}



	static OupProductWS getProduct(final EnforceableProductDto enforceableProduct) {
		OupProductWS oupProductWS = new OupProductWS();
//		oupProductWS.setId(enforceableProduct.getProductId());
		oupProductWS.setName(enforceableProduct.getName());
		oupProductWS.setDivisionId(enforceableProduct.getDivisionId());
		if(enforceableProduct.getParentIds()!=null)
		{
			OupProductIds oupProductIds= new OupProductIds();
			oupProductIds.getProductId().addAll(enforceableProduct.getParentIds());
			oupProductWS.setParentIds(oupProductIds);
		}
		oupProductWS.setSuspended(enforceableProduct.isSuspended());
		oupProductWS.getUrls().addAll(getOupProductUrls(enforceableProduct.getUrls()));
		oupProductWS.setState(enforceableProduct.getState());

		//added for new fields
		if(enforceableProduct.getLandingPage()!=null)
			oupProductWS.setLandingPage(enforceableProduct.getLandingPage());
		if(enforceableProduct.getHomePage()!=null)
			oupProductWS.setHomePage(enforceableProduct.getHomePage());
		if(enforceableProduct.getAdminEmail()!=null)
			oupProductWS.setAdminEmail(enforceableProduct.getAdminEmail());
		if(enforceableProduct.getSla()!=null)
			oupProductWS.setSla(enforceableProduct.getSla());
		if(enforceableProduct.getRegisterableType()!=null)
			oupProductWS.setRegisterableType(enforceableProduct.getRegisterableType());

		ActivationDetail activationDetail = new ActivationDetail();
		if(enforceableProduct.getActivationStrategy()==null) {
			activationDetail.setActivationType(ActivationType.INSTANT);
			oupProductWS.setActivationDetail(activationDetail);
		} else {
			if(enforceableProduct.getActivationStrategy().toString().toLowerCase()
					.contains(ActivationType.INSTANT.toString().toLowerCase())) {
				activationDetail.setActivationType(ActivationType.INSTANT);
			} else if(enforceableProduct.getActivationStrategy().toString().toLowerCase()
					.contains(ActivationType.SELF.toString().toLowerCase())) {
				activationDetail.setActivationType(ActivationType.SELF);
			} else {
				activationDetail.setActivationType(ActivationType.VALIDATED);
			}

			if(enforceableProduct.getValidatorEmail()!=null)
				activationDetail.setValidatorEmail(enforceableProduct.getValidatorEmail());
			oupProductWS.setActivationDetail(activationDetail);
		}

		/*Added for externalId*/
		if(enforceableProduct.getExternalIds()!=null && !enforceableProduct.getExternalIds().isEmpty()){
			List<ExternalIdentifier> externalIdent = new ArrayList<ExternalIdentifier>();
			
			for(ExternalProductId extId: enforceableProduct.getExternalIds()){
				ExternalIdentifier external = getExternalIdentifier(extId);
				externalIdent.add(external);                
			}
			oupProductWS.getExternal().addAll(externalIdent);
		}

		if(enforceableProduct.getConfirmationEmailEnabled()!=null)
			oupProductWS.setSendUserConfirmationEmail(enforceableProduct.getConfirmationEmailEnabled());

		if(enforceableProduct.getRegistrationDefinitionType()!=null)
		{
			if(enforceableProduct.getRegistrationDefinitionType().equals(RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION.toString()))
				oupProductWS.setRegistrationDefinitionType(com.oup.eac.integration.erights.RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION);
			else
				oupProductWS.setRegistrationDefinitionType(com.oup.eac.integration.erights.RegistrationDefinitionType.PRODUCT_REGISTRATION);	
		}
		if (enforceableProduct.getPlatformList() != null && enforceableProduct.getPlatformList().size() > 0) {
			PlatformIdentifier platformIdentifier = new PlatformIdentifier() ;
			for (Platform platform : enforceableProduct.getPlatformList() ) {
				platformIdentifier.setPlatformId(platform.getPlatformId());
				platformIdentifier.setPlatformName(platform.getName());
				platformIdentifier.setPlatformCode(platform.getCode());
				oupProductWS.getPlatformIds().add(platformIdentifier) ;
			}
			
		}
		return oupProductWS;
	}

	private static ExternalIdentifier getExternalIdentifier(ExternalId<?> extId) {
		ExternalIdentifier externalIdentifier = new ExternalIdentifier();
		externalIdentifier.setId(extId.getExternalId());
		ExternalSystemIdType externalSystemTypeId = extId.getExternalSystemIdType();
		externalIdentifier.setSystemId(externalSystemTypeId.getExternalSystem().getName());
		externalIdentifier.setTypeId(externalSystemTypeId.getName());
		return externalIdentifier;
	}


	static List<OupProductUrlWS> getOupProductUrls(List<EnforceableProductUrlDto> enforceableProductUrls) {
		List<OupProductUrlWS> oupUrls = new ArrayList<OupProductUrlWS>();
		for (EnforceableProductUrlDto url : enforceableProductUrls) {
			OupProductUrlWS oupUrl = new OupProductUrlWS();

			oupUrl.setProtocol(url.getProtocol());
			oupUrl.setHost(url.getHost());
			oupUrl.setPath(url.getPath());
			oupUrl.setQuery(url.getQuery());
			oupUrl.setFragment(url.getFragment());
			oupUrl.setMatchExp(url.getExpression());
			oupUrls.add(oupUrl);
		}
		return oupUrls;
	}
	static EnforceableProductDto getProduct(final OupGetProductWS oupProductWS) {
		/*Modified constructor to add externalId*/
		EnforceableProductDto enforceableProduct= null;
		if(oupProductWS.getParentIds()!=null && oupProductWS.getParentIds().getProductId()!=null)
			enforceableProduct = new EnforceableProductDto(oupProductWS.getId(), oupProductWS.getName(), oupProductWS.getParentIds().getProductId(), oupProductWS.isSuspended(),oupProductWS.getState(), getEnforceableProductUrls(oupProductWS.getUrls()), getExternalIds(oupProductWS.getExternal()));
		else
			enforceableProduct = new EnforceableProductDto(oupProductWS.getId(), oupProductWS.getName(), oupProductWS.isSuspended(),oupProductWS.getState(), getEnforceableProductUrls(oupProductWS.getUrls()), getExternalIds(oupProductWS.getExternal()));
		if(oupProductWS.getActivationDetail()!=null) {
			if(oupProductWS.getActivationDetail().getActivationType().toString().toLowerCase()
					.contains(ActivationType.INSTANT.toString().toLowerCase())) {
				enforceableProduct.setActivationStrategy(ActivationStrategy.INSTANT.toString());
			} else if(oupProductWS.getActivationDetail().getActivationType().toString().toLowerCase()
					.contains(ActivationType.SELF.toString().toLowerCase())) {
				enforceableProduct.setActivationStrategy(ActivationStrategy.SELF.toString());
			} else {
				enforceableProduct.setActivationStrategy(ActivationStrategy.VALIDATED.toString());
				enforceableProduct.setValidatorEmail(oupProductWS.getActivationDetail().getValidatorEmail());
			}
		}

		//List<LinkedProduct> linkedProducts = new ArrayList<LinkedProduct>() ;
		if(oupProductWS.getLinkedProducts() !=  null && oupProductWS.getLinkedProducts().getProductId().size() > 0)
		for (String linkedId: oupProductWS.getLinkedProducts().getProductId()){
			LinkedProductNew linkedProduct = new LinkedProductNew() ;
			linkedProduct.setProductId(linkedId) ;
			enforceableProduct.getLinkedProducts().add(linkedProduct) ;
		}
		if (oupProductWS.getRegisterableType() != null ){
			enforceableProduct.setRegisterableType(oupProductWS.getRegisterableType());
		}
		if ( oupProductWS.getLandingPage() != null ){
			enforceableProduct.setLandingPage(oupProductWS.getLandingPage()) ; 
		}
		if (oupProductWS.getHomePage() != null ){
			enforceableProduct.setHomePage(oupProductWS.getHomePage());
		}
		if ( oupProductWS.getAdminEmail() != null ){
			enforceableProduct.setAdminEmail(oupProductWS.getAdminEmail()) ; 
		}
		if (oupProductWS.getSla() != null ){
			enforceableProduct.setSla(oupProductWS.getSla());
		}
		if ( oupProductWS.getRegistrationDefinitionType() != null ){
			enforceableProduct.setRegistrationDefinitionType(oupProductWS.getRegistrationDefinitionType().toString()) ; 
		}
		
		if (oupProductWS.getDivisionId() != null ){
			enforceableProduct.setDivisionId(oupProductWS.getDivisionId());
		}
		
		OupProductLicenseWS productLicenseWS = oupProductWS.getProductLicense() ;
		LicenceDto licenceDto = new LicenceDto() ;
		
		if (oupProductWS.getProductLicense() != null ) {
			OupLicenseDetail oupLicenseDetail = oupProductWS.getProductLicense().getLicenseDetail();
			LicenceDetailDto licenceDetailDto = null;
			if(oupLicenseDetail.isSetOupRollingLicenseData()) {
				OupRollingLicenseData oupRollingLicenseData = oupLicenseDetail.getOupRollingLicenseData();
				RollingLicenceDetailDto dtoObj = new RollingLicenceDetailDto(RollingBeginOn.valueOf(oupRollingLicenseData.getBeginOn().toString()),RollingUnitType.valueOf(oupRollingLicenseData.getPeriodUnit().toString()) , oupRollingLicenseData.getPeriodValue(), null);
				licenceDetailDto = dtoObj;
			} else if(oupLicenseDetail.isSetOupStandardConcurrencyLicenseData()) {
				OupStandardConcurrencyLicenseData  obj = oupLicenseDetail.getOupStandardConcurrencyLicenseData();
				StandardConcurrentLicenceDetailDto dtoObj = new StandardConcurrentLicenceDetailDto(obj.getTotalConcurrency(), obj.getUserConcurrency());
				licenceDetailDto = dtoObj;
			} else if(oupLicenseDetail.isSetOupUsageLicenseData()) {
				UsageLicenceDetailDto dtoObj = new UsageLicenceDetailDto();
				dtoObj.setAllowedUsages(oupLicenseDetail.getOupUsageLicenseData().getQuantityLimit());;
				licenceDetailDto = dtoObj;
			}
			if (oupProductWS.getProductLicense().getStartDate() != null) {
				licenceDetailDto.setStartDate(dateConverter.safeConvertDate(productLicenseWS.getStartDate()));
			}
			if (oupProductWS.getProductLicense().getEndDate() != null) {
				licenceDetailDto.setEndDate(dateConverter.safeConvertDate(productLicenseWS.getEndDate()));
				
			}
			licenceDto.setLicenceDetail(licenceDetailDto);
		}
		enforceableProduct.setLicenceDetail(licenceDto);
		enforceableProduct.setConfirmationEmailEnabled(oupProductWS.isSendUserConfirmationEmail());
		
		if (oupProductWS.getPlatformDetails() != null && oupProductWS.getPlatformDetails().size() > 0 ) {
			List<Platform> platformList = new ArrayList<Platform>() ; 
			for (PlatformIdentifier platformIdentifier : oupProductWS.getPlatformDetails()) {
				Platform platform = new Platform() ;
				platform.setCode(platformIdentifier.getPlatformCode());
				platform.setName(platformIdentifier.getPlatformName());
				platform.setPlatformId(platformIdentifier.getPlatformId());
				platformList.add(platform) ;
			}
			enforceableProduct.setPlatformList(platformList);
		}
		
		return enforceableProduct; 
	}

	static List<EnforceableProductUrlDto> getEnforceableProductUrls(List<OupProductUrlWS> oupProductUrlsWS) {
		List<EnforceableProductUrlDto> urls = new ArrayList<EnforceableProductUrlDto>(oupProductUrlsWS.size());
		for (OupProductUrlWS oupProductUrlWS : oupProductUrlsWS) {
			EnforceableProductUrlDto url = new EnforceableProductUrlDto(oupProductUrlWS.getProtocol(), 
					oupProductUrlWS.getHost(),oupProductUrlWS.getPath(), oupProductUrlWS.getQuery(), oupProductUrlWS.getFragment(), oupProductUrlWS.getMatchExp());

			urls.add(url);
		}
		return urls;
	}

	/*Added for externalId*/
	static List<ExternalProductId>  getExternalIds(List<ExternalIdentifier> externalId){
		List<ExternalProductId> externalProductId = new ArrayList<ExternalProductId>();
		for(ExternalIdentifier extId : externalId){
			ExternalProductId externalProduct = new ExternalProductId();
			ExternalSystemIdType externalSystemTypeId = new ExternalSystemIdType();
			ExternalSystem extSys = new ExternalSystem();
			extSys.setName(extId.getSystemId());
			extSys.setId(extId.getId());
			externalSystemTypeId.setExternalSystem(extSys);
			externalSystemTypeId.setName(extId.getTypeId());
			externalProduct.setExternalId(extId.getId());
			externalProduct.setExternalSystemIdType(externalSystemTypeId);
			externalProduct.setId(extId.getId()+","+extId.getSystemId()+","+extId.getTypeId());
			
			
			externalProductId.add(externalProduct) ;
		}
		return externalProductId;
	}

	static Set<ExternalCustomerId>  getExternalUserIds(List<ExternalIdentifier> externalId){
		Set<ExternalCustomerId> externalCustomerId = new HashSet<ExternalCustomerId>();
		for(ExternalIdentifier extId : externalId){
			ExternalCustomerId result = getExternalCustomerId(extId);
			externalCustomerId.add(result); 
		}
		return externalCustomerId;
	}

	private static ExternalProductId getExternalProductId(ExternalIdentifier extId) {
		ExternalProductId externalProductId = new ExternalProductId();

		ExternalSystemIdType externalSystemTypeId = new ExternalSystemIdType();
		ExternalSystem extSys = new ExternalSystem();
		extSys.setName(extId.getSystemId());
		externalSystemTypeId.setExternalSystem(extSys);
		externalSystemTypeId.setName(extId.getTypeId());
		externalProductId.setExternalSystemIdType(externalSystemTypeId);
		externalProductId.setExternalId(extId.getId());
		return externalProductId;
	}

	private static ExternalCustomerId getExternalCustomerId(ExternalIdentifier extId) {
		ExternalCustomerId externalCustoemrId = new ExternalCustomerId();

		ExternalSystemIdType externalSystemTypeId = new ExternalSystemIdType();
		ExternalSystem extSys = new ExternalSystem();
		extSys.setName(extId.getSystemId());
		externalSystemTypeId.setExternalSystem(extSys);
		externalSystemTypeId.setName(extId.getTypeId());
		externalSystemTypeId.setDescription("");
		externalCustoemrId.setExternalSystemIdType(externalSystemTypeId);
		externalCustoemrId.setExternalId(extId.getId());
		return externalCustoemrId;
	}

	static OupLicenseWS getOupLicenseWS(final LicenceTemplate licenceTemplate, final List<String> productIds) {
		OupLicenseWS oupLicenseWS = new OupLicenseWS();
		oupLicenseWS.setStartDate(DateUtils.safeConvertLocalDate(licenceTemplate.getStartDate()));
		oupLicenseWS.setEndDate(DateUtils.safeConvertLocalDate(licenceTemplate.getEndDate()));
		oupLicenseWS.getProductIds().addAll(productIds);
		if (licenceTemplate.getLicenceType() != LicenceType.STANDARD) {
			oupLicenseWS.setLicenseDetail(getOupLicenseDetail(licenceTemplate));
		} else {
			oupLicenseWS.setLicenseDetail(new OupLicenseDetail());
		}
		return oupLicenseWS;
	}

	static OupLicenseWS getOupLicenseWS(final LicenceTemplate licenceTemplate, final List<String> productIds, final boolean enabled) {
		OupLicenseWS oupLicenseWS = new OupLicenseWS();
		oupLicenseWS.setEnabled(enabled);
		if(licenceTemplate.getStartDate() != null ) {
			oupLicenseWS.setStartDate(DateUtils.safeConvertLocalDate(licenceTemplate.getStartDate()));
		}
		if ( licenceTemplate.getEndDate() != null ) {
			oupLicenseWS.setEndDate(DateUtils.safeConvertLocalDate(licenceTemplate.getEndDate()));
		}
		oupLicenseWS.getProductIds().addAll(productIds);
		if (licenceTemplate.getLicenceType() != LicenceType.STANDARD) {
			oupLicenseWS.setLicenseDetail(getOupLicenseDetail(licenceTemplate));
		} else {
			oupLicenseWS.setLicenseDetail(new OupLicenseDetail());
		}
		return oupLicenseWS;
	}

	static OupLicenseWS getOupLicenseWS(final LicenceDto licence) {
		OupLicenseWS oupLicenseWS = new OupLicenseWS();
		oupLicenseWS.setId(licence.getLicenseId());
		oupLicenseWS.setEnabled(licence.isEnabled());
		oupLicenseWS.setStartDate(DateUtils.safeConvertLocalDate(licence.getStartDate()));
		oupLicenseWS.setEndDate(DateUtils.safeConvertLocalDate(licence.getEndDate()));
		oupLicenseWS.getProductIds().addAll(licence.getProductIds());
		oupLicenseWS.setLicenseDetail(getOupLicenseDetail(licence.getLicenceDetail()));  
		if (licence.getLicenceTemplateId() != null ) {
			oupLicenseWS.setLicenseTemplateId(licence.getLicenceTemplateId());
		}
		return oupLicenseWS;
	}

	static OupLicenseDetail getOupLicenseDetail(final LicenceDetailDto licenceDetail) {
		OupLicenseDetail oupLicenseDetail = new OupLicenseDetail();
		if (licenceDetail == null || licenceDetail.getLicenceType() == null) {
			oupLicenseDetail.setOupUsageLicenseData(null);
		} else {
			switch (licenceDetail.getLicenceType()) {
			case CONCURRENT:
				oupLicenseDetail.setOupStandardConcurrencyLicenseData(getOupStandardConcurrencyLicenseData((StandardConcurrentLicenceDetailDto) licenceDetail));
				break;
			case ROLLING:
				oupLicenseDetail.setOupRollingLicenseData(getOupRollingLicenseData((RollingLicenceDetailDto) licenceDetail));
				break;
			case USAGE:
				oupLicenseDetail.setOupUsageLicenseData(getOupUsageLicenseData((UsageLicenceDetailDto) licenceDetail));
				break;
			default:
				throw new IllegalArgumentException("Unknown Licence Type");
			}
		}
		return oupLicenseDetail;
	}

	static OupLicenseDetail getOupLicenseDetail(final LicenceTemplate licenceTemplate) {
		OupLicenseDetail oupLicenseDetail = new OupLicenseDetail();
		if (licenceTemplate.getLicenceType() == null) {
			oupLicenseDetail.setOupUsageLicenseData(null);
		} else {
			switch (licenceTemplate.getLicenceType()) {
			case CONCURRENT:
				oupLicenseDetail.setOupStandardConcurrencyLicenseData(getOupStandardConcurrencyLicenseData((ConcurrentLicenceTemplate) licenceTemplate));
				break;
			case ROLLING:
				oupLicenseDetail.setOupRollingLicenseData(getOupRollingLicenseData((RollingLicenceTemplate) licenceTemplate));
				break;
			case USAGE:
				oupLicenseDetail.setOupUsageLicenseData(getOupUsageLicenseData((UsageLicenceTemplate) licenceTemplate));
				break;
			default:
				throw new IllegalArgumentException("Unknown Licence Type");
			}
		}
		return oupLicenseDetail;
	}

	static List<LicenceDto> getLicences(final List<OupLicenseInfoWS> oupLicensesInfoWS) {

		List<LicenceDto> licences = new ArrayList<LicenceDto>(oupLicensesInfoWS.size());
		List<String> productIdLst=null;
		//EnforceableProductDto products=new EnforceableProductDto();
		//List<Integer> productIdLst=new ArrayList<Integer>();
		
		for (OupLicenseInfoWS oupLicenseInfoWS : oupLicensesInfoWS) {
			productIdLst=new ArrayList<String>();
			//products=new EnforceableProductDto();
			
			//List<EnforceableProductDto> productLst=new ArrayList<EnforceableProductDto>();
			OupGetLicenseWS oupLicenseWS = oupLicenseInfoWS.getOupLicense();
			LicenceDto licence = null;
			/*System.out.println(oupLicenseWS + "OupLicense");
			System.out.println(oupLicenseWS.getId() + "OupLicenseId");
			System.out.println(oupLicenseInfoWS.getExpiryDate() + "getExpiryDate");
			System.out.println(oupLicenseInfoWS.isExpired() + "isExpired");
			System.out.println(oupLicenseInfoWS.isActive() + "isActive");
			System.out.println(oupLicenseInfoWS.isCompleted() + "isCompleted");
			System.out.println(oupLicenseInfoWS.isAwaitingValidation() + "isAwaitingValidation");
			System.out.println(oupLicenseInfoWS.isDenied() + "isDenied");
			AuditLogger.logEvent(oupLicenseWS + "OupLicense");
			AuditLogger.logEvent(oupLicenseWS.getId() + "OupLicenseId");
			AuditLogger.logEvent(oupLicenseInfoWS.getExpiryDate() + "getExpiryDate");
			AuditLogger.logEvent(oupLicenseInfoWS.isExpired() + "isExpired");
			AuditLogger.logEvent(oupLicenseInfoWS.isActive() + "isActive");
			AuditLogger.logEvent(oupLicenseInfoWS.isCompleted() + "isCompleted");
			AuditLogger.logEvent(oupLicenseInfoWS.isAwaitingValidation() + "isAwaitingValidation");
			AuditLogger.logEvent(oupLicenseInfoWS.isDenied() + "isDenied");*/
			try {
				licence = new LicenceDto(oupLicenseWS.getId(), DateUtils.safeConvertDateTime(oupLicenseInfoWS.getExpiryDate()), oupLicenseInfoWS.isExpired(), oupLicenseInfoWS.isActive(), oupLicenseInfoWS.isCompleted(), oupLicenseInfoWS.isAwaitingValidation(), oupLicenseInfoWS.isDenied());
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			licence.setStartDate(DateUtils.safeConvertDate(oupLicenseWS.getStartDate()));
			licence.setEndDate(DateUtils.safeConvertDate(oupLicenseWS.getEndDate()));
			licence.setStartDateTime(DateUtils.safeConvertDateTime(oupLicenseWS.getStartDate()));
			licence.setEndDateTime  (DateUtils.safeConvertDateTime(oupLicenseWS.getEndDate()));
			licence.setCreatedDate(DateUtils.safeConvertDateTime(oupLicenseInfoWS.getCreatedDate()));
			licence.setUpdatedDate(DateUtils.safeConvertDateTime(oupLicenseInfoWS.getUpdatedDate()));
			licence.setActivationCode(oupLicenseInfoWS.getActivationCode());
			licence.setLicenceTemplateId(oupLicenseWS.getLicenseTemplateId());
			
			// to set product and its information
			for(OupProducts oupProduct : oupLicenseWS.getProducts())
			{
				productIdLst.add(oupProduct.getProductId());
						EnforceableProductDto enforceableProductDto=new EnforceableProductDto();
						if(oupProduct.getProductId()!=null)
							enforceableProductDto.setProductId(oupProduct.getProductId());
						if(oupProduct.getProductName()!=null)
							enforceableProductDto.setName(oupProduct.getProductName());
							
						if(oupProduct.getExternalSystem()!=null && oupProduct.getExternalSystem().size()>0)
						{
						
							List<ExternalProductId> productExternalIds=new ArrayList<ExternalProductId>();
 							for(int i=0;i<oupProduct.getExternalSystem().size();i++)
							{
 								ExternalIdentifier exIdentifier =  oupProduct.getExternalSystem().get(i);
 								
								ExternalProductId externalProductId=new ExternalProductId();
								ExternalSystemIdType externalSystemIdType=new ExternalSystemIdType();
								ExternalSystem externalSystem=new ExternalSystem();
								
								externalSystem.setId(exIdentifier.getSystemId());
								externalSystem.setName(exIdentifier.getSystemId());
								
								externalSystemIdType.setExternalSystem(externalSystem);
								externalSystemIdType.setName(exIdentifier.getTypeId());
								externalSystemIdType.setId(exIdentifier.getTypeId());
								
								externalProductId.setId(exIdentifier.getId());
								externalProductId.setExternalSystemIdType(externalSystemIdType);
								externalProductId.setExternalId(exIdentifier.getId());
								
								productExternalIds.add(externalProductId);
								
								
							}
							
							enforceableProductDto.setExternalIds(productExternalIds);
						}	
						if(oupProduct.getDivisionName()!=null)
						{
							Division division=new Division();
							division.setDivisionType(oupProduct.getDivisionName());
							enforceableProductDto.setDivision(division);
						}	
							
						if(oupProduct.getLandingPage()!=null)
							enforceableProductDto.setLandingPage(oupProduct.getLandingPage());
								
						if(oupProduct.getHomePage()!=null)
							enforceableProductDto.setHomePage(oupProduct.getHomePage());
						
						//to set link product details
						if(oupProduct.getLinkedProducts()!=null && oupProduct.getLinkedProducts().size()>0)
						{
							List<LinkedProductNew> linkedProductsLst=new ArrayList<LinkedProductNew>(); 
							for(OupLinkedProductDetails oupLinkProduct : oupProduct.getLinkedProducts())
							{
								LinkedProductNew linkedProductNew=new LinkedProductNew();
								
								
								if(oupLinkProduct.getLinkedProductId()!=null)
									linkedProductNew.setProductId(oupLinkProduct.getLinkedProductId());
								
								if(oupLinkProduct.getProductName()!=null)
									linkedProductNew.setName(oupLinkProduct.getProductName());
									
								if(oupLinkProduct.getExternalSystem()!=null && oupLinkProduct.getExternalSystem().size()>0)
								{
									List<ExternalProductId> linkProductExternalIds=new ArrayList<ExternalProductId>();
									
									for(int i=0;i<oupLinkProduct.getExternalSystem().size();i++)
									{
										ExternalIdentifier exIdentifier =  oupLinkProduct.getExternalSystem().get(i);
										ExternalProductId externalLinkProductId=new ExternalProductId();
										ExternalSystemIdType externalSystemIdType=new ExternalSystemIdType();
										ExternalSystem externalSystem=new ExternalSystem();
										
										externalSystem.setId(exIdentifier.getSystemId());
										externalSystem.setName(exIdentifier.getSystemId());
										
										externalSystemIdType.setExternalSystem(externalSystem);
										externalSystemIdType.setName(exIdentifier.getTypeId());
										externalSystemIdType.setId(exIdentifier.getTypeId());
										
										externalLinkProductId.setId(exIdentifier.getId());
										externalLinkProductId.setExternalSystemIdType(externalSystemIdType);
										externalLinkProductId.setExternalId(exIdentifier.getId());
										
										linkProductExternalIds.add(externalLinkProductId);
									}
									
									linkedProductNew.setExternalIds(linkProductExternalIds);
									
								}	
								if(oupLinkProduct.getDivisionName()!=null)
								{
									Division division=new Division();
									division.setDivisionType(oupLinkProduct.getDivisionName());
									linkedProductNew.setDivision(division);
								}	
									
								if(oupLinkProduct.getLandingPage()!=null)
									linkedProductNew.setLandingPage(oupLinkProduct.getLandingPage());
										
								if(oupLinkProduct.getHomePage()!=null)
									linkedProductNew.setHomePage(oupLinkProduct.getHomePage());
								
								
								linkedProductsLst.add(linkedProductNew);
								
							}
							enforceableProductDto.setLinkedProducts(linkedProductsLst);
						}
								
					licence.setProducts(enforceableProductDto);	
			}
			licence.setProductIds(productIdLst);
			licence.setEnabled(oupLicenseWS.isEnabled());
			long startTime = System.currentTimeMillis();
			licence.setLicenceDetail(getLicenceDetail(oupLicenseInfoWS));
			AuditLogger.logEvent("Time to getLicenceDetail :: " + (System.currentTimeMillis() - startTime));

			licences.add(licence);
		}

		return licences;
	}

	static LicenceDetailDto getLicenceDetail(final OupLicenseInfoWS oupLicenseInfoWS) {

		OupGetLicenseWS oupLicenseWS = oupLicenseInfoWS.getOupLicense();

		if (oupLicenseWS.getLicenseDetail() == null) {
			// Atypon don't return a licence detail for standard licences.
			// However, we return an instance of StandardLicenceDetailDto
			// rather than returning null. This avoids null checking in client code.
			return new StandardLicenceDetailDto();
		} else if (oupLicenseWS.getLicenseDetail().getOupStandardConcurrencyLicenseData() != null) {
			OupStandardConcurrencyLicenseData detailWS = oupLicenseWS.getLicenseDetail().getOupStandardConcurrencyLicenseData();
			StandardConcurrentLicenceDetailDto detail = new StandardConcurrentLicenceDetailDto(detailWS.getTotalConcurrency(), detailWS.getUserConcurrency());    		
			return detail;
		} else if (oupLicenseWS.getLicenseDetail().getOupRollingLicenseData() != null) {
			OupRollingLicenseData detailWS = oupLicenseWS.getLicenseDetail().getOupRollingLicenseData();
			OupRollingLicenseInfoWS oupRollingLicenseInfoWS = (OupRollingLicenseInfoWS)oupLicenseInfoWS; 
			XMLGregorianCalendar temp = oupRollingLicenseInfoWS.getFirstUse();
			DateTime firstUse = DateUtils.safeConvertDateTime(temp);
			RollingLicenceDetailDto detail = new RollingLicenceDetailDto(getRollingBeginOn(detailWS.getBeginOn()), getRollingUnitType(detailWS.getPeriodUnit()), detailWS.getPeriodValue(), firstUse);
			return detail;
		} else if (oupLicenseWS.getLicenseDetail().getOupUsageLicenseData() != null) {
			OupUsageLicenseData detailWS = oupLicenseWS.getLicenseDetail().getOupUsageLicenseData();
			OupUsageLicenseInfoWS oupUsageLicenseInfoWS = (OupUsageLicenseInfoWS)oupLicenseInfoWS;
			UsageLicenceDetailDto detail = new UsageLicenceDetailDto(oupUsageLicenseInfoWS.getUsageRemaining());
			detail.setAllowedUsages(detailWS.getQuantityLimit());
			return detail;
		}
		else {
			return null;
		}
	}

	public static LicenceDetailDto getLicenceTemplateDto(LicenceTemplate licenceTemplate) {
		LicenceDetailDto licenceDetailDto = null;
		if(licenceTemplate == null)
			return null;

		if(licenceTemplate.getLicenceType().equals(LicenceType.ROLLING)) {
			RollingLicenceTemplate obj = (RollingLicenceTemplate)licenceTemplate;
			RollingLicenceDetailDto dtoObj = new RollingLicenceDetailDto(obj.getBeginOn(), obj.getUnitType(), obj.getTimePeriod(), null);
			licenceDetailDto = dtoObj;
		} else if(licenceTemplate.getLicenceType().equals(LicenceType.CONCURRENT)) {
			ConcurrentLicenceTemplate obj = (ConcurrentLicenceTemplate)licenceTemplate;
			StandardConcurrentLicenceDetailDto dtoObj = new StandardConcurrentLicenceDetailDto(obj.getTotalConcurrency(), obj.getUserConcurrency());
			licenceDetailDto = dtoObj;
		} else if(licenceTemplate.getLicenceType().equals(LicenceType.USAGE)) {
			UsageLicenceTemplate obj = (UsageLicenceTemplate)licenceTemplate;
			UsageLicenceDetailDto dtoObj = new UsageLicenceDetailDto();
			dtoObj.setAllowedUsages(obj.getAllowedUsages());;
			licenceDetailDto = dtoObj;
		}
		licenceDetailDto.setStartDate(licenceTemplate.getStartDate());
		licenceDetailDto.setEndDate(licenceTemplate.getEndDate());
		return licenceDetailDto;
	}

	public static OupLicenseWS getOupLicenseWS(LicenceDetailDto licenceDetailDto, List<Integer> productIds) {
		OupLicenseWS oupLicenseWS = new OupLicenseWS();
		OupLicenseDetail oupLicenseDetail = getOupLiceseDetail(licenceDetailDto);
		oupLicenseWS.setStartDate(DateUtils.safeConvertLocalDate(licenceDetailDto.getStartDate()));
		oupLicenseWS.setEndDate(DateUtils.safeConvertLocalDate(licenceDetailDto.getEndDate()));
		oupLicenseWS.setLicenseDetail(oupLicenseDetail);
		return oupLicenseWS;
	}

	public static OupLicenseDetail getOupLiceseDetail(LicenceDetailDto licenceDetailDto) {
		OupLicenseDetail oupLicenseDetail = new OupLicenseDetail();
		if(licenceDetailDto.getLicenceType().equals(LicenceDetailDto.LicenceType.ROLLING)) {
			OupRollingLicenseData obj = new OupRollingLicenseData();
			RollingLicenceDetailDto dtoObj = (RollingLicenceDetailDto)licenceDetailDto;
			obj.setBeginOn(RollingBEGINON.valueOf(dtoObj.getBeginOn().name()));
			obj.setPeriodUnit(RollingUNITTYPE.valueOf(dtoObj.getUnitType().name()));
			obj.setPeriodValue(dtoObj.getTimePeriod());
			oupLicenseDetail.setOupRollingLicenseData(obj);
		} else if(licenceDetailDto.getLicenceType().equals(LicenceDetailDto.LicenceType.CONCURRENT)) {
			OupStandardConcurrencyLicenseData obj = new OupStandardConcurrencyLicenseData();
			StandardConcurrentLicenceDetailDto dtoObj = (StandardConcurrentLicenceDetailDto)licenceDetailDto;
			obj.setTotalConcurrency(dtoObj.getTotalConcurrency());
			obj.setUserConcurrency(dtoObj.getUserConcurrency());
			oupLicenseDetail.setOupStandardConcurrencyLicenseData(obj);
		} else if(licenceDetailDto.getLicenceType().equals(LicenceDetailDto.LicenceType.USAGE)) {
			OupUsageLicenseData obj = new OupUsageLicenseData();
			UsageLicenceDetailDto dtoObj = (UsageLicenceDetailDto)licenceDetailDto;
			obj.setQuantityLimit(dtoObj.getAllowedUsages());
			oupLicenseDetail.setOupUsageLicenseData(obj);
		}
		return oupLicenseDetail;
	}

	public static OupLicenseWS getOupLicenseWSWithDateTimes(LicenceDtoDateTime licence) {
		OupLicenseWS oupLicenseWS = new OupLicenseWS();
		oupLicenseWS.setId(licence.getLicenseId());
		oupLicenseWS.setEnabled(licence.isEnabled());
		oupLicenseWS.setStartDate(DateUtils.safeConvertFromDateTime(licence.getStartDateTime()));
		oupLicenseWS.setEndDate(DateUtils.safeConvertFromDateTime(licence.getEndDateTime()));
		oupLicenseWS.getProductIds().addAll(licence.getProductIds());
		oupLicenseWS.setLicenseDetail(getOupLicenseDetail(licence.getLicenceDetail()));        
		return oupLicenseWS;
	}

	public static CustomerDto getCustomerDto(OupGetUserWS oupGetUserWS) {
		UserType userType= UserType.CUSTOMER;
		if(oupGetUserWS.getUserType().equalsIgnoreCase("CUSTOMER"))
			userType= UserType.CUSTOMER;
		else if(oupGetUserWS.getUserType().equalsIgnoreCase("ADMIN"))
			userType= UserType.ADMIN;
		
		EmailVerificationState emailVerificationState = EmailVerificationState.UNKNOWN;
		if(oupGetUserWS.getEmailVerificationState().equalsIgnoreCase("UNKNOWN"))
			emailVerificationState = EmailVerificationState.UNKNOWN;
		else if(oupGetUserWS.getEmailVerificationState().equalsIgnoreCase("EMAIL_SENT"))
			emailVerificationState = EmailVerificationState.EMAIL_SENT;
		else if(oupGetUserWS.getEmailVerificationState().equalsIgnoreCase("VERIFIED"))
			emailVerificationState = EmailVerificationState.VERIFIED;

		Locale locale = null;
		if(null != oupGetUserWS.getLocale())
			if(oupGetUserWS.getLocale().getLanguage() != null && oupGetUserWS.getLocale().getCountry() == null && oupGetUserWS.getLocale().getVariant() == null)
				locale = new Locale(oupGetUserWS.getLocale().getLanguage());
			else if(oupGetUserWS.getLocale().getLanguage() != null && oupGetUserWS.getLocale().getCountry() != null && oupGetUserWS.getLocale().getVariant() == null)
				locale=new Locale(oupGetUserWS.getLocale().getLanguage(), oupGetUserWS.getLocale().getCountry());
			else if(oupGetUserWS.getLocale().getLanguage() != null && oupGetUserWS.getLocale().getCountry() != null && oupGetUserWS.getLocale().getVariant() != null)
				locale=new Locale(oupGetUserWS.getLocale().getLanguage(), oupGetUserWS.getLocale().getCountry(), oupGetUserWS.getLocale().getVariant());

		CustomerDto customerDto = new CustomerDto(
				getLoginPasswordCredential(oupGetUserWS).getUsername(),
				oupGetUserWS.getGroupIds(),
				oupGetUserWS.getConcurrency(),
				getLoginPasswordCredential(oupGetUserWS), 
				oupGetUserWS.isSuspended(),
				userType,
				oupGetUserWS.getId(),
				oupGetUserWS.getEmailAddress(),
				oupGetUserWS.getFirstName(),
				oupGetUserWS.getLastName(),
				emailVerificationState,
				oupGetUserWS.getTimeZone(),
				locale, getExternalUserIds(oupGetUserWS.getExternal()),
				oupGetUserWS.isForceResetPassword());
		customerDto.setFailedLoginAttemptes(oupGetUserWS.getFailedLoginAtempt());
		if(oupGetUserWS.getLastLoginDate() != null) {
			customerDto.setLastLoginDateTime(DateTime.parse(oupGetUserWS.getLastLoginDate().toString()));
		}
		if(oupGetUserWS.getCreatedDate() != null) {
			customerDto.setCreatedDateTime(DateTime.parse(oupGetUserWS.getCreatedDate().toString()));
		} else { //TODO:Remove this else block once created date will be added in response of all getUserAccount SOAP Services. 
			customerDto.setCreatedDateTime(new DateTime());
		}
		return customerDto;		
	}   

	public static ExternalSystemWS getExternalSystemWs(ExternalSystem externalSystem)
	{
		ExternalSystemWS externalSystemWS=new ExternalSystemWS();
		if(externalSystem!=null)
		{
			if(externalSystem.getName()!=null)
				externalSystemWS.setSystemId(externalSystem.getName());

			if(externalSystem.getDescription()!=null)
				externalSystemWS.setDescription(externalSystem.getDescription());

			for(ExternalSystemIdType externalSystemIdType : externalSystem.getExternalSystemIdTypes())
			{
				SystemTypeId systemTypeId=new SystemTypeId();
				if(externalSystemIdType.getName()!=null)
					systemTypeId.setTypeId(externalSystemIdType.getName());

				if(externalSystemIdType.getDescription()!=null)
					systemTypeId.setDescription(externalSystemIdType.getDescription());

				externalSystemWS.getTypeIds().add(systemTypeId);
			}

		}
		return externalSystemWS;

	}

	static OupUpdateProductWS getUpdateProduct(final EnforceableProductDto enforceableProduct) {
		OupUpdateProductWS oupProductWS = new OupUpdateProductWS();
		oupProductWS.setId(enforceableProduct.getProductId());
		oupProductWS.setName(enforceableProduct.getName());
		/*OupProductIds oupProductIds= new OupProductIds();
		oupProductIds.getProductId().addAll(enforceableProduct.getParentIds());
		oupProductWS.setParentIds(oupProductIds);*/
		oupProductWS.setSuspended(enforceableProduct.isSuspended());
		oupProductWS.getUrls().addAll(getOupProductUrls(enforceableProduct.getUrls()));
		oupProductWS.setState(enforceableProduct.getState());
		oupProductWS.setDivisionId(enforceableProduct.getDivisionId());
		//added for new fields
		if(enforceableProduct.getLandingPage()!=null)
			oupProductWS.setLandingPage(enforceableProduct.getLandingPage());
		if(enforceableProduct.getHomePage()!=null)
			oupProductWS.setHomePage(enforceableProduct.getHomePage());
		if(enforceableProduct.getAdminEmail()!=null)
			oupProductWS.setAdminEmail(enforceableProduct.getAdminEmail());
		if(enforceableProduct.getSla()!=null)
			oupProductWS.setSla(enforceableProduct.getSla());
		if(enforceableProduct.getRegisterableType()!=null)
			oupProductWS.setRegisterableType(enforceableProduct.getRegisterableType());

		ActivationDetail activationDetail = new ActivationDetail();
		if(enforceableProduct.getActivationStrategy()==null) {
			activationDetail.setActivationType(ActivationType.INSTANT);
			oupProductWS.setActivationDetail(activationDetail);
		} else {
			if(enforceableProduct.getActivationStrategy().toString().toLowerCase()
					.contains(ActivationType.INSTANT.toString().toLowerCase())) {
				activationDetail.setActivationType(ActivationType.INSTANT);
			} else if(enforceableProduct.getActivationStrategy().toString().toLowerCase()
					.contains(ActivationType.SELF.toString().toLowerCase())) {
				activationDetail.setActivationType(ActivationType.SELF);
			} else {
				activationDetail.setActivationType(ActivationType.VALIDATED);
			}

			if(enforceableProduct.getValidatorEmail()!=null)
				activationDetail.setValidatorEmail(enforceableProduct.getValidatorEmail());
			oupProductWS.setActivationDetail(activationDetail);
		}

		/*Added for externalId*/
		if(enforceableProduct.getExternalIds()!=null && !enforceableProduct.getExternalIds().isEmpty()){
			List<ExternalIdentifier> externalIdent = new ArrayList<ExternalIdentifier>();
			for(ExternalProductId extId: enforceableProduct.getExternalIds()){
				ExternalIdentifier external = getExternalIdentifier(extId);
				externalIdent.add(external);                
			}
			oupProductWS.getExternal().addAll(externalIdent);
		}
		LicenceDetailDto licenceDetailDto = enforceableProduct.getLicenceDetail().getLicenceDetail() ;
		
		OupProductLicenseWS oupProductLicenseWS = new OupProductLicenseWS();
		if(licenceDetailDto != null ) {
			if(licenceDetailDto.getStartDate() != null)
			{
				oupProductLicenseWS.setStartDate(dateConverter.safeConvertLocalDate(licenceDetailDto.getStartDate()));
			}
			if(licenceDetailDto.getEndDate() != null)
			{
				oupProductLicenseWS.setEndDate(dateConverter.safeConvertLocalDate(licenceDetailDto.getEndDate()));
			}
			oupProductLicenseWS.setLicenseDetail(ErightsObjectFactory.getOupLiceseDetail(licenceDetailDto));	
		}
		oupProductWS.setProductLicense(oupProductLicenseWS);
		if(enforceableProduct.getConfirmationEmailEnabled()!=null)
			oupProductWS.setSendUserConfirmationEmail(enforceableProduct.getConfirmationEmailEnabled());

		if (enforceableProduct.getPlatformList() != null && enforceableProduct.getPlatformList().size() > 0) {
			for (Platform platform : enforceableProduct.getPlatformList() ) {
				PlatformIdentifier platformIdentifier = new PlatformIdentifier() ;
				platformIdentifier.setPlatformId(platform.getPlatformId());
				oupProductWS.getPlatformIds().add(platformIdentifier) ;
			}
			
		}
		return oupProductWS;
	}

	public static OupProductGroupWS getOupProductGroupWS(
			ProductGroupDto productGroupDto) {

		OupProductGroupWS productGroupWS = new OupProductGroupWS();
		//productGroupWS.setId(productGroupDto.getProductGroupId());
		productGroupWS.setName(productGroupDto.getProductGroupName());
		OupProductIds oupProductIds = new OupProductIds();
		oupProductIds.getProductId().addAll(productGroupDto.getProductIds());
		productGroupWS.setProductIds(oupProductIds);
		return productGroupWS;
	}

	public static ProductGroupDto getProductGroup(OupProductGroupWS productGroup) {
		ProductGroupDto productGroupDto = new ProductGroupDto();
		productGroupDto.setProductGroupId(productGroup.getId());
		productGroupDto.setProductGroupName(productGroup.getName());
		productGroupDto.setProductIds(productGroup.getProductIds().getProductId());

		return productGroupDto;
	}

	public static OupUpdateProductWS convertOupProduct(OupProductWS oupProductWS)
	{
		OupUpdateProductWS oupUpdateProductWS= new OupUpdateProductWS();

		oupUpdateProductWS.setActivationDetail(oupProductWS.getActivationDetail());
		oupUpdateProductWS.setAdminEmail(oupProductWS.getAdminEmail());
		oupUpdateProductWS.setHomePage(oupProductWS.getHomePage());
		oupUpdateProductWS.setId(oupProductWS.getId());
		oupUpdateProductWS.setLandingPage(oupProductWS.getLandingPage());
		oupUpdateProductWS.setName(oupProductWS.getName());
		oupUpdateProductWS.setProductLicense(oupProductWS.getProductLicense());
		oupUpdateProductWS.setRegisterableType(oupProductWS.getRegisterableType());
		oupUpdateProductWS.setSendUserConfirmationEmail(oupProductWS.isSendUserConfirmationEmail());
		oupUpdateProductWS.setSla(oupProductWS.getSla());
		oupUpdateProductWS.setState(oupProductWS.getState());
		oupUpdateProductWS.setSuspended(oupProductWS.isSuspended());
		oupUpdateProductWS.getUrls().addAll(oupProductWS.getUrls());
		oupUpdateProductWS.getExternal().addAll(oupProductWS.getExternal());
		/*if(oupProductWS.getRegistrationDefinitionType()!=null)
		{

			oupUpdateProductWS.setRegistrationDefinitionType(oupProductWS.getRegistrationDefinitionType());
		}*/

		return oupUpdateProductWS;
	}
	public static ActivationCodeBatch getOupActivationCodeBatch(ActivationCodeBatchDto activationCodeBatchDto){
		
		ActivationCodeBatch activationCodeBatch = new ActivationCodeBatch();
		activationCodeBatch.setBatchId(activationCodeBatchDto.getBatchId());
		activationCodeBatch.setAllowedUsages(activationCodeBatchDto.getAllowedUsages());
		activationCodeBatch.setNumberOfTokens(activationCodeBatchDto.getNumberOfTokens());
		String codeFormat = activationCodeBatchDto.getCodeFormat() ;
		if(codeFormat.equals(CodeFormatEnum.EAC_NUMERIC.toString()))
			activationCodeBatch.setCodeFormat(CodeFormatEnum.EAC_NUMERIC);
		
		if(activationCodeBatchDto.getValidFrom() != null){
			activationCodeBatch.setValidFrom(dateConverter.safeConvertLocalDate(activationCodeBatchDto.getValidFrom()));
		}
		if(activationCodeBatchDto.getValidTo() != null) {
			activationCodeBatch.setValidTo(dateConverter.safeConvertLocalDate(activationCodeBatchDto.getValidTo()));
		}
		return activationCodeBatch ;
	}
	
	public static ActivationCodeBatchDto getActivationCodeBatch(GetActivationCodeBatchByBatchIdResponse response){
		ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
		activationCodeBatchDto.setBatchId(response.getBatchId());
		activationCodeBatchDto.setNumberOfTokens(response.getNumberOfTokens());
		activationCodeBatchDto.setActivationCodes(response.getActivationCode());
		activationCodeBatchDto.setCodeFormat(ActivationCodeFormat.EAC_NUMERIC.toString());
		activationCodeBatchDto.setAddedInPool(response.getAddedInPool().toString());
		if(response.getActivationCodeLicense().getProductGroupId() != null) {
			activationCodeBatchDto.setProductGroupId(response.getActivationCodeLicense().getProductGroupId());
		} else {
			activationCodeBatchDto.setProductId(response.getActivationCodeLicense().getProductId());
		}
		activationCodeBatchDto.setLicenceDetailDto(getActivationLicenseDetailDto(response.getActivationCodeLicense().getLicenseDetails()));
		if(response.getValidFrom() != null ){
			activationCodeBatchDto.setValidFrom(dateConverter.safeConvertDate(response.getValidFrom()));
		}
		if(response.getValidTo() != null ){
			activationCodeBatchDto.setValidTo(dateConverter.safeConvertDate(response.getValidTo()));
		}
		return activationCodeBatchDto ;
	}
	
	public static ActivationCodeBatchDto getActivationCodeBatch(GetActivationCodeBatchByActivationCodeResponse response){
		ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
		activationCodeBatchDto.setBatchId(response.getBatchId());
		activationCodeBatchDto.setNumberOfTokens(response.getNumberOfTokens());
		activationCodeBatchDto.setActivationCodes(response.getActivationCode());
		activationCodeBatchDto.setCodeFormat(ActivationCodeFormat.EAC_NUMERIC.toString());
		
		if(response.getActivationCodeLicense().getProductGroupId() != null) {
			activationCodeBatchDto.setProductGroupId(response.getActivationCodeLicense().getProductGroupId());
		} else {
			activationCodeBatchDto.setProductId(response.getActivationCodeLicense().getProductId());
		}
		activationCodeBatchDto.setLicenceDetailDto(getActivationLicenseDetailDto(response.getActivationCodeLicense().getLicenseDetails()));
		if(response.getValidFrom() != null ){
			activationCodeBatchDto.setValidFrom(dateConverter.safeConvertDate(response.getValidFrom()));
		}
		if(response.getValidTo() != null ){
			activationCodeBatchDto.setValidTo(dateConverter.safeConvertDate(response.getValidTo()));
		}
		return activationCodeBatchDto ;
	}
	
	
	public static ActivationCodeBatchDto getActivationCodeDetailsByActivationCode(GetActivationCodeDetailsByActivationCodeResponse response){
		ActivationCodeBatchDto activationCodeBatchDto = new ActivationCodeBatchDto();
		activationCodeBatchDto.setBatchId(response.getBatchId());		
		activationCodeBatchDto.setAllowedUsages(response.getAllowedUsage());
		activationCodeBatchDto.setActualUsage(response.getActualUsage());
		activationCodeBatchDto.setCodeFormat(ActivationCodeFormat.EAC_NUMERIC.toString());
				
		List<ActivationCodeRegistration> activationCodeRegistrations = new ArrayList<ActivationCodeRegistration>();
		ActivationCodeRegistration activationCodeRegistration = new ActivationCodeRegistration();
		ActivationCodeRegistrationDefinition regDef = new ActivationCodeRegistrationDefinition();
		if (response.getProductId() != null) {
			Product product = new RegisterableProduct();
			product.setId(response.getProductId());
			//product.setErightsId(response.getProductId());
			regDef.setProduct(product);
			activationCodeBatchDto.setProductId(response.getProductId());
		}
		if (response.getProductGroupId() != null) {
			EacGroups eacGroup = new EacGroups();
			eacGroup.setId(response.getProductGroupId().toString());
			regDef.setEacGroup(eacGroup);
			activationCodeBatchDto.setProductGroupId(response.getProductGroupId());
		}
		//activationCodeRegistration.setErightsLicenceId(response.getRedeemActivatoinCodeUserDetails());
		activationCodeRegistration.setRegistrationDefinition(regDef);
		/*activationCodeRegistration.setActivated(activated);
		//activationCodeRegistration.setActivationCode();
		activationCodeRegistration.setCreatedDate(response.get);
		activationCodeRegistration.setLinkedRegistrations(linkedRegistrations);
		activationCodeRegistration.setUpdatedDate(updatedDate);
		activationCodeRegistration.setVersion(version);*/
		
		List<RedeemActivatoinCodeUserDetails> redeemActivationCodeResponse = response.getRedeemActivatoinCodeUserDetails();
		for (RedeemActivatoinCodeUserDetails redeemActivationCode : redeemActivationCodeResponse) {
			Customer customer = new Customer();	
			DateTime createdDate = null;
			activationCodeRegistration = new ActivationCodeRegistration();
				
			if (redeemActivationCode.getUserName() != null) {
				customer.setUsername(redeemActivationCode.getUserName());
			}
			
			if (redeemActivationCode.getFirstName() != null) {
				customer.setFirstName(redeemActivationCode.getFirstName());
			}
			if (redeemActivationCode.getLastName() != null) {
				customer.setFamilyName(redeemActivationCode.getLastName());
			}
			if (redeemActivationCode.getActivationDate() != null) {
				createdDate = new DateTime(redeemActivationCode.getActivationDate().toString());				
				activationCodeRegistration.setCreatedDate(createdDate);
			}
			activationCodeRegistration.setCustomer(customer);
			activationCodeRegistration.setRegistrationDefinition(regDef);
			activationCodeRegistrations.add(activationCodeRegistration);
			customer = null;
			activationCodeRegistration = null;
		}		
		activationCodeBatchDto.setActivationCodeRegistration(activationCodeRegistration);
		activationCodeBatchDto.setActivationCodeRegistrations(activationCodeRegistrations);
		return activationCodeBatchDto;
	}
	
	private static LicenceDetailDto getActivationLicenseDetailDto(OupActivationCodeLicenseWS activationCodeLicense){
		LicenceDetailDto licenceDetailDto = null ;
		OupLicenseDetail oupLicenseDetail = activationCodeLicense.getLicenseDetail();
		
		if(oupLicenseDetail.isSetOupRollingLicenseData()) {
			OupRollingLicenseData oupRollingLicenseData = oupLicenseDetail.getOupRollingLicenseData();
			RollingLicenceDetailDto dtoObj = new RollingLicenceDetailDto(RollingBeginOn.valueOf(oupRollingLicenseData.getBeginOn().toString()),RollingUnitType.valueOf(oupRollingLicenseData.getPeriodUnit().toString()) , oupRollingLicenseData.getPeriodValue(), null);
			licenceDetailDto = dtoObj;
		} else if(oupLicenseDetail.isSetOupStandardConcurrencyLicenseData()) {
			OupStandardConcurrencyLicenseData  obj = oupLicenseDetail.getOupStandardConcurrencyLicenseData();
			StandardConcurrentLicenceDetailDto dtoObj = new StandardConcurrentLicenceDetailDto(obj.getTotalConcurrency(), obj.getUserConcurrency());
			licenceDetailDto = dtoObj;
		} else if(oupLicenseDetail.isSetOupUsageLicenseData()) {
			UsageLicenceDetailDto dtoObj = new UsageLicenceDetailDto();
			dtoObj.setAllowedUsages(oupLicenseDetail.getOupUsageLicenseData().getQuantityLimit());;
			licenceDetailDto = dtoObj;
		}
		
		if(activationCodeLicense.getStartDate() != null) {
			licenceDetailDto.setStartDate(dateConverter.safeConvertDate(activationCodeLicense.getStartDate())) ;
		}
		if(activationCodeLicense.getEndDate() != null ){
			licenceDetailDto.setEndDate(dateConverter.safeConvertDate(activationCodeLicense.getEndDate())) ;
		}
		return licenceDetailDto ;
	}
	
	static List<DivisionDto> getDivisionDtos(List<OupDivisions> oupDivisions){
		List<DivisionDto> divisionDtos = new ArrayList<DivisionDto>() ;
		for(OupDivisions oupDivision : oupDivisions){
			DivisionDto divisionDto = new DivisionDto() ;
			if(oupDivision.getDivisionId() != null) {
				divisionDto.setId(oupDivision.getDivisionId());
			}
			divisionDto.setDivisionType(oupDivision.getDivisionType());
			divisionDtos.add(divisionDto);
		}
		return divisionDtos ;
	}
	static List<OupDivisions> getOupDivisions(List<DivisionDto> divisionDtos){
		List<OupDivisions> oupDivisions = new ArrayList<OupDivisions>() ;
		for(DivisionDto divisionDto : divisionDtos){
			OupDivisions oupDivision = new OupDivisions() ;
			if(divisionDto.getId() != null) {
				oupDivision.setDivisionId(divisionDto.getId());
			}
			oupDivision.setDivisionType(divisionDto.getDivisionType());
			oupDivisions.add(oupDivision) ;
		}
		return oupDivisions ;
	}
	
	/**
	 * getAllValidatorEmails 
	 * @param validatorEmails
	 * @return 
	 * List<String> 
	 * @author Developed by TCS
	 */
	static List<String> getAllValidatorEmails(ValidatorEmails validatorEmails) {
		List<String> listOfValidatedEmail  = new ArrayList<String>() ;
		for ( String emailValidated : validatorEmails.getValidatorEmail()) {
			listOfValidatedEmail.add(emailValidated) ;
		}
		return listOfValidatedEmail ;
	}
	
	/**
	 * getAllExternalSystem 
	 * @param externalSystemWS
	 * @return 
	 * List<ExternalSystem> 
	 * @author Developed by TCS
	 */
	static List<ExternalSystem> getAllExternalSystem(List<ExternalSystemWS> externalSystemWS) {
		List<ExternalSystem> externalSystem  = new ArrayList<ExternalSystem>() ;
		
		for (ExternalSystemWS extSys : externalSystemWS) {
			ExternalSystem externalSys = new ExternalSystem() ;
			externalSys.setName(extSys.getSystemId());
			externalSys.setDescription(extSys.getDescription());
			externalSys.setExternalSystemIdTypes(getAllTypeIds(extSys.getTypeIds()));
			externalSys.setId(extSys.getSystemId());
			externalSystem.add(externalSys) ;
			
		}
		return externalSystem ;
	}
	
	/**
	 * getAllTypeIds 
	 * @param systemTypeId
	 * @return 
	 * Set<ExternalSystemIdType> 
	 * @author Developed by TCS
	 */
	static Set<ExternalSystemIdType> getAllTypeIds(List<SystemTypeId> systemTypeId) {
		Set<ExternalSystemIdType> externalSystemIdTypes = new HashSet<ExternalSystemIdType>() ;
		for(SystemTypeId typeId : systemTypeId) {
			ExternalSystemIdType externalSystemIdType = new ExternalSystemIdType() ;
			externalSystemIdType.setDescription(typeId.getDescription());
			externalSystemIdType.setName(typeId.getTypeId());
			externalSystemIdType.setId(typeId.getTypeId());
			externalSystemIdType.setDeletable(typeId.isIsDeletable());
			externalSystemIdTypes.add(externalSystemIdType);
			
		}
		return externalSystemIdTypes ;
	}
	
	/**
	 * getExternalSystem 
	 * @param externalSystemWs
	 * @return 
	 * ExternalSystem 
	 * @author Developed by TCS
	 */
	public static ExternalSystem getExternalSystem(ExternalSystemWS externalSystemWs)
	{
		ExternalSystem externalSystem=new ExternalSystem();
		if(externalSystemWs!=null)
		{
			if(externalSystemWs.getSystemId()!=null)
				externalSystem.setName(externalSystemWs.getSystemId());

			if(externalSystemWs.getDescription()!=null)
				externalSystem.setDescription(externalSystemWs.getDescription());
			externalSystem.setId(externalSystemWs.getSystemId());
			
			for(SystemTypeId systemTypeId : externalSystemWs.getTypeIds())
			{
				ExternalSystemIdType externalSystemIdType=new ExternalSystemIdType();
				if(systemTypeId.getTypeId()!=null)
					externalSystemIdType.setName(systemTypeId.getTypeId());

				if(systemTypeId.getDescription()!=null)
					externalSystemIdType.setDescription(systemTypeId.getDescription());
				externalSystemIdType.setId(systemTypeId.getTypeId());
				externalSystemIdType.setExternalSystem(externalSystem);
				externalSystem.getExternalSystemIdTypes().add(externalSystemIdType);
			}
		}
			return externalSystem;
		}
	
	public static List<GuestRedeemActivationCodeDto> OupGuesdtRedeemProductToGuestRedeemDto(List<OupGuestRedeemProduct> oupGuestRedeemProducts) {
		List<GuestRedeemActivationCodeDto> guestRedeemProducts = new ArrayList<GuestRedeemActivationCodeDto>();
		for ( OupGuestRedeemProduct oupGuestRedeemProduct : oupGuestRedeemProducts) {
			GuestRedeemActivationCodeDto guestRedeemProduct = new GuestRedeemActivationCodeDto() ;
			guestRedeemProduct.setActivationCode(oupGuestRedeemProduct.getActivationCode());
			OupGetProductWS product = oupGuestRedeemProduct.getProduct() ;
			List<ExternalProductId> externalProductIds = new ArrayList<ExternalProductId>() ;
			List<ExternalIdentifier> externalIds = product.getExternal() ;
			
			for(ExternalIdentifier externalId : externalIds) {
				ExternalProductId externalIdDto = new ExternalProductId();
				externalIdDto.setExternalId(externalId.getId());
				
				ExternalSystem externalSystem = new ExternalSystem() ;
				externalSystem.setName(externalId.getSystemId());
				
				ExternalSystemIdType externalSystemIdType = new ExternalSystemIdType() ;
				externalSystemIdType.setName(externalId.getTypeId());
				externalSystemIdType.setExternalSystem(externalSystem);
				externalIdDto.setExternalSystemIdType(externalSystemIdType);
				
				externalProductIds.add(externalIdDto) ;
			}
			
			guestRedeemProduct.setExternalProductId(externalProductIds);
			guestRedeemProduct.setProductId(product.getId());
			guestRedeemProduct.setProductName(product.getName());
			guestRedeemProducts.add(guestRedeemProduct) ;
			
		}
		return guestRedeemProducts ;
	}
	
	static BulkActivationCodeLicense getActivationCodeLicense(final ActivationCodeBatchDto activationCodeBatchDto) {
		BulkActivationCodeLicense activationCodeLicense = new BulkActivationCodeLicense();
		if (activationCodeBatchDto.getLicenceDetailDto() == null ) {
			activationCodeLicense.setLicenseDetails(null);
		} else {
			if(activationCodeBatchDto.getProductId()!=null)
			{
				activationCodeLicense.setProductName(activationCodeBatchDto.getProductId());
			}
			else if(activationCodeBatchDto.getProductGroupId()!=null)
			{
				activationCodeLicense.setProductGroupName(activationCodeBatchDto.getProductGroupId());
			}
			OupActivationCodeLicenseWS oupActivationCodeLicenseWS =new OupActivationCodeLicenseWS();
			OupLicenseDetail oupLicenseDetail = new OupLicenseDetail();
			switch (activationCodeBatchDto.getLicenceDetailDto().getLicenceType()) {
			case CONCURRENT:
				OupStandardConcurrencyLicenseData oupStandardConcurrencyLicenseData = new OupStandardConcurrencyLicenseData();
				StandardConcurrentLicenceDetailDto standardConcurrentLicenceDetailDto = (StandardConcurrentLicenceDetailDto)activationCodeBatchDto.getLicenceDetailDto();
				oupStandardConcurrencyLicenseData.setTotalConcurrency(standardConcurrentLicenceDetailDto.getTotalConcurrency());
				oupStandardConcurrencyLicenseData.setUserConcurrency(standardConcurrentLicenceDetailDto.getUserConcurrency());
				oupLicenseDetail.setOupStandardConcurrencyLicenseData(oupStandardConcurrencyLicenseData);
				break;
			case ROLLING:
				OupRollingLicenseData oupRollingLicenseData = new OupRollingLicenseData();
				RollingLicenceDetailDto rollingLicenceDetailDto = (RollingLicenceDetailDto)activationCodeBatchDto.getLicenceDetailDto();
				oupRollingLicenseData.setBeginOn(RollingBEGINON.valueOf(rollingLicenceDetailDto.getBeginOn().name()));
				oupRollingLicenseData.setPeriodUnit(RollingUNITTYPE.valueOf(rollingLicenceDetailDto.getUnitType().name()));
				oupRollingLicenseData.setPeriodValue(rollingLicenceDetailDto.getTimePeriod());
				oupLicenseDetail.setOupRollingLicenseData(oupRollingLicenseData);
				break;
			case USAGE:
				OupUsageLicenseData oupUsageLicenseData = new OupUsageLicenseData();
				UsageLicenceDetailDto usageLicenceDetailDto = (UsageLicenceDetailDto)activationCodeBatchDto.getLicenceDetailDto();
				oupUsageLicenseData.setQuantityLimit(usageLicenceDetailDto.getAllowedUsages());
				oupLicenseDetail.setOupUsageLicenseData(oupUsageLicenseData);
				break;
			default:
				throw new IllegalArgumentException("Unknown Licence Type");
			}
			GregorianCalendar calender = new GregorianCalendar();
			oupActivationCodeLicenseWS.setLicenseDetail(oupLicenseDetail);
			oupActivationCodeLicenseWS.setStartDate(dateConverter.safeConvertLocalDate(activationCodeBatchDto.getLicenceDetailDto().getStartDate()));
			oupActivationCodeLicenseWS.setEndDate((dateConverter.safeConvertLocalDate(activationCodeBatchDto.getLicenceDetailDto().getEndDate())));
			activationCodeLicense.setLicenseDetails(oupActivationCodeLicenseWS);
		}
		return activationCodeLicense;
	}
	
	static OupUserIdentifier getUserIdentifier(WsUserIdDto wsUserIdDto){
		OupUserIdentifier oupUserIdentifier = new OupUserIdentifier() ;
		oupUserIdentifier.setSessionToken(wsUserIdDto.getSessionToken());
		oupUserIdentifier.setUserId(wsUserIdDto.getUserId());
		oupUserIdentifier.setUsername(wsUserIdDto.getUserName());
		ExternalIdentifier extUserId = new ExternalIdentifier() ;
		if(wsUserIdDto.getExternalId() != null ) {
			extUserId.setId(wsUserIdDto.getExternalId().getId());
			extUserId.setSystemId(wsUserIdDto.getExternalId().getSystemId());
			extUserId.setTypeId(wsUserIdDto.getExternalId().getType());
			oupUserIdentifier.setExternalUserId(extUserId);
		}
		return oupUserIdentifier ;
	}
	
	static List<RegisterableProduct> convertOupProductsToRegisterableProducts(List<OupProducts> oupProducts) {
		List<RegisterableProduct> products = new ArrayList<RegisterableProduct>() ;
		for (OupProducts oupProduct : oupProducts) {
			RegisterableProduct product = new RegisterableProduct() ;
			product.setId(oupProduct.getProductId());
			product.setProductName(oupProduct.getProductName());
			Set<ExternalProductId> extProductIds = new HashSet<ExternalProductId>() ;
			for (ExternalIdentifier extIdentifier : oupProduct.getExternalSystem()) {
				ExternalProductId extProductId = new ExternalProductId() ;
				extProductId.setExternalId(extIdentifier.getId());
				
				ExternalSystem externalSystem = new ExternalSystem() ;
				externalSystem.setName(extIdentifier.getSystemId());
				
				ExternalSystemIdType extSysType = new ExternalSystemIdType() ;
				extSysType.setId(extIdentifier.getTypeId());
				extSysType.setName(extIdentifier.getTypeId());
				extSysType.setExternalSystem(externalSystem);
				
				extProductId.setExternalSystemIdType(extSysType);
				extProductIds.add(extProductId) ;
				
			}
			if (extProductIds.size() > 0 ) {
				product.setExternalIds(extProductIds);
			}
			products.add(product) ;
		}
		return products ;
	}
}
