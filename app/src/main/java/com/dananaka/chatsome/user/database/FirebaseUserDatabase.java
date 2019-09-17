package com.dananaka.chatsome.user.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.dananaka.chatsome.Constants;
import com.dananaka.chatsome.rxrelay.FirebaseObservableListeners;
import com.dananaka.chatsome.user.data_model.User;
import com.dananaka.chatsome.user.data_model.Users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Vicknesh on 27/12/16.
 */

public class FirebaseUserDatabase implements UserDatabase {

    private final DatabaseReference usersDB;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseUserDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        usersDB = firebaseDatabase.getReference(Constants.FIREBASE_USERS);
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<Users> observeUsers() {
        return firebaseObservableListeners.listenToValueEvents(usersDB, toUsers());
    }
    @Override
    public Observable<Users> observeOnlineUsers() {
        return firebaseObservableListeners.listenToValueEvents(usersDB.orderByChild("lastSeen").equalTo(0), toUsers());
    }

    @Override
    public Observable<User> readUserFrom(String userId) {
        return firebaseObservableListeners.listenToSingleValueEvents(usersDB.child(userId), as(User.class));
    }

    @Override
    public Observable<Users> singleObserveUsers() {
        return firebaseObservableListeners.listenToSingleValueEvents(usersDB, toUsers());
    }

    @Override
    public Observable<User> observeUser(String userId) {
        return firebaseObservableListeners.listenToValueEvents(usersDB.child(userId), as(User.class));
    }

    @Override
    public Observable<Boolean> initUserLastSeen() {
        DatabaseReference amOnline = usersDB.getParent().child(".info").child("connected");
        return firebaseObservableListeners.listenToValueEvents(amOnline,lastSeenHandler());
    }

    @Override
    public void setUserLastSeen(String userId) {
        DatabaseReference lastSeenRef = usersDB.child(userId).child(Constants.FIREBASE_USERS_LASTSEEN);
        lastSeenRef.setValue(0);
        lastSeenRef.onDisconnect().removeValue();
        lastSeenRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
    }

    @Override
    public void setUserLastSeenAfterLogout(String userId) {
        DatabaseReference lastSeenRef = usersDB.child(userId).child(Constants.FIREBASE_USERS_LASTSEEN);
        lastSeenRef.setValue(ServerValue.TIMESTAMP);
    }

    @Override
    public void setUserName(String userId, String name) {
        usersDB.child(userId).child(Constants.FIREBASE_USERS_NAME).setValue(name);
    }

    @Override
    public void setUserStatus(String userId, String status) {
        usersDB.child(userId).child(Constants.FIREBASE_USERS_STATUS).setValue(status);
    }

    @Override
    public void setUserPlaceName(String userId, String placeName) {
        usersDB.child(userId).child(Constants.FIREBASE_USERS_PLACENAME).setValue(placeName);
    }

    @Override
    public void setUserPlaceLat(String userId, String placeLat) {
        usersDB.child(userId).child(Constants.FIREBASE_USERS_PLACELAT).setValue(placeLat);
    }

    @Override
    public void setUserPlaceLong(String userId, String placeLong) {
        usersDB.child(userId).child(Constants.FIREBASE_USERS_PLACELONG).setValue(placeLong);
    }

    @Override
    public void setUserImage(String userId, String image) {
        usersDB.child(userId).child(Constants.FIREBASE_USERS_IMAGE).setValue(image);
    }

    private Func1<DataSnapshot, Boolean> lastSeenHandler() {
        return new Func1<DataSnapshot, Boolean>() {
            @Override
            public Boolean call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(Boolean.class);
            }
        };
    }


    private Func1<DataSnapshot, Users> toUsers() {
        return new Func1<DataSnapshot, Users>() {
            @Override
            public Users call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<User> users = new ArrayList<>();

                for (DataSnapshot child : children) {
                    User message = child.getValue(User.class);
                    users.add(message);
                }
                return new Users(users);
            }
        };
    }

    private <T> Func1<DataSnapshot, T> as(final Class<T> tClass) {
        return new Func1<DataSnapshot, T>() {
            @Override
            public T call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(tClass);
            }
        };
    }
}