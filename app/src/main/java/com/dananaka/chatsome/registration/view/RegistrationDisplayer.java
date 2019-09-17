package com.dananaka.chatsome.registration.view;

/**
 * Created by Vicknesh on 28/12/16.
 */

public interface RegistrationDisplayer {

    void attach(RegistrationActionListener actionListener);

    void detach(RegistrationActionListener actionListener);

    void showRegistrationAlertDialog(int id);

    void showErrorFromResourcesString(int id);

    public interface RegistrationActionListener {

        void onRegistrationSubmit(String email, String password, String confirm);

        void onLoginSelected();

        void onAlertDialogDismissed();

    }

}
