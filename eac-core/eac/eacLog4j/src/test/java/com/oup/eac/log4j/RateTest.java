package com.oup.eac.log4j;

import junit.framework.Assert;

import org.junit.Test;

import com.oup.eac.log4j.BurstDetector.State;

public class RateTest {

    @Test
    public void testNormalToBurst() {

        // 100 in a minute;
        BurstDetector detector = new BurstDetector(100, 60 * 1000);
        for (int i = 0; i < 100; i++) {
            boolean added = detector.increment();
            Assert.assertEquals(i + 1, detector.getEvents().size());
            Assert.assertTrue(added);
        }
        Assert.assertEquals(100, detector.getEvents().size());
        boolean added = detector.increment();
        Assert.assertFalse(added);
        Assert.assertEquals(State.IN_BURST, detector.state);
    }

    @Test
    public void testBurstToBurst() {
        BurstDetector detector = new BurstDetector(100, 60 * 1000);
        long t1 = System.currentTimeMillis();
        detector.setState(State.IN_BURST, t1);
        for (int i = 0; i < 100; i++) {
            Assert.assertFalse(detector.increment());
            Assert.assertEquals(t1, detector.burstTime);
        }
        long t2 = System.currentTimeMillis();
        ;
        Assert.assertFalse(detector.increment());
        Assert.assertTrue(t2 <= detector.burstTime);
        Assert.assertEquals(State.IN_BURST, detector.state);
    }

    @Test
    public void testBurstToNormal() throws InterruptedException {
        BurstDetector detector = new BurstDetector(100, 60 * 1000);
        long t1 = System.currentTimeMillis() - 60001;
        detector.setState(State.IN_BURST, t1);
        detector.inBurstCount = 99;
        boolean result = detector.increment();
        Assert.assertTrue(result);
        Assert.assertEquals(State.NORMAL, detector.state);
    }

}
