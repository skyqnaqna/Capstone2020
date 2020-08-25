package com.cs2020.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class SelectedCategoryActivity extends AppCompatActivity
{
    MainAdapter adapter;
    ItemTouchHelper itemTouchHelper;
    String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category);

        Intent intent = getIntent();
        name = intent.getStringExtra("category_name");

        RecyclerView rv = findViewById(R.id.SelectedCategoryRecycle);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        rv.setLayoutManager(layoutManager);

        adapter = new MainAdapter(this);

        // TODO : DB에서 카테고리 == name 인 제품들 추가하기
        adapter.addProduct(new Product("가나파이", "과자", "롯데", 2020, 8,31,R.drawable.chocopie));

        // 아이템 드래그 적용
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback((ItemTouchHelperCallback.OnItemMoveListener) adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rv);

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

                intent2.putExtra("name", adapter.getItem(position).name);
                intent2.putExtra("category", adapter.getItem(position).category);
                intent2.putExtra("date", adapter.getItem(position).end_day);
                intent2.putExtra("company", adapter.getItem(position).company);
                intent2.putExtra("img", adapter.getItem(position).image_src);

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
                                Toast.makeText(SelectedCategoryActivity.this, "Delete click", Toast.LENGTH_SHORT).show();
                                adapter.removeItem(pos);
                            }
                        }));
            }
        };
    }
}

















