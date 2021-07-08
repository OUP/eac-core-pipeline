package com.oup.eac.admin.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Component;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.oup.eac.admin.beans.CustomerBean;
import com.oup.eac.admin.beans.CustomerSearchCriteriaBean;
import com.oup.eac.admin.beans.RegistrationStateBean;
import com.oup.eac.admin.validators.CustomerBeanValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.Password;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.domain.entitlement.ProductEntitlementGroupDto;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.paging.PagingCriteria.SortDirection;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.integration.facade.exceptions.UserLoginCredentialAlreadyExistsException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.UserEntitlementsService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.ExternalIdTakenException;
import com.oup.eac.service.exceptions.LicenceNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.exceptions.SessionNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.UsernameExistsException;

@Component(value = "customerAction")
public class CustomerAction extends FormAction {

    private static final Logger LOG = Logger.getLogger(CustomerAction.class);
    
	private static final String NO_SYSTEM_ID = null;

	private final CustomerService customerService;
	private final RegistrationService registrationService;
	private final UserEntitlementsService userEntitlementsService;
	private final CustomerBeanValidator customerBeanValidator;
	private final ExternalIdService externalIdService;

	@Autowired
	public CustomerAction(final CustomerService customerService, final RegistrationService registrationService,
			final UserEntitlementsService userEntitlementsService, final CustomerBeanValidator customerBeanValidator, final ExternalIdService externalIdService) {
		this.customerService = customerService;
		this.registrationService = registrationService;
		this.userEntitlementsService = userEntitlementsService;
		this.customerBeanValidator = customerBeanValidator;
		this.externalIdService = externalIdService;
	}

	public Event searchCustomers(final RequestContext context) throws Exception {
		CustomerSearchCriteriaBean csb = (CustomerSearchCriteriaBean) context.getFlowScope().get("customerSearchCriteriaBean");
		/*
		 *  all fileds can contain _ or % for cloudSearch
		 *  
		csb.setUsername(AdminUtils.escapeSpecialChar(csb.getUsername()));
        csb.setEmail(AdminUtils.escapeSpecialChar(csb.getEmail()));
        csb.setFirstName(AdminUtils.escapeSpecialChar(csb.getFirstName()));
        csb.setFamilyName(AdminUtils.escapeSpecialChar(csb.getFamilyName()));
        csb.setExternalId(AdminUtils.escapeSpecialChar(csb.getExternalId()));
        csb.setRegistrationData(AdminUtils.escapeSpecialChar(csb.getRegistrationData()));
		*/
		Paging<Customer> resultsPage = customerService.searchCustomers(csb.toCustomerSearchCriteria(), new PagingCriteria(csb.getResultsPerPage(), csb.getPageNumber(), SortDirection.DESC, "createdDate"));
		context.getViewScope().put("pageInfo", resultsPage);
		context.getViewScope().put("customers", resultsPage.getItems());
		
		return success();
	}

