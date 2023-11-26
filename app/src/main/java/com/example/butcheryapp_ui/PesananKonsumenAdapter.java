package com.example.butcheryapp_ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

public class PesananKonsumenAdapter extends RecyclerView.Adapter<PesananKonsumenAdapter.ProdukViewHolder> {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private List<PesananModel> pesananList;
    private PesananSayaPage pesananSayaPage;

    public PesananKonsumenAdapter(List<PesananModel> pesananList, PesananSayaPage pesananSayaPage){
        this.pesananList = pesananList;
        this.pesananSayaPage = pesananSayaPage;
    }

    public void setPesananList(List<PesananModel> pesananList) {
        this.pesananList = pesananList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesanan_saya, parent, false);
        return new ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        PesananModel pesanan = pesananList.get(position);

        holder.namaProdukTextView.setText(pesanan.getNama_produk());
        holder.hargaProdukTextView.setText(pesanan.getHarga());
        holder.variasiProdukTextView.setText(pesanan.getVarian());
        holder.statusPesnaanTextView.setText(pesanan.getStatus());

        holder.btn_nilai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menampilkan dialog rating nanti tambahin event ketika klik btn nilai
                RateDialog rateDialog = new RateDialog(v.getContext(),pesanan.getId(),pesanan.getId_user(),pesanan.getId_produk());
                rateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(v.getContext().getResources().getColor(android.R.color.transparent)));
                rateDialog.setCancelable(false);
                rateDialog.show();
            }
        });

        holder.btn_sampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    changeStatusByPesananID("Sampai", pesanan.getId());
                    pesanan.setStatus("Sampai");
                    notifyDataSetChanged();
            }
        });

        if(holder.statusPesnaanTextView.getText().toString().equals("Sudah dinilai")){
            holder.btn_nilai.setEnabled(false);
            holder.btn_sampai.setEnabled(false);
        }else if(holder.statusPesnaanTextView.getText().toString().equals("sudah bayar")){
            holder.btn_nilai.setEnabled(false);
            holder.btn_sampai.setEnabled(true);
        }else if(holder.statusPesnaanTextView.getText().toString().equals("Dikirim oleh Kurir")){
            holder.btn_nilai.setEnabled(false);
            holder.btn_sampai.setEnabled(true);
        }else if(holder.statusPesnaanTextView.getText().toString().equals("Kurir menuju toko anda")){
            holder.btn_nilai.setEnabled(false);
            holder.btn_sampai.setEnabled(true);
        }else if(holder.statusPesnaanTextView.getText().toString().equals("Sampai")){
            holder.btn_nilai.setEnabled(true);
            holder.btn_sampai.setEnabled(false);
        }else if(holder.statusPesnaanTextView.getText().toString().equals("belum bayar")){
            holder.btn_nilai.setEnabled(false);
            holder.btn_sampai.setEnabled(false);
        }



        getSupplierByID(holder, pesanan.getId_supplier());
    }

    private void getSupplierByID(ProdukViewHolder holder , String id_supplier) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getSupplierByID?id=" + id_supplier;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(pesananSayaPage);

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
                    Toast.makeText(pesananSayaPage, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(pesananSayaPage, "Error: Gagal mengambil data supplier", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(pesananSayaPage, "Status gagal diubah", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(pesananSayaPage, "Status berhasil diubah", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(pesananSayaPage, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
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
        Button btn_sampai,btn_nilai;
        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            namaProdukTextView = itemView.findViewById(R.id.nama_produk_pesananSaya);
            hargaProdukTextView = itemView.findViewById(R.id.harga_produk_pesananSaya);
            variasiProdukTextView = itemView.findViewById(R.id.variasi_pesananSaya);
            namaTokoTextView = itemView.findViewById(R.id.nama_toko_pesananSaya);
            statusPesnaanTextView = itemView.findViewById(R.id.status_pesananSaya);
            btn_nilai = itemView.findViewById(R.id.nilai);
            btn_sampai = itemView.findViewById(R.id.sampai);
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
