package com.jastley.wheelycool.ui.models;

import android.widget.TextView;

public class WordSpinModel {

    private int viewId;
    private String text;
    private int startRotation;
    private int startAngle;
    private TextView textView;

    public WordSpinModel(int viewId, String text, int rotation, int angle, TextView textView) {
        this.viewId = viewId;
        this.text = text;
        this.startRotation = rotation;
        this.startAngle = angle;
        this.textView = textView;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getStartingRotation() {
        return startRotation;
    }

    public void setRotation(int rotation) {
        this.startRotation = rotation;
    }

    public TextView getTextView() {
        return textView;
    }

    public int getStartingAngle() {
        return startAngle;
    }
}
