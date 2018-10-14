package com.jastley.wheelycool.utils;

import android.util.Log;

import com.jastley.wheelycool.ui.models.WordSpinModel;

import java.util.List;

public class WheelSpinner {

    public static String getWheelSelection(int finalDegree, int winWindow, List<WordSpinModel> wordList) {

        int marker = 360 - finalDegree;

        //Calculate max/min and compensate for gap between 359 - >0 if necessary
        double min = ((marker - (winWindow / 2)) + 360) % 360;
        double max = ((marker + (winWindow / 2)) + 360) % 360;

        boolean isWin = false;

        //check each word to see which is closest
        for(WordSpinModel word : wordList) {

            if(marker - winWindow < 0 || marker + winWindow > 359) {

                if(word.getStartingAngle() >= min && word.getStartingAngle() <= max) {
                    //win
                    Log.e("word: ", String.valueOf(word.getStartingAngle() + "wins"));
                    return word.getText() + "!";
                }

                else if(word.getStartingAngle() >= min && word.getStartingAngle() <= 359) {
                    //win
                    Log.e("word: ", String.valueOf(word.getStartingAngle() + "wins"));
                    return word.getText() + "!";
                }

                else if(word.getStartingAngle() <= max && word.getStartingAngle() >= 0) {
                    //win
                    Log.e("word: ", String.valueOf(word.getStartingAngle() + "wins"));
                    return word.getText() + "!";
                }
            }
            else if(word.getStartingAngle() >= min && word.getStartingAngle() <= max) {
                //win
                Log.e("word: ", String.valueOf(word.getStartingAngle() + "wins"));
                return word.getText() + "!";
            }
        }
        //No wins triggered, no word within range
        return "Not within range!";
    }
}
