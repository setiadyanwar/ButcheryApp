package com.example.butcheryapp_ui;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class CartProdukAdapter extends RecyclerView.Adapter<CartProdukAdapter.ProdukViewHolder> {
    private String nama_produk, harga_produk, varian_produk, qty_produk;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private List<CartModel> cartList;
    private CartPage cartPage;
    CartModel cart;
    private Integer item_position;

    private int totalProdukManually = 0;
    private int totalProduk = 0;
    private int hargaProduk = 0;
    private int subtotal = 0;

    public CartProdukAdapter(List<CartModel> List,CartPage cartPage) {
        this.cartList = List;
        this.cartPage = cartPage;  // Initialize the reference
    }
    public void setCartList(List<CartModel> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    public List<CartModel> getCartList() {
        return cartList;
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_produk, parent, false);
        return new ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, @SuppressLint("RecyclerView") int position) {
        cart = cartList.get(position);

        // Initialize the quantity and subtotal for each item
        holder.qtyProdukTextView.setText(cart.getQty());
        cartPage.setSubtotal(formatCurrency(parseInt(cartList.get(position).getSubtotal())));

        // Set the checkbox state based on the item's selection status
        holder.checkItem.setOnCheckedChangeListener(null);
        holder.checkItem.setChecked(cart.isSelected());

        // Handle checkbox state changes
        holder.checkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setItem_position(position);
                cartList.get(position).setPosition(getItem_position());
                cartList.get(position).setSelected(isChecked);
                updateTotalProduk();
            }
        });

        // Set other item details
        holder.namaProdukTextView.setText(cart.getNama_produk());
        holder.hargaProdukTextView.setText("Rp" + cart.getHarga());
        holder.variasiProdukTextView.setText(cart.getVarian());

        // Handle item deletion
        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Membuat AlertDialog untuk konfirmasi
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Konfirmasi Hapus");
                builder.setMessage("Apakah Anda yakin ingin menghapus produk ini?");

                // Tombol positif (Yes)
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id_cart = cartList.get(position).getId();
                        deleteProdukCartByID(v.getContext(),id_cart);
                    }
                });

                // Tombol negatif (No)
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Batal menghapus, tutup dialog
                        dialog.dismiss();
                    }
                });

                // Menampilkan AlertDialog
                builder.create().show();
            }
        });

        // Handle quantity increase
        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = parseInt(cartList.get(position).getQty()) + 1;
                cartList.get(position).setQty(Integer.toString(newQuantity));
                holder.qtyProdukTextView.setText(Integer.toString(newQuantity));

                updateSubtotal(holder, position);
                updateTotalProduk();
            }
        });

        // Handle quantity decrease
        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer qty = parseInt(cartList.get(position).getQty());
                if (qty > 1) {
                    int newQuantity = parseInt(cartList.get(position).getQty()) - 1;
                    cartList.get(position).setQty(Integer.toString(newQuantity));
                    holder.qtyProdukTextView.setText(Integer.toString(newQuantity));

                    updateSubtotal(holder, position);
                    updateTotalProduk();
                }
            }
        });

        SharedPreferences sharedPref = cartPage.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String id_user = sharedPref.getString("id_user","");

        updateTotalProduk();
        getKonsumenByID(holder,id_user);
    }

    private void deleteProdukCartByID(Context context, String id_cart) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/deleteProdukCartByID?id="+id_cart;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(context, CartPage.class);
                context.startActivity(intent);
                Toast.makeText(context, "Cart berhasil di hapus!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void getKonsumenByID(ProdukViewHolder holder,String id_user) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getKonsumenByID?id=" + id_user;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(cartPage);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String getUsername = jsonObject.getString("username");
                        holder.namaPemesanTextView.setText(getUsername);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(cartPage, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(cartPage, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView namaPemesanTextView, namaProdukTextView, hargaProdukTextView, variasiProdukTextView, qtyProdukTextView, subtotalProdukTextView, noteProdukTextView;
        ImageView fotoProdukTextView;
        ImageButton btn_hapus, btn_plus,btn_minus;
        LinearLayout cardItemCart;
        CheckBox checkItem;
        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);

            checkItem = itemView.findViewById(R.id.ceklis);
            namaPemesanTextView = itemView.findViewById(R.id.nama_user);
            namaProdukTextView = itemView.findViewById(R.id.nama_produk);
            hargaProdukTextView = itemView.findViewById(R.id.harga_produk);
            variasiProdukTextView = itemView.findViewById(R.id.varian);
            qtyProdukTextView = itemView.findViewById(R.id.qty);
            btn_hapus = itemView.findViewById(R.id.btn_delete);
            btn_plus = itemView.findViewById(R.id.plus);
            btn_minus = itemView.findViewById(R.id.minus);
        }
    }

    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        } else {
            return text.substring(0, maxLength) + " ..."; // Tambahkan elipsis (...) jika diperlukan.
        }
    }

    // Metode untuk menghitung total produk yang dicentang
    // Function to update the total number of selected items and subtotal in the CartPage
    private void updateTotalProduk() {
        int totalProdukManually = 0;
        int hargaProduk = 0;

        for (CartModel cart : cartList) {
            if (cart.isSelected()) {
                totalProdukManually++;
                hargaProduk += parseInt(cart.getSubtotal());
            }
        }

        // Update the UI with the total number of selected items and subtotal
        cartPage.setTotalProduk(Integer.toString(totalProdukManually));
        cartPage.setSubtotal(formatCurrency(hargaProduk));
    }

    // Function to update the subtotal for an item and UI
    private void updateSubtotal(ProdukViewHolder holder, int position) {
        int subtotal = parseInt(cartList.get(position).getHarga()) * parseInt(cartList.get(position).getQty());
        cartList.get(position).setSubtotal(Integer.toString(subtotal));
        cartPage.setSubtotal(formatCurrency(subtotal));
    }

    // Function to format currency
    private String formatCurrency(int amount) {
        Currency customCurrency = Currency.getInstance("IDR");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        currencyFormat.setCurrency(customCurrency);
        return currencyFormat.format(amount);
    }

    public Integer getItem_position() {
        return item_position;
    }

    public void setItem_position(Integer item_position) {
        this.item_position = item_position;
    }
}

