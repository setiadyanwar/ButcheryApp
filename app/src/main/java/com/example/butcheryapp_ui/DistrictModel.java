package com.example.butcheryapp_ui;

public class DistrictModel {
    private String id;
    private String regency_id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegency_id() {
        return regency_id;
    }

    public void setRegency_id(String regency_id) {
        this.regency_id = regency_id;
    }

    public String toString() {
        return name; // Mengembalikan nama provinsi sebagai string
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
