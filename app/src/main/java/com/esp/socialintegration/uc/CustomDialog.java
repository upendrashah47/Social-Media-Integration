package com.esp.socialintegration.uc;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.esp.socialintegration.R;
import com.esp.socialintegration.backend.CustomDialogListener;

/**
 * Created by admin on 4/2/17.
 */

public class CustomDialog {

    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    public static final int OK_CODE = 100;
    public static final int CANCEL_CODE = 200;

    public void show(Context context, String title, String message, String okString, String cancelString, final int tag, final CustomDialogListener customDialogListener) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setMessage(message);

        if (cancelString == null) {
            builder.setPositiveButton(okString,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (customDialogListener != null) {
                                customDialogListener.onAlert(tag, OK_CODE);
                            }
                            dialog.dismiss();
                        }
                    });
        } else {
            builder.setPositiveButton(okString,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (customDialogListener != null) {
                                customDialogListener.onAlert(tag, OK_CODE);
                            }
                            dialog.dismiss();
                        }
                    });

            builder.setNegativeButton(cancelString,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (customDialogListener != null) {
                                customDialogListener.onAlert(tag, CANCEL_CODE);
                            }
                            dialog.dismiss();
                        }
                    });
        }

        dialog = builder.create();
        dialog.show();

    }

    public void show(Context context, String msg) {
        show(context, "", msg, context.getString(R.string.ok), null, 0, null);
    }

    public void show(Context context, String title, String msg) {
        show(context, title, msg, context.getString(R.string.ok), null, 0, null);
    }

    public void show(Context context, String title, String msg, String okString) {
        show(context, title, msg, okString, "", 0, null);
    }

    public void show(Context context, String title, String msg, String okString, String cancelString) {
        show(context, title, msg, okString, cancelString, 0, null);
    }

    public void show(Context context, String msg, int tag, final CustomDialogListener customDialogListener) {
        show(context, "", msg, context.getString(R.string.ok), null, tag, customDialogListener);
    }
}

