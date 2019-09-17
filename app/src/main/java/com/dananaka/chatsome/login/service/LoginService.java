package com.dananaka.chatsome.login.service;


import com.dananaka.chatsome.login.data_model.Authentication;

import rx.Observable;

/**
 * Created by Vicknesh on 27/01/17.
 */

public interface LoginService {

    Observable<Authentication> getAuthentication();

    void loginWithGoogle(String idToken);

    void loginWithFacebook(String idToken);

    void loginWithEmailAndPass(String email, String password);

    void sendPasswordResetEmail(String email);

}
