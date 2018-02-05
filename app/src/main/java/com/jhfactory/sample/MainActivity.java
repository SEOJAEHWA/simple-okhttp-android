package com.jhfactory.sample;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.jhfactory.sample.googleapi.CloudProjectsGoogleAPI;
import com.jhfactory.sample.googleapi.GoogleAPICallback;
import com.jhfactory.sample.googleapi.data.Error;
import com.jhfactory.sample.googleapi.data.Project;
import com.jhfactory.sample.googleapi.data.Projects;
import com.jhfactory.sample.oauth.RetrieveAccessTokenTask;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SIGN_IN_GOOGLE = 201;

    private TextView idTextView;
    private TextView tokenTextView;

    private GoogleSignInClient mGoogleSignInClient;

    public static String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleSignInClient = newInstance(this);

        idTextView = findViewById(R.id.tv_id);
        tokenTextView = findViewById(R.id.tv_token);

        findViewById(R.id.btn_sign_in).setOnClickListener(view -> signIn());
        findViewById(R.id.btn_sign_out).setOnClickListener(view -> signOut());
        findViewById(R.id.btn_http_request_get).setOnClickListener(view -> getProjectList());

        initSignIn(this);
    }

    private synchronized GoogleSignInClient newInstance(@NonNull Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(activity, gso);
    }

    private void initSignIn(@NonNull Activity activity) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(activity);
        Log.d(TAG, "[onCreate] Account: " + account);
        showGoogleSignInAccountInfo(account);
        if (account != null) {
            Log.i(TAG, "Retrieve access token in onCreate");
            retrieveAccessToken(activity, account.getAccount());
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN_GOOGLE);
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> updateUi(null, null));
    }

    private void getProjectList() {
        CloudProjectsGoogleAPI.getProjectList(this, new GoogleAPICallback<Projects>(this) {
            @Override
            public void onResponse(Projects projects, Bundle bundle) {
                List<Project> projectList = projects.getProjects();
                Log.i(TAG, "onResponse > " + projectList.size());
                for (Project project : projectList) {
                    Log.i(TAG, "  project[" + project.getProjectId() + "] " + project.getName());
                }
            }

            @Override
            public void onResponseFailed(Error error, Bundle bundle) {
                super.onResponseFailed(error, bundle);
                Log.w(TAG, "code: " + error.getError().getCode());
                Log.w(TAG, "status: " + error.getError().getStatus());
                Log.w(TAG, "message: " + error.getError().getMessage());
            }
        }).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "requestCode: " + requestCode);
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN_GOOGLE:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    showGoogleSignInAccountInfo(account);
                    Log.i(TAG, "Retrieve access token in onActivityResult");
                    retrieveAccessToken(this, account.getAccount());
                } catch (ApiException e) {
                    e.printStackTrace();
                }
                break;
            case RetrieveAccessTokenTask.REQUEST_CODE_GOOGLE_PLAY_SERVICES_AVAILABILITY_EXCEPTION:
                break;
            case RetrieveAccessTokenTask.REQUEST_CODE_USER_RECOVERABLE_AUTH_EXCEPTION:
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void retrieveAccessToken(@NonNull final Activity activity, @Nullable Account acct) {
        if (acct == null) {
            Log.w(TAG, "Can not retrieve access token. Account is null.");
            accessToken = null;
            return;
        }
        Log.d(TAG, "Account name: " + acct.name);
        Log.d(TAG, "Account type: " + acct.type);
        new RetrieveAccessTokenTask(activity, acct, (account, token, e) -> {
            if (e != null) {
                if (e instanceof UserRecoverableAuthException) {
                    RetrieveAccessTokenTask.handleAuthException(activity, (UserRecoverableAuthException) e);
                }
                return;
            }
            accessToken = token;
            updateUi(account, token);
        }).execute();
    }

    private void showGoogleSignInAccountInfo(GoogleSignInAccount acct) {
        if (acct == null) {
            Log.w(TAG, "Account is null!");
            return;
        }
        String personName = acct.getDisplayName();
        String personEmail = acct.getEmail();
        String personId = acct.getId();
        Uri personPhoto = acct.getPhotoUrl();
        String idToken = acct.getIdToken();
        Log.i(TAG, "-------------------------------------");
        Log.i(TAG, "personName: " + personName);
        Log.i(TAG, "personEmail: " + personEmail);
        Log.i(TAG, "personId: " + personId);
        Log.i(TAG, "personPhoto: " + personPhoto);
        Log.i(TAG, "idToken: " + idToken);
        Log.i(TAG, "-------------------------------------");
    }

    private void updateUi(@Nullable Account account, @Nullable String token) {
        if (account == null) {
            idTextView.setText("");
            tokenTextView.setText("");
            return;
        }
        idTextView.setText(account.name);
        tokenTextView.setText(token);
    }
}
