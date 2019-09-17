package com.dananaka.chatsome.registration.service;

import rx.Observable;

/**
 * Created by Vicknesh on 28/12/16.
 */

public interface RegistrationService {

    Observable<Boolean> getRegistration();

    void registerWithEmailAndPass(String email, String password);

}
