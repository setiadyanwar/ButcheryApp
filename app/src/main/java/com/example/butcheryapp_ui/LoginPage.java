package com.example.butcheryapp_ui;

import static com.android.volley.VolleyLog.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.mindrot.jbcrypt.BCrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

public class LoginPage extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);

        if (isLoggedIn) {
            Intent intent = new Intent(LoginPage.this, Homepage_MainLogin.class);
            startActivity(intent);
            finish();
        }

        TextView linkdaftar = findViewById(R.id.daftardulu);
        Button btnMasuk = findViewById(R.id.btn_masuk_login);
        ImageButton btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, HomePage_NotLogin.class);
                startActivity(intent);
                finish();
            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText identifier, password;

                identifier = findViewById(R.id.identifier);
                password = findViewById(R.id.password);

                String getIdentifier = identifier.getText().toString();
                String getPassword = password.getText().toString();

                if (TextUtils.isEmpty(getIdentifier) || TextUtils.isEmpty(getPassword)) {
                    Toast.makeText(LoginPage.this, "Isi semua field terlebih dahulu", Toast.LENGTH_LONG).show();
                } else {
                    getKonsumenByIdentifier(getIdentifier, getPassword);
                }
            }
        });

        linkdaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(i);
            }
        });
    }

    private void getKonsumenByIdentifier(String identifier, String password) {
        String url = "";

        if (isEmailValid(identifier)) {
            url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getKonsumenByEmail?email=" + identifier;
        } else if(isPhoneNumber(identifier)) {
            url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getKonsumenByNoHP?no_hp=" + identifier;
        }else{
            Toast.makeText(this, "Masukkan Email atau No Telp", Toast.LENGTH_LONG).show();
        }

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    boolean loginSuccessful = false;
                    String id_user = "";
                    String role = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String getIDUser = jsonObject.getString("_id");
                        String getRole = jsonObject.getString("role");
                        String getPassword = jsonObject.getString("password");

                        Verifyer verify = BCrypt.verifyer();
                        Result cocokPass = verify.verify(password.toCharArray(),getPassword);
                        Boolean result = cocokPass.verified;

                        if(result){
                            loginSuccessful = true;
                            id_user = getIDUser;
                            role = getRole;
                            break;
                        }
                    }

                    if (loginSuccessful) {
                        Intent intent = new Intent(LoginPage.this, Homepage_MainLogin.class);

                        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("login", true);
                        editor.putString("id_user", id_user);
                        editor.putString("role", role);
                        editor.apply();

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginPage.this, "No.Hp/Email dan Password salah!!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginPage.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(LoginPage.this, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
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


