package com.jhfactory.sample;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Utils {

    public static void showErrAuthToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showNetworkErrorAlert(Context context) {
        String title = context.getString(R.string.auth_error_title);
        String message = context.getString(R.string.auth_error_http_network);
        showErrorAlert(context, title, message);
    }

    public static void showNetworkErrorAlert(Context context, String message) {
        String title = context.getString(R.string.auth_error_title);
        showErrorAlert(context, title, message);
    }

    public static void showErrorAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null && !title.equals("")) {
            builder.setTitle(title);
        }
        if (message != null && !message.equals("")) {
            builder.setMessage(message);
        }
        builder.setCancelable(true);
        builder.setPositiveButton(context.getString(R.string.common_confirm), (dialog, which) -> dialog.dismiss());
        builder.create();
        builder.show();
    }
}
