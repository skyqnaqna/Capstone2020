package com.cs2020.capstone;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ModActivity extends AppCompatActivity{
    private static final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 19;
    private ImageView iv;
    private Spinner spinner1;
    private DatePicker dp;

    private EditText text1, text2, text3;
    DBActivityHelper mDbOpenHelper;
    BarAdapter mBarDbOpenHelper;

    private int year = 0, month = 0, day = 0, id = 0;
    private int Ayear = 0, Amonth = 0, Aday = 0;
    private String category = null, name = null, company = null, memo = null, Bcate = null;
    private String photoPath = null;
    private int amount = 0, Bamount =0;
    private Calendar calendar;
    Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod);
        mDbOpenHelper = new DBActivityHelper(this);
        mDbOpenHelper.open();

        mBarDbOpenHelper = new BarAdapter(this);
        mBarDbOpenHelper.open();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        checkSelfPermission();


        iv = findViewById(R.id.imageView);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //기기 기본 갤러리 접근
                //intent.setType(MediaStore.Images.Media.CONTENT_TYPE); 구글 갤러리 접근
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 101);
            }
        });

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        Toast.makeText(getApplicationContext(), "id :" + id, Toast.LENGTH_LONG).show();

        String[] coulumns = new String[]{DBActivity.COL_NAME, DBActivity.COL_CATE
                , DBActivity.COL_LYEAR, DBActivity.COL_LMONTH, DBActivity.COL_LDAY
                , DBActivity.COL_AYEAR, DBActivity.COL_AMONTH, DBActivity.COL_ADAY
                , DBActivity.COL_COM, DBActivity.COL_MEMO, DBActivity.COL_IMAGE};
        Cursor Lcursor = mDbOpenHelper.select(coulumns, "_ID = " + id, null, null, null, null);
        if (Lcursor != null) {
            while (Lcursor.moveToNext()) {

                name = Lcursor.getString(0);
                Bcate = Lcursor.getString(1);
                year = Lcursor.getInt(2);
                month = Lcursor.getInt(3)-1;
                day = Lcursor.getInt(4);
                Ayear = Lcursor.getInt(5);
                Amonth = Lcursor.getInt(6)-1;
                Aday = Lcursor.getInt(7);
                company = Lcursor.getString(8);
                memo = Lcursor.getString(9);
                photoPath = Lcursor.getString(10);
            }
        }

        text1 = (EditText) findViewById(R.id.editText);
        text1.setText(name);
        text1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = text1.getText().toString(); //제품명 추출
                Toast.makeText(getApplicationContext(), "Product name : " + name, Toast.LENGTH_LONG).show();
            }
        });

        text2 = (EditText) findViewById(R.id.editText2);
        text2.setText(company);
        text2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                company = text2.getText().toString(); //제조사 추출
            }
        });

        text3 = (EditText) findViewById(R.id.editText3);
        text3.setText(memo);
        text3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                memo = text3.getText().toString(); //메모 추출
            }
        });

        spinner1 = (Spinner) findViewById(R.id.spinner);
        String[] columns = new String[] {DBActivity.COL_CATE};
        ArrayList<String> cates = new ArrayList<String>();
        Cursor cursor = mDbOpenHelper.selectCate(columns, null, null, null, null, null);
        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                String cate = cursor.getString(0);
                cates.add(cate);
            }
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cates);
        spinner1.setAdapter(adapter1); //카테고리 배열과 어댑터 연결
        spinner1.setSelection(getIndex(spinner1, Bcate));

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = spinner1.getSelectedItem().toString(); //category 추출
                Toast.makeText(ModActivity.this, "선택된 아이템 : " + spinner1.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        calendar = Calendar.getInstance();

        dp = (DatePicker) findViewById(R.id.datePicker);
        dp.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int yy, int mm, int dd) {
                year = yy;
                month = mm;
                day = dd;
            }
        }); //dp에서 선택한 날짜 추출 이 부분 유지해주세요


        // 바코드 인식 버튼 누르면 스캐너 실행
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(ModActivity.this);
                integrator.setBeepEnabled(false);
                integrator.setCaptureActivity(CustomScannerActivity.class);
                integrator.initiateScan();
            }
        });

        //push 알람 실행
        //this.calendar = Calendar.getInstance();//현재 시간 불러오기

        calendar.set(Calendar.YEAR, Ayear);
        calendar.set(Calendar.MONTH, Amonth);
        calendar.set(Calendar.DATE, Aday);
        displayDate();
        //날짜 설정
        TextView txtDate=findViewById(R.id.txtDate);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();//달력 불러오기
            }
        });

        Button btnAlarm = findViewById(R.id.btnAlarm);
        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();//알람 등록
            }
        });

        if(photoPath == null){ //이미지 경로가 null
            iv.setImageResource(R.drawable.gallery_icon);
        }else if(photoPath.indexOf("http")==-1){ //이미지 경로가 sd카드 내부
            iv.setImageURI(Uri.parse(photoPath));
        }else{//이미지 경로가 인터넷 URL
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(photoPath);

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
    }

    private void setAlarm() {
        //시간 설정
        this.calendar.set(Calendar.HOUR_OF_DAY, 16);
        this.calendar.set(Calendar.MINUTE,20);
        this.calendar.set(Calendar.SECOND, 0);

        // 현재일보다 이전이면 등록 실패
        if (this.calendar.before(Calendar.getInstance())) {
            Toast.makeText(this, "현재시간 이후로 알람을 설정해주세요",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // Receiver 설정
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Toast 보여주기 (알람 시간 표시)
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        Toast.makeText(this, format.format(calendar.getTime())+" AM 08:30에 PUSH",
                Toast.LENGTH_LONG).show();

        NotificationSomething(calendar);
    }

    private void NotificationSomething(Calendar calendar) {
        PackageManager pm = this.getPackageManager();

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //알람 설정
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    //달력 보여주기
    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // 알람 날짜 설정

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, day);

                Ayear = calendar.get(calendar.YEAR);
                Amonth = calendar.get(Calendar.MONTH);
                Aday = calendar.get(Calendar.DAY_OF_MONTH);
                // 날짜 표시
                displayDate();

            }
        }, Ayear, Amonth, Aday);
        dialog.show();
    }

    //날짜 보여주기
    private void displayDate() {
        SimpleDateFormat format = new SimpleDateFormat
                ("yyyy년 MM월 dd일", Locale.getDefault());
        ((TextView) findViewById(R.id.txtDate)).setText(format.format(this.calendar.getTime()));
    }


    //권한에 대한 응답이 있을때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //권한을 허용 했을 경우
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("MainActivity", "권한 허용 : " + permissions[i]);
                }
            }
        }
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void checkSelfPermission() {
        String temp = "";
        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
        } else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {

            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Uri photoUri = data.getData();
                photoPath = getRealPathFromURI(this,photoUri);
                Bitmap bm = BitmapFactory.decodeStream(is);
                is.close();
                Toast.makeText(getApplicationContext(), "paht : "+photoPath, Toast.LENGTH_LONG).show();
                iv.setImageBitmap(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 101 && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show();
        }
        // 바코드 읽기 성공했을 때
        else if (resultCode == Activity.RESULT_OK)
        {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            String msg = scanResult.getContents();
            String barcode = msg;
            Log.d("onActivityResult", "onActivityResult: ." + msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

            getDateFromBarcodeDB(msg);
        }
    }

    // 스캔한 바코드로 DB에서 데이터 가져오기
    protected void getDateFromBarcodeDB(String barcode)
    {
        String[] columns = new String[]{BarDBActivity.COL_BARCODE, BarDBActivity.COL_BARNAME,
                BarDBActivity.COL_BARCOM, BarDBActivity.COL_BARIMAGE};

        Cursor cursor = mBarDbOpenHelper.selectBar(columns, BarDBActivity.COL_BARCODE + " = " + barcode,
                null, null, null, null);

        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                name = cursor.getString(1);
                company = cursor.getString(2);
                photoPath = cursor.getString(3);
            }
        }

        //cursor.close();
        text1.setText(name);
        text2.setText(company);

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(photoPath);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mod_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            // 수정완료버튼 눌렀을 때
            case R.id.complete :{
                Toast.makeText(getApplicationContext(),Aday+"/"+category+"/",Toast.LENGTH_LONG).show(); //toolbar의 완료키 눌렀을 때 동작
                mDbOpenHelper.updateColumn(id, name, category, year, month+1, day, Ayear, Amonth+1, Aday, company, memo, photoPath);
                String[] columns = new String[]{DBActivity.COL_AMOUNT};
                Cursor cursor1 = mDbOpenHelper.selectCate(columns,"category = "+"'"+ category+"'", null, null, null, null);
                if(cursor1 != null)
                {
                    while (cursor1.moveToNext())
                    {
                        amount = cursor1.getInt(0); //변경하고자 하는 amount
                    }
                }
                Cursor cursor2 = mDbOpenHelper.selectCate(columns,"category = "+"'"+ Bcate+"'", null, null, null, null);
                if(cursor2 != null)
                {
                    while (cursor2.moveToNext())
                    {
                        Bamount = cursor2.getInt(0); //DB에 있는 amount
            }
        }
                if(!category.equals(Bcate)){
                    mDbOpenHelper.updateCate(category, amount+1);
                    mDbOpenHelper.updateCate(Bcate, Bamount-1);
                }

                Intent intent = new Intent(this, InfoActivity.class);
                Product product = new Product(name, category, company, year, month, day, photoPath);
                intent.putExtra("product", product);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    private int getIndex(Spinner spinner, String item){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)){
                return i;
            }
        }
        return 0;
    }

    public static String getRealPathFromURI(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                } else {
                    String SDcardpath = getRemovableSDCardPath(context).split("/Android")[0];
                    return SDcardpath +"/"+ split[1];
                }
            }

            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static String getRemovableSDCardPath(Context context) {
        File[] storages = ContextCompat.getExternalFilesDirs(context, null);
        if (storages.length > 1 && storages[0] != null && storages[1] != null)
            return storages[1].toString();
        else
            return "";
    }


    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }


    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

}
