package com.dananaka.chatsome.registration.view;

import android.content.Context;
import android.content.DialogInterface;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dananaka.chatsome.R;

/**
 * Created by Vicknesh on 28/12/16.
 */

public class RegistrationView extends CoordinatorLayout implements RegistrationDisplayer {

    private CoordinatorLayout layout;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private View registrationButton;
    private View loginButton;
    private MaterialDialog alertDialog;

    public RegistrationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_registration_view, this);

        layout = (CoordinatorLayout) this.findViewById(R.id.layout);

        emailEditText = (EditText) this.findViewById(R.id.emailEditText);
        passwordEditText = (EditText) this.findViewById(R.id.passwordEditText);
        confirmEditText = (EditText) this.findViewById(R.id.confirmEditText);

        registrationButton = this.findViewById(R.id.registerButton);
        loginButton = this.findViewById(R.id.loginButton);

        alertDialog = new MaterialDialog.Builder(getContext())
                .title(R.string.registration_dialog_title)
                .content(R.string.registration_dialog_text)
                .positiveText(R.string.registration_dialog_positive)
                .build();
    }

    @Override
    public void attach(final RegistrationActionListener actionListener) {
        registrationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                actionListener.onRegistrationSubmit(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        confirmEditText.getText().toString()
                );
            }
        });
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onLoginSelected();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                actionListener.onAlertDialogDismissed();
            }
        });
    }

    @Override
    public void detach(RegistrationActionListener actionListener) {
        registrationButton.setOnClickListener(null);
        loginButton.setOnClickListener(null);
        alertDialog.setOnDismissListener(null);
    }

    @Override
    public void showRegistrationAlertDialog(int id) {
        alertDialog.show();
    }

    @Override
    public void showErrorFromResourcesString(int id) {
        Snackbar.make(layout, getContext().getString(id), Snackbar.LENGTH_LONG).show();
    }
}