	/**
	 * Save a customer and associated data (such as licences and registration state).
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public Event saveCustomer(final RequestContext context) throws Exception {
		CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
		Customer customer = customerBean.getCustomer();
		customer.setPassword(new Password(customerBean.getPassword(), false));
		customer.getExternalIds().clear();
		customer.getExternalIds().addAll(customerBean.getExternalIds());
		try {
			int concurrencyValue = 0;
            if (customer.getCustomerType() == CustomerType.SPECIFIC_CONCURRENCY) {
            	if (customerBean.getConcurrencyValue() != null) {
                    if (customerBean.getConcurrencyValue().length() > 9) {
                        throw new NumberFormatException("error.too.long");
                    }
                    concurrencyValue = Integer.parseInt(customerBean.getConcurrencyValue());
                } else{
                    concurrencyValue = CustomerType.SELF_SERVICE.getConcurrency();
                }
            	
                if (concurrencyValue < 0)
                    throw new ServiceLayerValidationException("Specific concurrency can not be negative.");
                if (concurrencyValue == 0)
                    concurrencyValue = CustomerType.SELF_SERVICE.getConcurrency();
                if (concurrencyValue < CustomerType.SELF_SERVICE.getConcurrency())
                    throw new ServiceLayerValidationException("Specific concurrency can not be less than default concurrency value.");
            }else if (customer.getCustomerType() == CustomerType.SELF_SERVICE) {
                concurrencyValue = CustomerType.SELF_SERVICE.getConcurrency();
            } else if (customer.getCustomerType() == CustomerType.SHARED) {
                concurrencyValue = CustomerType.SHARED.getConcurrency();
            }
            customer.getCustomerType().setConcurrency(concurrencyValue);
		    customerService.saveCustomer(customer, customerBean.isGeneratePassword());
        } catch (NumberFormatException nfe) {
            
            String msg = nfe.getMessage();
            if (!msg.equals("error.too.long")) {
                msg = "error.number.format";
            }
            return handleNumberFormatException(context, msg);

        } catch (UsernameExistsException sle) {
            return handleUsernameAlreadyExists(context);
        } catch (ServiceLayerValidationException slve) {
        	String code = "";
        	String msg = "";
        	if(slve.getMessage().contains("Specific concurrency can not be negative.") ){
        		code="error.negative.concurrency";
        		msg="Specific concurrency can not be negative.";
        		LOG.debug(msg);
        	}else if(slve.getMessage().contains("Specific concurrency can not be less than default concurrency value.")){
        		code="error.concurrency.less.than.default";
        		msg="Specific concurrency can not be less than default concurrency value.";
        		LOG.debug(msg);
        	}
        	return handleException(context,code, msg, "userConcurrency");
        }
		return success();
	}

	/**
	 * Updates a customer and associated data (such as licences and registration state). If the password is to be
	 * changed, decide this now.
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 *//*
	public Event updateCustomer(final RequestContext context) throws Exception {
		CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
		Customer customer = customerBean.getCustomer();
		List<ExternalCustomerId> newIds = customerBean.getExternalIds();
		customer.getExternalIds().clear();
		customer.getExternalIds().addAll(newIds);

		if (customerBean.isChangePassword() && !customerBean.isGeneratePassword()) {
			customer.setPassword(new Password(customerBean.getPassword(), false));
		}
		try {
		    int concurrencyValue = 0;
		    if (customer.getCustomerType() == CustomerType.SPECIFIC_CONCURRENCY) {
		    	if (customerBean.getConcurrencyValue() != null) {
                    if (customerBean.getConcurrencyValue().length() > 9) {
                        throw new NumberFormatException("error.too.long");
                    }
                    concurrencyValue = Integer.parseInt(customerBean.getConcurrencyValue());
                } else{
                    concurrencyValue = CustomerType.SELF_SERVICE.getConcurrency();;
                }
                              
                if (concurrencyValue < 0)
                    throw new ServiceLayerValidationException("Specific concurrency can not be negative.");
                if (concurrencyValue == 0)
                    concurrencyValue = CustomerType.SELF_SERVICE.getConcurrency();
                if (concurrencyValue < CustomerType.SELF_SERVICE.getConcurrency())
                    throw new ServiceLayerValidationException("Specific concurrency can not be less than default concurrency value.");
            } else if (customer.getCustomerType() == CustomerType.SELF_SERVICE) {
                concurrencyValue = CustomerType.SELF_SERVICE.getConcurrency();
            } else if (customer.getCustomerType() == CustomerType.SHARED) {
                concurrencyValue = CustomerType.SHARED.getConcurrency();
            }
            customer.getCustomerType().setConcurrency(concurrencyValue);
		    
			externalIdService.checkExistingExternalCustomerIds(customer, newIds);
			customerService.updateCustomerAndLicences(customer, customerBean.isGeneratePassword(), customerBean.getLicences());
			processRegistrationState(customerBean);
        } catch (NumberFormatException nfe) {
            String msg = nfe.getMessage();
            if (!msg.equals("error.too.long")) {
                msg = "error.number.format";
            }
            return handleNumberFormatException(context, msg);
        } catch (ExternalIdTakenException takenEx) {
            return handleExternalIdAlreadyTaken(takenEx, context);
        } catch (UsernameExistsException sle) {
            return handleUsernameAlreadyExists(context);
		} catch (UserLoginCredentialAlreadyExistsException exp){
	        	return handleUsernameAlreadyExists(context);
        } catch (PasswordPolicyViolatedServiceLayerException sle) {
            return handlePasswordPolicyViolated(context, sle.getMessage());
        } catch (ServiceLayerValidationException slve) {
        	String code = "";
        	String msg = "";
        	if(slve.getMessage().contains("Specific concurrency can not be negative.") ){
        		code="error.negative.concurrency";
        		msg="Specific concurrency can not be negative.";
        		LOG.debug(msg);
        	}else if(slve.getMessage().contains("Specific concurrency can not be less than default concurrency value.")){
        		code="error.concurrency.less.than.default";
        		msg="Specific concurrency can not be less than default concurrency value.";
        		LOG.debug(msg);
        	}
        	return handleException(context,code, msg, "userConcurrency");
        }

		return success();
	}*/
	
