package com.jastley.wheelycool.utils;

import android.graphics.Canvas;
import android.view.View;

import com.jastley.wheelycool.viewholders.WordViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerSwipeHelper extends ItemTouchHelper.SimpleCallback {

    private SwipeListener listener;

    public RecyclerSwipeHelper(int dragDirs, int swipeDirs, SwipeListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        final View foregoundView = ((WordViewHolder) viewHolder).getForeground();
        getDefaultUIUtil().onDraw(c, recyclerView, foregoundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final View foregoundView = ((WordViewHolder) viewHolder).getForeground();
        getDefaultUIUtil().clearView(foregoundView);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface SwipeListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
