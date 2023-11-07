package com.example.butcheryapp_ui;

import static com.android.volley.VolleyLog.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

//import org.mindrot.jbcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;


public class RegisPageFinish extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private Spinner provinsiSpinner;
    private Spinner kotaSpinner;
    private Spinner kecamatanSpinner;

    private List<ProvinceModel> provinceList;
    private List<RegencyModel> regencyList;
    private List<DistrictModel> districtList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis_page_finish);

        Intent intent = getIntent();
        String identifierValue = intent.getStringExtra("identifier");
        String passwordValue = intent.getStringExtra("password");

        final StringBuilder dataProv = new StringBuilder();
        final StringBuilder dataKota = new StringBuilder();
        final StringBuilder dataKec = new StringBuilder();

        provinsiSpinner = findViewById(R.id.provinsi);
        kotaSpinner = findViewById(R.id.kota);
        kecamatanSpinner = findViewById(R.id.kecamatan);

        provinceList = new ArrayList<>();
        regencyList = new ArrayList<>();
        districtList = new ArrayList();

        ArrayAdapter<ProvinceModel> provinsiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinceList);
        provinsiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinsiSpinner.setAdapter(provinsiAdapter);

        ArrayAdapter<RegencyModel> kotaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regencyList);
        kotaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kotaSpinner.setAdapter(kotaAdapter);

        ArrayAdapter<DistrictModel> kecamatanAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districtList);
        kecamatanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kecamatanSpinner.setAdapter(kecamatanAdapter);

        provinsiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Perbarui isi Spinner Kota/Kabupaten berdasarkan provinsi yang dipilih
                ProvinceModel selectedProvince = (ProvinceModel) parentView.getItemAtPosition(position);
                String provinceId = selectedProvince.getId(); // Ambil id prov sebagai string
                dataProv.setLength(0); // Bersihkan StringBuilder sebelum menambahkan
                dataProv.append(provinceId); // Tambahkan id prov ke StringBuilder

                fetchRegencies(selectedProvince);
                List<RegencyModel> filteredRegencies = filterRegenciesByProvince(selectedProvince);
                ArrayAdapter<RegencyModel> adapter = (ArrayAdapter<RegencyModel>) kotaSpinner.getAdapter();
                adapter.clear();
                adapter.addAll(filteredRegencies);
                kotaSpinner.setSelection(0); // Reset ke pilihan pertama
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Mengatur Listener untuk Spinner Kota/Kabupaten
        kotaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Perbarui isi Spinner Kecamatan berdasarkan kota/kabupaten yang dipilih
                RegencyModel selectedRegency = (RegencyModel) parentView.getItemAtPosition(position);
                String regencyId = selectedRegency.getId(); // Ambil id kota sebagai string
                dataKota.setLength(0); // Bersihkan StringBuilder sebelum menambahkan
                dataKota.append(regencyId); // Tambahkan id kota ke StringBuilder

                fetchDistrics(selectedRegency);
                List<DistrictModel> filteredDistricts = filterDistrictsByRegency(selectedRegency);
                ArrayAdapter<DistrictModel> adapter = (ArrayAdapter<DistrictModel>) kecamatanSpinner.getAdapter();
                adapter.clear();
                adapter.addAll(filteredDistricts);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        kecamatanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                DistrictModel selectedDistrict = (DistrictModel) parentView.getItemAtPosition(position);
                String districtId = selectedDistrict.getId(); // Ambil id kecamatan sebagai string
                dataKec.setLength(0); // Bersihkan StringBuilder sebelum menambahkan
                dataKec.append(districtId); // Tambahkan id kecamatan ke StringBuilder
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        Button btndaftarakun = findViewById(R.id.btn_masuk_regis);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisPageFinish.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        btndaftarakun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText getUsername,getAlamat;

                getUsername = findViewById(R.id.username);
                getAlamat = findViewById(R.id.alamat);

                String usernameValue = getUsername.getText().toString();
                String alamatValue = getAlamat.getText().toString();

                if(TextUtils.isEmpty(usernameValue) || TextUtils.isEmpty(alamatValue)){
                    Toast.makeText(RegisPageFinish.this, "Isi semua field terlebih dahulu", Toast.LENGTH_LONG).show();
                }

                insertDataKonsumens(identifierValue,passwordValue,usernameValue,dataProv.toString(),dataKota.toString(),dataKec.toString(),alamatValue);
            }
        });

        mRequestQueue = Volley.newRequestQueue(this);
        fetchProvinces();
    }

    private void fetchProvinces() {
        String apiUrl = "https://www.emsifa.com/api-wilayah-indonesia/api/provinces.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<ProvinceModel> provList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ProvinceModel province = new ProvinceModel();

                        String getId = jsonObject.getString("id");
                        String getProvince = jsonObject.getString("name");

                        province.setId(getId);
                        province.setName(getProvince);

                        provList.add(province);
                    }

                    // Perbarui daftar provinceList dengan data yang diterima dari API
                    provinceList.clear();
                    provinceList.addAll(provList);

                    // Beritahu adapter bahwa data provinsi telah berubah
                    ArrayAdapter<ProvinceModel> adapter = (ArrayAdapter<ProvinceModel>) provinsiSpinner.getAdapter();
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisPageFinish.this, "Error fetching provinces: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(stringRequest);
    }

    private void fetchRegencies(ProvinceModel selectedProvince) {
        String apiUrl = "https://www.emsifa.com/api-wilayah-indonesia/api/regencies/" + selectedProvince.getId() + ".json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<RegencyModel> regencyList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        RegencyModel regency = new RegencyModel();

                        String getId = jsonObject.getString("id");
                        String getProvId = jsonObject.getString("province_id");
                        String getName = jsonObject.getString("name");

                        regency.setId(getId);
                        regency.setProvince_id(getProvId);
                        regency.setName(getName);

                        regencyList.add(regency);
                    }

                    // Perbarui daftar kota (regencyList) dengan data yang diterima dari API
                    // Kemudian, beritahu adapter bahwa data kota telah berubah

                    ArrayAdapter<RegencyModel> adapter = (ArrayAdapter<RegencyModel>) kotaSpinner.getAdapter();
                    adapter.clear();
                    adapter.addAll(regencyList);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisPageFinish.this, "Error fetching regencies: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(stringRequest);
    }

    private void fetchDistrics(RegencyModel selectedRegency) {
        String apiUrl = "https://www.emsifa.com/api-wilayah-indonesia/api/districts/" + selectedRegency.getId() + ".json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<DistrictModel>  districtList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        DistrictModel district = new DistrictModel();

                        String getId = jsonObject.getString("id");
                        String getRegenId = jsonObject.getString("regency_id");
                        String getName = jsonObject.getString("name");

                        district.setId(getId);
                        district.setRegency_id(getRegenId);
                        district.setName(getName);

                        districtList.add(district);
                    }

                    ArrayAdapter<DistrictModel> adapter = (ArrayAdapter<DistrictModel>) kecamatanSpinner.getAdapter();
                    adapter.clear();
                    adapter.addAll(districtList);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisPageFinish.this, "Error fetching regencies: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(stringRequest);
    }

    private List<RegencyModel> filterRegenciesByProvince(ProvinceModel province) {
        List<RegencyModel> filteredRegencies = new ArrayList<>();
        for (RegencyModel regency : regencyList) {
            if (regency.getProvince_id().equals(province.getId())) {
                filteredRegencies.add(regency);
            }
        }
        return filteredRegencies;
    }

    private List<DistrictModel> filterDistrictsByRegency(RegencyModel regency) {
        List<DistrictModel> filteredDistricts = new ArrayList<>();
        for (DistrictModel district : districtList) {
            if (district.getRegency_id().equals(regency.getId())) {
                filteredDistricts.add(district);
            }
        }
        return filteredDistricts;
    }

    private void insertDataKonsumens(String identifier, String password, String username, String prov, String kota, String kecamatan, String alamat) {
        String apiUrl = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/insertKonsumen";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    Intent intent = new Intent(RegisPageFinish.this, LoginPage.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(RegisPageFinish.this, RegisPageFinish.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisPageFinish.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                    params.put("username", username);

                    if (isEmailValid(identifier)) {
                        params.put("email", identifier);
                        params.put("no_hp", "");
                    } else if (isPhoneNumber(identifier)) {
                        params.put("email", "");
                        params.put("no_hp", identifier);
                    }

                    params.put("provinsi", prov);
                    params.put("kota", kota);
                    params.put("kecamatan", kecamatan);
                    params.put("alamat", alamat);

                    BCrypt.Hasher hasher = BCrypt.withDefaults();
                    String hashPassword = hasher.hashToString(12,password.toCharArray());

                    params.put("password", hashPassword);

                    params.put("role", "konsumen");

                    return params;
            }

        };

        // Add the request to the request queue
        mRequestQueue.add(stringRequest);
    }
    public boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean isPhoneNumber(String phoneNumber) {
        // Define a regular expression pattern for a valid phone number
        String regex = "^(\\+[0-9]+)?[0-9-]+$";

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(regex);

        // Match the phone number against the pattern
        Matcher matcher = pattern.matcher(phoneNumber);

        // Return true if it's a valid phone number, otherwise false
        return matcher.matches();
    }


}