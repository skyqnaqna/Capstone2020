package com.cs2020.capstone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
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

        ArrayList<PieEntry> NoOfPro = new ArrayList<PieEntry>();

        NoOfPro.add(new PieEntry(945f,"육류" ));
        NoOfPro.add(new PieEntry(1040f, "음료"));
        NoOfPro.add(new PieEntry(1133f, "채소"));
        NoOfPro.add(new PieEntry(1240f, "간식"));
        PieDataSet dataSet = new PieDataSet(NoOfPro, " Number Of Product");

        Description description = new Description();
        description.setText("제품 카테고리"); //라벨
        description.setTextSize(20);
        description.setTextColor(Color.GRAY);
        pieChart.setDescription(description);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        // pieChart.animateXY(5000, 5000);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.YELLOW);

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
                        startActivityForResult(intent2, 101);
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
                        startActivityForResult(intent, 101);

                        break;
                    }
                }
                return true;
            }
        });
    }
}