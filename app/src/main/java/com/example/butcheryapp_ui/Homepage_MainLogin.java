package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class Homepage_MainLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_main_login);

//        SLIDER
        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide1,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide2,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide3,null, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels);

//        BTN
        ImageButton btnProfile = findViewById(R.id.nav_profile);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage_MainLogin.this, ProfilePage.class);
                startActivity(intent);
            }
        });
    }
}