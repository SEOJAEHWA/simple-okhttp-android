package com.jhfactory.api;

import android.os.Bundle;

import java.io.IOException;

public interface IHttpCallback {

    void onFailure(IOException e, Bundle bundle);

    void onResponse(int code, String respBody, Bundle bundle);
}
