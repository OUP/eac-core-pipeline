package com.oup.eac.admin.validators;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.EacGroupsBean;

@Component
public class EacGroupBeanValidator implements Validator {

    public EacGroupBeanValidator() {
        super();     
    }
    
    
    @Override
	public void validate(final Object target, final Errors errors) {
    	
    	
		EacGroupsBean eacGroupBean = (EacGroupsBean) target;

		//validateEacGroupId(eacGroupBean, errors);
		validateEacGroupName(eacGroupBean, errors);
		validateEacGroupProducts(eacGroupBean, errors);	
	}

	private void validateEacGroupId(final EacGroupsBean eacGroupBean, final Errors errors) {
		if (StringUtils.isBlank(eacGroupBean.getGroupId())) {
			errors.rejectValue("groupId", "error.emptyEacGroupId");
		}
	}

	private void validateEacGroupName(final EacGroupsBean eacGroupBean, final Errors errors) {
		if (StringUtils.isBlank(eacGroupBean.getGroupName())) {
			errors.rejectValue("groupName", "error.emptyEacGroupName");
		}
	}

	private void validateEacGroupProducts(final EacGroupsBean eacGroupBean, final Errors errors) {
		
		if(!eacGroupBean.isEditMode()){
			if (CollectionUtils.isEmpty(eacGroupBean.getProductIdsToAdd())) {
				errors.rejectValue("productIdsToAdd", "error.emptyEacGroupProducts");
			}
		}else if(getSizeofFinalProducts(eacGroupBean) < 1){
			errors.rejectValue("productIdsToAdd", "error.emptyEacGroupProducts");			
		}
	}


	@Override
	public boolean supports(Class<?> clazz) {
		final boolean supports = EacGroupsBean.class.isAssignableFrom(clazz);
		return supports;
	}

	private int getSizeofFinalProducts(final EacGroupsBean eacGroupsBean){
		int size= eacGroupsBean.getExistingProducts().size() - eacGroupsBean.getProductIdsToRemove().size() + eacGroupsBean.getProductIdsToAdd().size();
		return size;
	}
}
