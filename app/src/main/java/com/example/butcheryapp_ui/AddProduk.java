package com.example.butcheryapp_ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class AddProduk extends AppCompatActivity {
    String supplier_id, nama_toko, id_alamat, alamat, foto1, foto2, foto3;
    EditText nama_produk, deskripsi, varian1, harga1, stok1, varian2, harga2, stok2, varian3, harga3, stok3;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private ImageView mainImage;
    private ImageView firstImage;
    private ImageView secondImage;
    private ImageView thirdImage;

    private TextView simpan;

    Uri imageUri;

    private final int MAIN_IMAGE_REQUEST_CODE = 1;
    private final int SECOND_IMAGE_REQUEST_CODE = 2;
    private final int THIRD_IMAGE_REQUEST_CODE = 3;

    private Spinner kategoriSpinner;

    private List<KategoriModel> kategoriList;

    private final ActivityResultLauncher<Intent> openGalleryMain = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        setImageUri(imageUri);
                        if(imageUri != null){
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();

                                // Mengencode byte array menjadi string Base64
                                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);


                                // Mengambil path file gambar dari URI
                                String imagePath = getImagePath(imageUri);

                                //Ubah UrI jadi JSON ENCODE

                                // Mengambil hanya nama file dari path
                                File imageFile = new File(imagePath);
                                String imageName = imageFile.getName();

                                // Simpan path file ke variabel foto3
                                foto1 = encodedImage;
                                //changeFotoToLink(foto1);
                                //Toast.makeText(this, "foto 1" + foto1, Toast.LENGTH_SHORT).show();

                                // Set gambar langsung ke ImageView menggunakan setImageURI
                                mainImage.setImageURI(imageUri);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            foto1 = "Tidak upload foto";
                        }
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> openGallerySecond = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if(imageUri != null){
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();

                                // Mengencode byte array menjadi string Base64
                                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);


                                // Mengambil path file gambar dari URI
                                String imagePath = getImagePath(imageUri);

                                // Mengambil hanya nama file dari path
                                File imageFile = new File(imagePath);
                                String imageName = imageFile.getName();

                                // Simpan path file ke variabel foto3
                                foto2 = encodedImage;
                                //changeFotoToLink(foto2);
                                //Toast.makeText(this, "foto 2 : " + foto2, Toast.LENGTH_SHORT).show();

                                // Set gambar langsung ke ImageView menggunakan setImageURI
                                secondImage.setImageURI(imageUri);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            foto2 = "Tidak upload foto";
                        }

                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> openGalleryThird = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if(imageUri != null){
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();

                                // Mengencode byte array menjadi string Base64
                                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);



                                // Mengambil path file gambar dari URI
                                String imagePath = getImagePath(imageUri);

                                // Mengambil hanya nama file dari path
                                File imageFile = new File(imagePath);
                                String imageName = imageFile.getName();

                                // Simpan path file ke variabel foto3
                                foto3 = encodedImage;
                                //changeFotoToLink(foto3);
                                //Toast.makeText(this, "foto 3 :" + foto3, Toast.LENGTH_SHORT).show();

                                // Set gambar langsung ke ImageView menggunakan setImageURI
                                thirdImage.setImageURI(imageUri);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            foto3 = "Tidak upload gambar";
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produk);

        mainImage = findViewById(R.id.mainproduk);
        secondImage = findViewById(R.id.var1);
        thirdImage = findViewById(R.id.var2);

        mainImage.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            openGalleryMain.launch(i);
        });

        secondImage.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            openGallerySecond.launch(i);
        });

        thirdImage.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            openGalleryThird.launch(i);
        });

        kategoriSpinner = findViewById(R.id.kategori);

        final StringBuilder id_kategori = new StringBuilder();

        kategoriList = new ArrayList<>();

        ArrayAdapter<KategoriModel> kategoriAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriList);
        kategoriAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategoriSpinner.setAdapter(kategoriAdapter);

        kategoriSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                KategoriModel selectedKategori = (KategoriModel) parentView.getItemAtPosition(position);
                String kategoriSlug = selectedKategori.getSlug(); // Ambil slug kategori sebagai string
                id_kategori.setLength(0); // Bersihkan StringBuilder sebelum menambahkan
                id_kategori.append(kategoriSlug); // Tambahkan slug ke StringBuilder
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        getKategoriProduk();

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String id_user = sharedPref.getString("id_user", "");

        getSupplierByUserID(id_user);

        Intent intent = getIntent();
        String id_produk = intent.getStringExtra("id_produk");

        TextView btn_simpan = findViewById(R.id.simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nama_produk,deskripsi,variasi1,harga1,stok1,variasi2,harga2,stok2,variasi3,harga3,stok3;

                nama_produk = findViewById(R.id.nama_produk);
                deskripsi = findViewById(R.id.deskripsi);

                String namaProdukValue = nama_produk.getText().toString();
                String deskripsiValue = deskripsi.getText().toString();

                variasi1 = findViewById(R.id.variasi1);
                harga1 = findViewById(R.id.harga1);
                stok1 = findViewById(R.id.stok1);

                String variasi1Value = variasi1.getText().toString();
                String harga1Value = harga1.getText().toString();
                String stok1Value = stok1.getText().toString();

                variasi2 = findViewById(R.id.variasi2);
                harga2 = findViewById(R.id.harga2);
                stok2 = findViewById(R.id.stok2);

                String variasi2Value = variasi2.getText().toString();
                String harga2Value = harga2.getText().toString();
                String stok2Value = stok2.getText().toString();

                variasi3 = findViewById(R.id.variasi3);
                harga3 = findViewById(R.id.harga3);
                stok3 = findViewById(R.id.stok3);

                String variasi3Value = variasi3.getText().toString();
                String harga3Value = harga3.getText().toString();
                String stok3Value = stok3.getText().toString();

                if(TextUtils.isEmpty(namaProdukValue) || TextUtils.isEmpty(deskripsiValue)){
                    Toast.makeText(AddProduk.this, "Isi semua field dahulu!", Toast.LENGTH_LONG).show();
                }

                insertUpdateDataProduk((id_produk == null) ? "" : id_produk,supplier_id,nama_toko,id_alamat,alamat,foto1,foto2,foto3,namaProdukValue,id_kategori.toString(),deskripsiValue,(variasi1Value.isEmpty()) ? "" : variasi1Value,(harga1Value.isEmpty()) ? "" : harga1Value,(stok1Value.isEmpty()) ? "" : stok1Value,(variasi2Value.isEmpty()) ? "" : variasi2Value,(harga2Value.isEmpty()) ? "" : harga2Value,(stok2Value.isEmpty()) ? "" : stok2Value,(variasi3Value.isEmpty()) ? "" : variasi3Value,(harga3Value.isEmpty()) ? "" : harga3Value,(stok3Value.isEmpty()) ? "" : stok3Value);
            }
        });

            getProdukBYID(id_produk);
    }

    public void getKategoriProduk(){
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getKategoriProduk";

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<KategoriModel> kategoriModelList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        KategoriModel kategoriModel = new KategoriModel();

                        JSONObject nama_kategori = jsonObject.getJSONObject("nama_kategori");

                        kategoriModel.setSlug(nama_kategori.getString("slug"));

                        kategoriModelList.add(kategoriModel);
                    }
                    // Perbarui daftar kategoriModelList dengan data yang diterima dari API
                    kategoriList.clear();
                    kategoriList.addAll(kategoriModelList);

                    // Beritahu adapter bahwa data kategori telah berubah
                    ArrayAdapter<KategoriModel> adapter = (ArrayAdapter<KategoriModel>) kategoriSpinner.getAdapter();
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddProduk.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(AddProduk.this, "Error: Gagal mengambil data kategori", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    public void getSupplierByUserID(String id_user){
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getSupplierBYKonsumenID?user_id="+id_user;

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
                        supplier_id = jsonObject.getString("_id");
                        nama_toko = jsonObject.getString("nama_toko");

                        JSONArray fieldAlamat = jsonObject.getJSONArray("alamat");
                        JSONObject data = fieldAlamat.getJSONObject(0);

                        id_alamat = data.getString("kota");
                    }

                    getKotaByID(id_alamat);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddProduk.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(AddProduk.this, "Error: Gagal mengambil data kategori", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    public void getKotaByID(String id_alamat){
        String url = "https://www.emsifa.com/api-wilayah-indonesia/api/regency/"+id_alamat+".json";

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    alamat = jsonObject.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddProduk.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(AddProduk.this, "Error: Gagal mengambil data kota", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void insertUpdateDataProduk(String id_produk, String supplier_id, String nama_toko, String id_alamat, String alamat, String foto1, String foto2, String foto3, String nama_produk, String id_kategori, String deskripsi, String varian1, String harga1, String stok1, String varian2, String harga2, String stok2,String varian3, String harga3, String stok3) {
        String apiUrl = "";
        if(id_produk == ""){
            apiUrl = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/insertProdukBySupplier";
        }else {
            apiUrl = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/updateProdukByID?id="+id_produk;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    Intent intent = new Intent(AddProduk.this, ManageProdukToko.class);

                    SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("login", true);
                    editor.putString("imageUri", String.valueOf(getImageUri()));
                    editor.apply();

                    startActivity(intent);



                }else {
                    Intent intent = new Intent(AddProduk.this, AddProduk.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddProduk.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("supplier_id", supplier_id);
                params.put("nama_toko", nama_toko);
                params.put("id_alamat", id_alamat);
                params.put("alamat", alamat);

                params.put("foto1", foto1);
                params.put("foto2", foto2);
                params.put("foto3", foto3);

                params.put("nama_produk", nama_produk);
                params.put("id_kategori", id_kategori);
                params.put("deskripsi", deskripsi);

                params.put("varian1", varian1);
                params.put("harga1", harga1);
                params.put("stok1",stok1);

                params.put("varian2", varian2);
                params.put("harga2", harga2);
                params.put("stok2",stok2);

                params.put("varian3", varian3);
                params.put("harga3", harga3);
                params.put("stok3",stok3);

                return params;
            }

        };

        // Add the request to the request queue
        mRequestQueue.add(stringRequest);
    }

    public void getProdukBYID(String id_produk){

        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getProdukByID?id="+id_produk;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    EditText nama_produk, deskripsi, varian1, harga1, stok1, varian2, harga2, stok2, varian3, harga3, stok3;
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id_kategori_produk = jsonObject.getString("id_kategori");
                        JSONArray getVarianProduk = jsonObject.getJSONArray("varian");
                        JSONObject getFotoProduk = jsonObject.getJSONObject("foto");

                        nama_produk = findViewById(R.id.nama_produk);
                        deskripsi = findViewById(R.id.deskripsi);

                        varian1 = findViewById(R.id.variasi1);
                        harga1 = findViewById(R.id.harga1);
                        stok1 = findViewById(R.id.stok1);

                        varian2 = findViewById(R.id.variasi2);
                        harga2 = findViewById(R.id.harga2);
                        stok2 = findViewById(R.id.stok2);

                        varian3 = findViewById(R.id.variasi3);
                        harga3 = findViewById(R.id.harga3);
                        stok3 = findViewById(R.id.stok3);

                        foto1 = getFotoProduk.getString("foto1");
                        foto2 = getFotoProduk.getString("foto2");
                        foto3 = getFotoProduk.getString("foto3");



                        nama_produk.setText(jsonObject.getString("nama_produk"));
                        deskripsi.setText(jsonObject.getString("deskripsi"));

                        // Setel indeks kategori yang sesuai sebagai item yang terpilih di Spinner
                        for (int j = 0; j < kategoriList.size(); j++) {
                            if (kategoriList.get(j).getSlug().equals(id_kategori_produk)) {
                                kategoriSpinner.setSelection(j);
                                break;
                            }
                        }

                        varian1.setText(getVarianProduk.getJSONObject(0).getString("varian1"));
                        harga1.setText(getVarianProduk.getJSONObject(0).getString("harga1"));
                        stok1.setText(getVarianProduk.getJSONObject(0).getString("stok1"));

                        varian2.setText(getVarianProduk.getJSONObject(1).getString("varian2"));
                        harga2.setText(getVarianProduk.getJSONObject(1).getString("harga2"));
                        stok2.setText(getVarianProduk.getJSONObject(1).getString("stok2"));

                        varian3.setText(getVarianProduk.getJSONObject(2).getString("varian3"));
                        harga3.setText(getVarianProduk.getJSONObject(2).getString("harga3"));
                        stok3.setText(getVarianProduk.getJSONObject(2).getString("stok3"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddProduk.this, "tidak ada data", Toast.LENGTH_SHORT).show();
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

    // Fungsi untuk mengambil path file dari URI gambar
    private String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(column_index);
            cursor.close();
            return imagePath;
        } else {
            return null;
        }
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}