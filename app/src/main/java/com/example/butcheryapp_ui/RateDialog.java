package com.example.butcheryapp_ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

public class RateDialog extends Dialog {

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private Double userRate = 0.0;
    private PesananSayaPage pesananSayaPage;
    private String id_pesanan;
    private String id_user;
    private String id_produk;

    public RateDialog(@NonNull Context context,String id_pesanan ,String id_user, String id_produk){
        super(context);
        this.id_user = id_user;
        this.id_produk = id_produk;
        this.id_pesanan = id_pesanan;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_dialog);

        Button btnbatal = findViewById(R.id.batal);
        Button btnkirim = findViewById(R.id.kirim);

        RatingBar ratingBar = findViewById(R.id.ratingbar);
        ImageView ekspresi = findViewById(R.id.ekspresi);

        EditText ulasan = findViewById(R.id.ulasan);

        btnkirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUlasan = ulasan.getText().toString();

                //Toast.makeText(getContext(), "ulasan : " + getUlasan, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(), "id_user : " + id_user, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(), "id_produk : " + id_produk, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(), "rating : " + userRate, Toast.LENGTH_SHORT).show();

                insertDataRReviews(id_user,id_produk,getUlasan,userRate);
                changeStatusByPesananID("Sudah dinilai", id_pesanan);
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

                if(rating >= 0 && rating < 1){
                    ekspresi.setImageResource(R.drawable.emoji_1);
                    userRate = 0.5;
                }
                else if (rating <= 1){
                    ekspresi.setImageResource(R.drawable.emoji_1);
                    userRate = 1.0;

                }else if (rating >= 1 && rating < 2 ){
                    ekspresi.setImageResource(R.drawable.emoji_2);
                    userRate = 1.5;
                }
                else if (rating <=2){
                    ekspresi.setImageResource(R.drawable.emoji_2);
                    userRate = 2.0;
                }
                else if(rating >= 2 && rating < 3){
                    ekspresi.setImageResource(R.drawable.emoji_2);
                    userRate = 2.5;
                }
                else if (rating <=3){
                    ekspresi.setImageResource(R.drawable.emoji_3);
                    userRate = 3.0;
                }
                else if(rating >= 3 && rating < 4){
                    ekspresi.setImageResource(R.drawable.emoji_3);
                    userRate = 3.5;
                }
                else if (rating >=4){
                    ekspresi.setImageResource(R.drawable.emoji_4);
                    userRate = 4.0;

                }else if(rating >= 4 && rating < 5){
                    ekspresi.setImageResource(R.drawable.emoji_4);
                    userRate = 4.5;
                }
                else if (rating == 5){
                    ekspresi.setImageResource(R.drawable.emoji_5);
                    userRate = 5.0;
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

    private void insertDataRReviews(String id_user, String id_produk,String ulasan, Double rating) {
        String apiUrl = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/insertDataToRReviews";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    Intent intent = new Intent(getContext(), PesananSayaPage.class);
                    getContext().startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), PesananSayaPage.class);
                    getContext().startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id_user", id_user);
                params.put("id_produk", id_produk);
                params.put("reviews", ulasan);
                params.put("ratings", String.valueOf(rating));

                return params;
            }

        };

        // Add the request to the request queue
        mRequestQueue.add(stringRequest);
    }

    private void changeStatusByPesananID(String status, String id) {
        String apiUrl = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/changeStatusByPesananID?id=" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.isEmpty()) {
                            Toast.makeText(getContext(), "Status gagal diubah", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Status berhasil diubah", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("status", status);
                return params;
            }

        };

        // Add the request to the request queue
        mRequestQueue.add(stringRequest);

    }
}