package com.jastley.wheelycool.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jastley.wheelycool.R;
import com.jastley.wheelycool.database.entities.Word;
import com.jastley.wheelycool.viewholders.WordViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WordRecyclerAdapter extends RecyclerView.Adapter<WordViewHolder> {

    private List<Word> words = new ArrayList<>();

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        holder.setPosition(position);
        holder.setWord(words.get(position).getWord());
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void setWordList(List<Word> wordList) {
        this.words = wordList;
        notifyDataSetChanged();
    }

    public void removeWord(int pos) {
        words.remove(pos);
        notifyDataSetChanged();
    }
}
