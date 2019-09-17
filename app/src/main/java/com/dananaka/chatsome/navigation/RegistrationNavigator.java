package com.dananaka.chatsome.navigation;

/**
 * Created by Vicknesh on 28/12/16.
 */

public interface RegistrationNavigator extends Navigator {

    void toLogin();

    void attach(RegistrationResultListener registrationResultListener);

    void detach(RegistrationResultListener registrationResultListener);

    interface RegistrationResultListener {

        void onRegistrationSuccess(String statusMessage);

        void onRegistrationFailed(String statusMessage);

    }
}
