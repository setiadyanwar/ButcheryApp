package com.example.butcheryapp_ui;

import static java.lang.Integer.parseInt;
import static java.lang.Math.round;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.corekit.core.MidtransSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagePaymentPage extends AppCompatActivity {

    TextView namaPembeliTextview, alamatPembeliTextview, noHPPembeliTextView, subtotalCheckoutTextView ,subtotalTextView, opsiPengirimanTextView, totalHargaTextView, TotalProdukTextView, totalHargaRingkasanTextView;
    EditText getNoteEditText;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    private ChekoutItemAdapter checkoutAdapter;

    private CheckoutItemModel checkoutModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_payment_page);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);
        String id_user = sharedPref.getString("id_user","");

        if (!isLoggedIn) {
            Intent intent = new Intent(ManagePaymentPage.this, LoginPage.class);
            startActivity(intent);
        }

        recyclerView = findViewById(R.id.card_checkout);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        checkoutAdapter = new ChekoutItemAdapter(new ArrayList<>(),this);
        recyclerView.setAdapter(checkoutAdapter);

        ImageButton linkbutton = findViewById(R.id.buttonbeli_checkout);

        linkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSnapTokenFromServer(new ConfirmPayment.SnapTokenCallback() {
                    @Override
                    public void onSnapTokenReceived(String snapToken, String redirectUrl) {
                        // Display the SnapToken
                        //Toast.makeText(ManagePaymentPage.this, "snapToken: " + snapToken, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        // Handle the error
                        error.getStackTrace();
                        //Toast.makeText(ManagePaymentPage.this, "Error getting SnapToken", Toast.LENGTH_LONG).show();
                    }
                });

                List<CheckoutItemModel> checkoutList = new ArrayList<>();
                opsiPengirimanTextView = findViewById(R.id.pengiriman);
                getNoteEditText = findViewById(R.id.note_checkout);
                for (CheckoutItemModel checkout : checkoutAdapter.getCheckoutItemList()) {
                    CheckoutItemModel checkoutToBeli = new CheckoutItemModel();

                    checkoutToBeli.setId(checkout.getId());
                    checkoutToBeli.setId_user(checkout.getId_user());
                    checkoutToBeli.setId_supplier(checkout.getId_supplier());
                    checkoutToBeli.setId_produk(checkout.getId_produk());
                    checkoutToBeli.setFoto(checkout.getFoto());
                    checkoutToBeli.setNama_produk(checkout.getNama_produk());
                    checkoutToBeli.setVarian(checkout.getVarian());
                    checkoutToBeli.setHarga(checkout.getHarga());
                    checkoutToBeli.setQty(checkout.getQty());
                    checkoutToBeli.setAlamat_pengiriman(alamatPembeliTextview.getText().toString());
                    checkoutToBeli.setOpsi_pengiriman(opsiPengirimanTextView.getText().toString());
                    checkoutToBeli.setTotal_produk(Integer.toString(checkoutAdapter.getCheckoutItemList().size()));
                    checkoutToBeli.setBiaya_ongkir("10000");
                    checkoutToBeli.setBiaya_layanan("1000");
                    checkoutToBeli.setBiaya_asuransi("1000");
                    checkoutToBeli.setBiaya_tambahan("1000");
                    checkoutToBeli.setSubtotal(checkout.getTotal_harga());
                    checkoutToBeli.setTotal_harga(subtotalTextView.getText().toString());
                    checkoutToBeli.setNote(getNoteEditText.getText().toString());
                    checkoutToBeli.setStatus("belum bayar");

                    checkoutList.add(checkoutToBeli);
                }
                insertDataToBeli(checkoutList);

                /*
                for (int i = 0; i < checkoutList.size(); i++) {
                    CheckoutItemModel items = checkoutList.get(i);
                    Toast.makeText(ManagePaymentPage.this, " data user id -> " + items.getId_user(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data supplier id -> " + items.getId_supplier(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data produk id -> " + items.getId_produk(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data foto -> " + items.getFoto(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data nama -> " + items.getNama_produk(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data varian -> " + items.getVarian(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data harga -> " + items.getHarga(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data qty -> " + items.getQty(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data alamat -> " + items.getAlamat_pengiriman(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data opsi -> " + items.getOpsi_pengiriman(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data total_produk -> " + items.getTotal_produk(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data b_ongkir -> " + items.getBiaya_ongkir(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data layanan -> " + items.getBiaya_layanan(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data asuransi -> " + items.getBiaya_asuransi(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data tambahan -> " + items.getBiaya_tambahan(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data subtotal -> " + items.getSubtotal(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data total_harga -> " + items.getTotal_harga(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data note -> " + items.getNote(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManagePaymentPage.this, " data status -> " + items.getStatus(), Toast.LENGTH_SHORT).show();
                }
            */


            }
        });

        getDataItemCheckoutByUserID(id_user);
    }
    private void getDataItemCheckoutByUserID(String user_id) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getDataItemCheckoutByUserID?user_id="+user_id;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<CheckoutItemModel> checkoutList = new ArrayList<>();
                    String user_id = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CheckoutItemModel checkout = new CheckoutItemModel();

                        String getId = jsonObject.getString("_id");
                        String getUserId = jsonObject.getString("user_id");
                        String getSupplierId = jsonObject.getString("supplier_id");
                        String getProdukId = jsonObject.getString("produk_id");
                        String getFotoProduk = jsonObject.getString("foto");
                        String getNamaProduk = jsonObject.getString("nama_produk");
                        String getVarianProduk = jsonObject.getString("varian");
                        String getHargaProduk = jsonObject.getString("harga");
                        String getQtyProduk = jsonObject.getString("qty");
                        String getHargaTotalProduk = jsonObject.getString("harga_total");
                        String getNoteProduk = jsonObject.getString("note");

                        user_id = getUserId;
                        
                        //SET PRODUK KE DALAM PRODUK MODEL
                        checkout.setId(getId);
                        checkout.setId_user(getUserId);
                        checkout.setId_supplier(getSupplierId);
                        checkout.setId_produk(getProdukId);

                        checkout.setFoto(getFotoProduk);
                        checkout.setNama_produk(getNamaProduk);
                        checkout.setVarian(getVarianProduk);

                        Currency customCurrency = Currency.getInstance("IDR");
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                        currencyFormat.setCurrency(customCurrency);
                        String formattedCurrency = currencyFormat.format(parseInt(getHargaProduk));

                        checkout.setHarga(formattedCurrency);
                        checkout.setQty(parseInt(getQtyProduk));
                        checkout.setTotal_harga(getHargaTotalProduk);
                        checkout.setNote(getNoteProduk);

                        TotalProdukTextView = findViewById(R.id.total_produk);
                        subtotalTextView = findViewById(R.id.subtotal_checkout);
                        totalHargaRingkasanTextView = findViewById(R.id.total_harga_ringkasan);
                        totalHargaTextView = findViewById(R.id.total_harga_checkout);

                        TotalProdukTextView.setText("Subtotal (" + Integer.toString(jsonArray.length()) + ")");
                        subtotalTextView.setText(getHargaTotalProduk);

                        String subtotal = subtotalTextView.getText().toString();
                        // Remove non-numeric characters and replace commas with dots
                        String numericSubtotal = subtotal.replaceAll("[^0-9,]", "").replace(",", ".");

                        // Parse strings to floats
                        float floatValueSubtotal = Float.parseFloat(numericSubtotal);

                        // Formatted biaya aplikasi
                        Currency customCurrency2 = Currency.getInstance("IDR");
                        NumberFormat currencyFormat2 = NumberFormat.getCurrencyInstance();
                        currencyFormat2.setCurrency(customCurrency2);
                        String formattedBiayaAplikasi = currencyFormat2.format(13000);

                        // Remove non-numeric characters and replace commas with dots
                        String numericBiayaAplikasi = formattedBiayaAplikasi.replaceAll("[^0-9,]", "").replace(",", ".");

                        // Parse strings to floats
                        float floatValueBiayaAplikasi = Float.parseFloat(numericBiayaAplikasi);

                        // Add the float values
                        float totalHarga = floatValueSubtotal + floatValueBiayaAplikasi;

                        // Format the result back to currency
                        String formattedTotalHarga = currencyFormat2.format(totalHarga);

                         // Set the text to your TextView
                        totalHargaTextView.setText(formattedTotalHarga);

                        checkoutList.add(checkout);
                    }

                    getKonsumenByID(user_id);
                    
                    checkoutAdapter.setCheckoutItemList(checkoutList);
                    checkoutAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ManagePaymentPage.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ManagePaymentPage.this, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void getKonsumenByID(String id_user) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getKonsumenByID?id=" + id_user;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    //Toast.makeText(context, "response : " + jsonArray, Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        JSONArray alamat = jsonObject.getJSONArray("alamat");
                        String getAlamat = alamat.getJSONObject(0).getString("alamat");

                        namaPembeliTextview = findViewById(R.id.nama_pembeli);
                        alamatPembeliTextview = findViewById(R.id.alamat_pembeli) ;
                        noHPPembeliTextView = findViewById(R.id.no_telp_user);

                        alamatPembeliTextview.setText(getAlamat);
                        namaPembeliTextview.setText(jsonObject.getString("username"));
                        noHPPembeliTextView.setText(((jsonObject.getString("no_hp").isEmpty())) ? "(+62)Tidak tersedia" : jsonObject.getString("no_hp"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ManagePaymentPage.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ManagePaymentPage.this, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void insertDataToBeli(List<CheckoutItemModel> checkoutToBeliList) {
        String apiUrl = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/insertDataToBeli";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    for (int i = 0; i < checkoutToBeliList.size(); i++) {
                        CheckoutItemModel checkoutItem = checkoutToBeliList.get(i);
                        // Open a browser with the specified URL
                        String redirectUrl = "https://5ebd-120-188-92-12.ngrok-free.app/login";  // Replace with your actual URL
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl));
                        startActivity(browserIntent);
                    }
                }else {
                    Intent intent = new Intent(ManagePaymentPage.this, ManagePaymentPage.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ManagePaymentPage.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                for (int i = 0; i < checkoutToBeliList.size(); i++) {
                    CheckoutItemModel checkoutItems = checkoutToBeliList.get(i);
                    params.put("id_user_" + i, checkoutItems.getId_user());
                    params.put("id_supplier_" + i, checkoutItems.getId_supplier());
                    params.put("id_produk_" + i, checkoutItems.getId_produk());
                    params.put("foto_" + i, checkoutItems.getFoto());
                    params.put("nama_produk_" + i, checkoutItems.getNama_produk());
                    params.put("varian_" + i, checkoutItems.getVarian());
                    params.put("harga_" + i, checkoutItems.getHarga());
                    params.put("qty_" + i, Integer.toString(checkoutItems.getQty()));
                    params.put("alamat_pengiriman_" + i, checkoutItems.getAlamat_pengiriman());
                    params.put("opsi_pengiriman_" + i, checkoutItems.getOpsi_pengiriman());
                    params.put("biaya_ongkir_" + i, checkoutItems.getBiaya_ongkir());
                    params.put("biaya_layanan_" + i, checkoutItems.getBiaya_layanan());
                    params.put("biaya_asuransi_" + i, checkoutItems.getBiaya_asuransi());
                    params.put("biaya_tambahan_" + i, checkoutItems.getBiaya_tambahan());
                    params.put("total_produk_" + i, checkoutItems.getTotal_produk());
                    params.put("subtotal_" + i, subtotalTextView.getText().toString());
                    params.put("total_harga_" + i, totalHargaTextView.getText().toString());
                    params.put("note_" + i, checkoutItems.getNote());
                    params.put("status_" + i, checkoutItems.getStatus());
                }
                return params;
            }
        };

        // Add the request to the request queue
        mRequestQueue.add(stringRequest);
    }

    private void clickPay(String snapToken) {
        // Check if snapToken is not null before proceeding
        if (snapToken != null) {
            // Set the snapToken in the TransactionRequest
            MidtransSDK.getInstance().setTransactionRequest(transactionRequest("101", 2000, 1, "John", snapToken));
        } else {
            // Handle the case where snapToken is null
            Toast.makeText(this, "Error: SNAP token ID is null", Toast.LENGTH_LONG).show();
        }
    }

    // Example method to get snapToken from your server
    public interface SnapTokenCallback {
        void onSnapTokenReceived(String snapToken, String redirectUrl);
        void onError(VolleyError error);

    }
    private void getSnapTokenFromServer(final ConfirmPayment.SnapTokenCallback callback) {
        // Make a network request to get the SnapToken JSON response
        // Use Volley, Retrofit, or any other networking library

        // Example using Volley
        String url = "https://a004-120-188-92-237.ngrok-free.app/api/getSnapToken/charge/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Handle the JSON response
                try {
                    String snapToken = response.getString("token");
                    String redirectUrl = response.getString("redirect_url");

                    // Invoke the callback with the obtained SnapToken and Redirect URL
                    callback.onSnapTokenReceived(snapToken, redirectUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                    callback.onError(new VolleyError("JSON parsing error"));
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Invoke the callback with the error
                        callback.onError(error);
                    }
                });

        // Add the request to the RequestQueue to execute the network request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    private void makePayment(String linkSnap){
        SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl(BuildConfig.BASE_URL)
                .setClientKey(BuildConfig.CLIENT_KEY)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#777777","#f77474" , "#3f0d0d"))
                .buildSDK();
    }

    public static CustomerDetails customerDetails(){
        CustomerDetails cd = new CustomerDetails();
        cd.setFirstName("Angga Yunanda");
        cd.setEmail("anggaY@gmail.com");
        cd.setPhone("0856000123");
        return cd;
    }

    public static TransactionRequest transactionRequest(String id, double price, int qty, String name, String snapToken){
        TransactionRequest request =  new TransactionRequest(System.currentTimeMillis() + " " , 2000 );
        CustomerDetails customerDetails = customerDetails();

        request.setCustomerDetails(customerDetails);

        ItemDetails details = new ItemDetails(id, price, qty, name);

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(details);
        request.setItemDetails(itemDetails);
        CreditCard creditCard = new CreditCard();
        creditCard.setSaveCard(false);
        creditCard.setAuthentication(CreditCard.RBA);

        request.setCreditCard(creditCard);

        // Add the snapToken as a custom field
        request.setCustomField1(snapToken);

        return request;
    }
}