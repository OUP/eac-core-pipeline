package com.oup.eac.integration.facade.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.dto.ActivationCodeBatchDto;
import com.oup.eac.dto.AuthenticationResponseDto;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.DivisionDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.dto.GroupDto;
import com.oup.eac.dto.GuestRedeemActivationCodeDto;
import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.LicenceDtoDateTime;
import com.oup.eac.dto.LoginPasswordCredentialDto;
import com.oup.eac.dto.ProductGroupDto;
import com.oup.eac.dto.WsUserIdDto;
import com.oup.eac.integration.erights.ExternalIdentifier;
import com.oup.eac.integration.erights.GetActivationCodeBatchByBatchIdResponse;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ChildProductFoundException;
import com.oup.eac.integration.facade.exceptions.DivisionAlreadyExistsException;
import com.oup.eac.integration.facade.exceptions.DivisionNotFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ErightsSessionNotFoundException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.InvalidCredentialsException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ParentGroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.ParentProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.PasswordPolicyViolatedException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserAlreadyExistsException;
import com.oup.eac.integration.facade.exceptions.UserLoginCredentialAlreadyExistsException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;

@Service("fakeErightsFacade")
public class FakeErightsFacadeImpl implements ErightsFacade {

	private static Map<String, List<Integer>> urlMap = new HashMap<String, List<Integer>>();
	private static Map<String, List<Integer>> usersMap = new HashMap<String, List<Integer>>();
	private static Map<Integer, List<LicenceDto>> userLicencesMap = new HashMap<Integer, List<LicenceDto>>();
	private static Map<Integer, CustomerDto> customers = new HashMap<Integer, CustomerDto>();
	
	
	
	private Random random = new Random();
	
	
	static {
		reset();
	}
	
	public static final void reset() {
		urlMap.put("http://localhost/product1.html", Arrays.asList(new Integer[] {Integer.valueOf(1)}));
		urlMap.put("http://localhost/product2.html", Arrays.asList(new Integer[] {Integer.valueOf(2)}));
		urlMap.put("http://localhost/product3.html", Arrays.asList(new Integer[] {Integer.valueOf(3)}));
		urlMap.put("http://localhost/product4.html", Arrays.asList(new Integer[] {Integer.valueOf(4)}));
		usersMap.put("osdijfsdpfompsdfpsdkf", Arrays.asList(new Integer[] {Integer.valueOf(1)}));
		usersMap.put("oiweuroweijfnosdnfosdfn", Arrays.asList(new Integer[] {Integer.valueOf(2)}));
		usersMap.put("zxcklnzxlcnkzxkcjnzxkcjn", Arrays.asList(new Integer[] {Integer.valueOf(3)}));
		usersMap.put("oijdfjgiodfjgodfjsdfions", Arrays.asList(new Integer[] {Integer.valueOf(4)}));
		userLicencesMap.put(Integer.valueOf(1), new ArrayList<LicenceDto>());
		userLicencesMap.put(Integer.valueOf(2), new ArrayList<LicenceDto>());
		userLicencesMap.put(Integer.valueOf(3), new ArrayList<LicenceDto>());
		userLicencesMap.put(Integer.valueOf(4), new ArrayList<LicenceDto>());	
	}
/*	
    @Override
    public void activateLicense(final Integer erightsUserId, final Integer erightsLicenseId) throws ErightsException {
    	List<LicenceDto> userLicences = userLicencesMap.get(erightsUserId);
    	if(userLicences == null) {
    		throw new IllegalStateException("User does not exist");
    	}
		for(LicenceDto licence : userLicences) {
    		if (licence.getErightsId().equals(erightsLicenseId)) {
    			if (licence.isEnabled()) {
    				throw new IllegalStateException("Licence is already activated for userId: " + erightsUserId + " and licenceId:" + erightsLicenseId);
    			}
    			licence.setEnabled(true);
    			return;
    		}
		}
    	throw new IllegalStateException("User does not have a licence");
    }*/

