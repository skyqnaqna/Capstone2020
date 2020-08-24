package com.cs2020.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>
    implements OnProductItemClickListener, ItemTouchHelperCallback.OnItemMoveListener
{
    // 모든 상품들을 담을 ArrayList
    ArrayList<Product> items = new ArrayList<>();
    ArrayList<Product> remainItmes = new ArrayList<>();
    ArrayList<Product> goneItems = new ArrayList<>();

    int flag = 0; // 0 : all / 1 : remain / -1 : gone

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
            textView2.setText(String.format(Locale.KOREA, "유통기한 %d-%d-%d 까지", item.getYear(), item.getMonth(), item.getDay()));
            imageView.setImageResource(item.image_src);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.product_item, parent, false);

        sortItemsByDate();

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {


        if (flag == 0)
        {
            Product item = items.get(position);

            holder.setItem(item);

        }
        else if (flag == 1)
        {
            Product item = remainItmes.get(position);
            holder.setItem(item);
        }
        else if (flag == -1)
        {
            Product item = goneItems.get(position);
            holder.setItem(item);
        }

        //holder.itemView.setVisibility(View.INVISIBLE);
        //holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

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

    public void nameAsc()
    {
        Comparator<Product> nAsc = new Comparator<Product>()
        {
            @Override
            public int compare(Product o1, Product o2)
            {
                return o1.getName().compareTo(o2.getName());
            }
        };
        Collections.sort(items, nAsc);
        notifyDataSetChanged();
    }

    public void nameDsc()
    {
        Comparator<Product> nDsc = new Comparator<Product>()
        {
            @Override
            public int compare(Product o1, Product o2)
            {
                return o2.getName().compareTo(o1.getName());
            }
        };
        Collections.sort(items, nDsc);
        notifyDataSetChanged();
    }

    public void dateAsc()
    {
        Comparator<Product> dAsc = new Comparator<Product>()
        {
            @Override
            public int compare(Product o1, Product o2)
            {
                return o1.getDate().compareTo(o2.getDate());
            }
        };
        Collections.sort(items, dAsc);
        notifyDataSetChanged();
    }

    public void dateDsc()
    {
        Comparator<Product> dDsc = new Comparator<Product>()
        {
            @Override
            public int compare(Product o1, Product o2)
            {
                return o2.getDate().compareTo(o1.getDate());
            }
        };
        Collections.sort(items, dDsc);
        notifyDataSetChanged();
    }

    public void sortItemsByDate()
    {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date time = new Date();
        String today = format1.format(time);

        for (int i = 0; i < items.size(); ++i)
        {
            int compare = today.compareTo(items.get(i).getDate());

            if (compare < 0) remainItmes.add(items.get(i));
            else if (compare > 0) goneItems.add(items.get(i));
        }
    }

    public void setFlag(int f)
    {
        flag = f;
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
