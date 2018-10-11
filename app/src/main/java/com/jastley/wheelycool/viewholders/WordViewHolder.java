package com.jastley.wheelycool.viewholders;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jastley.wheelycool.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WordViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.word_position_count) TextView position;
    @BindView(R.id.word_text) TextView word;
    @BindView(R.id.row_background) RelativeLayout background;
    @BindView(R.id.row_foreground) RelativeLayout foreground;

    public WordViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setPosition(int position) {
        this.position.setText(String.valueOf(position));
    }

    public void setWord(String word) {
        this.word.setText(word);
    }

    public String getWord() {
        return word.getText().toString();
    }

    public RelativeLayout getBackground() {
        return background;
    }

    public RelativeLayout getForeground() {
        return foreground;
    }
}
