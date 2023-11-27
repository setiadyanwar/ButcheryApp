package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class HomeTokoPage extends AppCompatActivity {
    TextView nama_toko;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    private ProdukAdapter produkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_toko_page);

        ImageButton btnproduktoko = findViewById(R.id.btn_produktoko);
        ImageView btnback = findViewById(R.id.btn_back);
        ImageButton btnpesanan = findViewById(R.id.pesanan);
        ImageButton btntrend = findViewById(R.id.aitrend);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);
        String id_user = sharedPref.getString("id_user","");


        btnproduktoko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeTokoPage.this,ManageProdukToko.class);
                startActivity(intent);
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeTokoPage.this,ProfilePage.class);
                startActivity(i);
            }
        });
        btnpesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeTokoPage.this,PesananPage.class);
                startActivity(i);
            }
        });

        btntrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeTokoPage.this,PrekdiksiTrend.class);
                startActivity(i);
            }
        });
        getSupplierByID(id_user);
    }
    private void getSupplierByID(String id_user) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getSupplierBYKonsumenID?user_id=" + id_user;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                       nama_toko = findViewById(R.id.nama_toko_supplier);
                       nama_toko.setText(jsonObject.getString("nama_toko"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeTokoPage.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(HomeTokoPage.this, "Error: Gagal mengambil data supplier", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}