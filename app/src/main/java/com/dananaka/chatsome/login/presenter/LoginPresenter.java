package com.dananaka.chatsome.login.presenter;


import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.login.data_model.Authentication;
import com.dananaka.chatsome.login.service.LoginService;
import com.dananaka.chatsome.login.view.LoginDisplayer;
import com.dananaka.chatsome.navigation.LoginNavigator;

import java.util.Arrays;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Vicknesh on 27/01/17.
 */

public class LoginPresenter {

    private final LoginService loginService;
    private final LoginDisplayer loginDisplayer;
    private final LoginNavigator navigator;

    private final CallbackManager mCallbackManager;

    private Subscription subscription;

//    private static final String TAG = "FacebookLogin";

    public LoginPresenter(LoginService loginService,
                          LoginDisplayer loginDisplayer,
                          LoginNavigator navigator, CallbackManager mCallbackManager) {
        this.loginService = loginService;
        this.loginDisplayer = loginDisplayer;
        this.navigator = navigator;
        this.mCallbackManager = mCallbackManager;
    }

    public void startPresenting() {
        navigator.attach(loginResultListener,forgotDialogListener);
        loginDisplayer.attach(actionListener);
        subscription = loginService.getAuthentication()
                .subscribe(new Subscriber<Authentication>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Authentication authentication) {
                        if (authentication.isSuccess()) {
                            navigator.toMainActivity();
                        } else {
                            loginDisplayer.showAuthenticationError(authentication.getFailure().getLocalizedMessage());
                        }
                    }
                });
    }

    public void stopPresenting() {
        navigator.detach(loginResultListener,forgotDialogListener);
        loginDisplayer.detach(actionListener);
        subscription.unsubscribe();
    }

    private final LoginDisplayer.LoginActionListener actionListener = new LoginDisplayer.LoginActionListener() {

        @Override
        public void onGooglePlusLoginSelected() {
            navigator.toGooglePlusLogin();
        }

        @Override
        public void onFacebookLoginSelected(LoginButton loginButton) {
            // Set permissions
            loginButton.setReadPermissions(Arrays.asList( "email","public_profile"));
            loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
//                    Log.d(TAG, "facebook:onSuccess:" + loginResult.getAccessToken().getToken());
                    loginService.loginWithFacebook(loginResult.getAccessToken().getToken());
                }

                @Override
                public void onCancel() {
                    //Log.d(TAG, "facebook:onCancel");
                }

                @Override
                public void onError(FacebookException error) {
//                    loginDisplayer.showErrorFromResourcesString(Integer.parseInt(error.getMessage()));
                }
            });
        }


        @Override
        public void onEmailAndPassLoginSelected(String email, String password) {
            if (email.length() == 0) {
                loginDisplayer.showErrorFromResourcesString(R.string.login_snackbar_email_short);
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginDisplayer.showErrorFromResourcesString(R.string.login_snackbar_email_error);
                return;
            }
            if (password.length() == 0) {
                loginDisplayer.showErrorFromResourcesString(R.string.login_snackbar_password_short);
                return;
            }
            loginService.loginWithEmailAndPass(email, password);
        }

        @Override
        public void onRegistrationSelected() {
            navigator.toRegistration();
        }

        @Override
        public void onForgotSelected() {
            navigator.showForgotDialog();
        }

    };

    private final LoginNavigator.LoginResultListener loginResultListener = new LoginNavigator.LoginResultListener() {

        @Override
        public void onGoogleLoginSuccess(String tokenId) {
            loginService.loginWithGoogle(tokenId);
        }

        @Override
        public void onLoginFailed(String statusMessage) {
            loginDisplayer.showAuthenticationError(statusMessage);
        }
    };

    private final LoginNavigator.ForgotDialogListener forgotDialogListener = new LoginNavigator.ForgotDialogListener() {

        @Override
        public void onPositiveSelected(String email) {
            loginService.sendPasswordResetEmail(email);
        }

    };

}

