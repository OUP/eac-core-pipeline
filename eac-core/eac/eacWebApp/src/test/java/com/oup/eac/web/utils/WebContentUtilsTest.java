package com.oup.eac.web.utils;

import org.junit.Assert;
import org.junit.Test;

import com.oup.eac.domain.Customer;
import com.oup.eac.web.utils.impl.WebContentUtilsImpl;

public class WebContentUtilsTest {

    private Customer getCustomer(String first, String last, String username) {
        Customer customer = new Customer();
        customer.setFirstName(first);
        customer.setFamilyName(last);
        customer.setUsername(username);
        return customer;
    }

    @Test
    public void testFirstLast() {
        WebContentUtils utils = new WebContentUtilsImpl(false, 10);
        String name = utils.getCustomerName(getCustomer("first", "last", "blah"));
        Assert.assertEquals("first last", name);
    }

    @Test
    public void testFirstOnly1() {
        WebContentUtils utils = new WebContentUtilsImpl(false, 10);
        String name = utils.getCustomerName(getCustomer("first", null, "blah"));
        Assert.assertEquals("first", name);
    }

    @Test
    public void testFirstOnly2() {
        WebContentUtils utils = new WebContentUtilsImpl(false, 10);
        String name = utils.getCustomerName(getCustomer("first", "     ", "blah"));
        Assert.assertEquals("first", name);
    }

    @Test
    public void testLastOnly1() {
        WebContentUtils utils = new WebContentUtilsImpl(false, 10);
        String name = utils.getCustomerName(getCustomer(null, "last", "blah"));
        Assert.assertEquals("last", name);
    }

    @Test
    public void testLastOnly2() {
        WebContentUtils utils = new WebContentUtilsImpl(false, 10);
        String name = utils.getCustomerName(getCustomer("    ", "last", "blah"));
        Assert.assertEquals("last", name);
    }

    @Test
    public void testEmpty1() {
        WebContentUtils utils = new WebContentUtilsImpl(false, 10);
        String name = utils.getCustomerName(getCustomer("    ", "    ", "username"));
        Assert.assertEquals("", name);
    }

    @Test
    public void testEmpty2() {
        WebContentUtils utils = new WebContentUtilsImpl(true, 10);
        String name = utils.getCustomerName(getCustomer("    ", "    ", "username123"));
        Assert.assertEquals("", name);
    }

    @Test
    public void oupUATname1() {
        WebContentUtils utils = new WebContentUtilsImpl(true, 10);
        String name = utils.getCustomerName(getCustomer("    ", "    ", "username12"));
        Assert.assertEquals("username12", name);
    }

    @Test
    public void oupUATname2() {
        WebContentUtils utils = new WebContentUtilsImpl(true, -1);
        String name = utils.getCustomerName(getCustomer("    ", "    ", "username123"));
        Assert.assertEquals("username123", name);
    }

    @Test
    public void testNullCustomer() {
        WebContentUtils utils = new WebContentUtilsImpl(true, -1);
        String name = utils.getCustomerName(null);
        Assert.assertNull(name);
    }
}
