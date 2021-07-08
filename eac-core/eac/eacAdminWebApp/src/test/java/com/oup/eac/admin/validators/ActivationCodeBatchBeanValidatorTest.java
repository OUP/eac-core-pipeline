package com.oup.eac.admin.validators;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class ActivationCodeBatchBeanValidatorTest {
    
    LocalDate dateA = new LocalDate(2012,1,1);
    LocalDate dateB = new LocalDate(2012,3,1);
    LocalDate dateC = new LocalDate(2012,6,1);
    LocalDate dateD = new LocalDate(2012,9,1);

    @Test
    public void testInOrderButNoOverlap() {
        Assert.assertTrue(ActivationCodeBatchBeanValidator.inOrderButNoOverlap(dateA, dateB, dateC, dateD));
        Assert.assertTrue(ActivationCodeBatchBeanValidator.inOrderButNoOverlap(dateA, dateA, dateD, dateD));
        Assert.assertFalse(ActivationCodeBatchBeanValidator.inOrderButNoOverlap(dateB, dateA, dateC, dateD));
        Assert.assertFalse(ActivationCodeBatchBeanValidator.inOrderButNoOverlap(dateA, dateC, dateB, dateD));
        
        Assert.assertFalse(ActivationCodeBatchBeanValidator.inOrderButNoOverlap(null,null,null,null));
        
        Assert.assertFalse(ActivationCodeBatchBeanValidator.inOrderButNoOverlap(null,dateB,dateA,null));
    }
    
}
