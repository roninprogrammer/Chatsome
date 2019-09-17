package com.dananaka.chatsome.registration.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jakewharton.rxrelay.BehaviorRelay;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

/**
 * Created by Vicknesh on 28/12/16.
 */

public class FirebaseRegistrationService implements RegistrationService {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private BehaviorRelay<Boolean> registerRelay;
    private Boolean isRegistrationCompleted;

    public FirebaseRegistrationService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.isRegistrationCompleted = false;
        registerRelay = BehaviorRelay.create();
    }

    @Override
    public Observable<Boolean> getRegistration() {
        return registerRelay.startWith(initRelay());
    }

    private Observable<Boolean> initRelay() {
        return Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                if (registerRelay.hasValue() && registerRelay.getValue()) {
                    return Observable.empty();
                } else {
                    return Observable.create(new Observable.OnSubscribe<Boolean>() {
                        @Override
                        public void call(Subscriber<? super Boolean> subscriber) {
                            subscriber.onCompleted();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void registerWithEmailAndPass(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            firebaseUser.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            isRegistrationCompleted = true;
                                        }
                                    });
                        }
                        registerRelay.call(isRegistrationCompleted);
                    }
                });
    }

}
