package com.cs2020.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.CustomViewHolder>
{
    private Context context;
    private List<Integer> items;
    private int i;

    public RvAdapter(Context context, List<Integer> items, int i)
    {
        this.context = context;
        this.items = items;
        this.i = i;
    }

    @NonNull
    @Override
    public RvAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.su_category_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(CustomViewHolder holder, int position)
    {
        final Integer item = items.get(position);
        holder.title.setText(item + "");
        holder.content.setText(item + "content");
    }

    public int getItemCount()
    {
        return this.items.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView content;
        public CustomViewHolder(View v)
        {
            super(v);
            title = v.findViewById(R.id.nameOfCategory);
            content = v.findViewById(R.id.testCategory);
        }
    }
}
