package com.jastley.wheelycool.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
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
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jastley.wheelycool.R;
import com.jastley.wheelycool.database.entities.Word;
import com.jastley.wheelycool.ui.models.WordSpinModel;
import com.jastley.wheelycool.utils.DensityCalculator;

import java.util.ArrayList;
import java.util.List;

public class WheelFragment extends Fragment {

    @BindView(R.id.wheel_background) ImageView wheelBackground;
    @BindView(R.id.center_guideline) ImageView centreGuideline;
    @BindView(R.id.wheel_constraint_layout) ConstraintLayout wheelConstraintLayout;
    @BindView(R.id.spin_button) Button spinButton;
    private MainViewModel mViewModel;
//    private List<Word> wordList = new ArrayList<>();
    List<Integer> idList = new ArrayList<>();
    private List<WordSpinModel> wordSpinModels = new ArrayList<>();

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
    }

    private void setupWheel() {

        mViewModel.getWordList().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if (!words.isEmpty()) {

//                    wordList = words;

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
                        idList.add(itemId);
                        wordTextView.setId(itemId);

                        //TextView MUST be added to view before adding constraints below
                        wheelConstraintLayout.addView(wordTextView);

                        wordSpinModels.add(new WordSpinModel(itemId, word.getWord(), rotate, nextAngle, wordTextView));
//                        textViews.add(wordTextView);

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

        for(WordSpinModel word : wordSpinModels) {

//            int index = wordSpinModels.indexOf(word);
            ValueAnimator animator = animateWord(word.getTextView(), wordSpinModels.size(), word.getStartingRotation(), word.getStartingAngle(), 5000);
            animator.start();
        }
    }

    private ValueAnimator animateWord(final TextView word, final int initSize, final int rotation, final int nextAngle, long spinDuration) {

        ValueAnimator anim = new ValueAnimator();
        anim.setValues(PropertyValuesHolder.ofInt("angle", nextAngle, nextAngle),
                PropertyValuesHolder.ofInt("rotation", rotation, rotation),
                PropertyValuesHolder.ofInt("incrementer", 0, 359));

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                //use each words starting angle/rotation to calculate each animation frame update
                int incrementCounter = (Integer) valueAnimator.getAnimatedValue("incrementer");

                int nextAngle = (((Integer) valueAnimator.getAnimatedValue("angle") +
                        incrementCounter ) + 360 ) % 360;

                int nextTextRotation = (((Integer) valueAnimator.getAnimatedValue("rotation") +
                        incrementCounter ) + 360 ) % 360;

                Log.e(word.getText().toString(), "nextAngle = " + String.valueOf(nextAngle)
                        + " rotate = " + String.valueOf(nextTextRotation));

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(wheelConstraintLayout);
                constraintSet.constrainCircle(word.getId(), R.id.center_guideline, DensityCalculator.calculateDp(getContext(), 70), nextAngle);
                constraintSet.setRotation(word.getId(), nextTextRotation);
                constraintSet.applyTo(wheelConstraintLayout);

            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        });
        anim.setDuration(spinDuration);
        anim.setInterpolator(new LinearInterpolator());
//        anim.setRepeatMode(ValueAnimator.RESTART);
//        anim.setRepeatCount(ValueAnimator.INFINITE);

        return anim;
    }

}
