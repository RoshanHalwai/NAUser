package com.kirtanlabs.nammaapartments;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/1/2018
 */

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.kirtanlabs.nammaapartments", appContext.getPackageName());
    }
}
