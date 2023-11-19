package com.example.butcheryapp_ui;

import androidx.annotation.NonNull;

public class CartToCheckOutModel {
    private Integer position;
    private String user_id;
    private String supplier_id;
    private String produk_id;
    private String foto;
    private String nama_produk;
    private String varian;
    private String harga;
    private String qty;
    private String harga_total;
    private String note;

    @NonNull
    @Override
    public String toString() {
        return nama_produk;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getProduk_id() {
        return produk_id;
    }

    public void setProduk_id(String produk_id) {
        this.produk_id = produk_id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getVarian() {
        return varian;
    }

    public void setVarian(String varian) {
        this.varian = varian;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getHarga_total() {
        return harga_total;
    }

    public void setHarga_total(String harga_total) {
        this.harga_total = harga_total;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