  /*  @Override
    public Integer addLicense(final Integer userId, final LicenceTemplate licenceTemplate, final List<Integer> productIds, boolean enabled)
            throws ErightsException {
    	List<LicenceDto> userLicences = userLicencesMap.get(userId);
    	if(userLicences == null) {
    		throw new IllegalStateException("User does not exist");
    	}
    	
    	Random r = new Random();
    	int id = r.nextInt();
    	
    	LicenceDto licence = new FakeLicenceDto(id, licenceTemplate, productIds, enabled); 
    	userLicences.add(licence);
    	return id;
    }*/

    @Override
    public AuthenticationResponseDto authenticateUser(final LoginPasswordCredentialDto loginPasswordCredential) throws ErightsException,
            InvalidCredentialsException {
        throw new UnsupportedOperationException();
    }

   /* @Override
    public void changePasswordByUserId(final Integer erightsUserId, final String password) throws ErightsException {
        throw new UnsupportedOperationException();
    }*/

    @Override
    public void changePasswordByUsername(final String username, final String password) throws ErightsException {
        throw new UnsupportedOperationException();
    }

    /*@Override
    public CustomerDto createUserAccount(final CustomerDto customerDto)
            throws ErightsException, UserAlreadyExistsException {
    	CustomerDto savedCustomerDto = new CustomerDto(random.nextInt(), customerDto);
    	
    	customers.put(savedCustomerDto.getErightsId(), savedCustomerDto);
    	
    	return savedCustomerDto;
    }*/

   /* @Override
    public List<Integer> getCustomerIdsFromSession(String sessionKey) throws ErightsException {
        return usersMap.get(sessionKey);
    }

    @Override
    public List<Integer> getProductIdsByUrl(String url) throws ErightsException {
        return urlMap.get(url);
    }

*/    @Override
    public void logout(String session) throws ErightsException {
        throw new UnsupportedOperationException();
    }

	@Override
	public GroupDto createGroup(GroupDto group) throws ErightsException,
			ParentGroupNotFoundException {
		return null;
	}

	@Override
	public EnforceableProductDto createProduct(EnforceableProductDto enforceableProduct, LicenceDetailDto licenceDetailDto)
			throws ErightsException, ParentProductNotFoundException {
		return null;
	}

	

	@Override
	public GroupDto getGroup(Integer erightsGroupId) throws ErightsException,
			GroupNotFoundException {
		return null;
	}

	@Override
	public List<Integer> getGroupUsers(Integer erightsGroupId,
			Boolean includeIndirectParents) throws ErightsException,
			GroupNotFoundException, UserNotFoundException {
		return null;
	}

	/*@Override
	public List<LicenceDto> getLicensesForUser(
			Integer erightsUserId) throws LicenseNotFoundException,
			UserNotFoundException, ErightsException {
		List<LicenceDto> userLicences = new ArrayList<LicenceDto>();
		
		List<LicenceDto> foundUserLicences = userLicencesMap.get(erightsUserId);
		if (foundUserLicences != null) {
			userLicences.addAll(foundUserLicences);
		}
		
		return userLicences;
	}*/

	/*@Override
	public List<LicenceDto> getLicensesForUserProduct(Integer erightsUserId, Integer erightsProductId) throws ErightsException, UserNotFoundException,
																																ProductNotFoundException {
		List<LicenceDto> userLicences = userLicencesMap.get(erightsUserId);
		List<LicenceDto> userProductLicences = new ArrayList<LicenceDto>();
		
		for (LicenceDto licence : userLicences) {
			if (licence.getProductIds().contains(erightsProductId)) userProductLicences.add(licence);
		}
		
		return userProductLicences;
	}

	@Override
	public EnforceableProductDto getProduct(Integer erightsProductId)
			throws ProductNotFoundException, ErightsException {
		return null;
	}

	@Override
	public CustomerDto getUserAccount(Integer erightsUserId)
			throws UserNotFoundException, ErightsException {
		
		return customers.get(erightsUserId);
	}*/

	@Override
	public void updateGroup(GroupDto group) throws GroupNotFoundException,
			ParentGroupNotFoundException, ErightsException {
	}

	@Override
	public void updateProduct(EnforceableProductDto enforceableProduct)
			throws ProductNotFoundException, ParentProductNotFoundException {
	}

	@Override
	public void updateUserAccount(CustomerDto customerDto)
			throws UserNotFoundException, GroupNotFoundException,
			UserLoginCredentialAlreadyExistsException, ErightsException {
	}

