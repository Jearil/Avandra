package com.linkedin.avandra;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.linkedin.avandra.services.MetricsIntentService;
import com.linkedin.avandra.tasks.TaskFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This will eventually be where the tests for MainActivity will be placed.
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private MainActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class).create().start().resume().visible().get();
    }

    @Test
    public void litmus() {
        View view = mock(View.class);
        when(view.getAlpha()).thenReturn(1.23f);

        float alpha = view.getAlpha();
        assertEquals(1.23f, alpha, 0.001); // mocking works

        Drawable icon = activity.getResources().getDrawable(R.drawable.icon);
        assertNotNull(icon);
    }

    @Config(qualifiers = "large")
    @Test
    public void tabletContainer() {
        View view = activity.findViewById(R.id.tablet_container);
        assertNotNull("The tablet container doesn't exist", view);
    }

    @Test
    public void phoneContainer() {
        View view = activity.findViewById(R.id.fragment_container);
        assertNotNull("The fragment container doesn't exists", view);
        View tablet = activity.findViewById(R.id.tablet_container);
        assertNull("The tablet container exists but should not", tablet);
    }

    @Test
    public void phoneOnSelect() {
        activity.onListSelected(1);
        TaskFragment fragment = (TaskFragment) activity.getFragmentManager().findFragmentByTag(TaskFragment.TAG);
        assertNotNull("Fragment should have existed", fragment);
    }

    @Test
    public void tabletOnSelect() {

    }

    @Test
    public void onListSendsMetric() {
        activity.onListSelected(1);
        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        Intent serviceIntent = shadowActivity.getNextStartedService();
        assertNotNull("No services were started", serviceIntent);
        assertEquals(MetricsIntentService.class.getCanonicalName(), serviceIntent.getComponent().getClassName());
        assertTrue("No tag supplied", serviceIntent.hasExtra(MetricsIntentService.TAG));
    }
}
