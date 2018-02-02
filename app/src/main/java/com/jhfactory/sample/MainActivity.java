package com.jhfactory.sample;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.android.gms.common.AccountPicker;
import com.jhfactory.jhlogger.LogLevel;
import com.jhfactory.jhlogger.Logger;
import com.jhfactory.sample.googleapi.CloudProjectsGoogleAPI;
import com.jhfactory.sample.googleapi.GoogleAPICallback;
import com.jhfactory.sample.googleapi.data.Error;
import com.jhfactory.sample.googleapi.data.Projects;
import com.jhfactory.sample.oauth.AuthorizedServiceTask;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_ACCOUNT = 101;

    private TextView idTextView;
    private TextView tokenTextView;

    public static String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.init()
                .setTag("SEOJAEHWA")
                .setLogLevel(LogLevel.DEBUG)
                .setShowThreadInfo(true);

        setContentView(R.layout.activity_main);

        idTextView = findViewById(R.id.tv_id);
        tokenTextView = findViewById(R.id.tv_token);

        if (getAccountFromPref(this) != null) {
            Account account = getAccountFromPref(this);
            new AuthorizedServiceTask(this, account, (account1, token) -> {
                Logger.d(account1.toString());
                setAuthUi(account1, token);
            }).execute();
        }

        findViewById(R.id.btn_pick_account).setOnClickListener(view -> {
            pickUserAccount();
        });

        findViewById(R.id.btn_http_request_get).setOnClickListener(view -> {
            CloudProjectsGoogleAPI.getProjectList(this, new GoogleAPICallback<Projects>(this) {
                @Override
                public void onResponse(Projects projects, Bundle bundle) {
                    Logger.i("onResponse > " + projects.getProjects().size());
                }

                @Override
                public void onResponseFailed(Error error, Bundle bundle) {
                    super.onResponseFailed(error, bundle);
                    Logger.w("code: " + error.getError().getCode());
                }
            }).execute();
        });

        findViewById(R.id.btn_http_request_cancel).setOnClickListener(view -> {
//            if (task != null && !task.isCancelled()) {
//                task.cancel(true);
//            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d("requestCode: " + requestCode);
        switch (requestCode) {
            case REQUEST_CODE_PICK_ACCOUNT:
                // Receiving a result from the AccountPicker
                if (resultCode == Activity.RESULT_OK) {
                    String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    String type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
                    Account account = new Account(name, type);
                    setAccountToPref(this, account);
                    new AuthorizedServiceTask(this, account, (account1, token) -> {
                        Logger.d(account1.toString());
                        setAuthUi(account1, token);
                    }).execute();
                }
            case AuthorizedServiceTask.REQUEST_CODE_GOOGLE_PLAY_SERVICES_AVAILABILITY_EXCEPTION:
                break;
            case AuthorizedServiceTask.REQUEST_CODE_USER_RECOVERABLE_AUTH_EXCEPTION:
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setAuthUi(Account account, String token) {
        idTextView.setText(account.name);
        tokenTextView.setText(token);
        accessToken = token;
    }

    protected void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(
                null, null, accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    public static void setAccountToPref(Context context, Account account) {
        SharedPrefs.putDefaultPrefString(context, "account_type", account.type);
        SharedPrefs.putDefaultPrefString(context, "account_name", account.name);
    }

    public static Account getAccountFromPref(Context context) {
        String type = SharedPrefs.getDefaultPrefString(context, "account_type", null);
        String name = SharedPrefs.getDefaultPrefString(context, "account_name", null);
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(name)) {
            return null;
        }
        return new Account(name, type);
    }
}
