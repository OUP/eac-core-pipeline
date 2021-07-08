package com.oup.eac.data.util;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

/**
 * HibernateSchemaExporter is designed to export the current hibernate schema to the configured output file. The export method expects to find a bean factory
 * using &sessionFactory.
 * 
 * The bean self initialises and runs export.
 * 
 * @see InitializingBean
 * 
 * @author packardi
 * 
 */
public class HibernateSchemaExporter implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private boolean createOnly;

    private String outputFile;

    /**
     * Set the output file.
     * 
     * @param outputFile
     *            where the schema exporter should output the schema
     */
    public final void setOutputFile(final String outputFile) {
        this.outputFile = outputFile;
    }

    /**
     * Get the output file.
     * 
     * @return the output file
     */
    public final String getOutputFile() {
        return outputFile;
    }

    /**
     * Export schema to location.
     * 
     * @throws Exception
     *             the exception
     */
    public final void export() throws Exception {
        AnnotationSessionFactoryBean sfb = (AnnotationSessionFactoryBean) applicationContext.getBean("&sessionFactory");

        Configuration config = sfb.getConfiguration();

        SchemaExport export = new SchemaExport(config);
        export.setOutputFile(outputFile);
        export.setDelimiter(";");
        export.execute(false, false, false, createOnly);
    }

    /**
     * @param applicationContext
     *            the application context for obtaining required beans.
     */
    @Override
    public final void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Set create only if the schema export should not include drop statements.
     * 
     * @param createOnly
     *            true if the exported schema will not include drop statements
     */
    public final void setCreateOnly(final boolean createOnly) {
        this.createOnly = createOnly;
    }

    @Override
    public final void afterPropertiesSet() throws Exception {
        export();
    }
}
