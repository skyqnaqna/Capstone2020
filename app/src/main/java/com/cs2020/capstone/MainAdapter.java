package com.cs2020.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>
    implements OnProductItemClickListener, ItemTouchHelperCallback.OnItemMoveListener
{
    ArrayList<Product> items = new ArrayList<>();
    OnProductItemClickListener listener;
    Context mContext;

    public MainAdapter(Context context)
    {
        mContext = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        ImageView drag;

        public ViewHolder(View itemView, final OnProductItemClickListener listener)
        {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_image);
            textView1 = itemView.findViewById(R.id.product_name);
            textView2 = itemView.findViewById(R.id.product_date);
            drag = itemView.findViewById(R.id.drag);

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
        }

        public void setItem(Product item)
        {
            textView1.setText(item.getName());
            textView2.setText(String.format(Locale.KOREA, "유통기한 %d-%d-%d", item.getYear(), item.getMonth(), item.getDay()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.product_item, parent, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        Product item = items.get(position);
        holder.setItem(item);

    }



    public void addProduct(Product item)
    {
        items.add(item);
    }

    public void removeItem(int position)
    {
        items.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    // 드래그 아이템 이동
    public void onItemMove(int fromPos, int toPos)
    {
        Collections.swap(items, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }

    public void setOnItemClickListener(OnProductItemClickListener listener)
    {
        this.listener = listener;
    }

    public void onItemClick(ViewHolder holder, View view, int position)
    {
        if (listener != null)
            listener.onItemClick(holder, view, position);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public Product getItem(int position)
    {
        return items.get(position);
    }

    public void setItem(int position, Product item)
    {
        items.set(position, item);
    }
}
