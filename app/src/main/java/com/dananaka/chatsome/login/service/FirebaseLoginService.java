package com.dananaka.chatsome.login.service;

import android.util.Log;

import com.jakewharton.rxrelay.BehaviorRelay;
import com.dananaka.chatsome.login.data_model.Authentication;
import com.dananaka.chatsome.login.database.AuthDatabase;
import com.dananaka.chatsome.main.database.CloudMessagingDatabase;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by Vicknesh on 27/01/17.
 */

public class FirebaseLoginService implements LoginService {

    private final AuthDatabase authDatabase;
    private final CloudMessagingDatabase cloudMessagingDatabase;
    private  BehaviorRelay<Authentication> authRelay;

    public FirebaseLoginService(AuthDatabase authDatabase, CloudMessagingDatabase cloudMessagingDatabase) {
        this.authDatabase = authDatabase;
        this.cloudMessagingDatabase = cloudMessagingDatabase;
        authRelay = BehaviorRelay.create();
    }

    @Override
    public Observable<Authentication> getAuthentication() {
        return authRelay
                .startWith(initRelay());
    }

    private Observable<Authentication> initRelay() {
        return Observable.defer(new Func0<Observable<Authentication>>() {
            @Override
            public Observable<Authentication> call() {
                if (authRelay.hasValue() && authRelay.getValue().isSuccess()) {
                    return Observable.empty();
                } else {
                    return fetchUser();
                }
            }
        });
    }

    private Observable<Authentication> fetchUser() {
        return authDatabase.readAuthentication()
                .doOnNext(authRelay)
                .ignoreElements();
    }

    @Override
    public void loginWithGoogle(String idToken) {
        authDatabase.loginWithGoogle(idToken)
                .subscribe(new Action1<Authentication>() {
                    @Override
                    public void call(Authentication authentication) {
                        if (authentication.isSuccess()) {
                            cloudMessagingDatabase.enableToken(authentication.getUser().getUid());
                        }
                        authRelay.call(authentication);
                    }
                });
    }

    @Override
    public void loginWithFacebook(String idToken) {
        authDatabase.loginWithFacebook(idToken)
                .subscribe(new Action1<Authentication>() {
                    @Override
                    public void call(Authentication authentication) {
                        if(authentication.isSuccess()) {
                            cloudMessagingDatabase.enableToken((authentication.getUser().getUid()));
                        }
                        authRelay.call(authentication);
                    }
                });
    }

    @Override
    public void loginWithEmailAndPass(final String email, final String password) {
        authDatabase.loginWithEmailAndPass(email,password)
                .subscribe(new Action1<Authentication>() {
                    @Override
                    public void call(Authentication authentication) {
                        if (authentication.isSuccess()) {
                            cloudMessagingDatabase.enableToken(authentication.getUser().getUid());
                        }
                        authRelay.call(authentication);
                    }
                });
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        authDatabase.sendPasswordResetEmail(email);
    }

}