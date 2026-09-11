package com.github.lzyzsd.circleprogress;

import android.content.res.Resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class UtilsTest {

    @Test
    public void dp2px_returnsCorrectValue() {
        Resources resources = RuntimeEnvironment.getApplication().getResources();
        float density = resources.getDisplayMetrics().density;
        float result = Utils.dp2px(resources, 10);
        float expected = 10 * density + 0.5f;
        assertEquals(expected, result, 0.01f);
    }

    @Test
    public void dp2px_zeroReturnsHalf() {
        Resources resources = RuntimeEnvironment.getApplication().getResources();
        float result = Utils.dp2px(resources, 0);
        assertEquals(0.5f, result, 0.01f);
    }

    @Test
    public void sp2px_returnsCorrectValue() {
        Resources resources = RuntimeEnvironment.getApplication().getResources();
        float scaledDensity = resources.getDisplayMetrics().scaledDensity;
        float result = Utils.sp2px(resources, 18);
        float expected = 18 * scaledDensity;
        assertEquals(expected, result, 0.01f);
    }

    @Test
    public void sp2px_zeroReturnsZero() {
        Resources resources = RuntimeEnvironment.getApplication().getResources();
        float result = Utils.sp2px(resources, 0);
        assertEquals(0f, result, 0.01f);
    }
}
