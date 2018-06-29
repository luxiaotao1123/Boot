package com.cool.boot.spiders;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


@Service("vinWorker")
public class VinWorker {

    public String getCode(String url) {

        StringBuilder res = new StringBuilder();
        BufferedReader in = null;
        String line;
        try {

            URL argUrl = new URL(url);
            URLConnection connection = argUrl.openConnection();
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = in.readLine()) != null) {
                res.append(line);
                res.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ignore) {
            }
        }

        return res.toString();
    }
}
