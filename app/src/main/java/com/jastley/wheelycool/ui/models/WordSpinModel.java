package com.jastley.wheelycool.ui.models;

import android.widget.TextView;

public class WordSpinModel {

    private String text;
    private int startRotation;
    private int startAngle;
    private TextView textView;
    private int currentAngle;

    public WordSpinModel(String text, int rotation, int angle, TextView textView) {
        this.text = text;
        this.startRotation = rotation;
        this.startAngle = angle;
        this.textView = textView;
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

    public int getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(int currentAngle) {
        this.currentAngle = currentAngle;
    }
}
