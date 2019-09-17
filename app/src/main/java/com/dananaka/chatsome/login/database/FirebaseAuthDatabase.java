package com.dananaka.chatsome.login.database;

import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.dananaka.chatsome.R;
import com.dananaka.chatsome.login.data_model.Authentication;
import com.dananaka.chatsome.user.data_model.User;

import org.w3c.dom.Text;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Vicknesh on 27/01/17.
 */

public class FirebaseAuthDatabase implements AuthDatabase {

        private final FirebaseAuth firebaseAuth;

        public FirebaseAuthDatabase(FirebaseAuth firebaseAuth) {
            this.firebaseAuth = firebaseAuth;
        }

        @Override
        public Observable<Authentication> readAuthentication() {
            return Observable.create(new Observable.OnSubscribe<Authentication>() {
                @Override
                public void call(Subscriber<? super Authentication> subscriber) {
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        subscriber.onNext(authenticationFrom(currentUser));
                    }
                    subscriber.onCompleted();
                }
            });
        }



        @Override
        public Observable<Authentication> loginWithGoogle(final String idToken) {
            return Observable.create(new Observable.OnSubscribe<Authentication>() {
                @Override
                public void call(final Subscriber<? super Authentication> subscriber) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                    firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = task.getResult().getUser();
                                        subscriber.onNext(authenticationFrom(firebaseUser));
                                    } else {
                                        subscriber.onNext(new Authentication(task.getException()));
                                    }
                                    subscriber.onCompleted();
                                }
                            });
                }
            });
        }

        @Override
        public Observable<Authentication> loginWithEmailAndPass(final String email, final String password) {
            return Observable.create(new Observable.OnSubscribe<Authentication>() {
                @Override
                public void call(final Subscriber<? super Authentication> subscriber) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = task.getResult().getUser();
                                        subscriber.onNext(authenticationFrom(firebaseUser));
                                    } else {
                                        subscriber.onNext(new Authentication(task.getException()));
                                    }
                                    subscriber.onCompleted();
                                }
                            });
                }
            });
        }

    @Override
    public Observable<Authentication> loginWithFacebook(final String idToken) {
        return Observable.create(new Observable.OnSubscribe<Authentication>() {
            @Override
            public void call(final Subscriber<? super Authentication> subscriber) {
                AuthCredential credential = FacebookAuthProvider.getCredential(idToken);
                firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = task.getResult().getUser();
                                    subscriber.onNext(authenticationFrom(firebaseUser));
                                } else {
                                    subscriber.onNext(new Authentication(task.getException()));
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            return;
                        }
                    }
                });
    }

    private Authentication authenticationFrom(FirebaseUser currentUser) {
        Uri photoUrl = currentUser.getPhotoUrl();
        String userStatus ="";
        String userPlaceName = "";
        String userPlaceLat = "";
        String userPlaceLong = "";
        return new Authentication(new User(currentUser.getUid(), currentUser.getDisplayName(), userStatus.equals("") ? "" : userStatus, userPlaceName.equals("") ? "" : userPlaceName, userPlaceLat.equals("") ? "" : userPlaceLat, userPlaceLong.equals("") ? "" : userPlaceLong, photoUrl == null ? "" : photoUrl.toString(), currentUser.getEmail()));
    }

}