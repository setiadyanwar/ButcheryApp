package com.example.butcheryapp_ui;

public class ProvinceModel {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
