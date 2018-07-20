package com.cool.boot.utils;

import com.cool.boot.exception.CoolException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
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

    public static void main(String[] args) throws Exception {
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        ExecutorService pool = Executors.newFixedThreadPool(5);

        for (int i =1;i<=10;i++){

            pool.execute(okHttpUtils.new MyThread());
        }
        pool.shutdown();

    }

    class MyThread implements Runnable{
        @Override
        public void run() {
            String url = "http://localhost:9090/api/user/test";
            Map map = new HashMap();
            map.put("appid","luxiaotao1123");
            map.put("sign","AgQBSFnybeNzHO/JWHZiWdgK+8BxPFH2okEEtt3gRvwS1uQ70oSQ1S8Bj+qSgBbyxnqZ5fyvuGb+LZr8ZkUMA8i+AIb8xBJJJ57MhNX54kU=");
            map.put("timestamp","1531207583132");
            System.out.println(toPost(url, map));
        }
    }

}
