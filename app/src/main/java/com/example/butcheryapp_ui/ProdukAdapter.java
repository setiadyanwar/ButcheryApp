package com.example.butcheryapp_ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailProduk.class);
                intent.putExtra("id_produk", produk.getId_produk());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView namaProdukTextView, hargaProdukTextView, namaTokoTextView, alamatTokoTextView;
        LinearLayout card;

        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            namaProdukTextView = itemView.findViewById(R.id.nama_produk);
            hargaProdukTextView = itemView.findViewById(R.id.harga_produk);
            namaTokoTextView = itemView.findViewById(R.id.nama_toko);
            alamatTokoTextView = itemView.findViewById(R.id.alamat_toko);
        }
    }
}

