package com.cs2020.capstone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
    Spinner spinner;
    DBActivityHelper mDbOpenHelper;
    private String sel = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbOpenHelper = new DBActivityHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        SharedPreferences pref = getSharedPreferences("checkFirst", MainActivity.MODE_PRIVATE);
        boolean checkFirst = pref.getBoolean("checkFirst", false);
        if (checkFirst == false)
        {
            // 앱 최초 실행시 하고 싶은 작업
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("checkFirst", true);
            editor.commit();

            mDbOpenHelper.insertCate("육류", 0);
            mDbOpenHelper.insertCate("해산물", 0);
            mDbOpenHelper.insertCate("음료", 0);
            mDbOpenHelper.insertCate("조미료", 0);
            mDbOpenHelper.insertCate("야채", 0);
            mDbOpenHelper.insertCate("냉동식품", 0);

        } else
        {
            // 최초 실행이 아닐때 진행할 작업
        }

        // 툴바
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        // 정렬
        final String[] sort_opt = getResources().getStringArray(R.array.sort_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sort_opt);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);

        RecyclerView rv = findViewById(R.id.ProductRecycle);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        rv.setLayoutManager(layoutManager);

        adapter = new MainAdapter(this);

        // TODO : DB에서 제품들 추가하는 형식으로 변경하기 (카테고리 count++)
//        adapter.addProduct(new Product("초코파이", "과자", "빙그레", 2021, 4, 5, R.drawable.chocopie));
//        adapter.addProduct(new Product("초코", "과자", "빙그레", 2022, 8, 1, R.drawable.add));
//        adapter.addProduct(new Product("파이", "과자", "빙그레", 2019, 12, 12, R.drawable.home));
//        adapter.addProduct(new Product("초파", "과자", "빙그레", 2019, 10, 17, R.drawable.edit));

        String[] coulumns = new String[]{DBActivity.COL_ID,DBActivity.COL_NAME,DBActivity.COL_CATE
                , DBActivity.COL_LYEAR, DBActivity.COL_LMONTH, DBActivity.COL_LDAY
                , DBActivity.COL_AYEAR, DBActivity.COL_AMONTH, DBActivity.COL_ADAY
                , DBActivity.COL_COM, DBActivity.COL_MEMO, DBActivity.COL_IMAGE};
        Cursor cursor = mDbOpenHelper.select(coulumns, null, null, null, null, null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                int id = cursor.getInt(0);
                String produdtName = cursor.getString(1);
                String category = cursor.getString(2);
                int lifeYear = cursor.getInt(3);
                int lifeMonth = cursor.getInt(4);
                int lifeDay = cursor.getInt(5);
                String company = cursor.getString(9);
                String image = cursor.getString(11);

                adapter.addProduct(new Product(id,produdtName, category, company, lifeYear, lifeMonth, lifeDay, image));
            }
        }

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

                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);

                intent.putExtra("id", adapter.getItem(position).primaryKey);
                intent.putExtra("img", adapter.getItem(position).image_src);

                startActivityForResult(intent, 111);
            }
        });

        // 스와이프하여 삭제
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
                                Toast.makeText(MainActivity.this, "Delete click", Toast.LENGTH_SHORT).show();
                                adapter.removeItem(pos);
                            }
                        }));
            }
        };

        // spinner 옵션 선택시
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        adapter.nameAsc();
                        break;
                    case 1:
                        adapter.nameDsc();
                        break;
                    case 2:
                        adapter.dateAsc();
                        break;
                    case 3:
                        adapter.dateDsc();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        // 하단 메뉴
        BottomNavigationView bottomNavigationView = findViewById(R.id.mainNavigationView);
        //bottomNavigationView.getMenu().getItem(0).setChecked(false);

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
                        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent, 102);

                        break;
                    }
                    case R.id.graph:
                    {
                        Intent intent2 = new Intent(getApplicationContext(), GraphActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent2, 103);

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

    // 상단 정렬 메뉴 선택
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
            mDbOpenHelper.close();
            finish();
    }
}