package com.cs2020.capstone;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class su_CategoryAdapter extends RecyclerView.Adapter<su_CategoryAdapter.ViewHolder>
        implements OnCategoryItemClickListener
{
//    Context mContext;
//    List<String> mdata;
//
//    public su_CategoryAdapter(Context context)
//    {
//        this.mContext = context;
//    }
//
//    public void setDate(List<String> data)
//    {
//        mdata = data;
//        notifyDataSetChanged();
//    }

    ArrayList<su_Category> items = new ArrayList<>();
    OnCategoryItemClickListener listener;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {
        TextView textView1;
        TextView textView2;

        public ViewHolder(View itemView, final OnCategoryItemClickListener listener)
        {
            super(itemView);

            textView1 = itemView.findViewById(R.id.nameOfCategory);
            textView2 = itemView.findViewById(R.id.numberOfItems);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int position = getAdapterPosition();
                    if (listener != null)
                        listener.onItemClick(ViewHolder.this, v, position);
                }
            });

            itemView.setOnCreateContextMenuListener(this);
        }

        public void setItem(su_Category item)
        {
            textView1.setText(item.getName());
            textView2.setText(String.valueOf(item.getCount()));
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.su_item_category, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position)
    {
        su_Category item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public void addItem(su_Category item)
    {
        items.add(item);
        item.addCount();
    }

    public void setOnItemClickListener(OnCategoryItemClickListener listener)
    {
        this.listener = listener;
    }

    public void onItemClick(ViewHolder holder, View view, int position)
    {
        if (listener != null)
            listener.onItemClick(holder, view, position);
    }

    public void setItems(ArrayList<su_Category> items)
    {
        this.items = items;
    }

    public su_Category getItem(int position)
    {
        return items.get(position);
    }

    public void setItem(int position, su_Category item)
    {
        items.set(position, item);
    }
}