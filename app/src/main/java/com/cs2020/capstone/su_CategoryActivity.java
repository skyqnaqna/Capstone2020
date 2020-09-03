package com.cs2020.capstone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class su_CategoryActivity extends AppCompatActivity
{

    @Override
    protected void onPause()
    {
        super.onPause();

        saveState();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        restoreState();
    }

    protected void saveState()
    {
        SharedPreferences pref = getSharedPreferences("cate", Activity.MODE_PRIVATE);
    }

    protected void restoreState()
    {
        SharedPreferences pref = getSharedPreferences("cate", Activity.MODE_PRIVATE);
    }

    DBActivityHelper mDbOpenHelper;
    su_CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.su_activity_category);

        mDbOpenHelper = new DBActivityHelper(this);
        mDbOpenHelper.open();

        RecyclerView rv = findViewById(R.id.CategoryRecycle);
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        rv.setLayoutManager(glm);

         adapter = new su_CategoryAdapter(this);

        // TODO : DB에서 카테고리 읽어오도록 수정하기 / DB에 기본 카테고리 넣기
//        adapter.addCategory(new su_Category("육류"));
//        adapter.addCategory(new su_Category("해산물"));
//        adapter.addCategory(new su_Category("음료"));
//        adapter.addCategory(new su_Category("조미료"));
//        adapter.addCategory(new su_Category("야채"));
//        adapter.addCategory(new su_Category("냉동식품"));

        String[] columns = new String[] { DBActivity.COL_CATE};
        Cursor cursor = mDbOpenHelper.selectCate(columns, null, null, null, null, null);
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                String categoryName = cursor.getString(0);
                adapter.addCategory(new su_Category(categoryName));
            }
        }

        rv.setAdapter(adapter);

        // 카테고리 선택 시 해당 제품들 목록창으로 이동
        adapter.setOnItemClickListener(new OnCategoryItemClickListener()
        {
            @Override
            public void onItemClick(su_CategoryAdapter.ViewHolder holder, View view, int position)
            {
                su_Category item = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "카테고리 선택됨 : " + item.getName()
                        , Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), SelectedCategoryActivity.class);

                intent.putExtra("category_name", adapter.getItem(position).name);

                startActivityForResult(intent, 112);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.categoryNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.home:
                    {
                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent2, 103);
                        finish();
                        break;
                    }
                    case R.id.addCategory:
                    {
                        //AddCategory();
                        adapter.showAddDialog();
                        break;
                    }
                    case R.id.graph:
                    {
                        finish();
                        Intent intent2 = new Intent(getApplicationContext(), GraphActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent2, 104);

                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 112 && resultCode == RESULT_OK)
        {
            Intent intent = getIntent();

            adapter.notifyDataSetChanged();
        }
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
        {
            mDbOpenHelper.close();
            finish();
        }
    }
}




















