package com.example.butcheryapp_ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PesananSupplierAdapter extends RecyclerView.Adapter<PesananSupplierAdapter.ProdukViewHolder> {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private List<PesananModel> pesananList;
    private PesananPage pesananPage;
    private Context context;

    public PesananSupplierAdapter(List<PesananModel> pesananList, PesananPage pesananPage){
        this.pesananList = pesananList;
        this.pesananPage = pesananPage;
    }

    public void setPesananList(List<PesananModel> pesananList) {
        this.pesananList = pesananList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daftarpesanan, parent, false);
        return new ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        PesananModel pesanan = pesananList.get(position);

        holder.namaProdukTextView.setText(pesanan.getNama_produk());
        holder.hargaProdukTextView.setText(pesanan.getHarga());
        holder.variasiProdukTextView.setText(pesanan.getVarian());
        holder.statusPesnaanTextView.setText(pesanan.getStatus());
        holder.btn_siap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatusByPesananID("Kurir menuju toko anda",pesanan.getId());
                pesanan.setStatus("Kurir menuju toko anda");
                notifyDataSetChanged();
            }
        });

        holder.btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatusByPesananID("Dikirim oleh Kurir",pesanan.getId());
                pesanan.setStatus("Dikirim oleh Kurir");
                notifyDataSetChanged();
            }
        });

        if(holder.statusPesnaanTextView.getText().toString().equals("Sudah dinilai")){
            holder.btn_kirim.setEnabled(false);
            holder.btn_siap.setEnabled(false);
        }else if(holder.statusPesnaanTextView.getText().toString().equals("sudah bayar")){
            holder.btn_kirim.setEnabled(true);
            holder.btn_siap.setEnabled(true);
        }else if(holder.statusPesnaanTextView.getText().toString().equals("Dikirim oleh Kurir")){
            holder.btn_kirim.setEnabled(false);
            holder.btn_siap.setEnabled(false);
        }else if(holder.statusPesnaanTextView.getText().toString().equals("Kurir menuju toko anda")){
            holder.btn_kirim.setEnabled(true);
            holder.btn_siap.setEnabled(false);
        }else if(holder.statusPesnaanTextView.getText().toString().equals("Sampai")){
            holder.btn_kirim.setEnabled(false);
            holder.btn_siap.setEnabled(false);
        }else if(holder.statusPesnaanTextView.getText().toString().equals("belum bayar")){
            holder.btn_kirim.setEnabled(false);
            holder.btn_siap.setEnabled(false);
        }

        getSupplierByID(holder, pesanan.getId_supplier());
    }

    private void getSupplierByID(ProdukViewHolder holder , String id_supplier) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getSupplierByID?id=" + id_supplier;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(pesananPage);

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
                    Toast.makeText(pesananPage, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(pesananPage, "Error: Gagal mengambil data supplier", Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void changeStatusByPesananID(String status, String id) {
        String apiUrl = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/changeStatusByPesananID?id=" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.isEmpty()) {
                            Toast.makeText(pesananPage, "Status gagal diubah", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(pesananPage, "Status berhasil diubah", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(pesananPage, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("status", status);
                return params;
            }

        };

        // Add the request to the request queue
        mRequestQueue.add(stringRequest);

    }

        @Override
    public int getItemCount() {
        return pesananList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView namaProdukTextView, hargaProdukTextView, variasiProdukTextView, namaTokoTextView, statusPesnaanTextView;
        Button btn_siap,btn_kirim;
        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            namaProdukTextView = itemView.findViewById(R.id.nama_produk_pesananSupplier);
            hargaProdukTextView = itemView.findViewById(R.id.harga_produk_pesananSupplier);
            variasiProdukTextView = itemView.findViewById(R.id.variasi_pesananSupplier);
            namaTokoTextView = itemView.findViewById(R.id.nama_toko_pesananSupplier);
            statusPesnaanTextView = itemView.findViewById(R.id.status_pesananSupplier);
            btn_siap = itemView.findViewById(R.id.siap);
            btn_kirim = itemView.findViewById(R.id.dikirim);
        }
    }

    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        } else {
            return text.substring(0, maxLength) + " ..."; // Tambahkan elipsis (...) jika diperlukan.
        }
    }
}