	public Event updateCustomerProfile(final RequestContext context) throws Exception {
		CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
		
		customerBeanValidator.validateCustomerProfile(customerBean, context.getMessageContext());
		if (context.getMessageContext().hasErrorMessages()) {
			return error();
		}
		
		Customer customer = customerBean.getCustomer();

		try {
		    int concurrencyValue = 0;
		    if (customer.getCustomerType() == CustomerType.SPECIFIC_CONCURRENCY) {
		    	if (customerBean.getConcurrencyValue() != null) {
                    if (customerBean.getConcurrencyValue().length() > 9) {
                        throw new NumberFormatException("error.too.long");
                    }
                    concurrencyValue = Integer.parseInt(customerBean.getConcurrencyValue());
                } else{
                    concurrencyValue = CustomerType.SELF_SERVICE.getConcurrency();;
                }
                              
                if (concurrencyValue < 0)
                    throw new ServiceLayerValidationException("Specific concurrency can not be negative.");
                if (concurrencyValue == 0)
                    concurrencyValue = CustomerType.SELF_SERVICE.getConcurrency();
                if (concurrencyValue < CustomerType.SELF_SERVICE.getConcurrency())
                    throw new ServiceLayerValidationException("Specific concurrency can not be less than default concurrency value.");
            } else if (customer.getCustomerType() == CustomerType.SELF_SERVICE) {
                concurrencyValue = CustomerType.SELF_SERVICE.getConcurrency();
            } else if (customer.getCustomerType() == CustomerType.SHARED) {
                concurrencyValue = CustomerType.SHARED.getConcurrency();
            }
            customer.getCustomerType().setConcurrency(concurrencyValue);
		    
            customerService.updateCustomerDetails(customer);
			processRegistrationState(customerBean);
        } catch (NumberFormatException nfe) {
            String msg = nfe.getMessage();
            if (!msg.equals("error.too.long")) {
                msg = "error.number.format";
            }
            return handleNumberFormatException(context, msg);
        } catch (ExternalIdTakenException takenEx) {
            return handleExternalIdAlreadyTaken(takenEx, context);
        } catch (UsernameExistsException sle) {
            return handleUsernameAlreadyExists(context);
		} catch (UserLoginCredentialAlreadyExistsException exp){
	        	return handleUsernameAlreadyExists(context);
        } catch (PasswordPolicyViolatedServiceLayerException sle) {
            return handlePasswordPolicyViolated(context, sle.getMessage());
        } catch (ServiceLayerValidationException slve) {
        	String code = "";
        	String msg = "";
        	if(slve.getMessage().contains("Specific concurrency can not be negative.") ){
        		code="error.negative.concurrency";
        		msg="Specific concurrency can not be negative.";
        		LOG.debug(msg);
        	}else if(slve.getMessage().contains("Specific concurrency can not be less than default concurrency value.")){
        		code="error.concurrency.less.than.default";
        		msg="Specific concurrency can not be less than default concurrency value.";
        		LOG.debug(msg);
        	}
        	return handleException(context,code, msg, "userConcurrency");
        }

		return success();
	}
	
