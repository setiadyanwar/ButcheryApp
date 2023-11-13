package com.example.butcheryapp_ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.List;

public class DaftarProdukSupplierAdapter extends RecyclerView.Adapter<DaftarProdukSupplierAdapter.ProdukViewHolder> {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private List<ProdukModel> produkList;

    public DaftarProdukSupplierAdapter(List<ProdukModel> List){
        this.produkList = List;
    }

    public void setProdukList(List<ProdukModel> produkList) {
        this.produkList = produkList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daftarproduk, parent, false);
        return new ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        ProdukModel produk = produkList.get(position);
        holder.namaProdukTextView.setText(truncateText(produk.getNamaProduk(),50));
        holder.hargaProdukTextView.setText(truncateText(produk.getHargaProduk(),12));
        holder.namaTokoTextView.setText(truncateText(produk.getNamaToko(),18));
        holder.variasiProdukTextView.setText(produk.getVarianProduk());
        holder.btn_ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddProduk.class);
                intent.putExtra("id_produk", produk.getId_produk());
                v.getContext().startActivity(intent);
            }
        });

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
                        String id_produk = produk.getId_produk();
                        deleteProdukByID(v.getContext(),id_produk);
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
    }

    private void deleteProdukByID(Context context, String id_produk) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/deleteProdukByID?id=" + id_produk;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(context, ManageProdukToko.class);
                context.startActivity(intent);
                Toast.makeText(context, "Produk berhasil di hapus!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView namaProdukTextView, hargaProdukTextView, variasiProdukTextView, namaTokoTextView;
        Button btn_ubah,btn_hapus;
        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            namaProdukTextView = itemView.findViewById(R.id.nama_produk);
            hargaProdukTextView = itemView.findViewById(R.id.harga_produk);
            variasiProdukTextView = itemView.findViewById(R.id.variasi);
            namaTokoTextView = itemView.findViewById(R.id.nama_toko);
            btn_ubah = itemView.findViewById(R.id.btnubah);
            btn_hapus = itemView.findViewById(R.id.btnhapus);
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
