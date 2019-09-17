package com.dananaka.chatsome.main.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.dananaka.chatsome.Constants;
import com.dananaka.chatsome.rxrelay.FirebaseObservableListeners;
import com.dananaka.chatsome.user.data_model.User;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Vicknesh on 17/12/16.
 */

public class FirebaseCloudMessagingDatabase implements CloudMessagingDatabase {

    private final DatabaseReference databaseReference;
    private final FirebaseObservableListeners firebaseObservableListeners;
    private final String firebaseToken;

    public FirebaseCloudMessagingDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners, String firebaseToken) {
        this.databaseReference = firebaseDatabase.getReference(Constants.FIREBASE_FCM);
        this.firebaseObservableListeners = firebaseObservableListeners;
        this.firebaseToken = firebaseToken;
    }

    @Override
    public Observable<String> readToken(User user) {
        return firebaseObservableListeners.listenToValueEvents(
                databaseReference.child(user.getUid()),asString());
    }

    @Override
    public void setToken(User user) {
        databaseReference.child(user.getUid() + "/" + Constants.FIREBASE_FCM_TOKEN).setValue(firebaseToken);
        databaseReference.child(user.getUid() + "/" + Constants.FIREBASE_FCM_ENABLED).setValue(Boolean.TRUE.toString());
    }

    @Override
    public void enableToken(String userId) {
        databaseReference.child(userId + "/" + Constants.FIREBASE_FCM_ENABLED).setValue(Boolean.TRUE.toString());
    }

    @Override
    public void disableToken(String userId) {
        databaseReference.child(userId + "/" + Constants.FIREBASE_FCM_ENABLED).setValue(Boolean.FALSE.toString());
    }


    private static Func1<DataSnapshot, String> asString() {
        return new Func1<DataSnapshot, String>() {
            @Override
            public String call(DataSnapshot dataSnapshot) {
                return dataSnapshot.child(Constants.FIREBASE_FCM_TOKEN).getValue(String.class);
            }
        };
    }
}
