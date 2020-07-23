package com.cs2020.capstone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    MainAdapter adapter;
    ItemTouchHelper itemTouchHelper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        RecyclerView rv = findViewById(R.id.ProductRecycle);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        rv.setLayoutManager(layoutManager);

        adapter = new MainAdapter(this);

        adapter.addProduct(new Product("초코파이", "과자", "빙그레", 2020, 12, 12, R.drawable.chocopie));
        adapter.addProduct(new Product("초코", "과자", "빙그레", 2020, 12, 12, R.drawable.add));
        adapter.addProduct(new Product("파이", "과자", "빙그레", 2020, 12, 12, R.drawable.home));
        adapter.addProduct(new Product("초파", "과자", "빙그레", 2020, 12, 12, R.drawable.edit));

        // 아이템 드래그 적용
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback((ItemTouchHelperCallback.OnItemMoveListener)adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rv);

        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnProductItemClickListener()
        {
            @Override
            public void onItemClick(MainAdapter.ViewHolder holder, View view, int position)
            {
                Product item = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "제품 선택됨 : " + item.getName(),
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);

                intent.putExtra("name", adapter.getItem(position).name);
                intent.putExtra("category", adapter.getItem(position).category);
                intent.putExtra("date", adapter.getItem(position).day);
                intent.putExtra("company", adapter.getItem(position).company);
                intent.putExtra("img", adapter.getItem(position).image_src);

                startActivityForResult(intent, 111);
            }
        });

        SwipeHelper swipeHelper = new SwipeHelper(MainActivity.this, rv, 300)
        {
            @Override
            public void instantiatrMyButton(RecyclerView.ViewHolder viewHolder, List<SwipeHelper.MyButton> buffer)
            {
                buffer.add(new MyButton(MainActivity.this,
                        "Delete", 30, R.drawable.ic_baseline_delete_24,
                        Color.parseColor("#FF3C30"),
                        new MyButtonClickListener()
                        {
                            @Override
                            public void onClick(int pos)
                            {
                                Toast.makeText(MainActivity.this, "Delete click",Toast.LENGTH_SHORT).show();
                                adapter.removeItem(pos);
                            }
                        }));
            }
        };


        // 하단 메뉴
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.category:
                    {
                        Intent intent2 = new Intent(getApplicationContext(), su_CategoryActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent2, 101);

                        break;
                    }
                    case R.id.addProduct:
                    {

                        break;
                    }
                    case R.id.graph:
                    {
                        Intent intent2 = new Intent(getApplicationContext(), GraphActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent2, 102);

                        break;
                    }
                }
                return true;
            }
        });
    }

    // 상단 툴바
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.sort:
                Toast.makeText(getApplicationContext(), "정렬 클릭", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_main:
                Toast.makeText(getApplicationContext(), "편집 클릭", Toast.LENGTH_SHORT).show();
                break;
            case R.id.show_all:
                Toast.makeText(getApplicationContext(), "전체 클릭", Toast.LENGTH_SHORT).show();
                break;
            case R.id.show_remain:
                Toast.makeText(getApplicationContext(), "남은거 클릭", Toast.LENGTH_SHORT).show();
                break;
            case R.id.show_pass:
                Toast.makeText(getApplicationContext(), "지난거 클릭", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 뒤로가기 버튼 클릭시
    private long time = 0;

    @Override
    public void onBackPressed()
    {
        if (System.currentTimeMillis() - time >= 2000)
        {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        else if (System.currentTimeMillis() - time < 2000)
            finish();
    }
}