	public Event updateCredentials(final RequestContext context) throws Exception {
		CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
		Customer customer = customerBean.getCustomer();

		if (customerBean.isChangePassword() && !customerBean.isGeneratePassword()) {
			customer.setPassword(new Password(customerBean.getPassword(), false));
		}
		
		customerBeanValidator.validateCustomerCredentials(customerBean, context.getMessageContext());
		if (context.getMessageContext().hasErrorMessages()) {
			return error();
		}
		
		try {
            customerService.updateCustomerCredentials(customer, customerBean.isGeneratePassword());
        } catch (UserLoginCredentialAlreadyExistsException exp){
	        	return handleUsernameAlreadyExists(context);
        } catch (PasswordPolicyViolatedServiceLayerException sle) {
            return handlePasswordPolicyViolated(context, sle.getMessage());
        } catch (ServiceLayerValidationException slve) {
        	String code = "";
        	String msg = "";
        	if(slve.getMessage().contains("Specific concurrency can not be negative.") ){
        		code="error.negative.concurrency";
        		msg="Specific concurrency can not be negative.";
        		LOG.debug(msg);
        	}else if(slve.getMessage().contains("Specific concurrency can not be less than default concurrency value.")){
        		code="error.concurrency.less.than.default";
        		msg="Specific concurrency can not be less than default concurrency value.";
        		LOG.debug(msg);
        	}
        	return handleException(context,code, msg, "userConcurrency");
        }

		return success();
	}
	
	public Event updateExternalIds(final RequestContext context) throws Exception {
		CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
		Customer customer = customerBean.getCustomer();
		
		List<ExternalCustomerId> newIds = customerBean.getExternalIds();
		customer.getExternalIds().clear();
		customer.getExternalIds().addAll(newIds);
		try {
			externalIdService.checkExistingExternalCustomerIds(customer, newIds);
			customerService.updateExternalIds(customer);
		} catch (ExternalIdTakenException takenEx) {
            return handleExternalIdAlreadyTaken(takenEx, context);
        } catch (ServiceLayerValidationException slve) {
			String code = "";
        	String msg = "";
        	return handleException(context,code, msg, "userConcurrency");
		}
		return success();
	}

	private void processRegistrationState(final CustomerBean customerBean) throws Exception {
		for (Registration<? extends ProductRegistrationDefinition> registration : customerBean.getRegistrations()) {
			RegistrationStateBean registrationStateBean = customerBean.getRegistrationState(registration.getId());
			
			if(null==registrationStateBean.getAllowDenySelected() || registrationStateBean.getAllowDenySelected().equals("") ){
				registrationStateBean.setValidate(null);
			}
			if (registrationStateBean.isActivate()) {
				registrationService.updateAllowRegistration(registration, registrationStateBean.isSendEmail(), null);
			} else if (registrationStateBean.getValidate() != null && !"".equals(String.valueOf(registrationStateBean.getValidate()))) {
				if (registrationStateBean.getValidate()) {
					registrationService.updateAllowRegistration(registration, registrationStateBean.isSendEmail(), null);
				} else {
					registrationService.updateDenyRegistration(registration, registrationStateBean.isSendEmail(), null);
				}
			}
		}
	}

	private Event handleExternalIdAlreadyTaken(final ExternalIdTakenException takenEx, final RequestContext context) {
		MessageContext messages = context.getMessageContext();
		ExternalIdDto dto = takenEx.getDto();
		Object[] args = { dto.getSystemId(), dto.getType(), dto.getId() };
		messages.addMessage(
				new MessageBuilder()
				.error().code("error.external.customer.id.taken").source("username")
				.defaultText("The ExternalCustomerId : System Id[{0}] Id Type[{1}] Id[{2}] has been associated with another customer")
				.args(args).build());
		return error();
	}
	
	private Event handleUsernameAlreadyExists(final RequestContext context) {
		MessageContext messages = context.getMessageContext();
		messages.addMessage(new MessageBuilder().error().code("error.username.taken").source("username")
				.defaultText("This username is already taken. Please try another.").build());
		return error();
	}
	
	private Event handlePasswordPolicyViolated(final RequestContext context, String string) {
		MessageContext messages = context.getMessageContext();
		//messages.addMessage(new MessageBuilder().error().code("error.username.taken").source("password")
			//	.defaultText("Password Policy Violated").build());
		messages.addMessage(new MessageBuilder().error().source("password").defaultText(string).build());
		return error();
	}
	
