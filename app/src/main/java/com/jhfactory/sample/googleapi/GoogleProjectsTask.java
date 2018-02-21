package com.jhfactory.sample.googleapi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jhfactory.api.OkHttpTask;
import com.jhfactory.api.IHttpCallback;
import com.jhfactory.api.Method;
import com.jhfactory.sample.MainActivity;

import java.lang.ref.WeakReference;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

class GoogleProjectsTask extends OkHttpTask {

  private final WeakReference<Context> contextWeakReference;

  private GoogleProjectsTask(
      Context context,
      @Nullable OkHttpClient client,
      Method method,
      HttpUrl url,
      RequestBody requestBody,
      Bundle bundle,
      IHttpCallback callback) {
    super(client, method, url, requestBody, bundle, callback);
    this.contextWeakReference = new WeakReference<>(context);
  }

  @Nullable
  @Override
  public String getAccessToken() {
    return MainActivity.accessToken;
  }

  public static class Builder extends OkHttpTask.Builder {

    final Context context;

    Builder(Context context) {
      this.context = context;
    }

    @Override
    public GoogleProjectsTask build() {
      return new GoogleProjectsTask(context, client, method, url, requestBody, bundle, callback);
    }
  }

  protected Context getContext() {
    return contextWeakReference.get();
  }
}
