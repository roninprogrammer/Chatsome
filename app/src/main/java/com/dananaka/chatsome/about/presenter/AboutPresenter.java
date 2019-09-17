package com.dananaka.chatsome.about.presenter;

import com.dananaka.chatsome.about.view.AboutDisplayer;
import com.dananaka.chatsome.login.data_model.Authentication;
import com.dananaka.chatsome.login.service.LoginService;
import com.dananaka.chatsome.navigation.AboutNavigator;
import com.dananaka.chatsome.storage.StorageService;
import com.dananaka.chatsome.user.data_model.User;
import com.dananaka.chatsome.user.service.UserService;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Vicknesh on 26/12/16.
 */

public class AboutPresenter {

    private final LoginService loginService;
    private final UserService userService;
    private final StorageService storageService;
    private final AboutDisplayer aboutDisplayer;
    private final AboutNavigator navigator;

    private Subscription loginSubscription;
    private Subscription userSubscription;

    public AboutPresenter(LoginService loginService,
                          UserService userService,
                          StorageService storageService,
                          AboutDisplayer aboutDisplayer,
                          AboutNavigator navigator) {
        this.loginService = loginService;
        this.userService = userService;
        this.storageService = storageService;
        this.aboutDisplayer = aboutDisplayer;
        this.navigator = navigator;
    }

    public void startPresenting() {
        aboutDisplayer.attach(actionListener);
        loginSubscription = loginService.getAuthentication()
                .subscribe(new Action1<Authentication>() {
                    @Override
                    public void call(final Authentication authentication) {
                        if (authentication.isSuccess()) {
                            userService.getUser(authentication.getUser().getUid())
                                    .subscribe(new Subscriber<User>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(User user) {
                                           //Todo any thing
                                        }
                                    });
                        } else {
                            navigator.toParent();
                        }
                    }
                });
    }

    public void stopPresenting() {
        aboutDisplayer.detach(actionListener);
        loginSubscription.unsubscribe();
        if (userSubscription != null)
            userSubscription.unsubscribe();
    }

    private AboutDisplayer.AboutActionListener actionListener = new AboutDisplayer.AboutActionListener() {

        @Override
        public void onUpPressed() {
            navigator.toParent();
        }

        @Override
        public void onOkPressed() {
            navigator.showOkDialog();
        }

    };


}