	/**
	 * Create a new customer bean. If the id resolves to a customer, place this in the bean otherwise create a new
	 * customer.
	 * 
	 * @param id
	 * @return
	 */
	public CustomerBean createCustomerBean(final String id) throws Exception {
		if (id == null) {
			CustomerBean bean = new CustomerBean();
			bean.setChangePassword(true);
			return bean;
		}
		
		Customer customer = customerService.getCustomerById(id);
        CustomerRegistrationsDto registrationsDto = registrationService.getEntitlementsForCustomerRegistrations(customer, null, false);
        /* List<LicenceDto> problemLicences = getProblemLicences(registrationsDto);
        if(registrationsDto.getLicences()!= null){
        	registrationsDto.getLicences().removeAll(problemLicences);
        }*/
        List<ProductEntitlementGroupDto> groups =//new ArrayList<ProductEntitlementGroupDto>(); 
        		/*getProductEntitlementGroups(registrationsDto);*/
        		
        		getProductEntitlementGroups(customer, registrationsDto);
        String concurrencyValue=Integer.toString(customer.getCustomerType().getConcurrency());
        
        CustomerBean customerBean = new CustomerBean(customer, groups);
        customerBean.setConcurrencyValue(concurrencyValue);
       /* if(!problemLicences.isEmpty() && problemLicences != null){
        	List<ProductEntitlementGroupDto> problemGroups = userEntitlementsService.getProblemProductEntitlementGroups(problemLicences);
        	customerBean.setProblemtEntitlementGroups(problemGroups);
        }*/
        return customerBean;
	}

	private List<ProductEntitlementGroupDto> getProductEntitlementGroups(
			CustomerRegistrationsDto registrationsDto) {
		List<ProductEntitlementGroupDto> groups = new ArrayList<ProductEntitlementGroupDto>(); 
        
		for (LicenceDto s: registrationsDto.getLicences()){
		ProductEntitlementGroupDto group = new ProductEntitlementGroupDto();
        ProductEntitlementDto entitlement = new ProductEntitlementDto();
    //    entitlement.setProductList(productList);
        List<Registration<? extends ProductRegistrationDefinition>> result = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
        List<ProductRegistration> productRegistrations = new ArrayList<ProductRegistration>();
//		entitlement.setRegistration(result);
		group.setEntitlement(entitlement);
		groups.add(group);
		}
		return groups;
	}

	private List<LicenceDto> getProblemLicences(CustomerRegistrationsDto registrationsDto){
	//	List<Registration<? extends ProductRegistrationDefinition>> registrations = registrationsDto.getRegistrations();
		List<LicenceDto> licences =registrationsDto.getLicences();
		List<LicenceDto> problemLicences = new ArrayList<LicenceDto>();
		Set<Integer> eacLicenceIds = new HashSet<Integer>();
       /* if (registrations != null) {
            for (Registration<? extends ProductRegistrationDefinition> reg : registrations) {
                addLicenceId(reg.getErightsLicenceId(), eacLicenceIds);
                for (LinkedRegistration linked : reg.getLinkedRegistrations()) {
                    addLicenceId(linked.getErightsLicenceId(), eacLicenceIds);
                }
            }
        }*/
        
        if(licences != null){
        	for (LicenceDto licenceDto : licences) {
    			if(!eacLicenceIds.contains(licenceDto.getLicenseId())){
    				problemLicences.add(licenceDto);
    			}
    		}
        }
		
		return problemLicences;
	}

	private void addLicenceId(Integer erightsLicenceId, Set<Integer> erightsLicenceIds) {
        if (erightsLicenceIds != null) {
            erightsLicenceIds.add(erightsLicenceId);
        }
    }
	
	private List<ProductEntitlementGroupDto> getProductEntitlementGroups(final Customer customer, final CustomerRegistrationsDto registrationsDto) throws ServiceLayerException {	
		List<ProductEntitlementGroupDto> groups = userEntitlementsService.getUserEntitlementGroups(registrationsDto, NO_SYSTEM_ID);		
        //sortByProductNameAndDateCreated(groups);
        /*Collections.sort(groups, new Comparator<ProductEntitlementGroupDto>() {
			@Override
			public int compare(ProductEntitlementGroupDto o1, ProductEntitlementGroupDto o2) {
				DateTime dateO1 = o1.getEntitlement().getRegistration().getUpdatedDate();
				DateTime dateO2 = o2.getEntitlement().getRegistration().getUpdatedDate();
				return dateO2.compareTo(dateO1);
			}			
		});*/
		return groups;
	}
	
