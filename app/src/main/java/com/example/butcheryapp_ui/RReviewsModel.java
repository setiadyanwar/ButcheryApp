package com.example.butcheryapp_ui;

public class RReviewsModel {
    private String id;
    private String id_user;
    private String id_produk;
    private String reviews;
    private Double ratings;
    private Double AverageRating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_produk() {
        return id_produk;
    }

    public void setId_produk(String id_produk) {
        this.id_produk = id_produk;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public Double getRatings() {
        return ratings;
    }

    public void setRatings(Double ratings) {
        this.ratings = ratings;
    }

    public Double getAverageRating() {
        return AverageRating;
    }

    public void setAverageRating(Double averageRating) {
        AverageRating = averageRating;
    }
}
