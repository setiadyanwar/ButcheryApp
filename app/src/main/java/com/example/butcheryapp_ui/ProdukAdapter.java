package com.example.butcheryapp_ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder> {
    private List<ProdukModel> produkList;

    public ProdukAdapter(List<ProdukModel> List){
        this.produkList = List;
    }

    public void setProdukList(List<ProdukModel> produkList) {
        this.produkList = produkList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rekproduk, parent, false);
        return new ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        ProdukModel produk = produkList.get(position);
        holder.namaProdukTextView.setText(produk.getNamaProduk());
        holder.hargaProdukTextView.setText(produk.getHargaProduk());
        holder.namaTokoTextView.setText(produk.getNamaToko());
        holder.alamatTokoTextView.setText(produk.getAlamatToko());
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView namaProdukTextView, hargaProdukTextView, namaTokoTextView, alamatTokoTextView;

        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            namaProdukTextView = itemView.findViewById(R.id.nama_produk);
            hargaProdukTextView = itemView.findViewById(R.id.harga_produk);
            namaTokoTextView = itemView.findViewById(R.id.nama_toko);
            alamatTokoTextView = itemView.findViewById(R.id.alamat_toko);
        }
    }
}

