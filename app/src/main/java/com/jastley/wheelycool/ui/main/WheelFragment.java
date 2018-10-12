package com.jastley.wheelycool.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jastley.wheelycool.MainActivity;
import com.jastley.wheelycool.R;
import com.jastley.wheelycool.database.entities.Word;
import com.jastley.wheelycool.ui.models.WordSpinModel;
import com.jastley.wheelycool.utils.DensityCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WheelFragment extends Fragment {

    @BindView(R.id.wheel_background) ImageView wheelBackground;
    @BindView(R.id.center_guideline) ImageView centreGuideline;
    @BindView(R.id.wheel_constraint_layout) ConstraintLayout wheelConstraintLayout;
    @BindView(R.id.spin_button) Button spinButton;
    @BindView(R.id.decrease_window) Button decrementButton;
    @BindView(R.id.increase_window) Button incrementButton;
    @BindView(R.id.win_window_value) TextView currentWindowValue;
    @BindView(R.id.spin_result) TextView spinResult;
    private MainViewModel mViewModel;
    private List<WordSpinModel> wordSpinModels = new ArrayList<>();
    private RotateAnimation rotate;

    private int startDegree = 0;
    private int currentDegree = 0;
    private int winWindow = 20;

    public WheelFragment() {
        // Required empty public constructor
    }

    public static WheelFragment newInstance() {
        return new WheelFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_wheel, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupButtons(3);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Use the same viewModel to share data between fragments that can persist
        // across lifecycle changes (rotation/paused/destroyed)
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        setupWheel();

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity)getActivity();
        if(activity != null) {
            activity.setActionBarTitle(getString(R.string.spinWheelTitle));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(rotate != null) {
            rotate.cancel();
        }
    }

    private void setupWheel() {

        mViewModel.getWordList().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if (!words.isEmpty()) {

                    int initAngle = 360 / words.size();
                    int nextAngle = 0;

                    setupButtons(initAngle);

                    for (Word word : words) {

                        //Calculate angle of rotation for text to compensate for wheel angle position
                        int rotate = ((nextAngle - 90) + 360) % 360;

                        TextView wordTextView = new TextView(getContext());
                        //programmatically setting widths/heights sets pixel value, convert to DP to render size correctly for any device resolution
                        wordTextView.setLayoutParams(new ViewGroup.LayoutParams(DensityCalculator.calculateDp(getContext(), 150),
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        wordTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                        wordTextView.setText(word.getWord());
                        wordTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                        wordTextView.setTextColor(getContext().getResources().getColor(R.color.colorWhite));

                        //Generate a viewId to ensure a preset one won't clash with another resource
                        int itemId = View.generateViewId();
                        wordTextView.setId(itemId);

                        //TextView MUST be added to view before adding constraints below
                        wheelConstraintLayout.addView(wordTextView);

                        wordSpinModels.add(new WordSpinModel(word.getWord(), rotate, nextAngle, wordTextView));

                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(wheelConstraintLayout);
                        constraintSet.constrainCircle(itemId, R.id.center_guideline, DensityCalculator.calculateDp(getContext(), 70), nextAngle);
                        constraintSet.setRotation(itemId, rotate);
                        constraintSet.applyTo(wheelConstraintLayout);

                        nextAngle += initAngle;
                    }
                }
            }
        });
    }

    @OnClick(R.id.spin_button)
    public void spinWheel() {

        Random random = new Random();
        currentDegree = random.nextInt(360) + 1440;
        Log.e("startDegree: " + startDegree, " currentDegree: " + currentDegree);

        rotate = new RotateAnimation(startDegree, currentDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotate.setDuration(3000); //speed of animation, NOT actually duration
        rotate.setFillAfter(true);
        rotate.setInterpolator(new DecelerateInterpolator());
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                spinButton.setEnabled(false);
                spinResult.setText(R.string.spinning);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                spinButton.setEnabled(true);

                int finalDegree = ((currentDegree - startDegree) + 270) % 360;

                int marker = 360 - finalDegree;

                //Calculate max/min and compensate for gap between 359 - >0 if necessary
                double min = ((marker - (winWindow / 2)) + 360) % 360;
                double max = ((marker + (winWindow / 2)) + 360) % 360;

                boolean isWin = false;

                //check each word to see which is closest
                for(WordSpinModel word : wordSpinModels) {

                    if(marker - winWindow < 0 || marker + winWindow > 359) {

                        if(word.getStartingAngle() >= min && word.getStartingAngle() <= max) {
                            //win
                            Log.e("word: ", String.valueOf(word.getStartingAngle() + "wins"));
                            String winText = word.getText() + "!";
                            spinResult.setText(winText);
                            isWin = true;
                        }

                        else if(word.getStartingAngle() >= min && word.getStartingAngle() <= 359) {
                            //win
                            Log.e("word: ", String.valueOf(word.getStartingAngle() + "wins"));
                            String winText = word.getText() + "!";
                            spinResult.setText(winText);
                            isWin = true;
                        }

                        else if(word.getStartingAngle() <= max && word.getStartingAngle() >= 0) {
                            //win
                            Log.e("word: ", String.valueOf(word.getStartingAngle() + "wins"));
                            String winText = word.getText() + "!";
                            spinResult.setText(winText);
                            isWin = true;
                        }
                    }
                    else if(word.getStartingAngle() >= min && word.getStartingAngle() <= max) {
                        //win
                        Log.e("word: ", String.valueOf(word.getStartingAngle() + "wins"));
                        String winText = word.getText() + "!";
                        spinResult.setText(winText);
                        isWin = true;
                    }
                }
                if(!isWin) {
                    spinResult.setText("Not within range!");
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        wheelConstraintLayout.startAnimation(rotate);
    }

    @OnClick(R.id.decrease_window)
    public void decreaseWinWindow() {
        winWindow --;
        setupButtons(winWindow);
    }

    @OnClick(R.id.increase_window)
    public void increaseWinWindow() {
        winWindow++;
        setupButtons(winWindow);
    }


    private void setupButtons(int value) {

        if(wordSpinModels.size() != 0) {
            int maxWindow = 360 / wordSpinModels.size();

            if (value == maxWindow) {
                decrementButton.setEnabled(true);
                incrementButton.setEnabled(false);
                winWindow = value;
                currentWindowValue.setText(String.valueOf("Win range: " + value + "°"));
            } else if (value == 1) {
                decrementButton.setEnabled(false);
                incrementButton.setEnabled(true);
                winWindow = value;
                currentWindowValue.setText(String.valueOf("Win range: " + value + "°"));
            } else if (value >= 1 && value < maxWindow) {
                winWindow = value;
                decrementButton.setEnabled(true);
                incrementButton.setEnabled(true);
                currentWindowValue.setText(String.valueOf("Win range: " + value + "°"));
            }
        }
        else {
            winWindow = value;
            decrementButton.setEnabled(true);
            incrementButton.setEnabled(true);
            currentWindowValue.setText(String.valueOf("Win range: " + value + "°"));
        }
    }

}
