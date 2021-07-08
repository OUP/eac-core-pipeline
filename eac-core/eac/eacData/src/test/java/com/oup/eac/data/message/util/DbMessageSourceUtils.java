package com.oup.eac.data.message.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Used to help test the periodic reloading of localized messages from a HSQL database.
 * 
 * @author David Hay
 *
 */
public class DbMessageSourceUtils {

    private static final Logger LOG = Logger.getLogger(DbMessageSourceUtils.class);
    
    private final JdbcTemplate template;

    private final DataSource dataSource;

    public DbMessageSourceUtils(DataSource ds) {
        BasicDataSource bds = (BasicDataSource) ds;
        bds.setDefaultAutoCommit(true);
        this.template = new JdbcTemplate(ds);
        this.dataSource = ds;
    }
    
    public void persist(String baseName, Locale loc, String key, String message) {
        String language = loc.getLanguage();
        String country = loc.getCountry();
        String variant = loc.getVariant();
        int count = template.queryForInt("select count(*) from message " +
        		" where baseName=? " + 
        		" and language = ?" + 
        		" and country=? " + 
        		" and variant = ?" +
                " and key = ?" 
                , baseName, language, country, variant, key);
        if (count == 1) {
            update(baseName, language, country, variant, key, message);
        } else {
            insert(baseName, language, country, variant, key, message);
        }
    }
    
    private void update(String baseName, String language, String country, String variant, String key, String message) {
        template.update("update message set " +
        		"message = ? " +        		
        		" where basename = ?" +
        		" and language = ?" + 
        		" and country=? " + 
        		" and variant = ?" + 
        		" and key = ?", message, baseName, language, country, variant, key);
    }

    public void delete(String baseName, Locale loc, String key, String message) {
        String language = loc.getLanguage();
        String country = loc.getCountry();
        String variant = loc.getVariant();
        delete(baseName, language, country, variant, key, message);
    }

    private void delete(String baseName, String language, String country, String variant, String key, String message) {
        template.update("delete from message where where " + " language = ?" + " and country=? " + " and variant = ?" + " and key = ?" + " and message = ?",
                language, country, variant, key, message);
    }

    private void insert(String baseName, String language, String country, String variant, String key, String message) {

        template.update("insert into message(id,basename,language,country,variant,key,message,obj_version) values ('',?,?,?,?,?,?,0)", baseName, language, country, variant, key,
                message);

    }

    public void executeSQL(String sql, final int expected) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = dataSource.getConnection();
            ps = con.prepareStatement(sql);
            res = ps.executeQuery();

            ResultSetMetaData rsmd = res.getMetaData();
            int numColumns = rsmd.getColumnCount();

            Map<Integer, String> colMap = new HashMap<Integer, String>();
            // Get the column names; column indices start from 1
            for (int i = 1; i < numColumns + 1; i++) {
                String columnName = rsmd.getColumnName(i);
                colMap.put(i, columnName);
            }
            LOG.debug(String.format("%nSQL IS %s %n", sql));
            int counter = 0;
            while (res.next()) {
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("[%2d]", ++counter));
                for (int i = 1; i < numColumns + 1; i++) {
                    String value = res.getString(i);
                    sb.append(String.format(" %s[%s]", colMap.get(i), value));
                }
                LOG.debug(sb.toString());
            }
            LOG.debug(String.format("The number of rows read is [%2d]%n", counter));
            Assert.assertEquals(expected, counter);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Assert.fail("unexpected exception");
        } finally {
            close(res);
            close(ps);
            close(con);
        }
    }
    
    public static void close(Connection con) {
        try {
            con.close();
        } catch (Exception ex) {
        }
    }

    public static void close(ResultSet res) {
        try {
            res.close();
        } catch (Exception ex) {
        }
    }

    public static void close(PreparedStatement ps) {
        try {
            ps.close();
        } catch (Exception ex) {
        }
    }
}
