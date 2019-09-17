package com.dananaka.chatsome.navigation;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.about.AboutActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Vicknesh on 24/02/17.
 */

public class AndroidAboutNavigator implements AboutNavigator {

    private final AppCompatActivity activity;

    private AboutNavigator.AboutDialogListener dialogListener;

    public AndroidAboutNavigator(AboutActivity activity) {
        this.activity = activity;
    }



    @Override
    public void showOkDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .content(R.string.about_dialog_save_content)
                .positiveText(R.string.about_dialog_about_positive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
//                        activity.finish();
                    }
                })
                .show();
    }

    @Override
    public void attach(AboutDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void detach(AboutDialogListener dialogListener) {
        this.dialogListener = null;
    }

    @Override
    public void toLogin() {

    }

    @Override
    public void toMainActivity() {

    }

    @Override
    public void toParent() {
        activity.finish();
    }


    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {

        return true;
    }
}
