package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
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

import java.text.NumberFormat;
import java.util.Currency;

public class DetailProduk extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        Intent intent = getIntent();
        String id_produk = intent.getStringExtra("id_produk");

        Toast.makeText(this, "id _ produk : " + id_produk, Toast.LENGTH_SHORT).show();

        getProdukByID(id_produk);
    }
    public void getProdukByID(String id_produk){

        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getProdukByID?id="+id_produk;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    TextView nama_produk, deskripsi, harga_produk, nama_toko, alamat_toko;
                    RadioButton varian1, varian2, varian3;
                    JSONArray jsonArray = new JSONArray(response);
                    Toast.makeText(DetailProduk.this, "res", Toast.LENGTH_SHORT).show();
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        JSONArray getVarianProduk = jsonObject.getJSONArray("varian");

                        Currency customCurrency = Currency.getInstance("IDR");
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                        currencyFormat.setCurrency(customCurrency);
                        String hargaVarian1 = currencyFormat.format(Integer.parseInt(getVarianProduk.getJSONObject(0).getString("harga1")));

                        Currency customCurrency2 = Currency.getInstance("IDR");
                        NumberFormat currencyFormat2 = NumberFormat.getCurrencyInstance();
                        currencyFormat2.setCurrency(customCurrency2);
                        String hargaVarian2 = currencyFormat2.format(Integer.parseInt(getVarianProduk.getJSONObject(1).getString("harga2")));

                        Currency customCurrency3 = Currency.getInstance("IDR");
                        NumberFormat currencyFormat3 = NumberFormat.getCurrencyInstance();
                        currencyFormat3.setCurrency(customCurrency3);
                        String hargaVarian3 = currencyFormat3.format(Integer.parseInt(getVarianProduk.getJSONObject(2).getString("harga3")));

                        JSONObject getFotoProduk = jsonObject.getJSONObject("foto");
                        JSONObject getAlamatToko = jsonObject.getJSONObject("alamat_toko");


                        nama_produk = findViewById(R.id.detail_nama_produk);
                        deskripsi = findViewById(R.id.detail_deskripsi_produk);
                        harga_produk = findViewById(R.id.detail_harga_produk);
                        varian1 = findViewById(R.id.varian1);
                        varian2 = findViewById(R.id.varian2);
                        varian3 = findViewById(R.id.varian3);
                        nama_toko = findViewById(R.id.detail_nama_toko);
                        alamat_toko = findViewById(R.id.detail_alamat_toko);

                        nama_produk.setText(jsonObject.getString("nama_produk"));
                        deskripsi.setText(jsonObject.getString("deskripsi"));

                        varian1.setText(getVarianProduk.getJSONObject(0).getString("varian1"));
                        varian1.setBackground(Drawable.createFromPath("@drawable/radio_selected"));
                        harga_produk.setText(hargaVarian1);

                        nama_toko.setText(truncateText(jsonObject.getString("nama_toko"),5));
                        alamat_toko.setText(getAlamatToko.getString("alamat"));

                        varian1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RadioButton varian1,varian2,varian3;
                                TextView harga_produk = findViewById(R.id.detail_harga_produk);

                                varian1 = findViewById(R.id.varian1);
                                varian1.setBackground(Drawable.createFromPath("@drawable/radio_selected"));

                                varian2 = findViewById(R.id.varian2);
                                varian2.setBackground(Drawable.createFromPath("@drawable/radio_notselected"));

                                varian3 = findViewById(R.id.varian3);
                                varian3.setBackground(Drawable.createFromPath("@drawable/radio_notselected"));

                                harga_produk.setText(hargaVarian1);
                            }
                        });

                        varian2.setText(getVarianProduk.getJSONObject(1).getString("varian2"));
                        varian2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RadioButton varian1,varian2,varian3;
                                TextView harga_produk = findViewById(R.id.detail_harga_produk);

                                varian1 = findViewById(R.id.varian1);
                                varian1.setBackground(Drawable.createFromPath("@drawable/radio_notselected"));

                                varian2 = findViewById(R.id.varian2);
                                varian2.setBackground(Drawable.createFromPath("@drawable/radio_selected"));

                                varian3 = findViewById(R.id.varian3);
                                varian3.setBackground(Drawable.createFromPath("@drawable/radio_notselected"));

                                harga_produk.setText(hargaVarian2);
                            }
                        });

                        varian3.setText(getVarianProduk.getJSONObject(2).getString("varian3"));
                        varian3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RadioButton varian1,varian2,varian3;
                                TextView harga_produk = findViewById(R.id.detail_harga_produk);

                                varian1 = findViewById(R.id.varian1);
                                varian1.setBackground(Drawable.createFromPath("@drawable/radio_notselected"));

                                varian2 = findViewById(R.id.varian2);
                                varian2.setBackground(Drawable.createFromPath("@drawable/radio_notselected"));

                                varian3 = findViewById(R.id.varian3);
                                varian3.setBackground(Drawable.createFromPath("@drawable/radio_selected"));

                                harga_produk.setText(hargaVarian3);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DetailProduk.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(mStringRequest);
    }
    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        } else {
            return text.substring(0, maxLength) + " ..."; // Tambahkan elipsis (...) jika diperlukan.
        }
    }
}