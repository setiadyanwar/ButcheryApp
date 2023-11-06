package com.example.butcheryapp_ui;

import static com.android.volley.VolleyLog.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class HomePage_NotLogin extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getAllRekomendasiProduk";

//    card produk recycleviewnya
    //private RecyclerView recyclerView;
    //RecyclerView.LayoutManager layoutManager;
    //AdapterCardProdukHomeNotLogin adapterCardProdukHomeNotLogin;

//    data array gambar
    //int []arr= {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_notlogin);
        //recyclerView=findViewById(R.id.cardproduk);
        //layoutManager=new GridLayoutManager(this,2);
        //recyclerView.setLayoutManager(layoutManager);

//        import pake array
        //adapterCardProdukHomeNotLogin=new AdapterCardProdukHomeNotLogin(arr);

        //recyclerView.setAdapter(adapterCardProdukHomeNotLogin);

//        fix ukuran gambar
        //recyclerView.setHasFixedSize(true);


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

        getDataRekProduk();
    }
    private void getDataRekProduk() {
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String getSupplierId = jsonObject.getString("supplier_id");
                        String getNamaToko = jsonObject.getString("nama_toko");
                        JSONObject getAlamatToko = jsonObject.getJSONObject("alamat_toko");
                        JSONObject getFotoProduk = jsonObject.getJSONObject("foto");
                        String getNamaProduk = jsonObject.getString("nama_produk");
                        String getIdKategori = jsonObject.getString("id_kategori");
                        String getDeskripsiProduk = jsonObject.getString("deskripsi");
                        JSONArray getVarianProduk = jsonObject.getJSONArray("varian");

                        LinearLayout cardProdukLayout = findViewById(R.id.cardproduk);
                        View itemProdukView = getLayoutInflater().inflate(R.layout.item_rekproduk, null);

                        TextView nama_produk = itemProdukView.findViewById(R.id.nama_produk);
                        TextView harga_produk = itemProdukView.findViewById(R.id.harga_produk);
                        TextView nama_toko = itemProdukView.findViewById(R.id.nama_toko);
                        TextView alamat_toko = itemProdukView.findViewById(R.id.alamat_toko);

                        nama_produk.setText(getNamaProduk);

                        String getHargaProduk = getVarianProduk.getJSONObject(0).getString("harga");
                        Currency customCurrency = Currency.getInstance("IDR");

                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                        currencyFormat.setCurrency(customCurrency);

                        String formattedCurrency = currencyFormat.format(Integer.parseInt(getHargaProduk));
                        harga_produk.setText(formattedCurrency);

                        nama_toko.setText(getNamaToko);

                        String getDetailedAlamatToko = getAlamatToko.getString("alamat");
                        alamat_toko.setText(getDetailedAlamatToko);

                        cardProdukLayout.addView(itemProdukView);
                    }

                    Toast.makeText(HomePage_NotLogin.this, "response : ", Toast.LENGTH_LONG).show();

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }
}
