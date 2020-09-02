package com.cs2020.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectedCategoryActivity extends AppCompatActivity
{
    MainAdapter adapter;
    ItemTouchHelper itemTouchHelper;
    String name = null;
    DBActivityHelper mDbOpenHelper;
    private ArrayList<Product> items = new ArrayList<>();
    private int amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category);

        mDbOpenHelper = new DBActivityHelper(this);
        mDbOpenHelper.open();

        Intent intent = getIntent();
        name = intent.getStringExtra("category_name");

        RecyclerView rv = findViewById(R.id.SelectedCategoryRecycle);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        rv.setLayoutManager(layoutManager);

        adapter = new MainAdapter(this);

        // TODO : DB에서 카테고리 == name 인 제품들 추가하기
        //adapter.addProduct(new Product("가나파이", "과자", "롯데", 2020, 8,31,R.drawable.chocopie));

        // 아이템 드래그 적용
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback((ItemTouchHelperCallback.OnItemMoveListener) adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rv);

        initItemList();
        itemListToAdapter();

        rv.setAdapter(adapter);

        // 제품 선택 시 제품정보창으로 이동
        adapter.setOnItemClickListener(new OnProductItemClickListener()
        {
            @Override
            public void onItemClick(MainAdapter.ViewHolder holder, View view, int position)
            {
                Product item = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "제품 선택됨 : " + item.getName(),
                        Toast.LENGTH_SHORT).show();

                Intent intent2 = new Intent(getApplicationContext(), InfoActivity.class);
                intent2.putExtra("id", adapter.getItem(position).primaryKey);

                startActivityForResult(intent2, 111);
            }
        });

        // 스와이프하여 삭제
        SwipeHelper swipeHelper = new SwipeHelper(SelectedCategoryActivity.this, rv, 300)
        {
            @Override
            public void instantiatrMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer)
            {
                buffer.add(new MyButton(SelectedCategoryActivity.this,
                        "Delete", 30, R.drawable.ic_baseline_delete_24,
                        Color.parseColor("#FF3C30"),
                        new MyButtonClickListener()
                        {
                            @Override
                            public void onClick(int pos)
                            {
                                //Toast.makeText(SelectedCategoryActivity.this, "Delete click", Toast.LENGTH_SHORT).show();
                                Product item = adapter.getItem(pos);
                                int id = item.getID();
                                String cate = item.getCategory();
                                mDbOpenHelper.deleteColumn(id);
                                String[] columns = new String[]{DBActivity.COL_AMOUNT};
                                Cursor cursor = mDbOpenHelper.selectCate(columns,"category = "+"'"+ cate+"'", null, null, null, null);
                                if(cursor != null)
                                {
                                    while (cursor.moveToNext())
                                    {
                                        amount = cursor.getInt(0);
                                    }
                                }
                                if (amount>0){
                                    mDbOpenHelper.updateCate(cate, amount-1);
                                }
                                Toast.makeText(SelectedCategoryActivity.this, "id : "+id,Toast.LENGTH_LONG).show();
                                adapter.removeItem(pos);
                                adapter.notifyDataSetChanged();
                            }
                        }));
            }
        };
    }

    // items 초기화
    protected void initItemList()
    {
        if (items != null && !items.isEmpty())
            items.clear();


        String[] columns = new String[]{DBActivity.COL_ID,DBActivity.COL_NAME,DBActivity.COL_CATE
                , DBActivity.COL_LYEAR, DBActivity.COL_LMONTH, DBActivity.COL_LDAY
                , DBActivity.COL_AYEAR, DBActivity.COL_AMONTH, DBActivity.COL_ADAY
                , DBActivity.COL_COM, DBActivity.COL_MEMO, DBActivity.COL_IMAGE};
        Cursor cursor = mDbOpenHelper.select(columns, "category = " + "'" + name + "'" , null, null, null, null);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                int id = cursor.getInt(0);
                String productName = cursor.getString(1);
                String category = cursor.getString(2);
                int lifeYear = cursor.getInt(3);
                int lifeMonth = cursor.getInt(4);
                int lifeDay = cursor.getInt(5);
                String company = cursor.getString(9);
                String image = cursor.getString(11);

                items.add(new Product(id,productName, category, company, lifeYear, lifeMonth, lifeDay, image));
                //adapter.addProduct(new Product(productName, category, company, lifeYear, lifeMonth, lifeDay, image));
                Log.d("cursor", cursor.getString(0));
            }
            cursor.close();
        }
    }

    // adapter에 있는 item리스트 초기화
    protected void itemListToAdapter()
    {
        if (adapter.items != null && !adapter.items.isEmpty())
            adapter.items.clear();

        for (int i = 0; i < items.size(); ++i)
        {
            adapter.addProduct(items.get(i));
        }
    }
}

















