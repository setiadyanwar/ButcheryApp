package com.example.butcheryapp_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class SearchPage extends AppCompatActivity {

    RadioButton Viewsemua, Viewterakhir, Viewpopuler, Viewbersertifikat, Viewtermurah;
    RadioGroup sectionsNav;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    private ProdukAdapter produkAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        ImageView btnback = findViewById(R.id.arrow);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchPage.this, Homepage_MainLogin.class);
                startActivity(i);
            }
        });

        recyclerView = findViewById(R.id.cardproduk);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        produkAdapter = new ProdukAdapter(new ArrayList<>());
        recyclerView.setAdapter(produkAdapter);

        Intent intent = getIntent();
        String search = intent.getStringExtra("search");
        String id_kategori = intent.getStringExtra("id_kategori");

        Viewsemua = findViewById(R.id.semua);
        Viewterakhir = findViewById(R.id.terakhir);
        Viewpopuler = findViewById(R.id.populer);
        Viewbersertifikat = findViewById(R.id.bersertifikat);
        Viewtermurah = findViewById(R.id.termurah);
        sectionsNav = findViewById(R.id.sections_nav);

        Fragment semuaFragment = new SemuaFragment();
        Fragment terakhirFragment = new TerakhirFragment();
        Fragment populerFragment = new PopulerFragment();
        Fragment bersertifikatFragment = new BersertifikatFragment();
        Fragment termurahFragment = new TermurahFragment();

        addFragment(semuaFragment, "SemuaFragment");

        sectionsNav.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.semua:
                    toggleFragment(semuaFragment);
                    break;
                case R.id.terakhir:
                    toggleFragment(terakhirFragment);
                    break;
                case R.id.populer:
                    toggleFragment(populerFragment);
                    break;
                case R.id.bersertifikat:
                    toggleFragment(bersertifikatFragment);
                    break;
                case R.id.termurah:
                    toggleFragment(termurahFragment);
                    break;
            }
        });

        getProdukByNamaOrKategori(search,id_kategori);
    }

    private void addFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            fragmentTransaction.add(R.id.toko_views_fragment_container, fragment, tag);
            fragmentTransaction.commit();
        }
    }

    private void toggleFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.toko_views_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getProdukByNamaOrKategori(String search, String id_kategori) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getProdukByNamaOrKategori?nama_produk="+ search  +"&id_kategori=" + id_kategori;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<ProdukModel> produkList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ProdukModel produk = new ProdukModel();

                        String getSupplierId = jsonObject.getString("supplier_id");
                        String getNamaToko = jsonObject.getString("nama_toko");
                        JSONObject getAlamatToko = jsonObject.getJSONObject("alamat_toko");
                        JSONObject getFotoProduk = jsonObject.getJSONObject("foto");
                        String getNamaProduk = jsonObject.getString("nama_produk");
                        String getIdKategori = jsonObject.getString("id_kategori");
                        String getDeskripsiProduk = jsonObject.getString("deskripsi");
                        JSONArray getVarianProduk = jsonObject.getJSONArray("varian");

                        //SET PRODUK KE DALAM PRODUK MODEL
                        produk.setSupplierID(getSupplierId);
                        produk.setId_produk(jsonObject.getString("_id"));

                        produk.setNamaProduk(getNamaProduk);

                        String getHargaProduk = getVarianProduk.getJSONObject(0).getString("harga1");
                        Currency customCurrency = Currency.getInstance("IDR");
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                        currencyFormat.setCurrency(customCurrency);
                        String formattedCurrency = currencyFormat.format(Integer.parseInt(getHargaProduk));
                        produk.setHargaProduk(formattedCurrency);

                        produk.setNamaToko(getNamaToko);

                        String getDetailedAlamatToko = getAlamatToko.getString("alamat");
                        produk.setAlamatToko(getDetailedAlamatToko);

                        produkList.add(produk);
                    }

                    produkAdapter.setProdukList(SearchPage.this,produkList);
                    produkAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SearchPage.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(SearchPage.this, "Error: Gagal mengambil data produk", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

}
