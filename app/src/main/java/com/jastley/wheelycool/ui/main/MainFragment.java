package com.jastley.wheelycool.ui.main;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jastley.wheelycool.R;
import com.jastley.wheelycool.adapters.WordRecyclerAdapter;
import com.jastley.wheelycool.database.entities.Word;
import com.jastley.wheelycool.utils.RecyclerSwipeHelper;
import com.jastley.wheelycool.viewholders.WordViewHolder;

import java.util.List;

public class MainFragment extends Fragment implements RecyclerSwipeHelper.SwipeListener {

    @BindView(R.id.new_word_input) EditText newWordEditText;
    @BindView(R.id.insert_word_button) Button insertButton;
    @BindView(R.id.saved_words_recycler) RecyclerView wordsRecyclerView;
    @BindView(R.id.done_button) Button doneButton;
    private MainViewModel mViewModel;
    private WordRecyclerAdapter wordRecyclerAdapter;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.main_fragment, container, false);

        ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        loadWords();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseRecyclerView();

    }

    private void initialiseRecyclerView() {
        wordRecyclerAdapter = new WordRecyclerAdapter();
        wordsRecyclerView.setAdapter(wordRecyclerAdapter);
        wordsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerSwipeHelper(0, ItemTouchHelper.LEFT, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(wordsRecyclerView);
    }

    private void loadWords() {
        mViewModel.getWordList().observe(getActivity(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                wordRecyclerAdapter.setWordList(words);
                if(words.size() == 5) {
                    insertButton.setEnabled(false);
                }
            }
        });
    }

    @OnClick(R.id.insert_word_button)
    public void addNewWord() {
        if(!newWordEditText.getText().toString().trim().equals("")) {
            mViewModel.addWord(newWordEditText.getText().toString());
            newWordEditText.getText().clear();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        WordViewHolder wvh = (WordViewHolder) viewHolder;
        final int pos = viewHolder.getAdapterPosition();
        wordRecyclerAdapter.removeWord(pos);
        mViewModel.deleteWord(wvh.getWord());
    }
}
