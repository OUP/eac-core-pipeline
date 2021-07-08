package com.oup.eac.admin.webflow;

import org.apache.log4j.Logger;
import org.hibernate.StaleObjectStateException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.RequestContext;

/**
 * Listens to flow exceptions and logs them.
 * 
 * @author Ian Packard
 *
 */
public class ExceptionLoggingListener extends FlowExecutionListenerAdapter {

	private static final Logger LOGGER = Logger.getLogger(ExceptionLoggingListener.class);
	
	@Override
	public void exceptionThrown(RequestContext context, FlowExecutionException exception) {
		if (rootCauseIs(StaleObjectStateException.class, exception)) {
			LOGGER.warn("Concurrent modification of Hibernate object", exception);
		} else if (!rootCauseIs(AccessDeniedException.class, exception)) {
			LOGGER.error("Unhandled exception encountered in webflow.", exception);
		}
    }

	/**
	 * Unravels the FlowExecutionException to discover whether the root cause is an exception of the type specified.
	 * 
	 * @param exceptionType
	 *            The Class of the root cause.
	 * @param ex
	 *            The {@link FlowExecutionException} wrapper.
	 * @return True if the root cause of the FlowExecutionException is an exception of the specified Class. False
	 *         otherwise.
	 */
	private <T> boolean rootCauseIs(Class<T> exceptionType, FlowExecutionException ex) {
		Throwable cause = ex.getCause();

		while (cause != null) {
			if (exceptionType.isAssignableFrom(cause.getClass())) {
				return true;
			}

			cause = cause.getCause();
		}

		return false;
	}
}
