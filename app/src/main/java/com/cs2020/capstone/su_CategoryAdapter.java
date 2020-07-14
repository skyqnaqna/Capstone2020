package com.cs2020.capstone;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class su_CategoryAdapter extends RecyclerView.Adapter<su_CategoryAdapter.ViewHolder>
        implements OnCategoryItemClickListener
{

    ArrayList<su_Category> items = new ArrayList<>();
    OnCategoryItemClickListener listener;
    Context mContext;

    public su_CategoryAdapter(Context context)
    {
        mContext = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView1;
        TextView textView2;
        ImageButton imageButton;

        public ViewHolder(View itemView, final OnCategoryItemClickListener listener)
        {
            super(itemView);

            textView1 = itemView.findViewById(R.id.nameOfCategory);
            textView2 = itemView.findViewById(R.id.numberOfItems);
            imageButton = itemView.findViewById(R.id.imageButton);

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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position)
    {
        final su_Category item = items.get(position);
        viewHolder.setItem(item);
        ImageButton imageButton = viewHolder.imageButton;
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Log.d("Btn", "버튼눌린 위치 : " + position);
                PopupMenu popupMenu = new PopupMenu(mContext, viewHolder.imageButton);
                popupMenu.inflate(R.menu.su_category_item_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        switch (menuItem.getItemId())
                        {
                            case R.id.edit:
                                Log.d("Btn", "수정버튼 클릭");
                                showEditDialog(position, item);
                                break;
                            case R.id.delete:
                                Log.d("Btn", "삭제버튼 클릭");
                                showDeleteDialog(position);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public void addCategory(su_Category item)
    {
        items.add(item);
        //item.addCount();
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

    private void showEditDialog(final int position, final su_Category item)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_edit_box, null, false);
        builder.setView(view);

        final EditText editText = (EditText) view.findViewById(R.id.edit_category_name);
        final Button editOk = (Button) view.findViewById(R.id.edit_ok_btn);
        final Button editCancel = (Button) view.findViewById(R.id.edit_cancel_btn);

        final AlertDialog dialog = builder.create();
        editOk.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                item.setName(editText.getText().toString());
                notifyItemChanged(position);
                dialog.dismiss();
            }
        });

        editCancel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void removeItem(int position)
    {
        items.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    private void showDeleteDialog(final int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_delete_box, null, false);
        builder.setView(view);

        final Button deleteOk = (Button) view.findViewById(R.id.delete_ok);
        final Button deleteCancel = (Button) view.findViewById(R.id.delete_cancel);

        final AlertDialog dialog = builder.create();
        deleteOk.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                removeItem(position);
                dialog.dismiss();
            }
        });

        deleteCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}