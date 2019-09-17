package com.dananaka.chatsome.user.service;

import com.dananaka.chatsome.user.data_model.User;
import com.dananaka.chatsome.user.data_model.Users;
import com.dananaka.chatsome.user.database.UserDatabase;

import rx.Observable;

/**
 * Created by Vicknesh on 31/12/16.
 */

public class PersistedUserService implements UserService {

    private final UserDatabase userDatabase;


    public PersistedUserService(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Override
    public Observable<Users> syncUsers() {
        return userDatabase.observeUsers();
    }

    @Override
    public Observable<Users> syncOnlineUsers() {
        return userDatabase.observeOnlineUsers();
    }

    @Override
    public Observable<User> getUser(String userId) {
        return userDatabase.observeUser(userId);
    }

    @Override
    public Observable<Users> getUsers() {
        return userDatabase.singleObserveUsers();
    }

    @Override
    public void setName(User user, String name) {
        userDatabase.setUserName(user.getUid(),name);
    }

    @Override
    public void setStatus(User user, String status) {
        userDatabase.setUserStatus(user.getUid(),status);
    }

    @Override
    public void setPlaceName(User user, String placeName) {
        userDatabase.setUserStatus(user.getUid(),placeName);
    }

    @Override
    public void setPlaceLat(User user, String placeLat) {
        userDatabase.setUserStatus(user.getUid(),placeLat);
    }

    @Override
    public void setPlaceLong(User user, String placeLong) {
        userDatabase.setUserStatus(user.getUid(),placeLong);
    }

    @Override
    public void setProfileImage(User user, String image) {
        userDatabase.setUserImage(user.getUid(),image);
    }

}
