package com.oup.eac.web.tags;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class SetDateTimeTagTest {

    @Test
    public void testPaseDate() {
        DateTime result1 = SetDateTimeTag.getDateTime("31121999", "235957");
        DateTime result2 = SetDateTimeTag.getDateTime("31121999", "235958");
        DateTime result3 = SetDateTimeTag.getDateTime("31121999", "235959");
        String toString1 = SetDateTimeTag.FMT.print(result1);
        String toString2 = SetDateTimeTag.FMT.print(result2);
        String toString3 = SetDateTimeTag.FMT.print(result3);
        Assert.assertEquals("31121999235957", toString1);
        Assert.assertEquals("31121999235958", toString2);
        Assert.assertEquals("31121999235959", toString3);
        
        Assert.assertTrue(result1.isBefore(result2));
        Assert.assertTrue(result2.isBefore(result3));
    }
}

