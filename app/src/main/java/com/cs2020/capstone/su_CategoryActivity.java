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

        final su_CategoryAdapter adapter = new su_CategoryAdapter(this);

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
//                        Intent intent = new Intent();
//                        setResult(RESULT_OK, intent);
//                        finish();

                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent2, 103);
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
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
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





    //    public void AddCategory()
//    {
//        i++;
//        count.add(i);
//        RvAdapter adapter = new RvAdapter(getApplication(), count, i);
//        rv.setAdapter(adapter);
//        Log.d("Count", count + "");
//    }
}