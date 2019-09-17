package com.dananaka.chatsome.user.service;

import com.dananaka.chatsome.user.data_model.User;
import com.dananaka.chatsome.user.data_model.Users;

import rx.Observable;

/**
 * Created by Vicknesh on 31/12/16.
 */

public interface UserService {

    Observable<Users> syncUsers();

    Observable<Users> syncOnlineUsers();

    Observable<User> getUser(String userId);

    Observable<Users> getUsers();

    void setName(User user, String name);

    void setStatus(User user, String status);

    void setPlaceName(User user, String placeName);

    void setPlaceLat(User user, String placeLat);

    void setPlaceLong(User user, String placeLong);

    void setProfileImage(User user, String image);

}