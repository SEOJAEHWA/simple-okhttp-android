package com.jhfactory.sample.googleapi;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jhfactory.api.IHttpCallback;
import com.jhfactory.api.OkHttpTask;
import com.jhfactory.api.Method;
import com.jhfactory.api.OkHttpUtils;

public class CloudProjectsGoogleAPI {

  private static final String GOOGLE_CRM_API_URL_ROOT =
      "https://cloudresourcemanager.googleapis.com";

  /**
   * GET https://cloudresourcemanager.googleapis.com/v1/projects
   *
   * @param callback
   * @return
   */
  public static OkHttpTask getProjectList(@NonNull Context context, IHttpCallback callback) {
    final String url = GOOGLE_CRM_API_URL_ROOT + "/v1/projects";
    return new GoogleProjectsTask.Builder(context)
        .method(Method.GET)
        .url(OkHttpUtils.getUrl(url))
        .callback(callback)
        .build();
  }
}
