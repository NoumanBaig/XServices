package com.hss.xservices.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class ShowDialog {

    ProgressDialog dialog;

    public ShowDialog(Context context) {
        super();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading");
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
