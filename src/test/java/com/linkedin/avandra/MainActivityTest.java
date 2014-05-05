package com.linkedin.avandra;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This will eventually be where the tests for MainActivity will be placed.
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private Activity context;

    @Before
    public void setUp() {
        context = Robolectric.buildActivity(Activity.class).create().start().resume().visible().get();
    }

    @Test
    public void litmus() {
        View view = mock(View.class);
        when(view.getAlpha()).thenReturn(1.23f);

        float alpha = view.getAlpha();
        assertEquals(1.23f, alpha, 0.001); // mocking works

        Drawable icon = context.getResources().getDrawable(R.drawable.icon);
        assertNotNull(icon);
        fail("This is really a failing test");
    }
}
