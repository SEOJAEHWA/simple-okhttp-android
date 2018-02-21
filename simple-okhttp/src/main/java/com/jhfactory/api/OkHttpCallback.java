package com.jhfactory.api;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class OkHttpCallback<TData, EData> implements IHttpCallback {

  public abstract void onResponse(TData data, @Nullable Bundle bundle);

  public abstract void onResponseFailed(EData error, @Nullable Bundle bundle);

  public abstract void onComplete(@Nullable Bundle bundle);

  public OkHttpCallback() {}

  @Override
  public void onFailure(IOException e, Bundle bundle) {
    onComplete(bundle);
  }

  @Override
  public void onResponse(int code, String respBody, Bundle bundle) {
    if (OkHttpUtils.isSuccessful(code)) {
      // FIXME: respBody 가 empty 일 경우
      TData data = new Gson().fromJson(respBody, getTypeOfGeneric(0));
      onResponse(data, bundle);
      onComplete(bundle);
    } else {
      EData error = new Gson().fromJson(respBody, getTypeOfGeneric(1));
      onResponseFailed(error, bundle);
      onComplete(bundle);
    }
  }

  private Type getTypeOfGeneric(int index) {
    ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
    Type[] types = type.getActualTypeArguments();
    if (index >= types.length) {
      throw new IndexOutOfBoundsException("index: " + index + ", Size:" + types.length);
    }
    return types[index];
  }
}
