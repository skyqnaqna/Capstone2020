package com.cs2020.capstone;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity
{
    PieChart pieChart;
    DBActivityHelper mDbOpenHelper;
    private String category = null;
    private int amount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        mDbOpenHelper = new DBActivityHelper(this);
        mDbOpenHelper.open();

        final su_CategoryAdapter adapter = new su_CategoryAdapter(this);

        PieChart pieChart = findViewById(R.id.piechart);//piechart 찾아서 변수 선언

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(18f);
        pieChart.setEntryLabelColor(Color.rgb(62,27,23));
        pieChart.setTransparentCircleRadius(61f);


        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);// x-Values 리스트 숨기기

        ArrayList<PieEntry> NameOfCate = new ArrayList<PieEntry>();

        String[] columns = new String[]{DBActivity.COL_CATE, DBActivity.COL_AMOUNT};
        Cursor cursor = mDbOpenHelper.selectCate(columns,null, null, null, null, null);
        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                category = cursor.getString(0);
                amount = cursor.getInt(1);
                if(amount!=0){
                    NameOfCate.add(new PieEntry(amount, category));
                }

            }
        }
        //오른쪽 아래 제품 카테고리
        /*Description description = new Description();
        description.setText("제품 카테고리");
        description.setTextSize(20);
        description.setTextColor(Color.GRAY);
        pieChart.setDescription(description);*/

        PieDataSet dataSet = new PieDataSet(NameOfCate, "");
        dataSet.setSliceSpace(3f);//원그래프 카테고리 별 경계
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(23f);
        data.setValueTextColor(Color.rgb(141,110,63));

        pieChart.setData(data);

        BottomNavigationView bottomNavigationView = findViewById(R.id.graphNavigationView);

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

                        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent2, 105);
                        break;
                    }
                    case R.id.addCategory:
                    {
                        adapter.showAddDialog();//AddCategory();
                        break;
                    }
                    case R.id.category:
                    {
                        Intent intent = new Intent(getApplicationContext(), su_CategoryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent, 106);
                        finish();
                        break;
                    }
                }
                return true;
            }
        });
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