package com.cs2020.capstone;

import android.app.Activity;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    RecyclerView rv;
    MainAdapter adapter;
    ItemTouchHelper itemTouchHelper;
    Spinner spinner;
    DBActivityHelper mDbOpenHelper;
    private String sel = null;
    private ArrayList<Product> allItems = new ArrayList<>();
    private ArrayList<Product> remainItems = new ArrayList<>();
    private ArrayList<Product> goneItmes = new ArrayList<>();
    private int amount = 0;

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

        rv = findViewById(R.id.ProductRecycle);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        rv.setLayoutManager(layoutManager);

        adapter = new MainAdapter(this);

        // TODO : DB에서 제품들 추가하는 형식으로 변경하기 (카테고리 count++)
//        adapter.addProduct(new Product("초코파이", "과자", "빙그레", 2021, 4, 5, R.drawable.chocopie));
//        adapter.addProduct(new Product("초코", "과자", "빙그레", 2022, 8, 1, R.drawable.add));
//        adapter.addProduct(new Product("파이", "과자", "빙그레", 2019, 12, 12, R.drawable.home));
//        adapter.addProduct(new Product("초파", "과자", "빙그레", 2019, 10, 17, R.drawable.edit));

        initItemList();
        itemListToAdapter(allItems);

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
                                //Toast.makeText(MainActivity.this, "Delete click", Toast.LENGTH_SHORT).show();
                                Product item = adapter.getItem(pos);
                                int id = item.getID();
                                String cate = item.getCategory();
                                mDbOpenHelper.deleteColumn(id);
                                String[] columns = new String[]{DBActivity.COL_AMOUNT};
                                Cursor cursor = mDbOpenHelper.selectCate(columns,"category = "+"'"+ cate+"'", null, null, null, null);
                                if(cursor != null)
                                {
                                    while (cursor.moveToNext())
                                    {
                                        amount = cursor.getInt(0);
                                    }
                                }
                                if (amount>0){
                                    mDbOpenHelper.updateCate(cate, amount-1);
                                }
                                Toast.makeText(MainActivity.this, "id : "+id,Toast.LENGTH_LONG).show();
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

    // allItems 리스트 초기화
    protected void initItemList()
    {
        if (allItems != null && !allItems.isEmpty())
            allItems.clear();


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
                String productName = cursor.getString(1);
                String category = cursor.getString(2);
                int lifeYear = cursor.getInt(3);
                int lifeMonth = cursor.getInt(4);
                int lifeDay = cursor.getInt(5);
                String company = cursor.getString(9);
                String image = cursor.getString(11);

                allItems.add(new Product(id,productName, category, company, lifeYear, lifeMonth, lifeDay, image));
                //adapter.addProduct(new Product(productName, category, company, lifeYear, lifeMonth, lifeDay, image));
                Log.d("cursor", cursor.getString(0));
            }
            cursor.close();
        }
        divideItemList();
    }

    // MainAdapter에 있는 item리스트 초기화하고 list를 adapter에 있는 제품리스트에 반영
    protected void itemListToAdapter(ArrayList<Product> list)
    {
        if (adapter.items != null && !adapter.items.isEmpty())
            adapter.items.clear();

        for (int i = 0; i < list.size(); ++i)
        {
            adapter.addProduct(list.get(i));
        }
    }

    // 유통기한 지난 제품과 남은 제품들 나누기
    protected void divideItemList()
    {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date time = new Date();
        String today = format1.format(time);
        int compare;

        if (remainItems != null && !remainItems.isEmpty())
            remainItems.clear();
        if (goneItmes != null && !goneItmes.isEmpty())
            goneItmes.clear();

        for (int i = 0; i < allItems.size(); ++i)
        {
           compare = today.compareTo(allItems.get(i).getDate());

           if (compare <= 0) remainItems.add(allItems.get(i));
           else goneItmes.add(allItems.get(i));
        }
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
            case R.id.show_all:
                Toast.makeText(getApplicationContext(), "전체 클릭", Toast.LENGTH_SHORT).show();
                itemListToAdapter(allItems);
                rv.setAdapter(adapter);
                break;
            case R.id.show_remain:
                Toast.makeText(getApplicationContext(), "남은거 클릭", Toast.LENGTH_SHORT).show();
                itemListToAdapter(remainItems);
                rv.setAdapter(adapter);
                break;
            case R.id.show_pass:
                Toast.makeText(getApplicationContext(), "지난거 클릭", Toast.LENGTH_SHORT).show();
                itemListToAdapter(goneItmes);
                rv.setAdapter(adapter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 뒤로가기 버튼 클릭시
    private long time = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102)
        {
            if (resultCode == RESULT_OK)
            {
                Toast.makeText(getApplicationContext(), "102 Result OK", Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                Product mProduct = (Product) intent.getSerializableExtra("product");
                //adapter.addProduct(mProduct);
                //adapter.notifyItemInserted(adapter.items.size());
                //adapter.notifyDataSetChanged();

                // 제품 추가하면 리스트와 어댑터내의 리스트 초기화하여 리사이클러뷰에 반영하기
                initItemList();
                itemListToAdapter(allItems);
                rv.setAdapter(adapter);

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 111)
        {
            if (resultCode == RESULT_OK)
            {
                Toast.makeText(getApplicationContext(), "112 Result OK", Toast.LENGTH_SHORT).show();

                Intent intent1 = getIntent();
                Product mProduct = (Product) intent1.getSerializableExtra("product");

                // 제품 수정하면 리스트와 어댑터내의 리스트 초기화하여 리사이클러뷰에 반영하기
                initItemList();
                itemListToAdapter(allItems);
                rv.setAdapter(adapter);
            }
        }

    }

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
        SharedPreferences pref = getSharedPreferences("main", Activity.MODE_PRIVATE);
    }

    protected void restoreState()
    {
        SharedPreferences pref = getSharedPreferences("main", Activity.MODE_PRIVATE);
    }

}