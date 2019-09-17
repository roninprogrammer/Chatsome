package com.dananaka.chatsome.login.database;

import com.dananaka.chatsome.login.data_model.Authentication;

import rx.Observable;

/**
 * Created by Vicknesh on 27/01/17.
 */

public interface AuthDatabase {

    Observable<Authentication> readAuthentication();

    Observable<Authentication> loginWithGoogle(String idToken);

    Observable<Authentication> loginWithEmailAndPass(String email, String password);

    Observable<Authentication> loginWithFacebook(String idToken);

    void sendPasswordResetEmail(String email);

}
