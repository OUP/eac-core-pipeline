package com.oup.eac.admin.validators;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.ExternalSystemBean;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ExternalIdService;

@Component
public class ExternalSystemBeanValidator implements Validator {

	private ExternalIdService externalIdService;

	@Autowired
	public ExternalSystemBeanValidator(ExternalIdService externalIdService) {
		super();
		this.externalIdService = externalIdService;
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		ExternalSystemBean externalSystemBean = (ExternalSystemBean) target;

		if (ExternalSystemBean.NEW.equals(externalSystemBean.getSelectedExternalSystemGuid())) {
			validateNewExternalSystem(externalSystemBean, errors);
			validateNewExternalSystemIdTypes(externalSystemBean, errors);
			validateEmptySystemIdTypes(externalSystemBean, errors);
		} else {
			validateExistingExternalSystem(externalSystemBean, errors);
			validateEmptySystemIdTypes(externalSystemBean, errors);
			ExternalSystem selectedExternalSystem = externalSystemBean.getSelectedExternalSystem();

			if(selectedExternalSystem.getExternalSystemIdTypes() != null && !selectedExternalSystem.getExternalSystemIdTypes().isEmpty()){
				try {
					validateExistingExternalSystemIdTypes(externalSystemBean, errors);
				} catch (ErightsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    
			}
		}

	}

	private void validateEmptySystemIdTypes(
			ExternalSystemBean externalSystemBean, Errors errors) {
		if(externalSystemBean.getUpdatedExternalSystem()!=null)
			if(externalSystemBean.getUpdatedExternalSystem().getExternalSystemIdTypes()==null || externalSystemBean.getUpdatedExternalSystem().getExternalSystemIdTypes().size()==0)
				errors.rejectValue("", "error.externalSystemTypeIdRequired");
	}

	private void validateNewExternalSystem(final ExternalSystemBean externalSystemBean, final Errors errors) {
		List<ExternalSystem> externalSystems = null;
		try {
			externalSystems = externalIdService.getAllExternalSystemsOrderedByName();
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String newExternalSystemId = externalSystemBean.getNewExternalSystemName();
		if (StringUtils.isNotBlank(newExternalSystemId)) {
			//This validation has been moved to controller
			/*if(newExternalSystemId.length() > 255){
				errors.rejectValue("", "error.externalSystemIdLength");
			}*/
			for (ExternalSystem externalSystem : externalSystems) {
				if (StringUtils.equalsIgnoreCase(externalSystem.getName(), newExternalSystemId)) {
					errors.rejectValue("", "error.externalSystemIdInUse");
					break;
				}
			}
		} else {
			errors.rejectValue("", "error.emptyExternalSystemId");
		}
	}

	private final void validateExistingExternalSystem(final ExternalSystemBean externalSystemBean, final Errors errors) {
		ExternalSystem externalSystem = externalSystemBean.getSelectedExternalSystem();
		if (StringUtils.isBlank(externalSystem.getName())) {
			errors.rejectValue("", "error.emptyExternalSystemId");
		}
		List<ExternalSystem> externalSystems = null;
		try {
			externalSystems = externalIdService.getAllExternalSystemsOrderedByName();
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (ExternalSystem ex : externalSystems) {
			if (StringUtils.equalsIgnoreCase(ex.getName(), externalSystem.getName()) && !ex.getId().equals(externalSystem.getId())) {
				removeInvalidExternalSystem(externalSystemBean.getExternalSystems(), externalSystem.getName(), ex.getId());
				errors.rejectValue("", "error.externalSystemIdInUse");
				return;
			}
		}
		/*for (ExternalSystemIdType externalSystemIdType : externalSystem.getExternalSystemIdTypes()) {
			if (StringUtils.isBlank(externalSystemIdType.getName())) {
				errors.rejectValue("", "error.emptyExternalSystemTypeId");
				break;
			}
		}*/
	}

	private final void removeInvalidExternalSystem(final List<ExternalSystem> externalSystems, final String name, final String validId) {
		int removeIndex = -1;
		for(int i=0;i<externalSystems.size();i++) {
			ExternalSystem externalSystem = externalSystems.get(i);
			if (StringUtils.equalsIgnoreCase(externalSystem.getName(),name)) {
				if(!externalSystem.getId().equals(validId)) {
					removeIndex = i;
					break;
				}
			}
		}
		if(removeIndex != -1) {
			externalSystems.remove(removeIndex);
		}
	}

	private void validateNewExternalSystemIdTypes(final ExternalSystemBean externalSystemBean, final Errors errors) {

		List<String> externalSystemIdTypeList = new ArrayList<String>();	    
		for (ExternalSystemIdType newExternalSystemIdType : externalSystemBean.getNewExternalSystemIdTypes()) {
			if (StringUtils.isNotBlank(newExternalSystemIdType.getName())){
				if (externalSystemIdTypeList.contains(newExternalSystemIdType.getName())) {                
					errors.rejectValue("", "error.externalSystemTypeIdDuplicate");
					break;
				} else {                
					externalSystemIdTypeList.add(newExternalSystemIdType.getName());                
				}	            
			}else if (StringUtils.isBlank(newExternalSystemIdType.getName()) && StringUtils.isNotBlank(newExternalSystemIdType.getDescription())) {
				errors.rejectValue("", "error.emptyExternalSystemTypeId");
			}

		}

		/*
	    // New logic added to have unique external system id types per system and not across all systems.
	    for (ExternalSystemIdType newExternalSystemIdType : externalSystemBean.getNewExternalSystemIdTypes()){
	        if (StringUtils.isNotBlank(newExternalSystemIdType.getName())) {

	            if (!(ExternalSystemBean.NEW.equals(externalSystemBean.getSelectedExternalSystemGuid()))) {	                

	                ExternalSystem selectedExternalSystem = externalSystemBean.getSelectedExternalSystem();
	                for (ExternalSystemIdType selectedExternalSystemIdType : selectedExternalSystem.getExternalSystemIdTypes()) {
	                    if (StringUtils.equalsIgnoreCase(selectedExternalSystemIdType.getName(), newExternalSystemIdType.getName())) {
	                        errors.rejectValue("", "error.externalSystemTypeIdInUse");
	                    }
	                }
	            }	            

	        }else if (StringUtils.isBlank(newExternalSystemIdType.getName()) && StringUtils.isNotBlank(newExternalSystemIdType.getDescription())) {
                errors.rejectValue("", "error.emptyExternalSystemTypeId");
            }

	    }

		 */

		/*	    
		for (ExternalSystemIdType newExternalSystemIdType : externalSystemBean.getNewExternalSystemIdTypes()) {
			if (StringUtils.isNotBlank(newExternalSystemIdType.getName())) {
				for (List<ExternalSystemIdType> externalSystemIdTypes : externalSystemBean.getExternalSystemIdTypes()) {
					for (ExternalSystemIdType externalSystemIdType : externalSystemIdTypes) {
						if (StringUtils.equalsIgnoreCase(externalSystemIdType.getName(), newExternalSystemIdType.getName())) {
							errors.rejectValue("", "error.externalSystemTypeIdInUse");
						}
					}
				}
			} else if (StringUtils.isBlank(newExternalSystemIdType.getName()) && StringUtils.isNotBlank(newExternalSystemIdType.getDescription())) {
				errors.rejectValue("", "error.emptyExternalSystemTypeId");
			}
		}
		 */
	}

	private void validateExistingExternalSystemIdTypes(final ExternalSystemBean externalSystemBean, final Errors errors) throws ErightsException {

		ExternalSystem selectedExtSystem = externalSystemBean.getSelectedExternalSystem();
		List<ExternalSystemIdType> extSystemIdTypeList = externalIdService.getExternalSystemIdTypesOrderedByName(selectedExtSystem);
		List<String> existingIdTypeNames = new ArrayList<String>();
		for (ExternalSystemIdType extSystemIdTypeDB : extSystemIdTypeList) {
			existingIdTypeNames.add(extSystemIdTypeDB.getName());
		}

		for (ExternalSystemIdType externalSystemIdType : selectedExtSystem.getExternalSystemIdTypes()){
			if (StringUtils.isNotBlank(externalSystemIdType.getName())) {            
				if (externalSystemIdType.hashCode() != externalSystemIdType.getPrevHashCode()) {                    
					if (existingIdTypeNames.contains(externalSystemIdType.getName())) {
						errors.rejectValue("", "error.externalSystemTypeIdDuplicate");
						break;
					}else{
						existingIdTypeNames.add(externalSystemIdType.getName());
					}
				}
			}else if (StringUtils.isBlank(externalSystemIdType.getName())) {
				errors.rejectValue("", "error.emptyExternalSystemTypeId");
			}

		}



		/*// New logic added to have unique external system id types per system and not across all systems.
        for (ExternalSystemIdType newExternalSystemIdType : externalSystemBean.getNewExternalSystemIdTypes()){
            if (StringUtils.isNotBlank(newExternalSystemIdType.getName())) {

                if (!(ExternalSystemBean.NEW.equals(externalSystemBean.getSelectedExternalSystemGuid()))) {                 

                    ExternalSystem selectedExternalSystem = externalSystemBean.getSelectedExternalSystem();
                    for (ExternalSystemIdType selectedExternalSystemIdType : selectedExternalSystem.getExternalSystemIdTypes()) {
                        if (StringUtils.equalsIgnoreCase(selectedExternalSystemIdType.getName(), newExternalSystemIdType.getName())) {
                            errors.rejectValue("", "error.externalSystemTypeIdInUse");
                        }
                    }
                }               

            }else if (StringUtils.isBlank(newExternalSystemIdType.getName()) && StringUtils.isNotBlank(newExternalSystemIdType.getDescription())) {
                errors.rejectValue("", "error.emptyExternalSystemTypeId");
            }

        }
		 */

		/*      
        for (ExternalSystemIdType newExternalSystemIdType : externalSystemBean.getNewExternalSystemIdTypes()) {
            if (StringUtils.isNotBlank(newExternalSystemIdType.getName())) {
                for (List<ExternalSystemIdType> externalSystemIdTypes : externalSystemBean.getExternalSystemIdTypes()) {
                    for (ExternalSystemIdType externalSystemIdType : externalSystemIdTypes) {
                        if (StringUtils.equalsIgnoreCase(externalSystemIdType.getName(), newExternalSystemIdType.getName())) {
                            errors.rejectValue("", "error.externalSystemTypeIdInUse");
                        }
                    }
                }
            } else if (StringUtils.isBlank(newExternalSystemIdType.getName()) && StringUtils.isNotBlank(newExternalSystemIdType.getDescription())) {
                errors.rejectValue("", "error.emptyExternalSystemTypeId");
            }
        }
		 */
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		final boolean supports = ExternalSystemBean.class.isAssignableFrom(clazz);
		return supports;
	}

}
