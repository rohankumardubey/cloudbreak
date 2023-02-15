package com.sequenceiq.cloudbreak.cloud.aws.common.resource.volume;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Test cases are from: https://aws.amazon.com/blogs/storage/migrate-your-amazon-ebs-volumes-from-gp2-to-gp3-and-save-up-to-20-on-costs/
 */
public class AwsVolumeIopsAndThroughputCalculatorTest {

    private AwsVolumeIopsAndThroughputCalculator underTest = new AwsVolumeIopsAndThroughputCalculator();

    @Test
    void testNotGp3() {
        assertNull(underTest.getIops("gp2", 1000));
        assertNull(underTest.getIops("st1", 1000));

        assertNull(underTest.getThroughput("gp2", 1000));
        assertNull(underTest.getThroughput("st1", 1000));
    }

    @Test
    void testGp3Iops() {
        assertEquals(3000, underTest.getIops("gp3", 30));
        assertEquals(3000, underTest.getIops("gp3", 100));
        assertEquals(3000, underTest.getIops("gp3", 500));
        assertEquals(3000, underTest.getIops("gp3", 1000));
        assertEquals(6000, underTest.getIops("gp3", 2000));
        assertEquals(16000, underTest.getIops("gp3", 6000));
    }

    @Test
    void testGp3Throughput() {
        assertEquals(125, underTest.getThroughput("gp3", 30));
        assertEquals(125, underTest.getThroughput("gp3", 100));
        assertEquals(250, underTest.getThroughput("gp3", 500));
        assertEquals(250, underTest.getThroughput("gp3", 1000));
        assertEquals(250, underTest.getThroughput("gp3", 2000));
        assertEquals(250, underTest.getThroughput("gp3", 6000));
    }
}