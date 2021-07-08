/*package com.oup.eac.integration.erights;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.integration.erights.ws.ActivateLicenseResponseWS;
import com.oup.eac.integration.erights.ws.AddLicenseResponseWS;
import com.oup.eac.integration.erights.ws.AuthenticationResponseWS;
import com.oup.eac.integration.erights.ws.AuthorizeRequestResponseWS;
import com.oup.eac.integration.erights.ws.ChangePasswordResponseWS;
import com.oup.eac.integration.erights.ws.CreateGroupResponseWS;
import com.oup.eac.integration.erights.ws.CreateProductResponseWS;
import com.oup.eac.integration.erights.ws.CreateUserAccountAddLicenseResponseWS;
import com.oup.eac.integration.erights.ws.CreateUserAccountResponseWS;
import com.oup.eac.integration.erights.ws.DeactivateLicenseResponseWS;
import com.oup.eac.integration.erights.ws.DeleteGroupResponseWS;
import com.oup.eac.integration.erights.ws.DeleteProductResponseWS;
import com.oup.eac.integration.erights.ws.DeleteUserAccountResponseWS;
import com.oup.eac.integration.erights.ws.GetGroupResponseWS;
import com.oup.eac.integration.erights.ws.GetGroupUsersResponseWS;
import com.oup.eac.integration.erights.ws.GetLicensesForUserResponseWS;
import com.oup.eac.integration.erights.ws.GetProductResponseWS;
import com.oup.eac.integration.erights.ws.GetSessionsByUserIdResponseWS;
import com.oup.eac.integration.erights.ws.GetUserAccountResponseWS;
import com.oup.eac.integration.erights.ws.LicensesForUserProductResponseWS;
import com.oup.eac.integration.erights.ws.LogoutResponseWS;
import com.oup.eac.integration.erights.ws.OupCredentialWS;
import com.oup.eac.integration.erights.ws.OupGroupWS;
import com.oup.eac.integration.erights.ws.OupLicenseWS;
import com.oup.eac.integration.erights.ws.OupProductWS;
import com.oup.eac.integration.erights.ws.OupRightAccessServiceDefinition;
import com.oup.eac.integration.erights.ws.OupUserWS;
import com.oup.eac.integration.erights.ws.ProductsFromURLResponseWS;
import com.oup.eac.integration.erights.ws.RemoveLicenseResponseWS;
import com.oup.eac.integration.erights.ws.UpdateGroupResponseWS;
import com.oup.eac.integration.erights.ws.UpdateLicenseResponseWS;
import com.oup.eac.integration.erights.ws.UpdateProductResponseWS;
import com.oup.eac.integration.erights.ws.UpdateUserAccountResponseWS;
import com.oup.eac.integration.erights.ws.UserIdsFromSessionResponseWS;

@Component("networkTolerantErightsPort")
public class NetworkTolerantErightsPort implements OupRightAccessServiceDefinition, ApplicationListener<ContextStartedEvent> {
	
	private static final Logger LOGGER = Logger.getLogger(NetworkTolerantErightsPort.class);

	private final OupRightAccessServiceDefinition erightsPort;
	private int maxRetries;

	@Autowired
	public NetworkTolerantErightsPort(@Qualifier("erightsPort") final OupRightAccessServiceDefinition erightsPort) {
		this.erightsPort = erightsPort;
	}
	
	private Object invokeMethod(final String methodName, final Object[] params) {
		return invokeMethod(methodName, params, 0);
	}

	private Object invokeMethod(final String methodName, final Object[] params, final int retryCount) {
		List<Class<?>> parameterClasses = getParameterClasses(params);
		try {
			Method method = erightsPort.getClass().getMethod(methodName, parameterClasses.toArray(new Class<?>[0]));
			return method.invoke(erightsPort, params);
		} catch (final InvocationTargetException ite) {
			// Exceptions coming out of the web service call can only be RuntimeExceptions or Errors
			// because the API of the OupRightAccessServiceDefinition does not declare any checked exceptions.
			Throwable t = ite.getTargetException();
			if (t instanceof Error) {
				throw (Error) t; // We can't do anything with these so we propagate
			} else if (! (t instanceof RuntimeException)) {
				// The wrapped API can't throw anything other than a RuntimeException, but we double-check
				// just in case a future change to the API declares a checked exception to be thrown.
				throw new RuntimeException("Error invoking web service method on wrapped OupRightAccessServiceDefinition: " + t, t);
			}
			RuntimeException e = (RuntimeException) t; // Can *only* be a RuntimeException (no worry of ClassCastException here).
			if (isConnectionRefused(e)) {
				if (retryCount < maxRetries) {
					sleep(100);
					LOGGER.info("Got connection refused on '" + methodName + "' - retrying invocation (attempt=" + (retryCount + 1) + ")");
					return invokeMethod(methodName, params, retryCount + 1);
				}
				LOGGER.error("Aborting '" + methodName + "' after " + (maxRetries + 1) + " attempts due to connection refused");
				throw e;
			}
			throw e;
		} catch (final Exception e) {
			// Errors caused by this implementation
			throw new RuntimeException("Error invoking web service method on wrapped OupRightAccessServiceDefinition: " + e, e);
		}
	}

	private List<Class<?>> getParameterClasses(final Object[] params) {
		List<Class<?>> parameterClasses = new ArrayList<Class<?>>();
		for (Object param : params) {
			Class<?> theClass;
			if (param instanceof Integer) {
				theClass = Integer.TYPE;
			} else {
				theClass = param.getClass();
			}
			parameterClasses.add(theClass);
		}
		return parameterClasses;
	}


	private boolean isConnectionRefused(final Exception exception) {
		Throwable rootCause = getRootCause(exception);
		return rootCause instanceof ConnectException && StringUtils.contains(rootCause.getMessage(), "Connection refused");
	}
	
	private Throwable getRootCause(final Throwable e) {
		if (e.getCause() != null) {
			return getRootCause(e.getCause());
		}
		return e;
	}
	
	private void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException e) {
		}
	}

	@Override
	public CreateGroupResponseWS createGroup(final OupGroupWS oupGroupWS) {
		return (CreateGroupResponseWS) invokeMethod("createGroup", new Object[] { oupGroupWS });
	}

	@Override
	public GetLicensesForUserResponseWS getLicensesForUser(final int userId) {
		return (GetLicensesForUserResponseWS) invokeMethod("getLicensesForUser", new Object[] { userId });
	}

	@Override
	public UpdateGroupResponseWS updateGroup(final OupGroupWS oupGroupWS) {
		return (UpdateGroupResponseWS) invokeMethod("updateGroup", new Object[] { oupGroupWS });
	}

	@Override
	public UpdateProductResponseWS updateProduct(final OupProductWS oupProductWS) {
		return (UpdateProductResponseWS) invokeMethod("updateProduct", new Object[] { oupProductWS });
	}

	@Override
	public DeleteGroupResponseWS deleteGroup(final int groupId) {
		return (DeleteGroupResponseWS) invokeMethod("deleteGroup", new Object[] { groupId });
	}

	@Override
	public AuthenticationResponseWS authenticate(final OupCredentialWS credentialWS) {
		return (AuthenticationResponseWS) invokeMethod("authenticate", new Object[] { credentialWS });
	}

	@Override
	public ChangePasswordResponseWS changePasswordByUsername(final String username, final String password) {
		return (ChangePasswordResponseWS) invokeMethod("changePasswordByUsername", new Object[] { username, password });
	}

	@Override
	public ActivateLicenseResponseWS activateLicense(final int userId, final int licenseId) {
		return (ActivateLicenseResponseWS) invokeMethod("activateLicense", new Object[] { userId, licenseId });
	}

	@Override
	public GetUserAccountResponseWS getUserAccount(final int userId) {
		return (GetUserAccountResponseWS) invokeMethod("getUserAccount", new Object[] { userId });
	}

	@Override
	public LicensesForUserProductResponseWS getLicensesForUserProduct(final int userId, final int productId) {
		return (LicensesForUserProductResponseWS) invokeMethod("getLicensesForUserProduct", new Object[] { userId, productId });
	}

	@Override
	public CreateUserAccountResponseWS createUserAccount(final OupUserWS user) {
		return (CreateUserAccountResponseWS) invokeMethod("createUserAccount", new Object[] { user });
	}

	@Override
	public UserIdsFromSessionResponseWS getUserIdsFromSession(final String session) {
		return (UserIdsFromSessionResponseWS) invokeMethod("getUserIdsFromSession", new Object[] { session });
	}

	@Override
	public GetGroupUsersResponseWS getGroupUsers(final int groupId, final Boolean includeIndirectParents) {
		return (GetGroupUsersResponseWS) invokeMethod("getGroupUsers", new Object[] { groupId, includeIndirectParents });
	}

	@Override
	public GetGroupResponseWS getGroup(final int groupId) {
		return (GetGroupResponseWS) invokeMethod("getGroup", new Object[] { groupId });
	}

	@Override
	public DeleteProductResponseWS deleteProduct(final int productId) {
		return (DeleteProductResponseWS) invokeMethod("deleteProduct", new Object[] { productId });
	}

	@Override
	public GetProductResponseWS getProduct(final int productId) {
		return (GetProductResponseWS) invokeMethod("getProduct", new Object[] { productId });
	}

	@Override
	public AddLicenseResponseWS addLicense(final int userId, final OupLicenseWS license) {
		return (AddLicenseResponseWS) invokeMethod("addLicense", new Object[] { userId, license });
	}

	@Override
	public ProductsFromURLResponseWS getProductsFromURL(final String url) {
		return (ProductsFromURLResponseWS) invokeMethod("getProductsFromURL", new Object[] { url });
	}

	@Override
	public CreateProductResponseWS createProduct(final OupProductWS oupProductWS) {
		return (CreateProductResponseWS) invokeMethod("createProduct", new Object[] { oupProductWS });
	}

	@Override
	public UpdateLicenseResponseWS updateLicense(final int userId, final OupLicenseWS license) {
		return (UpdateLicenseResponseWS) invokeMethod("updateLicense", new Object[] { userId, license });
	}

	@Override
	public CreateUserAccountAddLicenseResponseWS createUserAccountAddLicense(final OupUserWS user, final OupLicenseWS license) {
		return (CreateUserAccountAddLicenseResponseWS) invokeMethod("createUserAccountAddLicense", new Object[] { user, license });
	}

	@Override
	public DeleteUserAccountResponseWS deleteUserAccount(final int userId) {
		return (DeleteUserAccountResponseWS) invokeMethod("deleteUserAccount", new Object[] { userId });
	}

	@Override
	public DeactivateLicenseResponseWS deactivateLicense(final int userId, final int licenseId) {
		return (DeactivateLicenseResponseWS) invokeMethod("deactivateLicense", new Object[] { userId, licenseId });
	}

	@Override
	public UpdateUserAccountResponseWS updateUserAccount(final OupUserWS oupUserWS) {
		return (UpdateUserAccountResponseWS) invokeMethod("updateUserAccount", new Object[] { oupUserWS });
	}

	@Override
	public ChangePasswordResponseWS changePasswordByUserId(final int userId, final String password) {
		return (ChangePasswordResponseWS) invokeMethod("changePasswordByUserId", new Object[] { userId, password });
	}

	@Override
	public LogoutResponseWS logout(final String session) {
		return (LogoutResponseWS) invokeMethod("logout", new Object[] { session });
	}

	@Override
	public RemoveLicenseResponseWS removeLicense(final int userId, final int licenseId) {
		return (RemoveLicenseResponseWS) invokeMethod("removeLicense", new Object[] { userId, licenseId });
	}
	
	public void setMaxRetries(final int maxRetries) {
		Assert.isTrue(maxRetries >= 0);
		this.maxRetries = maxRetries;
	}

	@Override
	public void onApplicationEvent(final ContextStartedEvent event) {
		setMaxRetries(Integer.parseInt(EACSettings.getProperty("erights.connectionRefused.retries", "3")));
	}
	
	@Override
    public GetSessionsByUserIdResponseWS getSessionsByUserId(final int userId) {
        return (GetSessionsByUserIdResponseWS) invokeMethod("getSessionsByUserId", new Object[] { userId });
    }

	@Override
    public AuthorizeRequestResponseWS authorizeRequest(String sessionKey, String url, int userId, int licenseId) {
        return (AuthorizeRequestResponseWS) invokeMethod("authorizeRequest", new Object[] { sessionKey, url, userId, licenseId });
    }

}
*/