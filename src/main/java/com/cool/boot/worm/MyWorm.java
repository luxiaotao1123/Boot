package com.cool.boot.worm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyWorm {

    public static String getCode(String url){

        StringBuilder res = new StringBuilder();
        BufferedReader in = null;
        String line;
        try {

            URL argUrl = new URL(url);
            URLConnection connection = argUrl.openConnection();
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = in.readLine()) != null){
                res.append(line);
                res.append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                if (in != null){
                    in.close();
                }
            }catch (Exception ignore){
            }
        }

        return res.toString();
    }

    private static String RegexString(String targetStr, String patternStr)
    {

        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(targetStr);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return "Nothing";
    }



    public static void main(String[] args) {

        String sourceCode = getCode("http://www.baidu.com");

        System.out.println(sourceCode);

        System.out.println( RegexString(sourceCode,"src=\\(.+?)>"));



    }


}
