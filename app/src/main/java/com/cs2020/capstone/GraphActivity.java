package com.cs2020.capstone;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity
{
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        PieChart pieChart = findViewById(R.id.piechart);//piechart 찾아서 변수 선언

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> NameOfCate = new ArrayList<PieEntry>();
        NameOfCate.add(new PieEntry(5,"육류" ));//value 부분에 카테고리 개수 들어와야함
        NameOfCate.add(new PieEntry(10, "해산물"));
        NameOfCate.add(new PieEntry(10, "음료"));
        NameOfCate.add(new PieEntry(30, "조미료"));
        NameOfCate.add(new PieEntry(25, "야채"));
        NameOfCate.add(new PieEntry(25, "냉동식품"));


        //오른쪽 아래 제품 카테고리
        Description description = new Description();
        description.setText("제품 카테고리");
        description.setTextSize(20);
        description.setTextColor(Color.GRAY);
        pieChart.setDescription(description);

        PieDataSet dataSet = new PieDataSet(NameOfCate, "최대 5개까지 표시됨");
        dataSet.setSliceSpace(3f);//원그래프 카테고리 별 경계
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

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
                        //AddCategory();
                        break;
                    }
                    case R.id.category:
                    {
                        Intent intent = new Intent(getApplicationContext(), su_CategoryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivityForResult(intent, 106);

                        break;
                    }
                }
                return true;
            }
        });
    }
}