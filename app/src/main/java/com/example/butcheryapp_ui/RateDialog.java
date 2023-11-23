package com.example.butcheryapp_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

public class RateDialog extends Dialog {

    private float userRate = 0;

    public RateDialog(@NonNull Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_dialog);

        Button btnbatal = findViewById(R.id.batal);
        Button btnkirim = findViewById(R.id.kirim);

        RatingBar ratingBar = findViewById(R.id.ratingbar);
        ImageView ekspresi = findViewById(R.id.ekspresi);


        btnkirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //buat kirim rating
            }
        });

        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //buat batal kirim
                dismiss();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if (rating <= 1){
                    ekspresi.setImageResource(R.drawable.emoji_1);
                }
                else if (rating <=2){
                    ekspresi.setImageResource(R.drawable.emoji_2);
                }
                else if (rating <=3){
                    ekspresi.setImageResource(R.drawable.emoji_3);
                }
                else if (rating <=4){
                    ekspresi.setImageResource(R.drawable.emoji_4);
                }
                else if (rating <=5){
                    ekspresi.setImageResource(R.drawable.emoji_5);
                }

                //animate emoji
                animateImage(ekspresi);
            }
        });

    }

    private void animateImage(ImageView ekspresi){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1f,0,1f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ekspresi.startAnimation(scaleAnimation);
    }


}