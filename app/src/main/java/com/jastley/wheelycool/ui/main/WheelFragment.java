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
import android.widget.SeekBar;
import android.widget.TextView;

import com.jastley.wheelycool.MainActivity;
import com.jastley.wheelycool.R;
import com.jastley.wheelycool.database.entities.Word;
import com.jastley.wheelycool.ui.models.WordSpinModel;
import com.jastley.wheelycool.utils.DensityCalculator;
import com.jastley.wheelycool.utils.WheelSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WheelFragment extends Fragment {

    @BindView(R.id.wheel_background) ImageView wheelBackground;
    @BindView(R.id.center_guideline) ImageView centreGuideline;
    @BindView(R.id.wheel_constraint_layout) ConstraintLayout wheelConstraintLayout;
    @BindView(R.id.spin_button) Button spinButton;
    @BindView(R.id.spin_result) TextView spinResult;
    @BindView(R.id.win_window_counter) TextView winCurrentValue;
    @BindView(R.id.win_window_seekbar) SeekBar winSeekbar;
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

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Use the same viewModel to share data between fragments that can persist
        // across lifecycle changes (rotation/paused/destroyed)
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        setupWheel();
        setupSeekbarListener();
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

                    setupSeekbar();
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

                String resultText = WheelSpinner.getWheelSelection(finalDegree, winWindow, wordSpinModels);
                spinResult.setText(resultText);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        wheelConstraintLayout.startAnimation(rotate);
    }

    private void setupSeekbar() {
        winSeekbar.setMax(360 / wordSpinModels.size());
        winSeekbar.setProgress(360 / wordSpinModels.size());
    }

    private void setupSeekbarListener() {
        winSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                //don't allow winWindow to be set to zero
                if(seekBar.getProgress() < 1) {
                    seekBar.setProgress(1);
                    winWindow = 1;
                    String valueText = "Win range: " + String.valueOf(1) + "°";
                    winCurrentValue.setText(valueText);
                } else {
                    winWindow = i;
                    String valueText = "Win range: " + String.valueOf(i) + "°";
                    winCurrentValue.setText(valueText);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
