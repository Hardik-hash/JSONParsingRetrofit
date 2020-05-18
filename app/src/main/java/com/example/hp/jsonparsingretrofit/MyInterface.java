package com.example.hp.jsonparsingretrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyInterface {

    String JSONURL = "http://jsonplaceholder.typicode.com/";

    @GET("users")
    Call<String> getString();



}
