package com.oup.eac.data.hibernate.interceptors;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.type.Type;
import org.joda.time.DateTime;

import com.oup.eac.domain.CreatedAudit;
import com.oup.eac.domain.UpdatedAudit;
import com.oup.eac.domain.User;

public class EACEntiyInterceptor extends EmptyInterceptor {
	private static Logger logger = Logger.getLogger(EACEntiyInterceptor.class);
	private static final String CREATED_DATE = "createdDate";
	private static final String UPDATED_DATE = "updatedDate";
	
	private static final String FIRSTNAME = "firstName";
	private static final String LASTNAME = "familyName";
	private static final String EMAIL_ADDRESS = "emailAddress";
	private static final String LOCALE = "locale";
	
	private static final String CURRENT_STATE = "current state";
	private static final String PREVIOUS_STATE = "previous state";
	
	@Override
	public boolean onSave(Object obj, Serializable id, Object[] newValues, String[] properties, Type[] types) {
		if(obj instanceof CreatedAudit) {
			((CreatedAudit) obj).setCreatedDate(new DateTime());
			int createdDateIndex = findPropertyIndex(CREATED_DATE,properties);
			newValues[createdDateIndex] = ((CreatedAudit) obj).getCreatedDate();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onFlushDirty(Object obj, Serializable id, Object[] currentState, Object[] previousState, String[] properties, Type[] types) {
		if(obj instanceof UpdatedAudit && (!(obj instanceof User) || (obj instanceof User && updateUserUpdatedDate(currentState, previousState, properties)))) {
			((UpdatedAudit) obj).setUpdatedDate(new DateTime());
			int updatedDateIndex = findPropertyIndex(UPDATED_DATE,properties);
			currentState[updatedDateIndex] = ((UpdatedAudit) obj).getUpdatedDate();
			return true;
		}
		return false;
	}
	
	private boolean updateUserUpdatedDate(Object[] currentState, Object[] previousState, String[] properties) {
		/*if(checkState(currentState, CURRENT_STATE) || checkState(previousState, PREVIOUS_STATE)) {
			return false;
		}
		return isValueChanged(currentState, previousState, properties, FIRSTNAME) || 
		       isValueChanged(currentState, previousState, properties, LASTNAME) || 
		       isValueChanged(currentState, previousState, properties, EMAIL_ADDRESS) || 
		       isValueChanged(currentState, previousState, properties, LOCALE);*/
		return true;
	}
	
	private boolean isValueChanged(Object[] currentState, Object[] previousState, String[] properties, String property) {
		Object currentValue = getProperty(property, properties, currentState);
		Object previousValue = getProperty(property, properties, previousState);
		if(currentValue == null) {
		    if(previousValue == null) {
		        return false;
		    }
		    return true;
		}
        if(previousValue == null) {
            return true;
        }
        return !currentValue.equals(previousValue);
	}
	
	private boolean checkState(Object[] state, String name) {
		if(state == null) {
			logger.warn("User state, '" + name + "' is null. Unable to establish if the User state has changed. This may be because the User is not attached to the hibernate session! The User updated date will NOT be changed.");
			return true;
		}
		return false;
	}
	
	private final Object getProperty(String property, String[] properties, Object[] values) {
		int index = findPropertyIndex(property, properties);
		return values[index];
	}
	
	private final int findPropertyIndex(String property, String[] properties) {
		int index = 0;
		for(String prop : properties) {
			if(property.equals(prop)) {
				return index; 
			}
			index++;
		}
		throw new PropertyNotFoundException("Property " + property + " not found.");
	}
}
