package com.cool.boot.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpUtils {

    public static String toGet(String uri) {
        try {
            Request request = new Request.Builder().url(uri).build();
            Response response = new OkHttpClient.Builder().build().newCall(request).execute();
            return response.isSuccessful() ? response.body().string() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
