package com.example.butcheryapp_ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder> {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private List<ProdukModel> produkList;
    private Context context;

    public ProdukAdapter(List<ProdukModel> List){
        this.produkList = List;
    }

    public void setProdukList(Context context, List<ProdukModel> produkList) {
        this.produkList = produkList;
        this.context = context;
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
        /*
        File imageFile = new File(String.valueOf(produk.getFotoProduk1()));
        Log.d("ImagePath", "Path: " + produk.getFotoProduk1());
        if (imageFile.exists()) {
            Uri imageUri = Uri.fromFile(imageFile);
            holder.foto.setImageURI(imageUri);
        } else {
            Log.e("ImageLoad", "Image file blocked");
            Toast.makeText(context, "Image file blocked", Toast.LENGTH_SHORT).show();
        }
         */
        holder.namaProdukTextView.setText(produk.getNamaProduk());
        holder.hargaProdukTextView.setText(produk.getHargaProduk());
        holder.namaTokoTextView.setText(produk.getNamaToko());
        holder.alamatTokoTextView.setText(produk.getAlamatToko());

        byte[] bytes1 = Base64.decode(produk.getGambarproduk1(), Base64.DEFAULT);
        Bitmap gambar1 = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
        holder.foto.setImageBitmap(gambar1);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailProduk.class);
                intent.putExtra("id_produk", produk.getId_produk());
                v.getContext().startActivity(intent);
            }
        });

        getRReviewsByProdukID(holder,produk.getId_produk());
        getItemPesananByProdukID(holder,produk.getId_produk());
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView namaProdukTextView, hargaProdukTextView, namaTokoTextView, alamatTokoTextView, ratingTaglineTextView, totalJualTaglineTextView;
        LinearLayout card;
        ImageView foto;

        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            foto = itemView.findViewById(R.id.image1);
            namaProdukTextView = itemView.findViewById(R.id.nama_produk);
            hargaProdukTextView = itemView.findViewById(R.id.harga_produk);
            namaTokoTextView = itemView.findViewById(R.id.nama_toko);
            alamatTokoTextView = itemView.findViewById(R.id.alamat_toko);
            ratingTaglineTextView = itemView.findViewById(R.id.rating_tagline);
            totalJualTaglineTextView = itemView.findViewById(R.id.total_jual_tagline);
        }
    }

    public void getRReviewsByProdukID(ProdukViewHolder holder, String id_produk) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getRReviewsByProdukID?id_produk=" + id_produk;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context);

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<RReviewsModel> rreviewsList = new ArrayList<>();
                    double totalRating = 0.0;
                    int numberOfReviews = jsonArray.length();

                    for (int i = 0; i < numberOfReviews; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        RReviewsModel rreviews = new RReviewsModel();

                        String id = jsonObject.getString("_id");
                        String id_user = jsonObject.getString("id_user");
                        String id_produk = jsonObject.getString("id_produk");
                        String reviews = jsonObject.getString("reviews");
                        double ratings = jsonObject.getDouble("ratings");

                        totalRating += ratings;

                        if (numberOfReviews > 0) {
                            double averageRating = totalRating / numberOfReviews;
                            rreviews.setAverageRating(averageRating);

                            // Round to one decimal place and set the RatingBar
                            holder.ratingTaglineTextView.setText(String.valueOf(round((float) averageRating)) + " | ");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    public void getItemPesananByProdukID(ProdukViewHolder holder,String id_produk) {

        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getItemPesananByProdukID?id_produk=" + id_produk;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(context);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int numberOfPurchase = jsonArray.length();

                    holder.totalJualTaglineTextView.setText(numberOfPurchase + " terjual");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private float round(float value) {
        return (float) (Math.round(value * 10.0) / 10.0);
    }
}

