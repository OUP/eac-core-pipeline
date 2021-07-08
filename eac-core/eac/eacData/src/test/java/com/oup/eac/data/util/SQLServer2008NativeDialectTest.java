package com.oup.eac.data.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class SQLServer2008NativeDialectTest {
    
    private static final String query = "select registrati0_.id as id01, registrati0_.name as name02, registrati0_.description as description03 from registration registrati0_ order by registrati0_.id";
    private static final String distinctQuery = "select distinct registrati0_.id as id01, registrati0_.name as name02, registrati0_.description as description03 from registration registrati0_ order by registrati0_.id";
    private static final String leftJoinQuery = "select distinct registrati0_.id as id01, registrati0_.name as name02, registrati0_.description as description03 from registration registrati0_ left join user u on registrati0_.user_id = u.id order by registrati0_.id";
    private static final String orderbyQuery = "select registrati0_.id as id01, registrati0_.name as name02, registrati0_.description as description03 from registration registrati0_";

    @Test
    public void testQuery() {
        SQLServer2008NativeDialect sqlServer2008NativeDialect = new SQLServer2008NativeDialect();
        String limitQuery = sqlServer2008NativeDialect.getLimitString(query, 0, 10);
        Assert.assertNotNull(limitQuery);
        Assert.assertEquals("select id01, name02, description03 from (select registrati0_.id as id01, registrati0_.name as name02, registrati0_.description as description03, Row_Number() over (order by registrati0_.id) as RowIndex from registration registrati0_) " +
        		            "as Sub Where Sub.RowIndex > 0 and Sub.RowIndex <= 10", limitQuery);
    }
    
    @Test
    public void testDistinctQuery() {
        SQLServer2008NativeDialect sqlServer2008NativeDialect = new SQLServer2008NativeDialect();
        String limitQuery = sqlServer2008NativeDialect.getLimitString(distinctQuery, 0, 10);
        Assert.assertNotNull(limitQuery);
        Assert.assertEquals("select id01, name02, description03 from (select distinct registrati0_.id as id01, registrati0_.name as name02, registrati0_.description as description03, Row_Number() over (order by registrati0_.id) as RowIndex from registration registrati0_) " +
                            "as Sub Where Sub.RowIndex > 0 and Sub.RowIndex <= 10", limitQuery);
    }
    
    @Test
    public void testLeftJoinQuery() {
        SQLServer2008NativeDialect sqlServer2008NativeDialect = new SQLServer2008NativeDialect();
        try {
            sqlServer2008NativeDialect.getLimitString(leftJoinQuery, 0, 10);
            Assert.fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Paginiated queries can not contain left joins. Please see left join pagination at https://forum.hibernate.org", e.getMessage());
        }
    }
    
    @Test
    public void testMissingOrderByQuery() {
        SQLServer2008NativeDialect sqlServer2008NativeDialect = new SQLServer2008NativeDialect();
        try {
            sqlServer2008NativeDialect.getLimitString(orderbyQuery, 0, 10);
            Assert.fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Query must have an order by statement", e.getMessage());
        }
    }
}
