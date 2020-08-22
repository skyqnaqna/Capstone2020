package com.cs2020.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InfoActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //툴바
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //뒤로가기 버튼 생성
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView imageView = (ImageView)findViewById(R.id.imageView);

        TextView name = (TextView) findViewById(R.id.pName);
        TextView cate = (TextView) findViewById(R.id.pCategory);
        TextView date = (TextView) findViewById(R.id.pDate);
        TextView alarm = (TextView) findViewById(R.id.pAlarm);
        TextView memo = (TextView) findViewById(R.id.pMemo);
        memo.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        imageView.setImageResource(intent.getIntExtra("img", 0));
        name.setText(intent.getStringExtra("name"));
        cate.setText(intent.getStringExtra("category"));
        date.setText(String.valueOf(intent.getIntExtra("date", 0)));
        alarm.setText(String.valueOf(intent.getStringExtra("alarm")));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    //뒤로가기 버튼 누르면 홈화면으로 이동
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}