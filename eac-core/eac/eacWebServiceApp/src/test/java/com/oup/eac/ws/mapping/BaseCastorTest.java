package com.oup.eac.ws.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.castor.CastorMarshaller;
import org.w3c.dom.Node;

import com.oup.eac.ws.util.XmlDiff;

public abstract class BaseCastorTest {

    private static final Logger LOG = Logger.getLogger(BaseCastorTest.class);
    
    protected String getXml(CastorMarshaller castor, Object response) throws Exception {
        DOMResult domRes= new DOMResult();        
        castor.marshal(response, domRes);
        String result = getPrettyXml(domRes);
        return result;
    }

    protected String getExpectedXml(String resourceName) throws IOException {
        Resource res = new ClassPathResource(resourceName);
        String result = IOUtils.toString(res.getInputStream());
        return result;
    }

    protected void checkEquals(String expectedXml, String actualXml) throws Exception {
        XmlDiff diff = new XmlDiff();
        List<String> diffs = new ArrayList<String>();
        diff.diff(expectedXml, actualXml, diffs);
        String differences = "differences : " + diffs.toString();
        Assert.assertEquals(differences, 0, diffs.size());
    }

    
    protected String getPrettyXml(DOMResult domResult) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(baos);
        Node doc = domResult.getNode();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        //initialize StreamResult with File object to save to file
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        String prettyXml = baos.toString();
        LOG.debug(prettyXml);
        return prettyXml;
    }

}
