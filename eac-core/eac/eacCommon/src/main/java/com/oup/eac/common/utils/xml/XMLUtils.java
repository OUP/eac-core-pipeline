package com.oup.eac.common.utils.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtils {
	
	private static final Logger LOG = Logger.getLogger(XMLUtils.class);
	
    /**
     * Pretty print the xml.
     * 
     * @param xml
     *            The xml to pretty print
     * @return Pretty printed xml
     */
    public static String prettyPrint(final SOAPMessage soapMessage) {
    	if(soapMessage == null) {
    		return null;
    	}
    	try {
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	soapMessage.writeTo(baos);
        	return prettyPrint(baos.toString("UTF-8"));
    	} catch (Exception e) {
			return null;
		}
    }	
	
    /**
     * Pretty print the xml.
     * 
     * @param xml
     *            The xml to pretty print
     * @return Pretty printed xml
     */
    public static String prettyPrint(final String xml) {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(parseXmlFile(xml));
            transformer.transform(source, result);
            return result.getWriter().toString();

        } catch (TransformerConfigurationException e) {
            LOG.error(e);
        } catch (TransformerFactoryConfigurationError e) {
            LOG.error(e);
        } catch (TransformerException e) {
            LOG.error(e);
        }
        return xml;
    }

    /**
     * The xml to be parsed.
     * 
     * @param in
     *            The xml
     * @return A document from the xml
     */
    private static Document parseXmlFile(final String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            Document doc = db.parse(is);
            obfuscatePasswords(doc);
            return doc;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Obfuscate Passwords
     * 
     * @param doc XML document
     */
    private static void obfuscatePasswords(Document doc) {
        NodeList nList = doc.getElementsByTagName("password");
        replaceNodeValues(nList);
        nList = doc.getElementsByTagName("wsse:Password");
        replaceNodeValues(nList);
    }
    
    /**
     * Replace node values with stars
     * 
     * @param nList
     */
    private static void replaceNodeValues(NodeList nList) {
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                element.setTextContent("****");
            }
        }            
    }
}
