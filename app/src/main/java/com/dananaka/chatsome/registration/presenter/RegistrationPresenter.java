package com.dananaka.chatsome.registration.presenter;

import com.dananaka.chatsome.R;
import com.dananaka.chatsome.navigation.RegistrationNavigator;
import com.dananaka.chatsome.registration.service.RegistrationService;
import com.dananaka.chatsome.registration.view.RegistrationDisplayer;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Vicknesh on 28/12/16.
 */

public class RegistrationPresenter {

    private final RegistrationService registrationService;
    private final RegistrationDisplayer registrationDisplayer;
    private final RegistrationNavigator navigator;

    private Subscription subscription;

    public RegistrationPresenter(RegistrationService registrationService,
                          RegistrationDisplayer registrationDisplayer,
                          RegistrationNavigator navigator) {

        this.registrationService = registrationService;
        this.registrationDisplayer = registrationDisplayer;
        this.navigator = navigator;

    }

    public void startPresenting() {
        registrationDisplayer.attach(actionListener);
        subscription = registrationService.getRegistration()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            registrationDisplayer.showRegistrationAlertDialog(R.string.registration_dialog_text);
                        }
                    }
                });
    }

    public void stopPresenting() {
        registrationDisplayer.detach(actionListener);
        subscription.unsubscribe();
    }

    private final RegistrationDisplayer.RegistrationActionListener actionListener = new RegistrationDisplayer.RegistrationActionListener() {

        @Override
        public void onRegistrationSubmit(String email, String password, String confirm) {
            if (email.length() == 0) {
                registrationDisplayer.showErrorFromResourcesString(R.string.registration_snackbar_email_short);
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                registrationDisplayer.showErrorFromResourcesString(R.string.registration_snackbar_email_error);
                return;
            }
            if (password.length() < 8) {
                registrationDisplayer.showErrorFromResourcesString(R.string.registration_snackbar_password_short);
                return;
            }
            if (!confirm.equals(password)) {
                registrationDisplayer.showErrorFromResourcesString(R.string.registration_snackbar_password_error);
                return;
            }
            registrationService.registerWithEmailAndPass(email,password);
        }

        @Override
        public void onLoginSelected() {
            navigator.toLogin();
        }

        @Override
        public void onAlertDialogDismissed() {
            navigator.toLogin();
        }
    };

}
