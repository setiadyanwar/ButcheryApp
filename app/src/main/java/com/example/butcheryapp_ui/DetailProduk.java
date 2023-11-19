package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.HashMap;
import java.util.Map;

public class DetailProduk extends AppCompatActivity {
    TextView nama_produk, deskripsi, harga_produk, nama_toko, alamat_toko;
    RadioButton varian1, varian2, varian3;
    Boolean isVarian1Selected, isVarian2Selected, isVarian3Selected;
    String supplier_id, getNamaProduk,getValVarianProduk,getHargaProduk,getQTYProduk, getSubtotalProduk, getNoteProduk, getValFotoProduk;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(DetailProduk.this, LoginPage.class);
            startActivity(intent);
        }

        Intent intent = getIntent();
        String id_produk = intent.getStringExtra("id_produk");

        Toast.makeText(this, "id _ produk : " + id_produk, Toast.LENGTH_SHORT).show();

        ImageButton btn_belilangsung = findViewById(R.id.buttonbeli);
        Button btn_addkeranjang = findViewById(R.id.btn_addkeranjang);

        btn_belilangsung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_addkeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                String id_user = sharedPref.getString("id_user","false");

                getQTYProduk = "1";
                getNoteProduk = "Tidak ada catatan";

                insertProdukToCart(id_user, id_produk, supplier_id, getNamaProduk,getValVarianProduk,getHargaProduk,getQTYProduk, getSubtotalProduk, getNoteProduk, getValFotoProduk);
            }
        });

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
                    JSONArray jsonArray = new JSONArray(response);
                    Toast.makeText(DetailProduk.this, "res", Toast.LENGTH_SHORT).show();
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        JSONArray getVarianProduk = jsonObject.getJSONArray("varian");

                        String getVarian1 = getVarianProduk.getJSONObject(0).getString("varian1");
                        String getVarian2 = getVarianProduk.getJSONObject(1).getString("varian2");
                        String getVarian3 = getVarianProduk.getJSONObject(2).getString("varian3");

                        String beforeFormattedHarga1 = getVarianProduk.getJSONObject(0).getString("harga1");
                        String beforeFormattedHarga2 = getVarianProduk.getJSONObject(1).getString("harga2");
                        String beforeFormattedHarga3 = getVarianProduk.getJSONObject(2).getString("harga3");

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

                        supplier_id = jsonObject.getString("supplier_id");
                        getValFotoProduk = getFotoProduk.getString("foto1");

                        nama_produk = findViewById(R.id.detail_nama_produk);
                        deskripsi = findViewById(R.id.detail_deskripsi_produk);
                        harga_produk = findViewById(R.id.detail_harga_produk);
                        varian1 = findViewById(R.id.varian1);
                        varian2 = findViewById(R.id.varian2);
                        varian3 = findViewById(R.id.varian3);
                        nama_toko = findViewById(R.id.detail_nama_toko);
                        alamat_toko = findViewById(R.id.detail_alamat_toko);

                        nama_produk.setText(jsonObject.getString("nama_produk"));
                        getNamaProduk = nama_produk.getText().toString();

                        deskripsi.setText(jsonObject.getString("deskripsi"));

                        varian1.setText(getVarian1);
                        varian1.setBackgroundResource(R.drawable.radio_selected);
                        getValVarianProduk = getVarian1;

                        harga_produk.setText(hargaVarian1);
                        getHargaProduk = beforeFormattedHarga1;
                        getSubtotalProduk = beforeFormattedHarga1;


                        nama_toko.setText(truncateText(jsonObject.getString("nama_toko"),5));
                        alamat_toko.setText(getAlamatToko.getString("alamat"));

                        varian1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RadioButton varian1,varian2,varian3;
                                TextView harga_produk = findViewById(R.id.detail_harga_produk);

                                varian1 = findViewById(R.id.varian1);
                                varian1.setBackgroundResource(R.drawable.radio_selected);
                                getValVarianProduk = getVarian1;

                                varian2 = findViewById(R.id.varian2);
                                varian2.setBackgroundResource(R.drawable.radio_notselected);

                                varian3 = findViewById(R.id.varian3);
                                varian3.setBackgroundResource(R.drawable.radio_notselected);

                                harga_produk.setText(hargaVarian1);
                                getHargaProduk = beforeFormattedHarga1;
                                getSubtotalProduk = beforeFormattedHarga1;

                                isVarian1Selected = true;
                                isVarian2Selected = false;
                                isVarian3Selected = false;
                            }
                        });

                        String varian2SetText = (TextUtils.isEmpty(getVarianProduk.getJSONObject(1).getString("varian2")) ? null : getVarianProduk.getJSONObject(1).getString("varian2"));
                        varian2.setText(varian2SetText);
                        varian2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RadioButton varian1,varian2,varian3;
                                TextView harga_produk = findViewById(R.id.detail_harga_produk);

                                varian1 = findViewById(R.id.varian1);
                                varian1.setBackgroundResource(R.drawable.radio_notselected);

                                varian2 = findViewById(R.id.varian2);
                                varian2.setBackgroundResource(R.drawable.radio_selected);
                                getValVarianProduk = getVarian2;

                                varian3 = findViewById(R.id.varian3);
                                varian3.setBackgroundResource(R.drawable.radio_notselected);

                                harga_produk.setText(hargaVarian2);
                                getHargaProduk = beforeFormattedHarga2;
                                getSubtotalProduk = beforeFormattedHarga2;


                                isVarian1Selected = false;
                                isVarian2Selected = true;
                                isVarian3Selected = false;
                            }
                        });

                        String varian3SetText = (TextUtils.isEmpty(getVarianProduk.getJSONObject(2).getString("varian3")) ? null : getVarianProduk.getJSONObject(2).getString("varian3"));
                        varian3.setText(varian3SetText);
                        varian3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RadioButton varian1,varian2,varian3;
                                TextView harga_produk = findViewById(R.id.detail_harga_produk);

                                varian1 = findViewById(R.id.varian1);
                                varian1.setBackgroundResource(R.drawable.radio_notselected);

                                varian2 = findViewById(R.id.varian2);
                                varian2.setBackgroundResource(R.drawable.radio_notselected);

                                varian3 = findViewById(R.id.varian3);
                                varian3.setBackgroundResource(R.drawable.radio_selected);
                                getValVarianProduk = getVarian3;

                                harga_produk.setText(hargaVarian3);
                                getHargaProduk = beforeFormattedHarga3;
                                getSubtotalProduk = beforeFormattedHarga3;

                                isVarian1Selected = false;
                                isVarian2Selected = false;
                                isVarian3Selected = true;
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

    private void insertProdukToCart(String user_id, String produk_id, String supplier_id, String nama_produk, String varian, String harga, String qty, String subtotal, String note, String foto) {
        String apiUrl = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/insertProdukToCart";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    Intent intent = new Intent(DetailProduk.this, CartPage.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(DetailProduk.this, DetailProduk.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailProduk.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("user_id", user_id);
                params.put("produk_id", produk_id);
                params.put("supplier_id", supplier_id);
                params.put("nama_produk", nama_produk);
                params.put("varian", varian);
                params.put("harga", harga);
                params.put("qty", qty);
                params.put("subtotal", subtotal);
                params.put("note", note);
                params.put("foto", foto);

                return params;
            }

        };

        // Add the request to the request queue
        mRequestQueue.add(stringRequest);
    }

    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        } else {
            return text.substring(0, maxLength) + " ..."; // Tambahkan elipsis (...) jika diperlukan.
        }
    }
}