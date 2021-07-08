package com.oup.eac.ws.filter;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.AbstractLoggingInterceptor;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.server.SoapEndpointInterceptor;
import org.springframework.xml.transform.TransformerObjectSupport;


/**
 * The Class AbstractRequestLoggingFilter.
 */
public class LoggingSOAPHandler extends TransformerObjectSupport implements SoapEndpointInterceptor {

	private static final Logger logger = Logger.getLogger(AbstractLoggingInterceptor.class);
	private boolean logFault = true;
	
	private boolean logRequest = true;

	private boolean logResponse = true;

	/** Indicates whether the request should be logged. Default is {@code true}. */
	public final void setLogRequest(boolean logRequest) {
		this.logRequest = logRequest;
	}

	/** Indicates whether the response should be logged. Default is {@code true}. */
	public final void setLogResponse(boolean logResponse) {
		this.logResponse = logResponse;
	}
	
	public final void setLogFault(boolean logFault) {
		this.logFault = logFault;
	}
	
	@Override
	public boolean handleResponse(MessageContext messageContext, Object endpoint)
			throws Exception {
		if (logResponse && isLogEnabled()) {
			logMessageSource("Response: ", getSource(messageContext.getResponse()));
		}
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext, Object endpoint)
			throws Exception {
		if (logFault && logger.isDebugEnabled()) {
			logMessageSource("Fault: ", getSource(messageContext.getResponse()));
		}
		return true;
	}



	@Override
	public boolean understands(SoapHeaderElement header) {

		return false;
	}
	
	protected Source getSource(WebServiceMessage message) {
		if (message instanceof SoapMessage) {
			SoapMessage soapMessage = (SoapMessage) message;
			return soapMessage.getEnvelope().getSource();
		}
		else {
			return null;
		}
	}

	@Override
	public boolean handleRequest(MessageContext messageContext, Object endpoint)
			throws Exception {
		if (logRequest && isLogEnabled()) {
			logMessageSource("Request: ", getSource(messageContext.getRequest()));
		}
		return true;
	}

	private Transformer createNonIndentingTransformer() throws TransformerConfigurationException {
		Transformer transformer = createTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		return transformer;
	}

	/**
	 * Logs the given {@link Source source} to the {@link #logger}, using the message as a prefix.
	 *
	 * <p>By default, this message creates a string representation of the given source, and delegates to {@link
	 * #logMessage(String)}.
	 *
	 * @param logMessage the log message
	 * @param source	 the source to be logged
	 * @throws TransformerException in case of errors
	 */
	protected void logMessageSource(String logMessage, Source source) throws TransformerException {
		if (source != null) {
			
			Transformer transformer = createNonIndentingTransformer();
			StringWriter writer = new StringWriter();			
			transformer.transform(source, new StreamResult(writer));
			String message = logMessage + writer.toString();
			int startIdx = message.indexOf("<aces:password>");
			int startIdx1 = message.indexOf("<password>");
			if (startIdx != -1) {
				int endIdx = message.indexOf ("</aces:password>",startIdx);
				message = message.substring(0, startIdx+15) + "*****" + message.substring(endIdx);
			} else if (startIdx1 != -1) {
				int endIdx = message.indexOf ("</password>",startIdx1);
				message = message.substring(0, startIdx1+10) + "*****" + message.substring(endIdx);
			}
			logMessage(message);
		}
	}
	
	protected boolean isLogEnabled() {
		return logger.isDebugEnabled();
	}
	
	protected void logMessage(String message) {
		logger.debug(message);
	}
}
