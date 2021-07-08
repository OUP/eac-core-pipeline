package com.oup.eac.domain;

import junit.framework.Assert;

import org.junit.Test;

public class FieldTest {

	@Test
	public void shouldReturnExpectedValueFromComparator() {
		Field field1 = new Field();
		field1.setSequence(0);
		Field field2 = new Field();
		field2.setSequence(1);
		Field field3 = new Field();
		field3.setSequence(1);
		
		Assert.assertTrue(field1.compareTo(field2)+"", field1.compareTo(field2) < 0);
		Assert.assertTrue(field2.compareTo(field1)+"", field2.compareTo(field1) > 0);
		Assert.assertTrue(field2.compareTo(field3)+"", field2.compareTo(field3) == 0);
	}
}
