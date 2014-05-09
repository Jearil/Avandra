package com.linkedin.avandra.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by cmiller on 5/8/14.
 */
@RunWith(RobolectricTestRunner.class)
public class MetricsIntentServiceTest {
    MetricsIntentService service;

    @Before
    public void setUp() {
        service = new MetricsIntentService();
        service.onCreate();
    }

    @After
    public void tearDown() {
        service.onDestroy();
    }

    @Test
    public void basicIncrementOfTapList() {
        Intent intent = new Intent(Robolectric.application, MetricsIntentService.class);
        intent.putExtra(MetricsIntentService.TAG, MetricsIntentService.LIST_TAP);

        SharedPreferences preferences = service.getSharedPreferences(MetricsIntentService.TAG, Context.MODE_PRIVATE);
        assertEquals(0, preferences.getInt(MetricsIntentService.LIST_TAP, 0));
        service.onHandleIntent(intent);
        assertEquals(1, preferences.getInt(MetricsIntentService.LIST_TAP, 0));
    }

    @Test
    public void networkedMetricRequest() {
        Intent intent = new Intent(Robolectric.application, MetricsIntentService.class);
        intent.putExtra(MetricsIntentService.TAG, MetricsIntentService.SERVER);
        Robolectric.addHttpResponseRule("http://localhost:1234", "5");

        SharedPreferences preferences = service.getSharedPreferences(MetricsIntentService.TAG, Context.MODE_PRIVATE);
        service.onHandleIntent(intent);
        assertEquals(5, preferences.getInt(MetricsIntentService.SERVER, 0));
    }
}
