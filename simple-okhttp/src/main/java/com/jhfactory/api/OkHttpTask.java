package com.jhfactory.api;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public abstract class OkHttpTask extends AsyncTask<Void, Void, Response> {

    private static final String TAG = OkHttpTask.class.getSimpleName();

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String MEDIA_TYPE_JSON = "application/json";
    private static final String MEDIA_TYPE_FORM = "application/x-www-form-urlencoded";

    private OkHttpClient client;
    private Method method;
    private HttpUrl url;
    private RequestBody requestBody;
    private Bundle bundle;
    private IHttpCallback callback;

    @Nullable
    public abstract String getAccessToken();

    protected OkHttpTask(@Nullable OkHttpClient client, Method method, HttpUrl url, RequestBody requestBody,
                         Bundle bundle, IHttpCallback callback) {
        if (client == null) {
            client = new OkHttpClient();
        }
        this.client = client;
        this.method = method;
        this.url = url;
        this.requestBody = requestBody;
        this.bundle = bundle;
        this.callback = callback;
    }

    @Override
    protected Response doInBackground(Void... params) {
        Request.Builder builder;
        builder = new Request.Builder();
        builder.header(AUTHORIZATION, BEARER + getAccessToken());
        if (requestBody != null) {
            if (requestBody.contentType() == MediaType.parse(MEDIA_TYPE_JSON)) {
                builder.addHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_JSON);
            } else if (requestBody.contentType() == MediaType.parse(MEDIA_TYPE_FORM)) {
                builder.addHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_FORM);
            }
        }
        if (method == Method.GET && requestBody != null) {
            Log.w(TAG, "GET method do not need to have a request body. ignore it.");
            requestBody = null;
        }
        builder.url(url);
        builder.method(method.name(), requestBody);
        Request request = builder.build();
        return call(client, request);
    }

    // TODO: Multipart form data 전송시 progress update 를 위한 callback 이 필요!
    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        try {
            final int code = response.code();
            final ResponseBody body = response.body();
            if (body == null) {
                Log.w(TAG, "Response body is null.");
                return;
            }
            final String bodyStr = body.string();
            callback.onResponse(code, bodyStr, bundle);
        } catch (IOException e) {
            callback.onFailure(e, bundle);
        } finally {
            response.close();
        }
    }

    @Override
    protected void onCancelled(Response response) {
        super.onCancelled(response);
    }

    private Response call(@NonNull OkHttpClient client, Request request) {
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            callback.onFailure(e, bundle);
        }
        return null;
    }

    public static abstract class Builder {

        protected OkHttpClient client;
        protected Method method;
        protected HttpUrl url;
        protected IHttpCallback callback;
        protected Bundle bundle;
        protected RequestBody requestBody;

        protected Builder() {
        }

        @SuppressWarnings("unused")
        public Builder client(@Nullable OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder method(Method method) {
            if (method == null) throw new NullPointerException("method is null");
            this.method = method;
            return this;
        }

        public Builder url(HttpUrl url) {
            if (url == null) throw new NullPointerException("url is null");
            this.url = url;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder requestBody(@Nullable RequestBody requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder bundle(@Nullable Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder callback(IHttpCallback callback) {
            if (callback == null) throw new NullPointerException("OkHttpCallback is null");
            this.callback = callback;
            return this;
        }

        public abstract OkHttpTask build();
    }
}
