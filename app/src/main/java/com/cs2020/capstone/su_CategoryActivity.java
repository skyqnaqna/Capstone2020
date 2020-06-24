package com.cs2020.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class su_CategoryActivity extends AppCompatActivity
{
    private RecyclerView rv;
    private GridLayoutManager glm;
    private List<Integer> count;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.su_category_main);

        rv = (RecyclerView) findViewById(R.id.CategoryRecycle);
        glm = new GridLayoutManager(this, 2);

        count = new ArrayList<>();

        rv.setHasFixedSize(true);
        rv.setLayoutManager(glm);


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
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();

                        break;
                    }
                    case R.id.addCategory:
                    {
                        AddCategory();
                        break;
                    }
                    case R.id.graph:
                    {

                        break;
                    }
                }
                return true;
            }
        });
    }

    public void AddCategory()
    {
        i++;
        count.add(i);
        RvAdapter adapter = new RvAdapter(getApplication(), count, i);
        rv.setAdapter(adapter);
        Log.d("Count", count + "");
    }
}