package com.oup.eac.advice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.xml.validation.XmlValidator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import com.oup.eac.common.advice.EacPointcuts;

/**
 * This advice is required to work around a bug in the eacWebServiceApp xml processing stack.
 * This is a problem where xml dom structures creating by castor that contain 'xsi:type' attributes do not pass xml validation.
 * 
 * <p>
 * <b><i>
 * cvc-attribute.3: The value 'ns14:UsageLicenceInfo' of attribute 'xsi:type' on element 'ns14:info' is not valid with respect to its type, 'QName'.</spring-ws:ValidationError><spring-ws:ValidationError xmlns:spring-ws="http://springframework.org/spring-ws">cvc-complex-type.2.4.d: Invalid content was found starting with element 'ns14:firstUsedDate'. No child element is expected at this point.
 * </i>
 * </b>
 * </p>
 * 
 * The 'xsi:type' attribute is introduced when there is a type hierarchy in the xsd schema.
 * The validation fails even though the xml is valid.
 * When the dom structure is serialized, un-serialized and re-validated the validation passes.
 * 
 * This advice is applied to validators created by the Spring XmlValidatorFactory.
 * 
 * @author David Hay
 *
 * @see com.oup.eac.common.advice.EacPointcuts#serviceLayerMethods()
 * @see org.springframework.xml.validation.XmlValidatorFactory#createValidator(org.springframework.core.io.Resource[], String)
 * @see com.oup.eac.ws.v2.RedeemActivationCodesAuthTest
 * @see com.oup.eac.ws.v2.GetUserEntitlementsAuthTest
 */

@Aspect
public class XmlValidationAdvice {

    public static final Logger LOG = Logger.getLogger(XmlValidationAdvice.class);

    public XmlValidationAdvice() {
    }

    @Pointcut(EacPointcuts.XML_MESSAGE_VALIDATION)
    public void xmlValidation() {
    }

    @Around("xmlValidation()")
    public Object performValidation(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        result = pjp.proceed();
        if (result instanceof XmlValidator) {
            XmlValidator xmlValidator = (XmlValidator) result;
            XmlValidator eacValidator = getEacValidator(xmlValidator);
            result = eacValidator;
        }
        return result;
    }

    /**
     * get a Validator which wraps the original with 'if fail then serialized, un-serialized and validate again logic'. 
     * @param xmlValidator the validator to decorate
     * @return a decorated validator
     */
    private XmlValidator getEacValidator(final XmlValidator xmlValidator) {
        XmlValidator result = new XmlValidator() {

            @Override
            public SAXParseException[] validate(Source source) throws IOException {
                SAXParseException[] initialResult = xmlValidator.validate(source);
                if (initialResult == null || initialResult.length == 0) {
                    return initialResult;
                }
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    StreamResult streamResult = new StreamResult(baos);
                    TransformerFactory factory = TransformerFactory.newInstance();
                    Transformer transformer = factory.newTransformer();// identify transform
                    transformer.transform(source, streamResult);
                    byte[] data = baos.toByteArray();
                    
                    if(LOG.isTraceEnabled()){
                        String xml = new String(data);
                        LOG.trace("Re-Validated XML is " + xml);
                    }
                    
                    ByteArrayInputStream bais = new ByteArrayInputStream(data);
                    Source source2 = new StreamSource(bais);
                    SAXParseException[] result = xmlValidator.validate(source2);
                    
                    if(LOG.isDebugEnabled()){
                        InputSource isource = new InputSource(new ByteArrayInputStream(data));
                        XPathFactory fact = XPathFactory.newInstance();
                        XPath path = fact.newXPath();        
                        String topLevelElementName = (String)path.evaluate("name(/*)", isource, XPathConstants.STRING);
                        String msg = String.format("XML Re-Validation for Document with root [%s] before [%d] after[%d]",topLevelElementName,initialResult.length, result.length);
                        LOG.debug(msg);
                    }
                    return result;
                } catch (Exception ex) {
                    LOG.warn("Problem in XML re-validation ",ex);
                    return initialResult;
                }
            }
        };
        return result;
    }
    
    

}
