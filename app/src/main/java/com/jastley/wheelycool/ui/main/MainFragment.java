package com.jastley.wheelycool.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.jastley.wheelycool.MainActivity;
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
    @BindView(R.id.empty_list_hint) TextView emptyListHint;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseRecyclerView();

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        setupOnEnterListener();
        loadWords();
        setupSnackbar();
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity activity = (MainActivity)getActivity();
        if(activity != null) {
            activity.setActionBarTitle(getString(R.string.wheelyCool));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.delete_all:
                mViewModel.deleteAllWords();
                wordRecyclerAdapter.emptyList();
                setupEmptyUi();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialiseRecyclerView() {
        wordRecyclerAdapter = new WordRecyclerAdapter();
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.anim_slide_right);
        wordsRecyclerView.setAdapter(wordRecyclerAdapter);
        wordsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        wordsRecyclerView.setLayoutAnimation(animationController);
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerSwipeHelper(0, ItemTouchHelper.LEFT, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(wordsRecyclerView);
    }

    private void loadWords() {
        mViewModel.getWordList().observe(getActivity(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                if(!words.isEmpty()) {
                    emptyListHint.setVisibility(View.GONE);
                    wordRecyclerAdapter.setWordList(words);
                    doneButton.setEnabled(true);
                }
            }
        });
    }

    private void setupSnackbar() {
        mViewModel.getSnackBarMessage().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Snackbar.make(getView(), s, Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @OnClick(R.id.insert_word_button)
    public void addNewWord() {
        if(!newWordEditText.getText().toString().trim().equals("")) {
            saveWord(newWordEditText.getText().toString());
        }
    }

    private void setupOnEnterListener() {
        newWordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE
                        && !newWordEditText.getText().toString().trim().equals("")) {
                    saveWord(newWordEditText.getText().toString());
                }
                return false;
            }
        });
    }

    private void saveWord(String text) {
        mViewModel.addWord(text);
        newWordEditText.getText().clear();
    }

    private void setupEmptyUi() {
        doneButton.setEnabled(false);
        emptyListHint.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        WordViewHolder wvh = (WordViewHolder) viewHolder;
        final int pos = viewHolder.getAdapterPosition();
        wordRecyclerAdapter.removeWord(pos);
        mViewModel.deleteWord(wvh.getWord());

        //disable button if all words have been deleted
        if(wordRecyclerAdapter.getItemCount() == 0) {
            setupEmptyUi();
        }
    }

    @OnClick(R.id.done_button)
    public void doneButtonClick() {
        if(getActivity() != null) {

            Fragment wheelFragment = WheelFragment.newInstance();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, wheelFragment, "WHEEL_FRAGMENT")
                    .addToBackStack("WORD_INPUT")
                    .commit();

        }
    }
}
