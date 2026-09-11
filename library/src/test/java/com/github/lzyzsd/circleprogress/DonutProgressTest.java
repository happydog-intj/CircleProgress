package com.github.lzyzsd.circleprogress;

import android.graphics.Color;
import android.os.Parcelable;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class DonutProgressTest {

    private DonutProgress view;

    @Before
    public void setUp() {
        view = new DonutProgress(RuntimeEnvironment.getApplication());
    }

    // --- Default values ---

    @Test
    public void defaultProgress_isZero() {
        assertEquals(0f, view.getProgress(), 0.01f);
    }

    @Test
    public void defaultMax_is100() {
        assertEquals(100, view.getMax());
    }

    @Test
    public void defaultSuffixText_isPercent() {
        assertEquals("%", view.getSuffixText());
    }

    @Test
    public void defaultPrefixText_isEmpty() {
        assertEquals("", view.getPrefixText());
    }

    @Test
    public void defaultShowText_isTrue() {
        assertTrue(view.isShowText());
    }

    @Test
    public void defaultFinishedColor_isBlue() {
        assertEquals(Color.rgb(66, 145, 241), view.getFinishedStrokeColor());
    }

    @Test
    public void defaultUnfinishedColor_isGray() {
        assertEquals(Color.rgb(204, 204, 204), view.getUnfinishedStrokeColor());
    }

    @Test
    public void defaultTextColor_isBlue() {
        assertEquals(Color.rgb(66, 145, 241), view.getTextColor());
    }

    @Test
    public void defaultInnerBackgroundColor_isTransparent() {
        assertEquals(Color.TRANSPARENT, view.getInnerBackgroundColor());
    }

    @Test
    public void defaultStartingDegree_isZero() {
        assertEquals(0, view.getStartingDegree());
    }

    // --- setProgress / getProgress ---

    @Test
    public void setProgress_updatesValue() {
        view.setProgress(50);
        assertEquals(50f, view.getProgress(), 0.01f);
    }

    @Test
    public void setProgress_floatValue_isPreserved() {
        view.setProgress(33.5f);
        assertEquals(33.5f, view.getProgress(), 0.01f);
    }

    @Test
    public void setProgress_exceedingMax_wrapsAround() {
        view.setMax(100);
        view.setProgress(150);
        assertEquals(50f, view.getProgress(), 0.01f);
    }

    @Test
    public void setProgress_atMax_staysAtMax() {
        view.setMax(100);
        view.setProgress(100);
        assertEquals(100f, view.getProgress(), 0.01f);
    }

    // --- setMax / getMax ---

    @Test
    public void setMax_positiveValue_updates() {
        view.setMax(500);
        assertEquals(500, view.getMax());
    }

    @Test
    public void setMax_zeroValue_ignored() {
        view.setMax(100);
        view.setMax(0);
        assertEquals(100, view.getMax());
    }

    @Test
    public void setMax_negativeValue_ignored() {
        view.setMax(100);
        view.setMax(-1);
        assertEquals(100, view.getMax());
    }

    // --- Text ---

    @Test
    public void setText_customText_isRetrievable() {
        view.setText("Done");
        assertEquals("Done", view.getText());
    }

    @Test
    public void defaultText_isNull() {
        assertNull(view.getText());
    }

    // --- setDonut_progress ---

    @Test
    public void setDonut_progress_parsesStringToInt() {
        view.setDonut_progress("75");
        assertEquals(75f, view.getProgress(), 0.01f);
    }

    @Test
    public void setDonut_progress_emptyString_doesNotCrash() {
        view.setProgress(50);
        view.setDonut_progress("");
        // Should remain unchanged
        assertEquals(50f, view.getProgress(), 0.01f);
    }

    @Test
    public void setDonut_progress_nullString_doesNotCrash() {
        view.setProgress(50);
        view.setDonut_progress(null);
        assertEquals(50f, view.getProgress(), 0.01f);
    }

    // --- Stroke width ---

    @Test
    public void setFinishedStrokeWidth_updatesValue() {
        view.setFinishedStrokeWidth(20.0f);
        assertEquals(20.0f, view.getFinishedStrokeWidth(), 0.01f);
    }

    @Test
    public void setUnfinishedStrokeWidth_updatesValue() {
        view.setUnfinishedStrokeWidth(15.0f);
        assertEquals(15.0f, view.getUnfinishedStrokeWidth(), 0.01f);
    }

    // --- Color setters ---

    @Test
    public void setFinishedStrokeColor_updatesValue() {
        view.setFinishedStrokeColor(Color.RED);
        assertEquals(Color.RED, view.getFinishedStrokeColor());
    }

    @Test
    public void setUnfinishedStrokeColor_updatesValue() {
        view.setUnfinishedStrokeColor(Color.GREEN);
        assertEquals(Color.GREEN, view.getUnfinishedStrokeColor());
    }

    @Test
    public void setTextColor_updatesValue() {
        view.setTextColor(Color.YELLOW);
        assertEquals(Color.YELLOW, view.getTextColor());
    }

    @Test
    public void setInnerBackgroundColor_updatesValue() {
        view.setInnerBackgroundColor(Color.LTGRAY);
        assertEquals(Color.LTGRAY, view.getInnerBackgroundColor());
    }

    @Test
    public void setInnerBottomTextColor_updatesValue() {
        view.setInnerBottomTextColor(Color.BLUE);
        assertEquals(Color.BLUE, view.getInnerBottomTextColor());
    }

    // --- Inner bottom text ---

    @Test
    public void setInnerBottomText_updatesValue() {
        view.setInnerBottomText("Steps");
        assertEquals("Steps", view.getInnerBottomText());
    }

    @Test
    public void setInnerBottomTextSize_updatesValue() {
        view.setInnerBottomTextSize(20.0f);
        assertEquals(20.0f, view.getInnerBottomTextSize(), 0.01f);
    }

    @Test
    public void innerBottomTextDefaults_areCorrectRegardlessOfShowText() {
        // Inner bottom text attributes are read unconditionally (not gated by showText).
        // Default constructed view (showText=true) should have correct defaults.
        float expectedSize = Utils.sp2px(
                RuntimeEnvironment.getApplication().getResources(), 18);
        assertEquals(expectedSize, view.getInnerBottomTextSize(), 0.01f);
        assertEquals(Color.rgb(66, 145, 241), view.getInnerBottomTextColor());
        assertNull(view.getInnerBottomText());
    }

    // --- Starting degree ---

    @Test
    public void setStartingDegree_updatesValue() {
        view.setStartingDegree(90);
        assertEquals(90, view.getStartingDegree());
    }

    // --- Prefix/Suffix ---

    @Test
    public void setPrefixText_updatesValue() {
        view.setPrefixText("$");
        assertEquals("$", view.getPrefixText());
    }

    @Test
    public void saveAndRestore_innerBottomTextColor_usesIntNotFloat() {
        // Regression: onSaveInstanceState previously saved color as putFloat
        // then putInt, causing the float entry to pollute the bundle.
        view.setInnerBottomTextColor(Color.RED);

        Parcelable state = view.onSaveInstanceState();
        DonutProgress restored = new DonutProgress(RuntimeEnvironment.getApplication());
        restored.onRestoreInstanceState(state);

        assertEquals(Color.RED, restored.getInnerBottomTextColor());
    }

    @Test
    public void setSuffixText_updatesValue() {
        view.setSuffixText(" km");
        assertEquals(" km", view.getSuffixText());
    }

    // --- State save/restore ---

    @Test
    public void multipleSetterCalls_workWithoutInitPaintersRebuild() {
        // Verify that calling setters multiple times works correctly
        // even though invalidate() no longer rebuilds paints from scratch.
        view.setFinishedStrokeColor(Color.RED);
        view.setFinishedStrokeColor(Color.GREEN);
        assertEquals(Color.GREEN, view.getFinishedStrokeColor());

        view.setTextColor(Color.BLUE);
        view.setTextColor(Color.YELLOW);
        assertEquals(Color.YELLOW, view.getTextColor());

        view.setFinishedStrokeWidth(5.0f);
        view.setFinishedStrokeWidth(10.0f);
        assertEquals(10.0f, view.getFinishedStrokeWidth(), 0.01f);
    }

    @Test
    public void saveAndRestoreInstanceState_preservesAllFields() {
        view.setProgress(80);
        view.setMax(200);
        view.setTextColor(Color.MAGENTA);
        view.setTextSize(28.0f);
        view.setFinishedStrokeColor(Color.CYAN);
        view.setUnfinishedStrokeColor(Color.DKGRAY);
        view.setFinishedStrokeWidth(12.0f);
        view.setUnfinishedStrokeWidth(8.0f);
        view.setInnerBackgroundColor(Color.LTGRAY);
        view.setStartingDegree(45);
        view.setPrefixText("PRE");
        view.setSuffixText("SUF");
        view.setText("custom");
        view.setInnerBottomText("bottom");
        view.setInnerBottomTextSize(14.0f);
        view.setInnerBottomTextColor(Color.RED);

        Parcelable state = view.onSaveInstanceState();

        DonutProgress restored = new DonutProgress(RuntimeEnvironment.getApplication());
        restored.onRestoreInstanceState(state);

        assertEquals(80f, restored.getProgress(), 0.01f);
        assertEquals(200, restored.getMax());
        assertEquals(Color.MAGENTA, restored.getTextColor());
        assertEquals(28.0f, restored.getTextSize(), 0.01f);
        assertEquals(Color.CYAN, restored.getFinishedStrokeColor());
        assertEquals(Color.DKGRAY, restored.getUnfinishedStrokeColor());
        assertEquals(12.0f, restored.getFinishedStrokeWidth(), 0.01f);
        assertEquals(8.0f, restored.getUnfinishedStrokeWidth(), 0.01f);
        assertEquals(Color.LTGRAY, restored.getInnerBackgroundColor());
        assertEquals(45, restored.getStartingDegree());
        assertEquals("PRE", restored.getPrefixText());
        assertEquals("SUF", restored.getSuffixText());
        assertEquals("custom", restored.getText());
        assertEquals("bottom", restored.getInnerBottomText());
        assertEquals(14.0f, restored.getInnerBottomTextSize(), 0.01f);
        assertEquals(Color.RED, restored.getInnerBottomTextColor());
    }

    // --- onMeasure / onSizeChanged ---

    @Test
    public void onSizeChanged_recalculatesRectAfterMeasure() {
        // After measure + layout, the view should be able to draw without
        // recalculating RectF in onDraw (moved to onSizeChanged).
        int spec = View.MeasureSpec.makeMeasureSpec(300, View.MeasureSpec.EXACTLY);
        view.measure(spec, spec);
        view.layout(0, 0, 300, 300);
        // If onSizeChanged didn't set up the rects, onDraw would use zero rects.
        // Just verify no exception during measure+layout cycle.
        assertEquals(300, view.getMeasuredWidth());
        assertEquals(300, view.getMeasuredHeight());
    }

    @Test
    public void onMeasure_exactly_usesExactSize() {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(300, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(300, View.MeasureSpec.EXACTLY);
        view.measure(widthSpec, heightSpec);
        assertEquals(300, view.getMeasuredWidth());
        assertEquals(300, view.getMeasuredHeight());
    }

    @Test
    public void onMeasure_atMost_clampsToMinSize() {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.AT_MOST);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.AT_MOST);
        view.measure(widthSpec, heightSpec);
        assertTrue(view.getMeasuredWidth() <= 800);
        assertTrue(view.getMeasuredHeight() <= 800);
    }

    @Test
    public void onMeasure_atMost_smallConstraint_clampsToConstraint() {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(50, View.MeasureSpec.AT_MOST);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(50, View.MeasureSpec.AT_MOST);
        view.measure(widthSpec, heightSpec);
        assertEquals(50, view.getMeasuredWidth());
        assertEquals(50, view.getMeasuredHeight());
    }

    @Test
    public void onMeasure_unspecified_usesMinSize() {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
        assertTrue(view.getMeasuredWidth() > 0);
        assertTrue(view.getMeasuredHeight() > 0);
    }

    // --- Accessibility ---

    @Test
    public void accessibility_reportsProgressBarClass() {
        AccessibilityNodeInfo info = AccessibilityNodeInfo.obtain();
        view.onInitializeAccessibilityNodeInfo(info);
        assertEquals("android.widget.ProgressBar", info.getClassName().toString());
        info.recycle();
    }

    @Test
    public void accessibility_reportsRangeInfo() {
        view.setMax(200);
        view.setProgress(75);
        AccessibilityNodeInfo info = AccessibilityNodeInfo.obtain();
        view.onInitializeAccessibilityNodeInfo(info);
        AccessibilityNodeInfo.RangeInfo rangeInfo = info.getRangeInfo();
        assertNotNull(rangeInfo);
        assertEquals(0f, rangeInfo.getMin(), 0.01f);
        assertEquals(200f, rangeInfo.getMax(), 0.01f);
        assertEquals(75f, rangeInfo.getCurrent(), 0.01f);
        info.recycle();
    }

    @Test
    public void accessibility_reportsContentDescription() {
        view.setMax(100);
        view.setProgress(50);
        AccessibilityNodeInfo info = AccessibilityNodeInfo.obtain();
        view.onInitializeAccessibilityNodeInfo(info);
        assertEquals("Progress: 50 of 100", info.getContentDescription().toString());
        info.recycle();
    }
}
