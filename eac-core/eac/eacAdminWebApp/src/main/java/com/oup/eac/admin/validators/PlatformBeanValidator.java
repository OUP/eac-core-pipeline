package com.oup.eac.admin.validators;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.PlatformBean;
import com.oup.eac.domain.Platform;
import com.oup.eac.service.PlatformService;

@Component
public class PlatformBeanValidator implements Validator {
	
	private PlatformService platformService;

	@Override
	public void validate(final Object target, final Errors errors) {
		PlatformBean platformBean = (PlatformBean) target;
		
		if (PlatformBean.NEW.equals(platformBean.getSelectedPlatformGuid())) {
			validateNewPlatform(platformBean, errors);
		}
		else{
			validateUpdatePlatform(platformBean, errors);
		}
	}
	
	private void validateNewPlatform(PlatformBean platformBean, Errors errors) {
		
		String newPlatformName = platformBean.getNewPlatformName();
		String newPlatformCode = platformBean.getNewPlatformCode();
		if (newPlatformName == null){
			errors.rejectValue("", "error.platformNameRequired");
		}
		if (newPlatformCode == null){
			errors.rejectValue("", "error.platformCodeRequired");
		}
	}
	
	private void validateUpdatePlatform(PlatformBean platformBean, Errors errors) {
		
		List<Platform> platformList = platformBean.getPlatforms();
		
		/*try {
			platformList = platformService.getAllPlatforms();
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		for (Platform platform : platformList){
			if (platformBean.getSelectedPlatformGuid().equals(platform.getPlatformId())){
				if(platform.getName() == null){
					errors.rejectValue("", "error.platformNameRequired");
					break;
					
				}
			}
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		final boolean supports = PlatformBean.class.isAssignableFrom(clazz);
		return supports;
	}
}
