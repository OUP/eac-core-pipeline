package com.oup.eac.domain;

import java.util.Locale;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "activation_type", discriminatorType = DiscriminatorType.STRING, length = 50)
public abstract class RegistrationActivation extends BaseDomainObject {
	
	private static final Logger LOGGER = Logger.getLogger(RegistrationActivation.class);

	public static enum ActivationStrategy {
		INSTANT,
		SELF,
		VALIDATED;
	}
	
	public abstract ActivationStrategy getActivationStrategy(final Locale... locale);
	
	/**
	 * Method for obtaining meta-data from registration activations. This method will return the value
	 * from invoking a property with the specified name. The property value should be a String and the
	 * property method should be a no-argument bean-style accessor. Sub-classes may take into account 
	 * the specified {@link Locale} when evaluating the property.
	 * 
	 * @param name The name of the property to get the value for.
	 * @param locale The Locale.
	 * @return The value of the property or null if no property was found with the specified name.
	 */
	public String getProperty(final String name, final Locale... locale) {
		String property = null;
		try {
			Object result = PropertyUtils.getProperty(this, name);
			property = (String) result;
		} catch (Exception e) {
			// Likely to result from programmer error
			LOGGER.error("Unable to get property '" + name + "' from '" + getClass().getSimpleName() + "': " + e, e);
		} 
		return property;
	}
	
    /**
     * Gets the name.
     * You can't rely on ".class.simpleName" in JSP EL because the runtime class may be a proxy so the class name might end up containing something like '$$ javassist' 
     * @return the name.
     */
    public abstract String getName();

}
