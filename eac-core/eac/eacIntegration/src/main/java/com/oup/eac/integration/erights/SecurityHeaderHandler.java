package com.oup.eac.integration.erights;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

import com.oup.eac.common.utils.EACSettings;

/**
 * A <@code>SOAPHandler<@code> that will attach a WS-Security header to an out-bound soap message.
 * 
 * @author packardi
 * @author harlandd
 * 
 */
public class SecurityHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    /**
     * The logger.
     */
    private static final Logger LOG = Logger.getLogger(SecurityHeaderHandler.class);

    /**
     * Handle the message. Checks out-bound message property and will apply WS-Security header if required.
     * 
     * @param smc
     *            The message context
     * 
     * @return true if the handler chain should continue
     */
    @Override
    public final boolean handleMessage(final SOAPMessageContext smc) {

        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {

            LOG.debug("Applying WS-Security header to outbound message");
            try {

                SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();
                SOAPHeader header = envelope.addHeader();

                SOAPElement security = header.addChildElement("Security", "wsse",
                        "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");

                SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
                usernameToken.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");

                SOAPElement username = usernameToken.addChildElement("Username", "wsse");
                username.addTextNode(EACSettings.getProperty(EACSettings.WS_SECURITY_USERNAME));

                SOAPElement password = usernameToken.addChildElement("Password", "wsse");
                password.setAttribute("Type", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");

                //password.addTextNode(EACSettings.getProperty(EACSettings.WS_SECURITY_PASSWORD));
                password.addTextNode(EACSettings.getDecryptedProperty(EACSettings.WS_SECURITY_PASSWORD));
            } catch (Exception e) {
                LOG.error("Error applying WS-Security header to outbound message", e);
            }

        }

        return outboundProperty.booleanValue();
    }

    /**
     * Return a list of headers to be added to the message.
     * 
     * @return the headers
     */
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
}
