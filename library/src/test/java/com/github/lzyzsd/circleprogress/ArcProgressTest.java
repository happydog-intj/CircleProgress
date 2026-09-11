package com.github.lzyzsd.circleprogress;

import android.graphics.Color;
import android.os.Parcelable;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class ArcProgressTest {

    private ArcProgress view;

    @Before
    public void setUp() {
        view = new ArcProgress(RuntimeEnvironment.getApplication());
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
    public void defaultFinishedColor_isWhite() {
        assertEquals(Color.WHITE, view.getFinishedStrokeColor());
    }

    @Test
    public void defaultUnfinishedColor_isBlue() {
        assertEquals(Color.rgb(72, 106, 176), view.getUnfinishedStrokeColor());
    }

    @Test
    public void defaultTextColor_isBlue() {
        assertEquals(Color.rgb(66, 145, 241), view.getTextColor());
    }

    @Test
    public void defaultArcAngle_is288() {
        // 360 * 0.8 = 288
        assertEquals(288f, view.getArcAngle(), 0.01f);
    }

    @Test
    public void defaultTextSize_isSp40() {
        // default_text_size should be sp2px(40), not sp2px(18)
        float expectedSize = Utils.sp2px(RuntimeEnvironment.getApplication().getResources(), 40);
        assertEquals(expectedSize, view.getTextSize(), 0.01f);
    }

    // --- setProgress / getProgress ---

    @Test
    public void setProgress_updatesValue() {
        view.setProgress(50);
        assertEquals(50f, view.getProgress(), 0.01f);
    }

    @Test
    public void setProgress_floatValue_roundsToTwoDecimals() {
        view.setProgress(33.3333f);
        assertEquals(33.33f, view.getProgress(), 0.01f);
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
        // 100 > 100 is false, so no modulo
        assertEquals(100f, view.getProgress(), 0.01f);
    }

    // --- setMax / getMax ---

    @Test
    public void setMax_positiveValue_updates() {
        view.setMax(200);
        assertEquals(200, view.getMax());
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
        view.setMax(-10);
        assertEquals(100, view.getMax());
    }

    // --- Text ---

    @Test
    public void setText_customText_isRetrievable() {
        view.setText("Loading...");
        assertEquals("Loading...", view.getText());
    }

    @Test
    public void setDefaultText_resetsToProgressString() {
        view.setProgress(42);
        view.setText("Custom");
        view.setDefaultText();
        assertEquals("42.0", view.getText());
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

    // --- Dimension setters ---

    @Test
    public void setStrokeWidth_updatesValue() {
        view.setStrokeWidth(10.0f);
        assertEquals(10.0f, view.getStrokeWidth(), 0.01f);
    }

    @Test
    public void setTextSize_updatesValue() {
        view.setTextSize(24.0f);
        assertEquals(24.0f, view.getTextSize(), 0.01f);
    }

    @Test
    public void setSuffixTextSize_updatesValue() {
        view.setSuffixTextSize(12.0f);
        assertEquals(12.0f, view.getSuffixTextSize(), 0.01f);
    }

    @Test
    public void setBottomText_updatesValue() {
        view.setBottomText("Bottom");
        assertEquals("Bottom", view.getBottomText());
    }

    @Test
    public void setBottomTextSize_updatesValue() {
        view.setBottomTextSize(16.0f);
        assertEquals(16.0f, view.getBottomTextSize(), 0.01f);
    }

    @Test
    public void setArcAngle_updatesValue() {
        view.setArcAngle(270.0f);
        assertEquals(270.0f, view.getArcAngle(), 0.01f);
    }

    @Test
    public void setSuffixText_updatesValue() {
        view.setSuffixText("pts");
        assertEquals("pts", view.getSuffixText());
    }

    @Test
    public void setSuffixTextPadding_updatesValue() {
        view.setSuffixTextPadding(8.0f);
        assertEquals(8.0f, view.getSuffixTextPadding(), 0.01f);
    }

    // --- State save/restore ---

    @Test
    public void saveAndRestoreInstanceState_preservesAllFields() {
        view.setProgress(65);
        view.setMax(200);
        view.setStrokeWidth(8.0f);
        view.setTextColor(Color.MAGENTA);
        view.setTextSize(32.0f);
        view.setSuffixTextSize(14.0f);
        view.setSuffixTextPadding(6.0f);
        view.setFinishedStrokeColor(Color.CYAN);
        view.setUnfinishedStrokeColor(Color.DKGRAY);
        view.setArcAngle(270.0f);
        view.setSuffixText("pts");
        view.setBottomText("Score");
        view.setBottomTextSize(16.0f);

        Parcelable state = view.onSaveInstanceState();

        ArcProgress restored = new ArcProgress(RuntimeEnvironment.getApplication());
        restored.onRestoreInstanceState(state);

        assertEquals(65f, restored.getProgress(), 0.01f);
        assertEquals(200, restored.getMax());
        assertEquals(8.0f, restored.getStrokeWidth(), 0.01f);
        assertEquals(Color.MAGENTA, restored.getTextColor());
        assertEquals(32.0f, restored.getTextSize(), 0.01f);
        assertEquals(14.0f, restored.getSuffixTextSize(), 0.01f);
        assertEquals(6.0f, restored.getSuffixTextPadding(), 0.01f);
        assertEquals(Color.CYAN, restored.getFinishedStrokeColor());
        assertEquals(Color.DKGRAY, restored.getUnfinishedStrokeColor());
        assertEquals(270.0f, restored.getArcAngle(), 0.01f);
        assertEquals("pts", restored.getSuffixText());
        assertEquals("Score", restored.getBottomText());
        assertEquals(16.0f, restored.getBottomTextSize(), 0.01f);
    }

    @Test
    public void saveAndRestore_arcAngle_isRestored() {
        view.setArcAngle(270.0f);
        assertEquals(270.0f, view.getArcAngle(), 0.01f);

        Parcelable state = view.onSaveInstanceState();

        ArcProgress restored = new ArcProgress(RuntimeEnvironment.getApplication());
        restored.onRestoreInstanceState(state);

        assertEquals(270.0f, restored.getArcAngle(), 0.01f);
    }

    // --- onMeasure ---

    @Test
    public void onMeasure_exactly_usesExactSize() {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(400, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(400, View.MeasureSpec.EXACTLY);
        view.measure(widthSpec, heightSpec);
        assertEquals(400, view.getMeasuredWidth());
        assertEquals(400, view.getMeasuredHeight());
    }

    @Test
    public void onMeasure_atMost_clampsToMinSize() {
        // When AT_MOST with a large constraint, should use min_size
        int widthSpec = View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.AT_MOST);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.AT_MOST);
        view.measure(widthSpec, heightSpec);
        // min_size is dp2px(100), on mdpi (Robolectric default density=1.0) ≈ 100
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
}
