package com.example.hp.jsonparsingretrofit;

public class ModelListView {

   private String name,email,city;
   private int id;

    public void setId(int id)
    {
        this.id=id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public int getId() {
        return id;
    }
}
