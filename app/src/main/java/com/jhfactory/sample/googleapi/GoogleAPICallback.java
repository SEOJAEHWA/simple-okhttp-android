package com.jhfactory.sample.googleapi;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.jhfactory.api.OkHttpCallback;
import com.jhfactory.api.OkHttpUtils;
import com.jhfactory.jhlogger.Logger;
import com.jhfactory.sample.Utils;
import com.jhfactory.sample.googleapi.data.Error;

import java.io.IOException;


public abstract class GoogleAPICallback<TData> extends OkHttpCallback<TData, Error> {

    protected Context context;

    protected GoogleAPICallback(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onResponse(int code, String respBody, Bundle bundle) {
        if (!OkHttpUtils.isSuccessful(code)) {
            Logger.json("resp. code: " + code, respBody);
            Error error = new Gson().fromJson(respBody, Error.class);
            onResponseFailed(error, bundle);
            return;
        }
        super.onResponse(code, respBody, bundle);
    }

    @Override
    public void onFailure(IOException e, Bundle bundle) {
        Utils.showNetworkErrorAlert(context);
    }

    @Override
    public void onResponseFailed(Error error, Bundle bundle) {

    }

    @Override
    public void onComplete(Bundle bundle) {

    }
}
