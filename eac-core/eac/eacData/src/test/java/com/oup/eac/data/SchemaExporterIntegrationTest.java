/*package com.oup.eac.data;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.oup.eac.data.util.HibernateSchemaExporter;

@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class SchemaExporterIntegrationTest extends AbstractJUnit4SpringContextTests  {

    @Autowired
    private HibernateSchemaExporter hibernateSchemaExporter;

    *//**
     * Test schema is exported.
     * 
     * @throws FileNotFoundException
     *             if exported file is not found
     *//*
    @Test
    public final void testSchemaExport() throws FileNotFoundException {
        String outputFile = hibernateSchemaExporter.getOutputFile();

        File schema = new File(outputFile);
        
        assertTrue("Check the schema file exists", schema.exists());
    }

    *//**
     * @param hibernateSchemaExporter
     *            the hibernateSchemaExporter to set
     *//*
    public final void setHibernateSchemaExporter(final HibernateSchemaExporter hibernateSchemaExporter) {
        this.hibernateSchemaExporter = hibernateSchemaExporter;
    }

}
*/