	private void sortByProductNameAndDateCreated(final List<ProductEntitlementGroupDto> groups) {
		Collections.sort(groups, new Comparator<ProductEntitlementGroupDto>() {
            @Override
            public int compare(ProductEntitlementGroupDto group1, ProductEntitlementGroupDto group2) {

                boolean hasProduct1 = group1.getEntitlement().getProductList().isEmpty() == false;
                boolean hasProduct2 = group2.getEntitlement().getProductList().isEmpty() == false;

                int compLicenceIds = group1.getEntitlement().getLicence().getLicenseId()
                        .compareTo(group2.getEntitlement().getLicence().getLicenseId());
                int result;

                if (hasProduct1 && hasProduct2) {
                    // Sort alphabetically by product name and then by
                    // registration created date
                    String productName1 = group1.getProduct(0).getProductName();
                    String productName2 = group2.getProduct(0).getProductName();
                    result = productName1.compareTo(productName2);
                    if (result == 0) {

                        boolean hasReg1 = group1.getEntitlement().getRegistration() != null;
                        boolean hasReg2 = group1.getEntitlement().getRegistration() != null;
                        //customer de-duplication
                        /*if (hasReg1 && hasReg2) {
                            DateTime createdDate1 = group1.getEntitlement().getRegistration().getCreatedDate();
                            DateTime createdDate2 = group2.getEntitlement().getRegistration().getCreatedDate();
                            result = createdDate1.compareTo(createdDate2);
                        } else {*/
                            result = getCompareValue(hasReg1, hasReg2, compLicenceIds);
                        /*}*/
                    }
                } else {
                    result = getCompareValue(hasProduct1, hasProduct2, compLicenceIds);
                }
                return result;
            }
        });
	}

    private int getCompareValue(boolean b1, boolean b2, int value) {
        final int result;
        if (b1) {
            result = 1;
        } else if (b2) {
            result = -1;
        } else {
            result = value;
        }
        return result;
    }

    public Event addExternalIdToCustomer(final RequestContext context) throws Exception {
		CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
		customerBean.getExternalSystemIdType().setExternalSystem(customerBean.getExternalSystem());
		customerBeanValidator.validateAddingNewExternalId(customerBean.getExternalIds(), customerBean.getExternalSystemIdType(), customerBean.getExternalId(),
				context.getMessageContext());

		if (context.getMessageContext().hasErrorMessages()) {
			return error();
		}

		customerBean.addExternalIdToCustomer();

		// reset external id ready for future additions
		customerBean.setExternalId(null);
		customerBean.setExternalSystem(null);
		customerBean.setExternalSystemIdType(null);
		//context.getFlowScope().put("customerBean", customerBean);
		return success();
	}

	public Event updateExternalId(final RequestContext context) throws Exception {
		CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
		customerBeanValidator.validateExternalId(customerBean.getExternalId(), context.getMessageContext());
		customerBean.getExternalSystemIdType().setExternalSystem(customerBean.getExternalSystem());
		if (context.getMessageContext().hasErrorMessages()) {
			return error();
		}
		for (ExternalCustomerId externalCustomerId : customerBean.getExternalIds()) {
			if (externalCustomerId.getExternalSystemIdType().equals(customerBean.getExternalSystemIdType())) {
				externalCustomerId.setExternalId(customerBean.getExternalId());
				customerBean.setExternalId(null);
				customerBean.setExternalSystem(null);
				customerBean.setExternalSystemIdType(null);
				break;
			}
		}
		return success();
	}

	public Event removeExternalIdFromCustomer(final RequestContext context) throws Exception {
		CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
		int index = context.getRequestParameters().getInteger("removalIndex");

		customerBean.removeExternalIdFromCustomer(index);

		return success();
	}
	
	/**
	 * Re-popuplates the CustomerBean with registrations reloaded from the database.
	 * 
	 * @param context
	 *            The {@link RequestContext}.
	 * @throws Exception
	 */
	public void reloadRegistrations(final RequestContext context) throws Exception {
		CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
		List<ProductEntitlementGroupDto> groups = getProductEntitlementGroups(customerBean.getCustomer());
		customerBean.setProductEntitlementGroups(groups);
	}

