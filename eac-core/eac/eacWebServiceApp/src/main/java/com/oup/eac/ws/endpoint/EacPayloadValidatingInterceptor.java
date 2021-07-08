package com.oup.eac.ws.endpoint;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.xml.sax.SAXParseException;

public class EacPayloadValidatingInterceptor extends PayloadValidatingInterceptor {

    /**
     * Template method that is called when the response message contains validation errors. Default implementation logs all errors, and returns
     * <code>false</code>, i.e. do not cot continue to process the respone interceptor chain.
     * 
     * @param messageContext
     *            the message context
     * @param errors
     *            the validation errors
     * @return <code>true</code> to continue the reponse interceptor chain, <code>false</code> (the default) otherwise
     */
    protected boolean handleResponseValidationErrors(MessageContext messageContext, SAXParseException[] errors) {
        for (SAXParseException error : errors) {
            String xml = getResponseXML(messageContext.getResponse());
            String msg = String.format("XML validation error [%s] on response : %n%s",error.getMessage(),xml);
            logger.error(msg);
        }
        if (messageContext.getResponse() instanceof SoapMessage) {
            SoapMessage response = (SoapMessage) messageContext.getResponse();
            SoapBody body = response.getSoapBody();
            SoapFault fault = body.addClientOrSenderFault(getFaultStringOrReason(), getFaultStringOrReasonLocale());
            if (getAddValidationErrorDetail()) {
                SoapFaultDetail detail = fault.addFaultDetail();
                for (SAXParseException error : errors) {
                    SoapFaultDetailElement detailElement = detail.addFaultDetailElement(getDetailElementName());
                    detailElement.addText(error.getMessage());
                }
            }
        }
        return false;
    }

    private String getResponseXML(WebServiceMessage wsMessage) {
        String result = "";
        if (wsMessage == null || wsMessage.getPayloadSource() == null) {
            return result;
        }
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(wsMessage.getPayloadSource(), new StreamResult(writer));
            result = writer.toString();

        } catch (Exception ex) {
            logger.warn("problem converting response to xml", ex);
        }
        return result;
    }

}
