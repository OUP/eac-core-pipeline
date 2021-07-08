package com.oup.eac.integration.erights;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

import com.oup.eac.common.utils.xml.XMLUtils;

public class LoggingSOAPHandler implements SOAPHandler<SOAPMessageContext> {

    /**
     * The logger.
     */
    private static final Logger LOG = Logger.getLogger(LoggingSOAPHandler.class);

    /**
     * Handle the message. Checks out-bound message property and will apply WS-Security header if required.
     * 
     * @param smc
     *            The SoapMessageContext
     * @return true if the handler chain should continue
     */
    @Override
    public final boolean handleMessage(final SOAPMessageContext smc) {

        logMessage(smc);

        return true;
    }

    /**
     * Return a list of headers to be added to the message.
     * 
     * @return the headers
     */
    @Override
    public final Set<QName> getHeaders() {
        return null;
    }

    /**
     * Handle fault if one is encountered.
     * 
     * @param smc
     *            The message context
     * @return True if the handler chain should continue
     */
    @Override
    public final boolean handleFault(final SOAPMessageContext smc) {
        logMessage(smc);
        return true;
    }

    /**
     * Close the handler.
     * 
     * @param context
     *            The message context
     */
    @Override
    public void close(final MessageContext context) {
    }

    /**
     * Log the message.
     * 
     * @param smc
     *            The soap message context to retrieve the message from
     */
    private void logMessage(final SOAPMessageContext smc) {
        if (LOG.isDebugEnabled()) {
            try {
                // Print out the SOAP message to LOG
                LOG.debug(XMLUtils.prettyPrint(smc.getMessage()));

            } catch (Exception e) {
                LOG.error("Error logging soap message", e);
            }
        }
    }
}