	/*@Override
	public void deactivateLicense(Integer erightsUserId,
			Integer erightsLicenceId)
			throws ErightsException, LicenseNotFoundException,
			UserNotFoundException {
	}

	@Override
	public void removeLicence(Integer erightsUserId, Integer erightsLicenceId) throws LicenseNotFoundException,
			UserNotFoundException, ErightsException {
	}*/

	@Override
	public void deleteGroup(Integer erightsGroupId) throws ErightsException,
			GroupNotFoundException {
	}

	/*@Override
	public void updateLicence(Integer erightsUserId, LicenceDto licence)
			throws UserNotFoundException, ProductNotFoundException,
			LicenseNotFoundException, ErightsException {
	}*/
	
	/*public void addCustomerDto(CustomerDto customerDto) {
		customers.put(customerDto.getErightsId(), customerDto);
	}
	
	public void removeCustomerDto(CustomerDto customerDto) {
		customers.remove(customerDto.getErightsId());
	}*/

    /*@Override
    public void updateLicenceUsingDateTimes(Integer erightsUserId, LicenceDtoDateTime licence)
            throws UserNotFoundException, ProductNotFoundException, LicenseNotFoundException, ErightsException {
    }
    
    @Override
    public List<String> getSessionsByUserId(int userId)
            throws ErightsException, ErightsSessionNotFoundException {
        return null;
    }*/
/*
    @Override
    public void authorizeRequest(String sessionId, String url, Integer erightsUserId, Integer erightsLicenceId) throws UserNotFoundException, LicenseNotFoundException, AccessDeniedException, ErightsException {
        List<Integer> sessionList = usersMap.get(sessionId);
        if(sessionList==null || sessionList.isEmpty()){
            throw new IllegalStateException("Invalid session.");
        }
        List<Integer> urlList = urlMap.get(url);
        if(urlList==null || urlList.isEmpty()){
            throw new IllegalStateException("Access denied.");
        }
        List<LicenceDto> userLicences = userLicencesMap.get(erightsUserId);
        if(userLicences == null) {
            throw new IllegalStateException("User does not exist");
        }
        for(LicenceDto licence : userLicences) {
            if (licence.getErightsId().equals(erightsLicenceId)) {
                if (licence.isEnabled()) {
                    throw new IllegalStateException("Licence is already activated for userId: " + erightsUserId + " and licenceId:" + erightsLicenceId);
                }
                licence.setEnabled(true);
                return;
            }
        }
        throw new IllegalStateException("User does not have a licence");
    }

	@Override
	public void removeUserExternalId(String externalId) throws ErightsException {
		
	}*/

