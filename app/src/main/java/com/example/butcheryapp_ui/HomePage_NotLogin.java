package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class HomePage_NotLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_notlogin);

        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.slide1,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide2,null, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slide3,null, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels);

        ImageButton btnMasuk = findViewById(R.id.btn_masuk);
        ImageButton btnProfile = findViewById(R.id.nav_profile);

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage_NotLogin.this, LoginPage.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage_NotLogin.this, ProfilePage.class);
                startActivity(intent);
            }
        });
    }
}
