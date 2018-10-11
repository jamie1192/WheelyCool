package com.jastley.wheelycool.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jastley.wheelycool.R;
import com.jastley.wheelycool.utils.DensityCalculator;

import java.util.ArrayList;
import java.util.List;

public class WheelFragment extends Fragment {

    @BindView(R.id.wheel_background) ImageView wheelBackground;
    @BindView(R.id.center_guideline) ImageView centreGuideline;
    @BindView(R.id.wheel_constraint_layout) ConstraintLayout wheelConstraintLayout;

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
        setupWheel();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupWheel() {

        List<String> wordList = new ArrayList<>();
        wordList.add("one");
        wordList.add("two");
        wordList.add("three");
        wordList.add("four");

        List<Integer> idList = new ArrayList<>();
        int initAngle = 360 / wordList.size();
        int nextAngle = 0;

        for(String word : wordList) {

            //Calculate angle of rotation for text to compensate for wheel angle position
            int rotate = ((nextAngle - 90) + 360) % 360;

            TextView first = new TextView(getContext());
            //programmatically setting widths/heights sets pixel value, convert to DP to render size correctly
            //for any device resolution
            first.setLayoutParams(new ViewGroup.LayoutParams(DensityCalculator.calculateDp(getContext(), 150),
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            first.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            first.setText(word);
            first.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            first.setTextColor(getContext().getResources().getColor(R.color.colorWhite));

            int itemId = View.generateViewId();
            idList.add(itemId);
            first.setId(itemId);

            wheelConstraintLayout.addView(first);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(wheelConstraintLayout);

//        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) first.getLayoutParams();
//
//        lp.circleRadius = DensityCalculator.calculateDp(getContext(), 70);
//        lp.circleAngle = 45;
//        first.setLayoutParams(lp);

            constraintSet.constrainCircle(itemId, R.id.center_guideline, DensityCalculator.calculateDp(getContext(), 70), nextAngle);
            constraintSet.setRotation(itemId, rotate);
            constraintSet.applyTo(wheelConstraintLayout);

            nextAngle += initAngle;
        }


    }
}
