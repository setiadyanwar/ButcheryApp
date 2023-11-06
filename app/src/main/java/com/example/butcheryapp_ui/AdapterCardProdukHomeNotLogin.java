//package com.example.butcheryapp_ui;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class AdapterCardProdukHomeNotLogin extends RecyclerView.Adapter<AdapterCardProdukHomeNotLogin.MyViewHolder> {
//
//    int []arr;
//
//    public AdapterCardProdukHomeNotLogin(int[] arr) {
//        this.arr = arr;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rekproduk,parent,false);
//        MyViewHolder myViewHolder = new MyViewHolder(view);
//        return myViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//
//        holder.imageView.setImageResource(arr[position]);
//        holder.textView.setText("Image ke-"+position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return arr.length;
//    }
//    public static class MyViewHolder extends RecyclerView.ViewHolder
//    {
//        ImageView imageView;
//        TextView textView;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView=itemView.findViewById(R.id.image1);
//            textView=itemView.findViewById(R.id.nama_produk);
//
//        }
//    }
//}
