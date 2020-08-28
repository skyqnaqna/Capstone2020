package com.cs2020.capstone;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InfoActivity extends AppCompatActivity {
    private int id = 0;
    DBActivityHelper mDbOpenHelper;
    private String name = null, cate = null, com = null, memo = null, image = null;
    private int Lyear = 0, Lmonth = 0, Lday = 0, Ayear = 0, Amonth = 0, Aday = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //툴바
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //뒤로가기 버튼 생성
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        TextView Nname = (TextView) findViewById(R.id.pName);
        TextView Ncate = (TextView) findViewById(R.id.pCategory);
        TextView Ndate = (TextView) findViewById(R.id.pDate);
        TextView Nalarm = (TextView) findViewById(R.id.pAlarm);
        TextView Ncom = (TextView)findViewById(R.id.pCompany);
        TextView Nmemo = (TextView) findViewById(R.id.pMemo);
        Nmemo.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        Toast.makeText(getApplicationContext(), "id :" + id, Toast.LENGTH_LONG).show();
        mDbOpenHelper = new DBActivityHelper(this);
        mDbOpenHelper.open();
        String[] coulumns = new String[]{DBActivity.COL_NAME, DBActivity.COL_CATE
                , DBActivity.COL_LYEAR, DBActivity.COL_LMONTH, DBActivity.COL_LDAY
                , DBActivity.COL_AYEAR, DBActivity.COL_AMONTH, DBActivity.COL_ADAY
                , DBActivity.COL_COM, DBActivity.COL_MEMO, DBActivity.COL_IMAGE};
        Cursor cursor = mDbOpenHelper.select(coulumns, "_ID = " + id, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {

                name = cursor.getString(0);
                cate = cursor.getString(1);
                Lyear = cursor.getInt(2);
                Lmonth = cursor.getInt(3);
                Lday = cursor.getInt(4);
                Ayear = cursor.getInt(5);
                Amonth = cursor.getInt(6);
                Aday = cursor.getInt(7);
                com = cursor.getString(8);
                memo = cursor.getString(9);
                image = cursor.getString(10);
            }
        }

            String Ldate = Lyear + "/" + Lmonth + "/" + Lday;
            String Adate = Ayear + "/" + Amonth + "/" + Aday;
            imageView.setImageResource(intent.getIntExtra("img", 0));
            Nname.setText(name);
            Ncate.setText(cate);
            Ndate.setText(Ldate);
            Nalarm.setText(Adate);
            Ncom.setText(com);
            Nmemo.setText(memo);

            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }



        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.info_menu, menu);
            return true;
        }

        //뒤로가기 버튼 누르면 홈화면으로 이동
        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                    finish();
                    return true;
                }
            }
            return super.onOptionsItemSelected(item);
        }
}