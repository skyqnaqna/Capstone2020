package com.cs2020.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        Intent intent = new Intent(getApplicationContext(), su_CategoryActivity.class);
                        startActivityForResult(intent, 101);
                        break;
                    }
                    case R.id.addProduct:
                    {

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
}