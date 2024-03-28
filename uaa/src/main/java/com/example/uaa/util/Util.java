package com.example.uaa.util;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class Util {
    public static String connectUrl(String url, String method, String param, Map<String, String> headersMap){

        String result = "";
        if (method.equals("GET")){

            Headers headers = Headers.of(headersMap);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                // 处理响应
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (method.equals("POST")){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json"), param))
                    .build();
            try {
                Response response = client.newCall(request).execute();
                // 处理响应
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }
}
