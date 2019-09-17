package com.dananaka.chatsome.login.view;

import com.facebook.login.widget.LoginButton;

/**
 * Created by Vicknesh on 27/01/17.
 */

public interface LoginDisplayer {

    void attach(LoginActionListener actionListener);

    void detach(LoginActionListener actionListener);

    void showAuthenticationError(String message);

    void showErrorFromResourcesString(int id);

    public interface LoginActionListener {

        void onGooglePlusLoginSelected();

        void onFacebookLoginSelected(LoginButton loginButton);

        void onEmailAndPassLoginSelected(String email, String password);

        void onRegistrationSelected();

        void onForgotSelected();

    }

}