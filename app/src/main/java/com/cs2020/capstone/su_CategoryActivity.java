package com.cs2020.capstone;

import android.content.Intent;
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.su_activity_category);

        RecyclerView rv = findViewById(R.id.CategoryRecycle);
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        rv.setLayoutManager(glm);

        final su_CategoryAdapter adapter = new su_CategoryAdapter(this);

        adapter.addCategory(new su_Category("육류"));
        adapter.addCategory(new su_Category("해산물"));
        adapter.addCategory(new su_Category("음료"));
        adapter.addCategory(new su_Category("조미료"));
        adapter.addCategory(new su_Category("야채"));
        adapter.addCategory(new su_Category("냉동식품"));

        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnCategoryItemClickListener()
        {
            @Override
            public void onItemClick(su_CategoryAdapter.ViewHolder holder, View view, int position)
            {
                su_Category item = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "카테고리 선택됨 : " + item.getName()
                        , Toast.LENGTH_LONG).show();
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