package com.dananaka.chatsome.login.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.dananaka.chatsome.R;

/**
 * Created by Vicknesh on 27/01/17.
 */

// Presenter make request to FirebaseLoginService -> FirebaseAuthDatabase -> then I retrieve the model Authentication
// Activity init all the Firebase vars I need and the LoginView and LoginNavigator

public class LoginView extends CoordinatorLayout implements LoginDisplayer {

    private CoordinatorLayout layout;
    private EditText emailEditText;
    private EditText passwordEditText;
    private View loginButton;
    private View forgotButton;
    private View registerButton;
    private Button gButton;
    private LoginButton fbLoginButton;
    private Button fButton;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_login_view, this);

        layout = (CoordinatorLayout) this.findViewById(R.id.activity_login);
        emailEditText = (EditText) this.findViewById(R.id.emailEditText);
        passwordEditText = (EditText) this.findViewById(R.id.passwordEditText);

        loginButton = this.findViewById(R.id.loginButton);
        forgotButton = this.findViewById(R.id.forgotButton);
        registerButton = this.findViewById(R.id.registerButton);
        SignInButton googleButton = (SignInButton) this.findViewById(R.id.google);
        gButton = (Button) this.findViewById(R.id.google_button);
//        setGooglePlusButtonText(googleButton, R.string.common_signin_button_text_long);
//        googleButton.setSize(SignInButton.SIZE_WIDE);// wide button style
        fbLoginButton = (LoginButton) findViewById(R.id.loginWithFacebook);
    }

//    protected void setGooglePlusButtonText(SignInButton signInButton, int buttonText) {
//        // Find the TextView that is inside of the SignInButton and set its text
//        for (int i = 0; i < signInButton.getChildCount(); i++) {
//            View v = signInButton.getChildAt(i);
//
//            if (v instanceof TextView) {
//                TextView tv = (TextView) v;
//                tv.setText(buttonText);
//                return;
//            }
//        }
//    }

    @Override
    public void attach(final LoginActionListener actionListener) {
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                actionListener.onEmailAndPassLoginSelected(emailEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });
        forgotButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onForgotSelected();
            }
        });
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onRegistrationSelected();
            }
        });
        gButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onGooglePlusLoginSelected();
            }
        });
        fbLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onFacebookLoginSelected(fbLoginButton);
            }
        });
    }

    @Override
    public void detach(LoginActionListener actionListener) {
        gButton.setOnClickListener(null);
        loginButton.setOnClickListener(null);
        fbLoginButton.setOnClickListener(null);
    }

    @Override
    public void showAuthenticationError(String message) {
        Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();
//        Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorFromResourcesString(int id) {
        Snackbar.make(layout, getContext().getString(id), Snackbar.LENGTH_LONG).show();
    }


}
