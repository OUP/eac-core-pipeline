package com.oup.eac.ws.endpoint;

import org.springframework.oxm.castor.CastorMarshaller;

/**
 * Enhancement to the Spring CastorMarshaller which 'supports' all types even though the underlying castor mapppings may not.
 * Required when we have 2 Marshallers each supporting different mappings.
 * @author David Hay
 *
 */
public class EacCastorMarshaller extends CastorMarshaller {

	private String packagePrefix;
	
	@Override
	public boolean supports(Class<?> clazz){
		boolean result = clazz.getPackage().getName().startsWith(packagePrefix);
		return result;
	}

	public String getPackagePrefix() {
		return packagePrefix;
	}

	public void setPackagePrefix(String packagePrefix) {
		this.packagePrefix = packagePrefix;
	}
	
}
