package com.example.adopta;

import android.text.Layout;
import android.text.TextPaint;

public class StatticLayout extends Layout {
    protected StatticLayout(CharSequence text, TextPaint paint, int width, Alignment align, float spacingMult, float spacingAdd, boolean includePadding) {
        super(text, paint, width, align, spacingMult, spacingAdd);
    }

    @Override
    public int getLineCount() {
        return 0;
    }

    @Override
    public int getLineTop(int i) {
        return 0;
    }

    @Override
    public int getLineDescent(int i) {
        return 0;
    }

    @Override
    public int getLineStart(int i) {
        return 0;
    }

    @Override
    public int getParagraphDirection(int i) {
        return 0;
    }

    @Override
    public boolean getLineContainsTab(int i) {
        return false;
    }

    @Override
    public Directions getLineDirections(int i) {
        return null;
    }

    @Override
    public int getTopPadding() {
        return 0;
    }

    @Override
    public int getBottomPadding() {
        return 0;
    }

    @Override
    public int getEllipsisStart(int i) {
        return 0;
    }

    @Override
    public int getEllipsisCount(int i) {
        return 0;
    }
}
