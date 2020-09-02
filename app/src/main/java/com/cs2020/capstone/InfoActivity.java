package com.cs2020.capstone;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InfoActivity extends AppCompatActivity {
    private int id = 0;
    DBActivityHelper mDbOpenHelper;
    private String name = null, cate = null, com = null, memo = null, image = null;
    private int Lyear = 0, Lmonth = 0, Lday = 0, Ayear = 0, Amonth = 0, Aday = 0;
    ImageView iv;
    Bitmap bm;

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

        iv = (ImageView) findViewById(R.id.imageView);

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

        Nname.setText(name);
        Ncate.setText(cate);
        Ndate.setText(Ldate);
        Nalarm.setText(Adate);
        Ncom.setText(com);
        Nmemo.setText(memo);


        if(image == null){ //이미지 경로가 null
            iv.setImageResource(R.drawable.gallery);
        }else if(image.indexOf("http")==-1){ //이미지 경로가 sd카드 내부
            Uri mUri = Uri.parse(image);
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(),mUri);
                iv.setImageBitmap(bm);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            iv.setImageBitmap(bm);
        }else{//이미지 경로가 인터넷 URL
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(image);

                        // Web에서 이미지를 가져온 뒤
                        // ImageView에 지정할 Bitmap을 만든다
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // 서버로 부터 응답 수신
                        conn.connect();
                        InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                        bm = BitmapFactory.decodeStream(is); // Bitmap으로 변환

                    } catch (MalformedURLException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            mThread.start(); // Thread 실행
            try {
                // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
                // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
                mThread.join();

                // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다
                iv.setImageBitmap(bm);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
                case R.id.modify :{
                    Toast.makeText(getApplicationContext(), "수정 제품 id : " + id,
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ModActivity.class);
                    intent.putExtra("id", id);
                    //finish();

                    startActivityForResult(intent, 112);
                return true;
                }
            }
            return super.onOptionsItemSelected(item);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // 제품 수정을 완료하면 intent를 받아서 MainActivity에 전달해준다
        if (requestCode == 112)
        {
            if (resultCode == RESULT_OK)
            {
                Intent intent2 = getIntent();

                setResult(RESULT_OK, intent2);
                finish();
            }
        }
    }
}