package com.oup.eac.data.util;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class TestDatabaseInitializer {

    private static final Object DB_LOCK = new Object();
    private static final boolean SHOW_DB_CONSOLE = false;
    private static boolean DB_INITIALISED = false;

    private BasicDataSource dataSource;
    private Resource[] scripts;

    public TestDatabaseInitializer(BasicDataSource dataSource, Resource scripts[]) {
        this.dataSource = dataSource;
        this.scripts = scripts;

        synchronized (DB_LOCK) {
            init();
        }
    }

    private void init() {
        if (DB_INITIALISED) {
            return;
        }
        Connection con = null;
        try {
            con = dataSource.getConnection();
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.setScripts(scripts);
            populator.populate(con);
            con.commit();
            DB_INITIALISED = true;
            
            if(SHOW_DB_CONSOLE){
                String[] args = new String[] {"--url",  dataSource.getUrl(), "--noexit" };
                org.hsqldb.util.DatabaseManagerSwing.main(args);
            }

        } catch (Exception ex) {
            try {
                con.rollback();
            } catch (Exception rbEx) {
                rbEx.printStackTrace();
            }
            throw new RuntimeException("Failed to initialised Test HSQL database", ex);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public DataSource getDataSource() {
        DataSource result = this.dataSource;
        return result;
    }

}