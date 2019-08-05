package com.codesense.driverapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String value = "123456789";
        Long l = new Long(value);
        System.out.print(l+4L);
        assertEquals(4, 2 + 2);
    }
}