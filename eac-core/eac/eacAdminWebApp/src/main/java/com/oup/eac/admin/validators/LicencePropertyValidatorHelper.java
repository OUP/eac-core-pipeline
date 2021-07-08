package com.oup.eac.admin.validators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.validation.Errors;

import com.oup.eac.dto.LicenceDetailDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RollingLicenceDetailDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;
import com.oup.eac.dto.UsageLicenceDetailDto;

/**
 * Utility class that provides functions for validating the properties of different licence types.
 * 
 * @author keelingw
 * 
 */
public final class LicencePropertyValidatorHelper {

	private LicencePropertyValidatorHelper() {
	}
	
	public static void validateLicenceDto(final LicenceDto licenceDto, final Errors errors) {
		List<String> errorList = validateDates(licenceDto.getStartDate(), licenceDto.getEndDate());
		errorList.addAll(validateLicenceDetailDto(licenceDto.getLicenceDetail()));
		populateErrors(errorList, errors);
	}
	
	public static void validateLicenceDto(final LicenceDto licenceDto, final MessageContext context) {
		List<String> errorList = validateDates(licenceDto.getStartDate(), licenceDto.getEndDate());
		errorList.addAll(validateLicenceDetailDto(licenceDto.getLicenceDetail()));
		populateMessageContext(errorList, context);
	}

	public static void validateTotalConcurrency(final String totalConcurrency, final Errors errors) {
		populateErrors(validateTotalConcurrency(totalConcurrency), errors);
	}

	public static void validateTotalConcurrency(final String totalConcurrency, final MessageContext context) {
		populateMessageContext(validateTotalConcurrency(totalConcurrency), context);
	}

	public static void validateUserConcurrency(final String userConcurrency, final Errors errors) {
		populateErrors(validateUserConcurrency(userConcurrency), errors);
	}

	public static void validateUserConcurrency(final String userConcurrency, final MessageContext context) {
		populateMessageContext(validateUserConcurrency(userConcurrency), context);
	}

	public static void validateTimePeriod(final String timePeriod, final Errors errors) {
		populateErrors(validateTimePeriod(timePeriod), errors);
	}

	public static void validateTimePeriod(final String timePeriod, final MessageContext context) {
		populateMessageContext(validateTimePeriod(timePeriod), context);
	}

	public static void validateLicenceAllowedUsages(final String licenceAllowedUsages, final Errors errors) {
		populateErrors(validateLicenceAllowedUsages(licenceAllowedUsages), errors);
	}

	public static void validateLicenceAllowedUsages(final String licenceAllowedUsages, final MessageContext context) {
		populateMessageContext(validateLicenceAllowedUsages(licenceAllowedUsages), context);
	}

	private static void populateMessageContext(final List<String> errors, final MessageContext context) {
		for (String error : errors) {
			context.addMessage(new MessageBuilder().error().code(error).build());
		}
	}

	private static void populateErrors(final List<String> errors, final Errors errorMsgs) {
		for (String error : errors) {
			errorMsgs.reject(error);
		}
	}
	
	private static List<String> validateLicenceDetailDto(final LicenceDetailDto licenceDetailDto) {
		List<String> errors = new ArrayList<String>();
		switch (licenceDetailDto.getLicenceType()) {
		case CONCURRENT:
			StandardConcurrentLicenceDetailDto standardConcurrentLicenceDetailDto = (StandardConcurrentLicenceDetailDto) licenceDetailDto;
			errors.addAll(validateTotalConcurrency(standardConcurrentLicenceDetailDto.getTotalConcurrency()+""));
			errors.addAll(validateTotalConcurrency(standardConcurrentLicenceDetailDto.getUserConcurrency()+""));
			break;
		case ROLLING:
			RollingLicenceDetailDto rollingLicenceDetailDto = (RollingLicenceDetailDto) licenceDetailDto;
			errors.addAll(validateTimePeriod(rollingLicenceDetailDto.getTimePeriod()+""));
			break;
		case USAGE:
			UsageLicenceDetailDto usageLicenceDetailDto = (UsageLicenceDetailDto) licenceDetailDto;
			errors.addAll(validateLicenceAllowedUsages(usageLicenceDetailDto.getAllowedUsages()+""));
			break;
		}
		return errors;
	}
	
	private static List<String> validateDates(final LocalDate startDate, final LocalDate endDate) {
		List<String> errors = new ArrayList<String>();
		if (startDate != null && endDate != null) {
			if (endDate.isBefore(startDate)) {
				errors.add("error.licence.endDate.beforeStartDate");
			}
		}
		return errors;
	}

	protected static List<String> validateTotalConcurrency(final String totalConcurrency) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isBlank(totalConcurrency)) {
			errors.add("error.licence.noTotalConcurrency");
		} else {
			try {
				int concurrency = Integer.parseInt(totalConcurrency);
				if (concurrency == 0) {
					errors.add("error.licence.invalidTotalConcurrency");
				}
			} catch (NumberFormatException e) {
				errors.add("error.licence.invalidTotalConcurrency");
			}
		}
		return errors;
	}

	protected static List<String> validateUserConcurrency(final String userConcurrency) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isBlank(userConcurrency)) {
			errors.add("error.licence.noUserConcurrency");
		} else {
			try {
				int concurrency = Integer.parseInt(userConcurrency);
				if (concurrency == 0) {
					errors.add("error.licence.invalidUserConcurrency");
				}
			} catch (NumberFormatException e) {
				errors.add("error.licence.invalidUserConcurrency");
			}
		}
		return errors;
	}

	protected static List<String> validateTimePeriod(final String timePeriod) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isBlank(timePeriod)) {
			errors.add("error.licence.noTimePeriod");
		} else {
			String message = "error.licence.invalidTimePeriod";
			try {
				int timePeriodInt = Integer.parseInt(timePeriod);
				if (timePeriodInt < 1 || timePeriodInt > 200000) {
					errors.add(message);
				}
			} catch (NumberFormatException e) {
				errors.add(message);
			}
		}
		return errors;
	}

	protected static List<String> validateLicenceAllowedUsages(final String licenceAllowedUsages) {
		List<String> errors = new ArrayList<String>();
		if (StringUtils.isBlank(licenceAllowedUsages)) {
			errors.add("error.licence.noAllowedUsages");
		} else {
			String message = "error.licence.invalidAllowedUsages";
			try {
				int licenceAllowedUsagesInt = Integer.parseInt(licenceAllowedUsages);
				if (licenceAllowedUsagesInt < 1 || licenceAllowedUsagesInt > 200000) {
					errors.add(message);
				}
			} catch (NumberFormatException e) {
				errors.add(message);
			}
		}
		return errors;
	}


}
