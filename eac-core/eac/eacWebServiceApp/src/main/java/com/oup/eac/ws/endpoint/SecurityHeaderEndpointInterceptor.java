package com.oup.eac.ws.endpoint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.interceptor.EndpointInterceptorAdapter;
import org.springframework.ws.soap.SoapElement;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.Node;

/**
 * When using org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor for WebService authentication, 
 * We must specify whether a 'nonce security header' is required or not. 
 * If it's not required we get an error if one is supplied. 
 * To be more flexible, this interceptor removes the unwanted 'nonce security header' to prevent this kind of error.
 * 
 * @author David Hay
 * 
 */
public class SecurityHeaderEndpointInterceptor extends EndpointInterceptorAdapter {

    private static final String UNKNOWN_USERNAME = "N/A";
    
    private static final String WSSE_PREFIX = "wsse";
    private static final String WSSE_NS_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    private static final String WSU_PREFIX = "wsu";
    private static final String WSU_NS_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";

    private static final QName SEC_HEADER_QNAME = new QName(WSSE_NS_URI, "Security");

    private static final Logger LOG = Logger.getLogger(SecurityHeaderEndpointInterceptor.class);

    private final Transformer transformerIdentity;// this is not thread safe
    private XPathExpression xexprSecurity;// this is not thread safe
    private XPathExpression xexprNonce;// this is not thread safe
    private XPathExpression xexprUsernameToken;// this is not thread safe
    private XPathExpression xexprCreated;// this is not thread safe
    private XPathExpression xexprUsername;// this is not thread safe
    
    public SecurityHeaderEndpointInterceptor() throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        this.transformerIdentity = tf.newTransformer();

        NamespaceContext nsContext = new NamespaceContext() {
            @Override
            public String getNamespaceURI(String prefix) {
                if (WSSE_PREFIX.equals(prefix)) {
                    return WSSE_NS_URI;
                } else if (WSU_PREFIX.equals(prefix)) {
                    return WSU_NS_URI;
                } else {
                    return null;
                }
            }

            @Override
            public Iterator<?> getPrefixes(String namespaceURI) {
                return null;
            }

            @Override
            public String getPrefix(String namespaceURI) {
                return null;
            }
        };
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        xpath.setNamespaceContext(nsContext);
        this.xexprSecurity = xpath.compile("./wsse:Security");
        this.xexprNonce = xpath.compile("./wsse:Nonce");
        this.xexprCreated = xpath.compile("./wsu:Created");
        this.xexprUsernameToken = xpath.compile("./wsse:UsernameToken");
        this.xexprUsername = xpath.compile("./wsse:Username/text()");
    }

    @Override
    public boolean handleRequest(MessageContext mc, Object endpoint) throws Exception {
        WebServiceMessage request = mc.getRequest();
        if (request instanceof SaajSoapMessage) {
            SaajSoapMessage soap = (SaajSoapMessage) request;
            SoapHeader header = soap.getSoapHeader();
            if(header != null){
                removeUsingXpath(header);
            }
        }
        return true;
    }

    /*
     * <wsse:Security soapenv:mustUnderstand="1" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
     *   <wsse:UsernameToken wsu:Id="UsernameToken-1" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
     *     <wsse:Username>admin</wsse:Username>
     *     <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">Passw0rd</wsse:Password> <wsse:Nonce EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary">fNupZfuukpksm9d3uOnJbw==</wsse:Nonce>
     *     <wsu:Created>2011-11-03T16:00:39.461Z</wsu:Created>
     *   </wsse:UsernameToken>
     * </wsse:Security>
     */
    private void removeUsingXpath(SoapHeader header) throws Exception {
        Node node = getDomNode(header);
        Node secHeader = getXpathNode(node, this.xexprSecurity);
        if (secHeader == null) {
            return;
        }
        Node usernameToken = getXpathNode(secHeader, this.xexprUsernameToken);
        if (usernameToken == null) {
            return;
        }
        List<String> headers = new ArrayList<String>();
        filterNode(usernameToken,this.xexprNonce, headers);
        filterNode(usernameToken,this.xexprCreated, headers);
        
        if (headers.size() > 0) {
            DOMSource source = new DOMSource(secHeader);
            identity(source, header.getResult());// this adds a second 'filtered' security header
            header.removeHeaderElement(SEC_HEADER_QNAME);// we still have to remove the first 'un-filtered' security header.
            if(LOG.isDebugEnabled()){
                String username = getXpathString(usernameToken, this.xexprUsername);
                if(username == null){
                    username = UNKNOWN_USERNAME;
                }                
                String msg = String.format("Filtered out security headers %s for '%s'",headers,username);
                LOG.debug(msg);
            }
        }
    }

    private Node getXpathNode(Node parent, XPathExpression xexpr) throws Exception {
        synchronized (xexpr) {
            return (Node) xexpr.evaluate( parent, XPathConstants.NODE);
        }
    }
    
    private String getXpathString(Node parent, XPathExpression xpathExpr) throws Exception {
        synchronized (xpathExpr) {
            return (String) xpathExpr.evaluate(parent, XPathConstants.STRING);
        }
    }
    
    private void filterNode(Node parent, XPathExpression xexpr, List<String> headers) throws Exception {
        Node node = getXpathNode(parent, xexpr);
        if (node != null) {
            parent.removeChild(node);            
            QName qn = new QName(node.getLocalName(), node.getNamespaceURI());
            headers.add(qn.toString());
        }
    }

    private void identity(Source source, Result result) throws Exception {
        synchronized (this.transformerIdentity) {
            this.transformerIdentity.transform(source, result);
        }
    }

    private Node getDomNode(SoapElement soapElem) throws Exception {
        Node result;
        Source source = soapElem.getSource();
        if (source instanceof DOMSource) {
            DOMSource domSource = (DOMSource) source;
            result = domSource.getNode();
        } else {
            DOMResult domResult = new DOMResult();
            identity(source, domResult);
            result = domResult.getNode();
        }
        return result;
    }

}