	/**
	 * Horrible workaround to get Hibernate to reload Registrations from the database to work around
	 * StaleObjectStateExceptions. These exceptions may occur when a Registration has been updated by a different
	 * transaction (e.g. when the registration answers are edited). To ensure that the Registrations that we hold are up
	 * to date, we thus need to do this.
	 * <p>
	 * Once WebFlow has been removed and only one transaction management mechanism is in place this will no longer be an
	 * issue and this code can be removed.
	 * 
	 * @param requestContext
	 *            The {@link RequestContext}.
	 * @param session
	 *            The Hibernate {@link Session}.
	 */
	/*public void refreshRegistrations(final RequestContext requestContext, final Session session) {
		CustomerBean customerBean = (CustomerBean) requestContext.getFlowScope().get("customerBean");
		for (Registration<? extends ProductRegistrationDefinition> registration : customerBean.getRegistrations()) {
			session.refresh(registration);
		}
	}*/
	public void refreshRegistrations(final RequestContext requestContext, final Session session) {
		CustomerBean customerBean = (CustomerBean) requestContext.getFlowScope().get("customerBean");
		for (Registration<? extends ProductRegistrationDefinition> registration : customerBean.getRegistrations()) {
			session.refresh(registration);
			if(registration.getLinkedRegistrations()!=null)
				for (LinkedRegistration linkedRegistration : registration.getLinkedRegistrations()) {
					session.refresh(linkedRegistration);
				}
			if(registration.getRegistrationDefinition().getRegistrationActivation().getId()!= null)
				session.refresh(registration.getRegistrationDefinition().getRegistrationActivation());
		}
	}

    private List<ProductEntitlementGroupDto> getProductEntitlementGroups(final Customer customer)
            throws ServiceLayerException {
        CustomerRegistrationsDto registrationsDto = registrationService
                .getEntitlementsForCustomerRegistrations(customer, null, true);
        return getProductEntitlementGroups(customer, registrationsDto);
    }
    
    public String saveOrcsCustomer(CustomerBean customerBean){
//		CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
		Customer customer = customerBean.getCustomer();
		customer.setPassword(new Password(customerBean.getPassword(), false));
		customer.getExternalIds().clear();
		customer.getExternalIds().addAll(customerBean.getExternalIds());

		try {
			customerService.saveCustomer(customer, customerBean.isGeneratePassword());
		} catch (Exception sle) {
			sle.printStackTrace();
			return "error";
		}

		return "success";
	
    }
    
    /**
     * Kill a customer session
     * 
     * @param context
     * @return
     * @throws Exception
     */
    public Event killUserSession(final RequestContext context) throws Exception {
        CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
        String sessionId = (String) context.getFlashScope().get("sessionId");        
        Customer customer = customerBean.getCustomer();
        try {
        	if(sessionId != null){
        		if(customer.getCustomerType() == CustomerType.SELF_SERVICE || customer.getCustomerType() == CustomerType.SPECIFIC_CONCURRENCY ){ 
        			List<String> sessions = customer.getSessions();
                    String sessionKey = null;
                    if(null != sessions && !sessions.isEmpty()){
                    	if(sessions.contains(sessionId)){
                    		sessionKey = sessionId;
                    	}else{
                    		LOG.debug("Invalid session : " + sessionId);
                            throw new ServiceLayerException("Multiple session available for customer type : " + customer.getCustomerType()); 
                    	}
                        /* Due to default user concurrency change there can me more than one sessions for self service customers
                         * if(sessions.size()==1){
                            sessionKey = sessions.get(0);    
                        }else{                        
                            LOG.debug("Multiple session available for customer type : " + customer.getCustomerType());
                            throw new ServiceLayerException("Multiple session available for customer type : " + customer.getCustomerType());         
                        }*/
                    }else {
                        LOG.debug("Session not found");
                        throw new SessionNotFoundServiceLayerException("Session not found");
                    }
                    if(sessionKey != null){
                        customerService.logout(customer, sessionKey);
                        customer.getSessions().remove(sessionKey);
                        customer.setDash();
                        LOG.debug("user session killed " + customer.getId());                            
                    }
                } else {
                    LOG.debug("The customer type is not valid : " + customer.getCustomerType());
                    throw new ServiceLayerException("The customer type is not valid : " + customer.getCustomerType());
                }
        	}else {                    
                LOG.debug("Session not found");
                throw new SessionNotFoundServiceLayerException("Session not found");
            } 
        } catch (SessionNotFoundServiceLayerException snfsle){
        	customer.removeSession(sessionId);
            customer.setDash();
            LOG.debug("Session not found", snfsle);
            return handleSessionNotFound(context);            
        
        } catch (ServiceLayerException sle) {            
            LOG.debug("There was a problem killing user session:" +  customer.getSessions().get(0), sle);
            return handleServiceLayerException(context);            
        }

        return success();
    }
    
