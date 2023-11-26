package com.example.butcheryapp_ui;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartPage extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private RecyclerView recyclerView;
    private CartProdukAdapter cartAdapter;
    TextView subtotalTextView, totalProdukTextView;
    Integer totalProduk = 0;
    String subtotal = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("login", false);
        String user_id = sharedPref.getString("id_user","");

        if (!isLoggedIn) {
            Intent intent = new Intent(CartPage.this, LoginPage.class);
            startActivity(intent);
        }

        recyclerView = findViewById(R.id.listcartproduk);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter= new CartProdukAdapter(new ArrayList<>(),this);
        recyclerView.setAdapter(cartAdapter);

        // Mendapatkan referensi ke CheckBox 'Semua'
        CheckBox ceklisSemua = findViewById(R.id.ceklis_semua);

        // Menangani klik checkbox 'Semua'
        ceklisSemua.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Mengatur status checkbox untuk setiap item di adapter
                for (CartModel cart : cartAdapter.getCartList()) {
                    cart.setSelected(isChecked);
                }
                // Memperbarui tampilan
                cartAdapter.notifyDataSetChanged();
            }
        });

        ImageButton buttoncartcheckout= findViewById(R.id.buttoncartcheckout);

        buttoncartcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CartModel> cartToCheckoutList = new ArrayList<>();
                for (CartModel cart : cartAdapter.getCartList()) {
                    if (cart.isSelected()) {
                        // Create a new CartModel for each selected item
                        CartModel cartToCheckout = new CartModel();

                        // Copy the values from the original cart to the new cart
                        cartToCheckout.setUser_id(cart.getUser_id());
                        cartToCheckout.setSupplier_id(cart.getSupplier_id());
                        cartToCheckout.setProduk_id(cart.getProduk_id());
                        cartToCheckout.setFoto(cart.getFoto());
                        cartToCheckout.setNama_produk(cart.getNama_produk());
                        cartToCheckout.setVarian(cart.getVarian());
                        cartToCheckout.setHarga(cart.getHarga());
                        cartToCheckout.setQty(cart.getQty());
                        cartToCheckout.setSubtotal(subtotalTextView.getText().toString());
                        cartToCheckout.setNote(cart.getNote());

                        cartToCheckoutList.add(cartToCheckout);
                    }
                }


                /*
                for (int i = 0; i < cartToCheckoutList.size(); i++){
                    CartModel selectedCart = cartToCheckoutList.get(i);
                    Toast.makeText(CartPage.this, " data user id -> " + selectedCart.getUser_id(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CartPage.this, " data supplier id -> " + selectedCart.getSupplier_id(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CartPage.this, " data produk id -> " + selectedCart.getProduk_id(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CartPage.this, " data foto -> " + selectedCart.getFoto(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CartPage.this, " data nama -> " + selectedCart.getNama_produk(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CartPage.this, " data varian -> " + selectedCart.getVarian(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CartPage.this, " data harga -> " + selectedCart.getHarga(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CartPage.this, " data qty -> " + selectedCart.getQty(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CartPage.this, " data harga_total -> " + subtotalTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CartPage.this, " data note -> " + selectedCart.getNote(), Toast.LENGTH_SHORT).show();
                }
                 */

                insertDataToCheckoutFromCart(cartToCheckoutList);


            }
        });

        getProdukCartByUserID(user_id);
    }

    private void getProdukCartByUserID(String user_id) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getProdukCartByUserID?user_id="+user_id;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<CartModel> cartList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CartModel cart = new CartModel();

                        String getId = jsonObject.getString("_id");
                        String getUserId = jsonObject.getString("user_id");
                        String getProdukId = jsonObject.getString("produk_id");
                        String getSupplierId = jsonObject.getString("supplier_id");
                        String getNamaProduk = jsonObject.getString("nama_produk");
                        String getVarianProduk = jsonObject.getString("varian");
                        String getHargaProduk = jsonObject.getString("harga");
                        String getQTYProduk = jsonObject.getString("qty");
                        String getSubtotalProduk = jsonObject.getString("subtotal");
                        String getNoteProduk = jsonObject.getString("note");
                        String getFotoProduk = jsonObject.getString("foto");

                        //SET PRODUK KE DALAM PRODUK MODEL
                        cart.setId(getId);
                        cart.setUser_id(getUserId);
                        cart.setProduk_id(getProdukId);
                        cart.setSupplier_id(getSupplierId);
                        cart.setNama_produk(getNamaProduk);
                        cart.setVarian(getVarianProduk);
                        cart.setHarga(getHargaProduk);
                        cart.setQty(getQTYProduk);
                        cart.setSubtotal(getSubtotalProduk);
                        cart.setNote(getNoteProduk);
                        cart.setFoto(getFotoProduk);

                        subtotalTextView = findViewById(R.id.subtotal_cart);
                        totalProdukTextView = findViewById(R.id.textjumlah);

                        subtotalTextView.setText("Rp 0");
                        totalProdukTextView.setText("0");

                        cartList.add(cart);
                    }

                    cartAdapter.setCartList(cartList);
                    cartAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CartPage.this, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(CartPage.this, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void insertDataToCheckoutFromCart(List<CartModel> cartToCheckoutList) {
        String apiUrl = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/insertDataToCheckoutFromCart";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.isEmpty()){
                    Intent intent = new Intent(CartPage.this, CheckoutPage.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(CartPage.this, CartPage.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartPage.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {

            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                for (int i = 0; i < cartToCheckoutList.size(); i++) {
                    CartModel selectedCart = cartToCheckoutList.get(i);
                    params.put("user_id_" + i, selectedCart.getUser_id());
                    params.put("supplier_id_" + i, selectedCart.getSupplier_id());
                    params.put("produk_id_" + i, selectedCart.getProduk_id());
                    params.put("foto_" + i, selectedCart.getFoto());
                    params.put("nama_produk_" + i, selectedCart.getNama_produk());
                    params.put("varian_" + i, selectedCart.getVarian());
                    params.put("harga_" + i, selectedCart.getHarga());
                    params.put("qty_" + i, selectedCart.getQty());
                    params.put("harga_total_" + i, selectedCart.getSubtotal());
                    params.put("note_" + i, selectedCart.getNote());
                }
                Log.d("params", "getParams: " + params.toString());
                return params;
            }

        };

        // Add the request to the request queue
        mRequestQueue.add(stringRequest);
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
        // Setelah mengatur totalProduk, update tampilan jika diperlukan
        TextView subtotalTextView = findViewById(R.id.subtotal_cart);
        subtotalTextView.setText(subtotal);
    }

    // Metode untuk mengatur totalProduk
    public void setTotalProduk(String setTotalProduk) {
        this.totalProduk = Integer.parseInt(setTotalProduk);
        // Setelah mengatur totalProduk, update tampilan jika diperlukan
        TextView totalProdukTextView = findViewById(R.id.textjumlah);
        totalProdukTextView.setText(String.valueOf(totalProduk));
    }
}