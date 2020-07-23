package com.cs2020.capstone;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback
{
    static int dragFlag = 0;

    public interface OnItemMoveListener
    {
        void onItemMove (int fromPos, int toPos);
    }

    private final OnItemMoveListener onItemMoveListener;
    public ItemTouchHelperCallback(OnItemMoveListener onItemMoveListener)
    {
        this.onItemMoveListener = onItemMoveListener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder)
    {
        viewHolder.itemView.findViewById(R.id.drag).setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                }
                else
                    dragFlag = 0;
                return false;
            }
        });
        return makeMovementFlags(dragFlag, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
    {
        onItemMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled()
    {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
    {

    }
}
