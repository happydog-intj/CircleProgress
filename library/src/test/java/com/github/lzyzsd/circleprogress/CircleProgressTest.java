package com.github.lzyzsd.circleprogress;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class CircleProgressTest {

    private CircleProgress view;

    @Before
    public void setUp() {
        view = new CircleProgress(RuntimeEnvironment.getApplication());
    }

    // --- Default values ---

    @Test
    public void defaultProgress_isZero() {
        assertEquals(0, view.getProgress());
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
    public void defaultFinishedColor_isBlue() {
        assertEquals(Color.rgb(66, 145, 241), view.getFinishedColor());
    }

    @Test
    public void defaultUnfinishedColor_isGray() {
        assertEquals(Color.rgb(204, 204, 204), view.getUnfinishedColor());
    }

    @Test
    public void defaultTextColor_isWhite() {
        assertEquals(Color.WHITE, view.getTextColor());
    }

    // --- setProgress / getProgress ---

    @Test
    public void setProgress_updatesValue() {
        view.setProgress(50);
        assertEquals(50, view.getProgress());
    }

    @Test
    public void setProgress_exceedingMax_wrapsAround() {
        view.setMax(100);
        view.setProgress(150);
        assertEquals(50, view.getProgress());
    }

    @Test
    public void setProgress_atMax_remainsAtMax() {
        view.setMax(100);
        view.setProgress(100);
        // progress > max is false, so no modulo
        assertEquals(100, view.getProgress());
    }

    @Test
    public void setProgress_zero_stays() {
        view.setProgress(0);
        assertEquals(0, view.getProgress());
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
        view.setMax(-5);
        assertEquals(100, view.getMax());
    }

    // --- Text ---

    @Test
    public void setText_customText_getTextReturnsIt() {
        view.setText("Loading");
        assertEquals("Loading", view.getText());
    }

    @Test
    public void setDefaultText_resetsToProgressValue() {
        view.setProgress(42);
        view.setText("Custom");
        view.setDefaultText();
        // After setDefaultText, getText returns progress value
        assertEquals("42", view.getText());
    }

    @Test
    public void getDrawText_combinesPrefixTextSuffix() {
        view.setProgress(75);
        view.setPrefixText("★");
        view.setSuffixText("%");
        assertEquals("★75%", view.getDrawText());
    }

    @Test
    public void getDrawText_withCustomText() {
        view.setText("Done");
        view.setPrefixText(">");
        view.setSuffixText("!");
        assertEquals(">Done!", view.getDrawText());
    }

    // --- Color setters ---

    @Test
    public void setTextColor_updatesValue() {
        view.setTextColor(Color.RED);
        assertEquals(Color.RED, view.getTextColor());
    }

    @Test
    public void setFinishedColor_updatesValue() {
        view.setFinishedColor(Color.GREEN);
        assertEquals(Color.GREEN, view.getFinishedColor());
    }

    @Test
    public void setUnfinishedColor_updatesValue() {
        view.setUnfinishedColor(Color.YELLOW);
        assertEquals(Color.YELLOW, view.getUnfinishedColor());
    }

    // --- setTextSize ---

    @Test
    public void setTextSize_updatesValue() {
        view.setTextSize(24.0f);
        assertEquals(24.0f, view.getTextSize(), 0.01f);
    }

    // --- getProgressPercentage ---

    @Test
    public void getProgressPercentage_returnsCorrectRatio() {
        view.setMax(200);
        view.setProgress(50);
        assertEquals(0.25f, view.getProgressPercentage(), 0.001f);
    }

    @Test
    public void getProgressPercentage_zeroProgress_returnsZero() {
        view.setMax(100);
        view.setProgress(0);
        assertEquals(0f, view.getProgressPercentage(), 0.001f);
    }

    // --- State save/restore ---

    @Test
    public void saveAndRestoreInstanceState_preservesAllFields() {
        view.setProgress(65);
        view.setMax(200);
        view.setTextColor(Color.MAGENTA);
        view.setTextSize(32.0f);
        view.setFinishedColor(Color.CYAN);
        view.setUnfinishedColor(Color.DKGRAY);
        view.setPrefixText("PRE");
        view.setSuffixText("SUF");

        Parcelable state = view.onSaveInstanceState();

        // Create a new view and restore
        CircleProgress restored = new CircleProgress(RuntimeEnvironment.getApplication());
        restored.onRestoreInstanceState(state);

        assertEquals(65, restored.getProgress());
        assertEquals(200, restored.getMax());
        assertEquals(Color.MAGENTA, restored.getTextColor());
        assertEquals(32.0f, restored.getTextSize(), 0.01f);
        assertEquals(Color.CYAN, restored.getFinishedColor());
        assertEquals(Color.DKGRAY, restored.getUnfinishedColor());
        assertEquals("PRE", restored.getPrefixText());
        assertEquals("SUF", restored.getSuffixText());
    }

    @Test
    public void restoreInstanceState_withNonBundleState_doesNotCrash() {
        // Should handle gracefully
        view.onRestoreInstanceState(null);
    }
}
