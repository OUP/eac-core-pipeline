package com.oup.eac.data.util;

import java.sql.Types;

/**
 * @author harlandd
 * 
 * Hibernate does not seem to have implemented paging correctly for MS SQL Server. The use TOP 
 * which will return 100 then 200 then 300 etc rows up the max number of rows that the query 
 * would return give no pagination.
 * 
 * The query below
 * <code>select distinct c.id as id1_0_, c.email_address as ea1_0_ 
 *       from customer c 
 *       where c.email_address like '%hotmail%' 
 *       order by c.id</code>
 * 
 * has to be transformed into
 * 
 * <code>select id1_0_, ea1_0_ from 
 *       (select distinct c.id as id1_0_, c.email_address as ea1_0_, 
 *       Row_Number() over (order by c.id) as RowIndex 
 *       from customer c where c.email_address like '%hotmail%') 
 *       as Sub Where Sub.RowIndex > 0 and Sub.RowIndex <= 10</code>
 *       
 */
public class SQLServer2008NativeDialect extends SQLServer2008Dialect {

    /**
     * Create custom sql server dialect that supports unicode charachters. Hibernate schema export will convert datatypes to unicode datatypes when this dialect
     * is provided.
     */
    public SQLServer2008NativeDialect() {
        registerColumnType(Types.CHAR, "nchar(1)");
        registerColumnType(Types.VARCHAR, "nvarchar($l)");
        registerColumnType(Types.LONGVARCHAR, "nvarchar($l)");
        registerColumnType(Types.CLOB, "ntext");
        registerHibernateType(Types.NVARCHAR, 4000, "string");
        registerColumnType(Types.BIT, "bit");
    }
    
}