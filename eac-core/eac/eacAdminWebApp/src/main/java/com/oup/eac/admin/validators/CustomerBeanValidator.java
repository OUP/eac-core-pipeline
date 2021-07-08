package com.oup.eac.admin.validators;

import java.net.IDN;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.oup.eac.admin.beans.CustomerBean;
import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.dto.LicenceDto;

@Component(value = "customerBeanValidator")
public class CustomerBeanValidator {
	
	private final UsernameValidator usernameValidator;
	 private static final String AT_SYMBOL = "@";
	 private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$", Pattern.CASE_INSENSITIVE);
	
	@Autowired
	public CustomerBeanValidator(final UsernameValidator usernameValidator) {
		this.usernameValidator = usernameValidator;
	}

	public void validateCreateOrEditCustomer(final CustomerBean customerBean, final ValidationContext context) {

		MessageContext messages = context.getMessageContext();

		validateCustomer(customerBean, messages);
		validateLicences(customerBean, messages);
	}

	private void validateCustomer(final CustomerBean customerBean, final MessageContext messages) {

		// Check email is available and is valid
		if (StringUtils.isBlank(customerBean.getCustomer().getEmailAddress())) {
			messages.addMessage(new MessageBuilder().error().code("error.emailAddress.required").source("emailAddress").defaultText("Email is required")
					.build());
			return;
		} 
		if (!isEmailValid(customerBean)) {
			messages.addMessage(new MessageBuilder().error().code("error.emailAddress.invalid").source("emailAddress")
					.defaultText("Email must be a valid email address").build());
			return;

		}
		
		// Check username is valid - according to rules.
		if (!usernameValidator.isValid(customerBean.getCustomer().getUsername())) {
			messages.addMessage(new MessageBuilder().error().code("error.username.required").source("username").defaultText("You must enter a valid username")
					.build());
			return;
		}

		// Check passwords match
		if (customerBean.isChangePassword() && !customerBean.isGeneratePassword()) {
			PasswordValidatorHelper.validate(customerBean.getPassword(), customerBean.getPasswordAgain(), messages);
		}

	}

	private void validateLicences(final CustomerBean customerBean, final MessageContext messages) {
		for (LicenceDto licence : customerBean.getLicences()) {
			LicencePropertyValidatorHelper.validateLicenceDto(licence, messages);
		}
	}
	
	/**
     * Validates customer details in Customer Tab. 
     * @param customerBean
     * @param messages
     * @return
     */
	public void validateCustomerProfile(final CustomerBean customerBean, final MessageContext messages) {

		// Check email is available and is valid
		if (StringUtils.isBlank(customerBean.getCustomer().getEmailAddress())) {
			messages.addMessage(new MessageBuilder().error().code("error.emailAddress.required").source("emailAddress").defaultText("Email is required")
					.build());
			return;
		} 
		if (!isEmailValid(customerBean)) {
			messages.addMessage(new MessageBuilder().error().code("error.emailAddress.invalid").source("emailAddress")
					.defaultText("Email must be a valid email address").build());
			return;

		}
	}
	
	
	 /**
     * Validates credential information of customer 
     * @param customerBean
     * @param messages
     * @return
     */
	public void validateCustomerCredentials(final CustomerBean customerBean, final MessageContext messages) {

		// Check username is valid - according to rules.
		if (!usernameValidator.isValid(customerBean.getCustomer().getUsername())) {
			messages.addMessage(new MessageBuilder().error().code("error.username.required").source("username").defaultText("You must enter a valid username")
					.build());
			return;
		}

		// Check passwords match
		if (customerBean.isChangePassword() && !customerBean.isGeneratePassword()) {
			PasswordValidatorHelper.validate(customerBean.getPassword(), customerBean.getPasswordAgain(), messages);
		}

	}

	public void validateAddingNewExternalId(final List<ExternalCustomerId> externalIds, final ExternalSystemIdType externalSystemIdType,
			final String externalId, final MessageContext messages) {

		for (ExternalCustomerId externalCustomerId : externalIds) {
			if (externalCustomerId.getExternalSystemIdType().equals(externalSystemIdType)) {
				messages.addMessage(new MessageBuilder().error().code("error.externalId.exists").source("externalId")
						.defaultText("An external id for this system id and type already exists for this customer.").build());
				return;
			}
		}

		validateExternalId(externalId, messages);
	}

	public void validateExternalId(final String externalId, final MessageContext messages) {
		try {
			Assert.hasLength(externalId);
		} catch (IllegalArgumentException iae) {
			messages.addMessage(new MessageBuilder().error().code("error.externalId.required").source("externalId")
					.defaultText("Id is required for new external id").build());
			return;
		}
	}
	
	 /**
     * Validate email address format, including IDN's
     * @param emailAddress
     * @return
     */
	 public static boolean isEmailValid(final CustomerBean customerBean) {
        final String emailAddress = customerBean.getCustomer().getEmailAddress();
        boolean valid = emailAddress != null;
        if(valid && emailAddress.endsWith(".")){
            valid = false;
        }
        if (valid) {
            final int atPosition = emailAddress.indexOf(AT_SYMBOL);
            valid = atPosition > 0;
            if (valid) {
                final String localPart = emailAddress.substring(0, atPosition);
                final String domain = emailAddress.substring(atPosition + 1);
                // handle IDNs by punycode conversion to ASCII Compatible Encoding (ACE)
                // before performing regex check
                try {
                    final String localPartAce = IDN.toASCII(localPart);
                    final String domainAce = IDN.toASCII(domain);
                    final String email = localPartAce + AT_SYMBOL + domainAce;         
                    final Matcher matcher = EMAIL_PATTERN.matcher(InternationalEmailAddress.convertToAscii(email));
                    valid =  matcher.find();
                    
                } catch (Exception e) {                
                    valid = false;
                }
            }
        }
        return valid;
    }
}
