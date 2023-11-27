package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class PrekdiksiTrend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prekdiksi_trend);

        ImageView btnback = findViewById(R.id.arrow);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PrekdiksiTrend.this, HomeTokoPage.class);
                startActivity(i);
            }
        });


//        //        SLIDER
//        ImageSlider imageSlider = findViewById(R.id.imageSlider);
//        List<SlideModel> slideModels = new ArrayList<>();
//        slideModels.add(new SlideModel(R.layout.item_banerprediksi,null, ScaleTypes.FIT));
//        slideModels.add(new SlideModel(R.layout.item_banerprediksi,null, ScaleTypes.FIT));
//        slideModels.add(new SlideModel(R.layout.item_banerprediksi,null, ScaleTypes.FIT));
//        slideModels.add(new SlideModel(R.layout.item_banerprediksi,null, ScaleTypes.FIT));
//
//        imageSlider.setImageList(slideModels);

        //grafik
        BarChart barChart = findViewById(R.id.barchart);

        ArrayList<BarEntry> visitors = new ArrayList<>();
        visitors.add(new BarEntry(2014,420));
        visitors.add(new BarEntry(2015,250));
        visitors.add(new BarEntry(2016,320));
        visitors.add(new BarEntry(2017,410));
        visitors.add(new BarEntry(2018,620));

        BarDataSet barDataSet = new BarDataSet(visitors, "Visitors");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Prediksi Trend Penjualan");
        barChart.animateY(2000);
    }
    }