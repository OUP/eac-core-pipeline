package com.oup.eac.common.utils;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.oup.eac.common.RuntimeContext;

public class RuntimeContextImplTest {

	private RuntimeContextImpl runtimeContext;
	
	private Properties properties = new Properties();
	
	@Before
	public void setUp() {
		
		properties.setProperty("test.property.1", "test.property.1.value");
		properties.setProperty("test.property.2", "2");
		properties.setProperty("test.property.3", "true");
		properties.setProperty("test.property.4", "test.property.4.value");
		properties.setProperty("test.property.5", "test.property.5.value");

		System.setProperty("test.property.5", "test.property.5.value.overriden.by.system");
	}
	
	@Test
	public void testPropertiesWithSystemOverride() {
		RuntimeContext context = new RuntimeContextImpl(properties, true);
		
		assertEquals("Check correct number of properties returned", 5, context.getProperties().size());
		
		assertEquals("Check property 1 not overridden by system", "test.property.1.value", context.getProperty("test.property.1"));
		assertEquals("Check property 5 overriden by system", "test.property.5.value.overriden.by.system", context.getProperty("test.property.5"));
	}
	
	@Test
	public void testPropertiesWithoutSystemOverride() {
		RuntimeContext context = new RuntimeContextImpl(properties, false);
		
		assertEquals("Check correct number of properties returned", 5, context.getProperties().size());
		
		assertEquals("Check property 1 not overridden by system", "test.property.1.value", context.getProperty("test.property.1"));
		assertEquals("Check property 5 overriden by system", "test.property.5.value", context.getProperty("test.property.5"));
	}
	
	@Test
	public void getBoolProperty() {
		RuntimeContext context = new RuntimeContextImpl(properties, true);
		
		assertTrue("Check bool property", context.getBoolProperty("test.property.3"));
	}
	
	@Test
	public void getIntProperty() {
		RuntimeContext context = new RuntimeContextImpl(properties, true);
		
		assertEquals("Check int property", Integer.class, new Integer(context.getIntProperty("test.property.2")).getClass());
		assertTrue("Check false property", !context.getBoolProperty("i.dont.exist"));
	}
	
	@Test
	public void getRequiredProperty() {
		RuntimeContext context = new RuntimeContextImpl(properties, true);
		
		assertEquals("Check null property", null, context.getProperty("i.dont.exist"));
		
		try {
			assertEquals("Check null property", null, context.getRequiredProperty("i.dont.exist"));
			assertTrue("Check correct exception was thrown", false);
		} catch (IllegalArgumentException e) {
			//Expected
		}
	}

}
