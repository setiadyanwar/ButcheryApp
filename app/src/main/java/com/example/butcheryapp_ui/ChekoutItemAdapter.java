package com.example.butcheryapp_ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

public class ChekoutItemAdapter extends RecyclerView.Adapter<ChekoutItemAdapter.ProdukViewHolder> {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private List<CheckoutItemModel> checkoutItemList;

    private CheckoutPage checkoutPage;

    public ChekoutItemAdapter(List<CheckoutItemModel> List, CheckoutPage checkoutPage){
        this.checkoutItemList = List;
        this.checkoutPage = checkoutPage;
    }

    public void setCheckoutItemList(List<CheckoutItemModel> checkoutList) {
        this.checkoutItemList = checkoutList;
        notifyDataSetChanged();
    }

    public List<CheckoutItemModel> getCheckoutItemList() {
        return checkoutItemList;
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pembayaran, parent, false);
        return new ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        CheckoutItemModel checkout = checkoutItemList.get(position);
        holder.namaProdukTextView.setText(checkout.getNama_produk());
        holder.hargaProdukTextView.setText(checkout.getHarga());
        holder.variasiProdukTextView.setText(checkout.getVarian());
        holder.qtyProdukTextView.setText("x" + checkout.getQty());

        getSupplierByID(holder,checkout.getId_supplier());
    }

    @Override
    public int getItemCount() {
        return checkoutItemList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView namaProdukTextView, hargaProdukTextView, variasiProdukTextView, qtyProdukTextView, namaTokoTextView;
        //Button btn_ubah,btn_hapus;
        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            namaProdukTextView = itemView.findViewById(R.id.nama_item_checkout);
            hargaProdukTextView = itemView.findViewById(R.id.harga_item_checkout);
            variasiProdukTextView = itemView.findViewById(R.id.varian_item_checkout);
            qtyProdukTextView = itemView.findViewById(R.id.qty_item_checkout);
            namaTokoTextView = itemView.findViewById(R.id.nama_toko);
        }
    }

    private void getSupplierByID(ProdukViewHolder holder ,String id_supplier) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getSupplierByID?id=" + id_supplier;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(checkoutPage);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        holder.namaTokoTextView.setText(jsonObject.getString("nama_toko"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(checkoutPage, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(checkoutPage, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
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

