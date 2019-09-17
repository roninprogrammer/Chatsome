package com.dananaka.chatsome.navigation;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dananaka.chatsome.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.login.LoginGoogleApiClient;
import com.dananaka.chatsome.registration.RegistrationActivity;

/**
 * Created by Vicknesh on 27/12/16.
 */

public class AndroidLoginNavigator implements LoginNavigator {

    private static final int RC_SIGN_IN = 9001;

    private final AppCompatActivity activity;
    private final LoginGoogleApiClient googleApiClient;
    private final Navigator navigator;
    private LoginResultListener loginResultListener;
    private ForgotDialogListener forgotDialogListener;
    private boolean doubleBackToExitPressedOnce = false;

    public AndroidLoginNavigator(LoginActivity activity, LoginGoogleApiClient googleApiClient, Navigator navigator) {
        this.activity = activity;
        this.googleApiClient = googleApiClient;
        this.navigator = navigator;
    }

    @Override
    public void toGooglePlusLogin() {
        Intent signInIntent = googleApiClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void toRegistration() {
        activity.startActivity(new Intent(activity, RegistrationActivity.class));
    }

    @Override
    public void showForgotDialog() {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title(R.string.login_dialog_forgot_title)
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .positiveText(R.string.login_dialog_forgot_ok)
                .negativeText(R.string.login_dialog_forgot_close)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dialog.getInputEditText() != null)
                            forgotDialogListener.onPositiveSelected(dialog.getInputEditText().getText().toString());
                        dialog.dismiss();
                    }
                })
                .input(R.string.login_dialog_forgot_title, 0, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                }).show();
    }

    @Override
    public void attach(LoginResultListener loginResultListener, ForgotDialogListener forgotDialogListener) {
        this.loginResultListener = loginResultListener;
        this.forgotDialogListener = forgotDialogListener;
    }

    @Override
    public void detach(LoginResultListener loginResultListener, ForgotDialogListener ForgotDialogListener) {
        this.loginResultListener = null;
        this.forgotDialogListener = null;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RC_SIGN_IN) {
            return false;
        }
        GoogleSignInResult result = googleApiClient.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            loginResultListener.onGoogleLoginSuccess(account.getIdToken());
        } else {
            loginResultListener.onLoginFailed(result.getStatus().getStatusMessage());
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Boolean onBackPressed() {
        if (doubleBackToExitPressedOnce)
            activity.finishAffinity();

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(activity, R.string.main_toast_exit_message, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
        return true;
    }

    @Override
    public void toLogin() {

    }

    @Override
    public void toMainActivity() {
        navigator.toMainActivity();
    }

    @Override
    public void toParent() {
        activity.finish();
    }
}
