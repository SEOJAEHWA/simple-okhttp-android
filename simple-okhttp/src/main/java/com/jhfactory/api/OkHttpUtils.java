package com.jhfactory.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class OkHttpUtils {

    private static final int TIMEOUT_CONN_SEC = 10;
    private static final int TIMEOUT_READ_SEC = 15;

    public static boolean isSuccessful(int code) {
        return code >= 200 && code < 300;
    }

    public static <T> RequestBody getJsonRequestBody(T reqObj) {
        MediaType mediaType = MediaType.parse("application/json");
        return RequestBody.create(mediaType, getJsonString(reqObj));
    }

    public static RequestBody getEmptyJsonRequestBody() {
        MediaType mediaType = MediaType.parse("application/json");
        return RequestBody.create(mediaType, getEmptyJsonString());
    }

    public static RequestBody getFormRequestBody(Map<String, String> formData) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : formData.keySet()) {
            builder.add(key, formData.get(key));
        }
        return builder.build();
    }

    // FIXME: 2017-09-27 멀티파트 폼 데이터 처리
    public static RequestBody getMultipartFormRequestBody(Map<String, Object> formData) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (String key : formData.keySet()) {
            Object obj = formData.get(key);
            if (obj instanceof String) {
                builder.addFormDataPart(key, (String) obj);
            } else if (obj instanceof File) {

            }
        }
        return builder.build();
    }

    private static String getEmptyJsonString() {
        return new JsonObject().toString();
    }

    private static <T> String getJsonString(T object) {
        return new Gson().toJson(object, new TypeToken<T>() {
        }.getType());
    }

    public static OkHttpClient.Builder getDefaultClientBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONN_SEC, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READ_SEC, TimeUnit.SECONDS);
    }

    public static HttpUrl getUrl(String endPoint) {
        return getUrl(endPoint, "");
    }

    public static HttpUrl getUrl(String endPoint, String urlPart) {
        return HttpUrl.parse(endPoint + urlPart);
    }

    public static HttpUrl getUrl(String endPoint, Map<String, String> queryParams) {
        return getUrl(endPoint, "", queryParams);
    }

    public static HttpUrl getUrl(String endPoint, String urlPart, Map<String, String> queryParams) {
        HttpUrl.Builder builder = getUrl(endPoint, urlPart).newBuilder();
        for (String key : queryParams.keySet()) {
            builder.addQueryParameter(key, queryParams.get(key));
        }
        return builder.build();
    }

    /*public static void showResponseHeaderInfo(Headers headers) {
        for (int i = 0; i < headers.size(); i++) {
            String name = headers.name(i);
            String value = headers.value(i);
            Log.d(TAG, "[Response:Header] " + name + ": " + value);
        }
    }*/

    /*public static void showResponseBodyInfo(int code, String message, String bodyStr) throws IOException {
        Log.d(TAG, "[Response:CODE   ] " + code);
        Log.d(TAG, "[Response:MESSAGE] " + message);
        Log.d(TAG, "[Response:BODY   ] " + bodyStr);
    }*/
}
