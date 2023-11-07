package com.example.butcheryapp_ui;

public class RegencyModel {
    private String id;
    private String province_id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
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
