// Copyright 2015 Google Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.jhfactory.sample.oauth;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GoogleApiAvailability;
import com.jhfactory.jhlogger.Logger;

import java.io.IOException;

/**
 * NOP async task that allows us to check if a new user has authorized the app
 * to access their account.
 */
public class AuthorizedServiceTask extends AsyncTask<Void, Void, String> {

    public static final String AUTH_SCOPE = "oauth2:https://www.googleapis.com/auth/cloud-platform";
    public static final int REQUEST_CODE_GOOGLE_PLAY_SERVICES_AVAILABILITY_EXCEPTION = 1002;
    public static final int REQUEST_CODE_USER_RECOVERABLE_AUTH_EXCEPTION = 1003;

    private final Activity activity;
    private final Account account;
    private OnPostExecuteCallback callback;

    public interface OnPostExecuteCallback {
        void onPostExecute(Account account, String token);
    }

    public AuthorizedServiceTask(Activity activity, Account account, OnPostExecuteCallback callback) {
        this.activity = activity;
        this.account = account;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... params) {
        Logger.d("checking authorization for " + account.name, false);
        try {
            return GoogleAuthUtil.getToken(activity, account, AUTH_SCOPE);
        }
        catch (UserRecoverableAuthException e) {
            // GooglePlayServices.apk is either old, disabled, or not present
            // so we need to show the user some UI in the activity to recover.
            Logger.printStackTrace(e);
            handleAuthException(e);
        }
        catch (GoogleAuthException e) {
            // Some other type of unrecoverable exception has occurred.
            // Report and log the error as appropriate for your app.
            Logger.printStackTrace(e);
        }
        catch (IOException e) {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.
            Logger.printStackTrace(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        Logger.d("GoogleAuthToken: " + s);
        callback.onPostExecute(account, s);
    }

    private void handleAuthException(final UserRecoverableAuthException e) {
        activity.runOnUiThread(() -> {
            if (e instanceof GooglePlayServicesAvailabilityException) {
                // The Google Play services APK is old, disabled, or not present.
                // Show a dialog created by Google Play services that allows
                // the user to update the APK
                int connectionStatusCode = ((GooglePlayServicesAvailabilityException) e).getConnectionStatusCode();
                GoogleApiAvailability.getInstance().getErrorDialog(activity, connectionStatusCode,
                        REQUEST_CODE_GOOGLE_PLAY_SERVICES_AVAILABILITY_EXCEPTION).show();
            }
            else {
                // Unable to authenticate, such as when the user has not yet granted
                // the app access to the account, but the user can fix this.
                // Forward the user to an activity in Google Play services.
                activity.startActivityForResult(e.getIntent(), REQUEST_CODE_USER_RECOVERABLE_AUTH_EXCEPTION);
            }
        });
    }
}
