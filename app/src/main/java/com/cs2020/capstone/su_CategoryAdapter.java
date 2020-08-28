package com.cs2020.capstone;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class su_CategoryAdapter extends RecyclerView.Adapter<su_CategoryAdapter.ViewHolder>
        implements OnCategoryItemClickListener
{

    ArrayList<su_Category> items = new ArrayList<>();
    OnCategoryItemClickListener listener;
    Context mContext;
    DBActivityHelper mDbOpenHelper;
    private int amount =0;
    private String cate = null;

    public su_CategoryAdapter(Context context)
    {
        mContext = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView1;
        TextView textView2;
        ImageButton imageButton;
        Context mCnt;
        DBActivityHelper mDbOpenHelper;
        private int amount = 0;
        private String cate = null;

        public ViewHolder(View itemView, final OnCategoryItemClickListener listener)
        {
            super(itemView);

            mCnt = itemView.getContext();
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
            cate = item.getName();
            textView1.setText(cate);
            mDbOpenHelper = new DBActivityHelper(mCnt);
            mDbOpenHelper.open();
            String[] columns = new String[]{DBActivity.COL_AMOUNT};
            Cursor cursor = mDbOpenHelper.selectCate(columns,"category = "+"'"+ cate+"'", null, null, null, null);
            if(cursor != null)
            {
                while (cursor.moveToNext())
                {
                    amount = cursor.getInt(0);
                }
            }
            textView2.setText(String.valueOf(amount));
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
                                showDeleteDialog(position, item);
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

    public void showAddDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_add_box, null, false);
        builder.setView(view);

        final EditText editText = (EditText) view.findViewById(R.id.add_category_name);
        final Button addOk = (Button) view.findViewById(R.id.add_ok_btn);
        final Button addCancel = (Button) view.findViewById(R.id.add_cancel_btn);

        final AlertDialog dialog = builder.create();

        addOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (editText.getText().toString().equals(""))
                {
                    Toast.makeText(mContext, "카테고리명을 입력하세요", Toast.LENGTH_SHORT).show();
                    //dialog.dismiss();
                }
                else
                {
                    cate = editText.getText().toString();
                    addCategory(new su_Category(cate));
                    mDbOpenHelper = new DBActivityHelper(mContext);
                    mDbOpenHelper.open();
                    mDbOpenHelper.insertCate(cate, 0);
                    notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });

        addCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showEditDialog(final int position, final su_Category item)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_edit_box, null, false);
        builder.setView(view);

        mDbOpenHelper = new DBActivityHelper(mContext);
        mDbOpenHelper.open();

        cate = item.getName();
        String[] columns = new String[]{DBActivity.COL_AMOUNT};
        Cursor cursor = mDbOpenHelper.selectCate(columns,"category = "+"'"+ cate+"'", null, null, null, null);
        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                amount = cursor.getInt(0);
            }
        }

        final EditText editText = (EditText) view.findViewById(R.id.edit_category_name);
        final Button editOk = (Button) view.findViewById(R.id.edit_ok_btn);
        final Button editCancel = (Button) view.findViewById(R.id.edit_cancel_btn);

        final AlertDialog dialog = builder.create();
        editOk.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String Ncate = editText.getText().toString();
                mDbOpenHelper.deleteCate(cate);
                mDbOpenHelper.insertCate(Ncate, amount);
                mDbOpenHelper.updateAllCate(cate, Ncate);
                Toast.makeText(mContext,Ncate+"으로 수정되었습니다.", Toast.LENGTH_LONG).show();
                item.setName(Ncate);
                notifyItemChanged(position);
                dialog.dismiss();
            }
        });

        editCancel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Toast.makeText(mContext,"취소되었습니다.", Toast.LENGTH_LONG).show();
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

    private void showDeleteDialog(final int position, su_Category item)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_delete_box, null, false);
        builder.setView(view);

        mDbOpenHelper = new DBActivityHelper(mContext);
        mDbOpenHelper.open();

        cate = item.getName();
        String[] columns = new String[]{DBActivity.COL_AMOUNT};
        Cursor cursor = mDbOpenHelper.selectCate(columns,"category = "+"'"+ cate+"'", null, null, null, null);
        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                amount = cursor.getInt(0);
            }
        }

        final Button deleteOk = (Button) view.findViewById(R.id.delete_ok);
        final Button deleteCancel = (Button) view.findViewById(R.id.delete_cancel);

        final AlertDialog dialog = builder.create();
        deleteOk.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(amount==0){
                    Toast.makeText(mContext, cate+"는 삭제되었습니다.", Toast.LENGTH_LONG).show();
                    mDbOpenHelper.deleteCate(cate);
                    removeItem(position);
                    dialog.dismiss();
                }else {
                    Toast.makeText(mContext, "해당 카테고리에 해당하는 제품이 있어 삭제할 수 없습니다.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }

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