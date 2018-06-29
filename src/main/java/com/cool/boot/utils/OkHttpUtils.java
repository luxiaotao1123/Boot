package com.cool.boot.utils;

import com.cool.boot.exception.CoolException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public static String toPost(String uri, Map<String, String> params) {
        if (params == null) throw new CoolException(uri + "接口参数为空");

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
            FormBody formBody = formBodyBuilder.build();
            Request request = new Request
                    .Builder()
                    .post(formBody)
                    .url(uri)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            return response.isSuccessful() ? response.body().string() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static void main(String[] args) {
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        ExecutorService pool = Executors.newFixedThreadPool(5);


        for (int i =0;i<20;i++){
            pool.execute(okHttpUtils.new MyThread());
        }

        pool.shutdown();
    }

    class MyThread implements Runnable{

        @Override
        public void run() {
            System.out.println(toGet("http://localhost:9090/user/test"));

        }
    }

}