    private Event handleSessionNotFound(final RequestContext context) {
        MessageContext messages = context.getMessageContext();
        messages.addMessage(new MessageBuilder().error().code("error.session.not.found").source("username")
                .defaultText("User Session not found.").build());
        return error();
    }
    
    private Event handleServiceLayerException(final RequestContext context) {
        MessageContext messages = context.getMessageContext();
        messages.addMessage(new MessageBuilder().error().code("error.kill.session").source("username")
                .defaultText("Error occurred while killing user session.").build());
        return error();
    }
    
    private Event handleNumberFormatException(final RequestContext context, String code) {
        String msg = "Concurrency value should be in numbers only.";
        if (code == null) {
            code = "error.number.format";
        } else if (code.equals("error.too.long")) {
            msg = "Specific concurrency value is too long.";
        }

        MessageContext messages = context.getMessageContext();
        messages.addMessage(new MessageBuilder().error().code(code).source("userConcurrency").defaultText(msg).build());
        return error();
    }


    private Event handleServiceLayerValidationException(final RequestContext context) {
        
        MessageContext messages = context.getMessageContext();
        messages.addMessage(new MessageBuilder().error().code("error.negative.concurrency").source("userConcurrency")
                .defaultText("Specific concurrency cannot be nagative.").build());
        return error();

        
    }
    
    public Event checkCustomerInEright(final RequestContext context) throws Exception {
    	String id = (String) context.getFlashScope().get("customerId");
    	try {
    		customerService.checkCustomerInEright(id);
    		return success();
    	} catch (CustomerNotFoundServiceLayerException e) {
    		return error();
    	}
    }
    
    /**
     * Remove a Erights Licence which is not in EAC
     * 
     * @param context
     * @return
     * @throws Exception
     */
    public Event removeLicence(final RequestContext context) throws Exception {
    	CustomerBean customerBean = (CustomerBean) context.getFlowScope().get("customerBean");
        String licenceID = (String) context.getFlashScope().get("eRightsLicenceID");
        String userID = customerBean.getCustomer().getId();
        try{
        	if(!StringUtils.isBlank(licenceID)){
        		
        		customerService.removeLicence(userID, licenceID);
        		customerBean.removeLicenceFromProblemEntitlement(licenceID);
            }else{
                throw new ServiceLayerException("The licence ID is invalid or null : " + licenceID);
            }
        } catch (NumberFormatException nfe) {
        	LOG.debug("Licence ID not in correct format.", nfe);
        	return handleException(context,"error.number.format", "Licence ID value should be in numbers only.", "removeLicence");
        } catch(LicenceNotFoundServiceLayerException lnfsle){
        	LOG.debug("Licence not found.", lnfsle);
        	return handleException(context,"error.licence.not.found", "Licence not found.", "removeLicence");
        } catch(CustomerNotFoundServiceLayerException cnfsle){
        	LOG.debug("Customer not found.", cnfsle);
        	return handleException(context,"error.customer.not.found", "Customer not found.", "removeLicence");
        } catch (ServiceLayerException sle) {
        	String code = "";
        	String msg = "";
        	if(sle.getMessage().contains("The licence ID is invalid or null") ){
        		LOG.debug("The licence ID is invalid or null : " + licenceID);
        		code="error.licenceId.null";
        		msg="The licence ID is invalid or null";
        	}else{
        		LOG.debug("There was a problem while removing licence." + sle);
        		code="error.remove.licence";
        		msg="There was a problem while removing licence.";
        	}
        	return handleException(context,code, msg, "removeLicence");
        }
        return success();
    }
    
    /**
     * Generic method to handle exception
     * 
     * @param context
     * @param message
     * @param source
     */
    private Event handleException(final RequestContext context, String code, String msg, String source) {
        MessageContext messages = context.getMessageContext();
        messages.addMessage(new MessageBuilder().error().code(code).source(source).defaultText(msg).build());
        return error();
    }
}
