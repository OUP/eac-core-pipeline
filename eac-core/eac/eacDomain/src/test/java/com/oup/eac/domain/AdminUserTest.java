package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

public class AdminUserTest {

   /* @Test
    public void testGetDivisions() {
        AdminUser admin = new AdminUser();
        List<Division> divisions1 = admin.getDivisions();
        Assert.assertNotNull(divisions1);
        Assert.assertTrue(divisions1.isEmpty());
        Set<DivisionAdminUser> divisionAdminUsers = getDivisionAdminUsers(admin, getDivisions());
        Assert.assertEquals(26,divisionAdminUsers.size());
        admin.setDivisionAdminUsers(divisionAdminUsers);
        List<Division> divisions2 = admin.getDivisions();
        Assert.assertEquals(26,divisions2.size());
        Assert.assertNotNull(divisions2);
        Assert.assertTrue(!divisions2.isEmpty());
        Iterator<Division> iter = divisions2.iterator();
        for (int i = 1; i <= 26; i++) {
            char expectedDivsionTypeChar = (char) ('a' + i - 1);
            Assert.assertTrue(iter.hasNext());
            Division div = iter.next();
            String expectedDivisionType = "" + expectedDivsionTypeChar;
            Assert.assertEquals(expectedDivisionType, div.getDivisionType());
        }
        Assert.assertFalse(iter.hasNext());
    }

    private Set<DivisionAdminUser> getDivisionAdminUsers(AdminUser adminUser, Set<Division> divisions) {
        HashSet<DivisionAdminUser> result = new HashSet<DivisionAdminUser>();
        for (Division division : divisions) {
            DivisionAdminUser dau = new DivisionAdminUser(adminUser, division);
            result.add(dau);
        }
        Assert.assertEquals(divisions.size(), result.size());
        return result;
    }

    private Set<Division> getDivisions() {
        HashSet<Division> result = new HashSet<Division>();
        for (char c = 'z'; c >= 'a'; c--) {
            String name = "" + c;
            Division division = new Division(name);
            result.add(division);
        }
        Assert.assertEquals(26, result.size());
        return result;
    }*/
}
