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
import android.widget.RatingBar;
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

public class RReviewsAdapter extends RecyclerView.Adapter<RReviewsAdapter.ProdukViewHolder> {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private List<RReviewsModel> rreviewsList;
    private DetailProduk detailProduk;
    private Context context;

    public RReviewsAdapter(List<RReviewsModel> rreviewsList, DetailProduk detailProduk){
        this.rreviewsList = rreviewsList;
        this.detailProduk = detailProduk;
    }

    public void setrReviewsList(List<RReviewsModel> rreviewsList) {
        this.rreviewsList = rreviewsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false);
        return new ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        RReviewsModel rreviews = rreviewsList.get(position);

        getKonsumenByID(holder, rreviews.getId_user());
        holder.ratingsStarTextView.setRating(Float.parseFloat(String.valueOf(rreviews.getRatings())));
        holder.ratingsTextTextView.setText(String.valueOf(rreviews.getRatings()));
        holder.reviewsTextView.setText(rreviews.getReviews());

        /*
        Toast.makeText(detailProduk, "id : " + rreviews.getId(), Toast.LENGTH_SHORT).show();
        Toast.makeText(detailProduk, "id user: " + rreviews.getId_user(), Toast.LENGTH_SHORT).show();
        Toast.makeText(detailProduk, "id produk : " + rreviews.getId_produk(), Toast.LENGTH_SHORT).show();
        Toast.makeText(detailProduk, "reviews : " + rreviews.getReviews(), Toast.LENGTH_SHORT).show();
        Toast.makeText(detailProduk, "ratings : " + rreviews.getRatings(), Toast.LENGTH_SHORT).show();
         */
    }


    @Override
    public int getItemCount() {
        return rreviewsList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView nama_userTextView, reviewsTextView, ratingsTextTextView;

        RatingBar ratingsStarTextView;
        public ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_userTextView = itemView.findViewById(R.id.nama_user_reviews);
            reviewsTextView = itemView.findViewById(R.id.reviews_text);
            ratingsStarTextView = itemView.findViewById(R.id.rating_star_reviews);
            ratingsTextTextView = itemView.findViewById(R.id.rating_text_reviews);
        }
    }

    private void getKonsumenByID(ProdukViewHolder holder, String id_user) {
        String url = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-fophn/endpoint/getKonsumenByID?id=" + id_user;

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(detailProduk);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String getUsername = jsonObject.getString("username");
                        holder.nama_userTextView.setText(getUsername);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(detailProduk, "tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(detailProduk, "Error: Gagal mengambil data konsumen", Toast.LENGTH_LONG).show();
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