	@Override
	public CustomerDto getUserAccountByUsername(String username)
			throws ErightsException {
		return null;
	}

	
	@Override
	public void createExternalSystem(ExternalSystem externalSystem)
			throws ErightsException {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void deleteExternalSystem(ExternalSystem externalSystem)
			throws ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateExternalSystem(ExternalSystem externalSystem,String oldSystemName)
			throws ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteExternalSystemTypes(ExternalSystem externalSystem,
			List<ExternalSystemIdType> externalSystemIdTypes)
			throws ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProduct(EnforceableProductDto enforceableProduct,
			LicenceDetailDto licenceDetailDto, List<Integer> erightsProductIds)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public int addLicenseUserProduct(int userId, int productId)
			throws ErightsException {
    	List<LicenceDto> userLicences = userLicencesMap.get(userId);
    	if(userLicences == null) {
    		throw new IllegalStateException("User does not exist");
    	}
    	
    	Random r = new Random();
    	int id = r.nextInt();
    	
    	
    	LicenceDto licence = new FakeLicenceDto(id, productId, false);
    	userLicences.add(licence);
    	return id;
    }

	@Override
	public void adminActivateLicense(Integer erightsUserId,
			Integer erightsLicenceId,final boolean sendEmail) throws UserNotFoundException,
			LicenseNotFoundException, ErightsException {
		List<LicenceDto> userLicences = userLicencesMap.get(erightsUserId);
    	if(userLicences == null) {
    		throw new IllegalStateException("User does not exist");
    	}
		for(LicenceDto licence : userLicences) {
    		if (licence.getErightsId().equals(erightsLicenceId)) {
    			if (licence.isEnabled()) {
    				throw new IllegalStateException("Licence is already activated for userId: " + erightsUserId + " and licenceId:" + erightsLicenceId);
    			}
    			licence.setEnabled(true);
    			return;
    		}
		}
    	throw new IllegalStateException("User does not have a licence");
		
	}*/

	@Override
	public void adminDeactivateLicense(Integer erightsUserId,
			Integer erightsLicenceId,final boolean sendEmail) throws UserNotFoundException,
			LicenseNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProductGroupDto createProductGroup(ProductGroupDto productGroupDto)
			throws ProductNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateProductGroup(ProductGroupDto productGroupDto)
			throws ProductNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProductGroup(String productName)
			throws ProductNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public ProductGroupDto getProductGroup(Integer productId, String productName)
			throws ProductNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}
*/
	@Override
	public void updateLinkedProduct(int linkedProductID, int parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public void addLinkedProduct(int linkedProductID, int parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}*/

	/*@Override
	public void removeLinkedProduct(int linkedProductID, int parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}*/

	/*@Override
	public void resetPassword(Integer erightsUserId, String token,
			String successUrl, String failureUrl) throws ErightsException,
			PasswordPolicyViolatedException {
		
		List<LicenceDto> userLicences = userLicencesMap.get(erightsUserId);
    	if(userLicences == null) {
    		throw new IllegalStateException("User does not exist");
    	}
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public ActivationCodeBatchDto createActivationCodeBatch(ActivationCodeBatchDto activationCodeBatchDto) throws ProductNotFoundException, 
	UserNotFoundException, LicenseNotFoundException, AccessDeniedException, GroupNotFoundException,ErightsException{
		// TODO Auto-generated method stub
		return null ;
	}

	@Override
	public void updateActivationCodeBatch(ActivationCodeBatchDto activationCodeBatchDto)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteActivationCodeBatch(String batchId)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public ActivationCodeBatchDto getActivationCodeBatch(String batchId)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}*/

	

	@Override
	public List<DivisionDto> createDivision(List<DivisionDto> divisionDtos)
			throws AccessDeniedException, DivisionAlreadyExistsException,
			DivisionNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivisionDto> updateDivision(List<DivisionDto> divisionDtos)
			throws AccessDeniedException, DivisionNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivisionDto> getAllDivisions() throws AccessDeniedException,
			DivisionNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDivision(List<DivisionDto> divisionDtos)
			throws AccessDeniedException, DivisionNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ActivationCodeBatchDto getActivationCodeBatchByActivationCode(
			String activationCode) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActivationCodeBatchDto getActivationCodeDetailsByActivationCode(
			String activationCode) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> getAllValidatorEmails() throws AccessDeniedException,
			ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExternalSystem> getAllExternalSystems() throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExternalSystem getExternalSystem(String externalSystem)
			throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerDto getUserAccountByExternalId(ExternalIdentifier external)
			throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

/*	@Override
	public List<LicenceDto> getLicensesForUser(Integer erightsUserId,
			Integer erightsLicenceId) throws LicenseNotFoundException,
			UserNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}*/

	
	@Override
	public EnforceableProductDto getProductByExternalId(
			ExternalIdDto externalDto) throws ProductNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetActivationCodeBatchByBatchIdResponse checkActivationCodeBatchExists(
			String batchId) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnforceableProductDto getProductByName(String productName)
			throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

/*	@Override
	public void mergeCustomer(int custid, String email)
			throws AccessDeniedException, DivisionNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<LicenceDto> redeemActivationCode(ActivationCode activationCode,
			int userId, String url, boolean sendActivationMail)
			throws ProductNotFoundException, UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException,
			GroupNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}
*/

	/*@Override
	public long getActivationCodeBatchReportCount(
			ActivationCodeBatchReportCriteria reportCriteria)
			throws ErightsException {
		// TODO Auto-generated method stub
		return 0;
	}*/

	@Override
	public List<GuestRedeemActivationCodeDto> guestRedeemActivationCode(
			String activationCode) throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activateLicense(String userId, String licenceId)
			throws UserNotFoundException, LicenseNotFoundException,
			ErightsException {
		List<LicenceDto> userLicences = userLicencesMap.get(userId);
    	if(userLicences == null) {
    		throw new IllegalStateException("User does not exist");
    	}
		for(LicenceDto licence : userLicences) {
    	
    			if (licence.isEnabled()) {
    				throw new IllegalStateException("Licence is already activated for userId: " + userId + " and licenceId:" + licenceId);
    			}
    			licence.setEnabled(true);
    			return;
    		
		}
    	throw new IllegalStateException("User does not have a licence");
		
	}

	@Override
	public String addLicense(String userId, LicenceTemplate licenceTemplate,
			List<String> productIds, boolean enabled) throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changePasswordByUserId(String userId, String password)
			throws PasswordPolicyViolatedException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivateLicense(String userId, String licenceId)
			throws ErightsException, UserNotFoundException,
			LicenseNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProduct(String erightsProductId) throws ErightsException,
			ProductNotFoundException, ChildProductFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUserAccount(String erightsUserId)
			throws ErightsException, UserNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<LicenceDto> getLicensesForUser(String userId, String licenceId)
			throws LicenseNotFoundException, UserNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LicenceDto> getLicensesForUserProduct(String userId,
			String productId) throws ErightsException, UserNotFoundException,
			ProductNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnforceableProductDto getProduct(String productId)
			throws ProductNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerDto getUserAccount(String userId)
			throws UserNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLicence(String userId, String licenceId)
			throws LicenseNotFoundException, UserNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void renewLicence(String userId, LicenceDto licence)
			throws UserNotFoundException, ProductNotFoundException,
			LicenseNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLicence(String userId, LicenceDto licence)
			throws UserNotFoundException, ProductNotFoundException,
			LicenseNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLicenceUsingDateTimes(String userId,
			LicenceDtoDateTime licence) throws UserNotFoundException,
			ProductNotFoundException, LicenseNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getSessionsByUserId(String userId)
			throws ErightsException, ErightsSessionNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void authorizeRequest(String sessionId, String url, String userId,
			String licenceId) throws UserNotFoundException,
			LicenseNotFoundException, AccessDeniedException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUserExternalId(String externalId, String userId)
			throws ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeProductExternalId(String externalId, String productId)
			throws ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String addLicenseUserProduct(String userId, String productId)
			throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void adminActivateLicense(String userId, String licenceId,
			boolean sendEmail) throws UserNotFoundException,
			LicenseNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProductGroupDto getProductGroup(String productId, String productName)
			throws ProductNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLinkedProduct(String linkedProductID, String parentProductID)
			throws ProductNotFoundException, ParentProductNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLinkedProduct(String linkedProductID,
			String parentProductID) throws ProductNotFoundException,
			ParentProductNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetPassword(String userId, String token, String successUrl,
			String failureUrl) throws ErightsException,
			PasswordPolicyViolatedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<LicenceDto> redeemActivationCode(ActivationCode activationCode,
			String userId, String url, boolean sendActivationMail,
			boolean completed) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mergeCustomer(String custid, String email)
			throws AccessDeniedException, DivisionNotFoundException,
			ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ActivationCodeBatchDto getActivationCodeBatch(String batchId,
			boolean activationCodeRequired) throws ProductNotFoundException,
			UserNotFoundException, LicenseNotFoundException,
			AccessDeniedException, GroupNotFoundException, ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bulkCreateActivationCodeBatchRequest(
			List<ActivationCodeBatchDto> activationCodeBatchDto,
			AdminUser adminUser) throws ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerDto createUserAccount(CustomerDto customerDto)
			throws ErightsException, UserAlreadyExistsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getProductIdsByUrl(String url) throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getCustomerIdsFromSession(String sessionKey)
			throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	public void addCustomerDto(CustomerDto customerDto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String validateUserAccount(WsUserIdDto wsUserIdDto)
			throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void killUserSession(WsUserIdDto wsUserIdDto)
			throws ErightsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<RegisterableProduct> validateActivationCode(
			String activationCode, String systemId) throws ErightsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateLicenseModifiedDate(String licenseId) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	

}
