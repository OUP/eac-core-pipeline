package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.ExternalId;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;

public class ExternalSystemBean implements Serializable {

	public static final String NEW = "new";
	
	private String selectedExternalSystemGuid;
	private List<ExternalSystem> externalSystems;
	private List<List<ExternalSystemIdType>> externalSystemIdTypes;
	private String systemTypesToRemove;
	private List<ExternalSystemIdType> systemTypes = createEmptySystemTypes();
	private String newExternalSystemName;
	private String newExternalSystemDescription;
	private String updatedSytemTypes;
	//added to support acess w/s call
	private boolean editFlag=false;
	private List<ExternalSystemIdType> systemTypesToDelete;
	private String oldSystemName;

    public ExternalSystemBean(final List<ExternalSystem> externalSystems) {
		initialise(externalSystems);
	}

	private void initialise(final List<ExternalSystem> externalSystems) {
		this.externalSystems = externalSystems;
		this.externalSystemIdTypes = new ArrayList<List<ExternalSystemIdType>>();
		
		for (ExternalSystem externalSystem : externalSystems) {
			List<ExternalSystemIdType> externalSystemIdTypes = new ArrayList<ExternalSystemIdType>(externalSystem.getExternalSystemIdTypes());
			
			for (ExternalSystemIdType externalSystemIdType : externalSystemIdTypes) {
                ExternalSystemIdType oldExtSysIdtype = new ExternalSystemIdType();
                oldExtSysIdtype.setName(externalSystemIdType.getName());
                oldExtSysIdtype.setDescription(externalSystemIdType.getDescription());
                if (!externalSystemIdType.isDeletable()) {
                	Set<ExternalId<?>> externalIds = new HashSet<ExternalId<?>>() ;
                	ExternalId<?> externalId = new ExternalProductId() ;
                	externalId.setExternalSystemIdType(externalSystemIdType);
                	externalIds.add(externalId) ;
                	externalSystemIdType.setExternalIds(externalIds);
                }
                externalSystemIdType.setOldExternalSystemIdtype(oldExtSysIdtype);
            }
			
			Collections.sort(externalSystemIdTypes, new Comparator<ExternalSystemIdType>() {
				@Override
				public int compare(ExternalSystemIdType o1, ExternalSystemIdType o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			
			this.externalSystemIdTypes.add(externalSystemIdTypes);
		}
		
		
	}
	
	public ExternalSystem getUpdatedExternalSystem() {
		ExternalSystem selectedExternalSystem = null;
		
		if (NEW.equals(selectedExternalSystemGuid)) {
			selectedExternalSystem = new ExternalSystem();
			selectedExternalSystem.setName(newExternalSystemName);
			selectedExternalSystem.setDescription(newExternalSystemDescription);
			this.editFlag=false;
		} else {
			for (int i = 0; i < externalSystems.size(); i++) {
				ExternalSystem externalSystem = externalSystems.get(i);
				if (selectedExternalSystemGuid.equals(externalSystem.getId())) {
					externalSystem.getExternalSystemIdTypes().retainAll(getSystemTypesToKeep(externalSystemIdTypes.get(i)));
					this.editFlag=true;
					selectedExternalSystem = externalSystem;
					break;
				}
			}
		}
		
		setNewExternalSystemIdTypesOn(selectedExternalSystem);
		
		return selectedExternalSystem;
	}
	
	private Set<ExternalSystemIdType> getSystemTypesToKeep(final List<ExternalSystemIdType> externalSystemIdTypes) {
		Set<ExternalSystemIdType> typesToKeep = new HashSet<ExternalSystemIdType>();
		int[] systemTypesToRemove = getSystemTypesToRemoveAsArray();
		systemTypesToDelete=new ArrayList<ExternalSystemIdType>();
		
		for (int i = 0; i < externalSystemIdTypes.size(); i++) {
			if (!ArrayUtils.contains(systemTypesToRemove, i)) {
				typesToKeep.add(externalSystemIdTypes.get(i));
			}
			else
			{
				this.systemTypesToDelete.add(externalSystemIdTypes.get(i));
			}
		}
		
		return typesToKeep;
	}
	
	private int[] getSystemTypesToRemoveAsArray() {
		if (StringUtils.isNotBlank(systemTypesToRemove)) {
			String[] systemTypeStrs = systemTypesToRemove.split(",");
			int[] systemTypeIndexes = new int[systemTypeStrs.length];
			
			for (int i = 0; i < systemTypeStrs.length; i++) {
				systemTypeIndexes[i] = Integer.parseInt(systemTypeStrs[i].trim());
			}
			
			return systemTypeIndexes;
		}
		
		return new int[0];
	}
	
	private void setNewExternalSystemIdTypesOn(final ExternalSystem externalSystem) {
		for (ExternalSystemIdType newType : systemTypes) {
			if (StringUtils.isNotBlank(newType.getName())) {
				newType.setExternalSystem(externalSystem);
				externalSystem.getExternalSystemIdTypes().add(newType);
			}
		}
	}
	
	public ExternalSystem getSelectedExternalSystem() {
		ExternalSystem selectedExternalSystem = null;
		for (ExternalSystem externalSystem : externalSystems) {
			if (selectedExternalSystemGuid.equals(externalSystem.getId())) {
				selectedExternalSystem = externalSystem;
				break;
			}
		}
		return selectedExternalSystem;
	}

	public List<ExternalSystem> getExternalSystems() {
		return externalSystems;
	}

	public List<List<ExternalSystemIdType>> getExternalSystemIdTypes() {
		return externalSystemIdTypes;
	}
	
	public List<ExternalSystemIdType> getNewExternalSystemIdTypes() {
		return systemTypes;
	}

	public String getSelectedExternalSystemGuid() {
		return selectedExternalSystemGuid;
	}

	public void setSelectedExternalSystemGuid(final String selectedExternalSystemGuid) {
		this.selectedExternalSystemGuid = selectedExternalSystemGuid;
	}

	public String getSystemTypesToRemove() {
		return systemTypesToRemove;
	}

	public void setSystemTypesToRemove(final String systemTypesToRemove) {
		this.systemTypesToRemove = systemTypesToRemove;
	}
	
	public void refreshWith(final List<ExternalSystem> externalSystems) {
		initialise(externalSystems);
	}
	
	private List<ExternalSystemIdType> createEmptySystemTypes() {
		List<ExternalSystemIdType> types = new ArrayList<ExternalSystemIdType>();
		for (int i = 0; i < 100; i++) {
			types.add(new ExternalSystemIdType());
		}
		return types;
	}

	public String getNewExternalSystemName() {
		return newExternalSystemName;
	}

	public void setNewExternalSystemName(String newExternalSystemName) {
		this.newExternalSystemName = newExternalSystemName;
	}

	public String getNewExternalSystemDescription() {
		return newExternalSystemDescription;
	}

	public void setNewExternalSystemDescription(String newExternalSystemDescription) {
		this.newExternalSystemDescription = newExternalSystemDescription;
	}
	
   public String getUpdatedSytemTypes() {
        return updatedSytemTypes;
    }

    public void setUpdatedSytemTypes(String updatedSytemTypes) {
        this.updatedSytemTypes = updatedSytemTypes;
    }

	public boolean isEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}

	public List<ExternalSystemIdType> getSystemTypesToDelete() {
		return systemTypesToDelete;
	}

	public void setSystemTypesToDelete(
			List<ExternalSystemIdType> systemTypesToDelete) {
		this.systemTypesToDelete = systemTypesToDelete;
	}

	public String getOldSystemName() {
		return oldSystemName;
	}

	public void setOldSystemName(String oldSystemName) {
		this.oldSystemName = oldSystemName;
	}
    
    


}
