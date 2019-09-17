package com.dananaka.chatsome.user.presenter;

import android.os.Bundle;

import com.dananaka.chatsome.login.data_model.Authentication;
import com.dananaka.chatsome.login.service.LoginService;
import com.dananaka.chatsome.navigation.AndroidConversationsNavigator;
import com.dananaka.chatsome.user.data_model.User;
import com.dananaka.chatsome.user.data_model.Users;
import com.dananaka.chatsome.user.service.UserService;
import com.dananaka.chatsome.user.view.UsersDisplayer;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Vicknesh on 19/12/16.
 */

public class UsersPresenter {

    private static final String SENDER = "sender";
    private static final String DESTINATION = "destination";

    private UsersDisplayer usersDisplayer;
    private AndroidConversationsNavigator navigator;
    private LoginService loginService;
    private UserService userService;

    private Subscription loginSubscription;
    private Subscription userSubscription;

    private User self;

    public UsersPresenter(
            UsersDisplayer conversationListDisplayer,
            AndroidConversationsNavigator navigator,
            LoginService loginService,
            UserService userService) {
        this.usersDisplayer = conversationListDisplayer;
        this.navigator = navigator;
        this.loginService = loginService;
        this.userService = userService;
    }

    public void startPresenting() {
        usersDisplayer.attach(conversationInteractionListener);

        final Subscriber<com.dananaka.chatsome.user.data_model.Users> usersSubscriber = new Subscriber<Users>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Users users) {
                usersDisplayer.display(users);
            }
        };
        Subscriber<com.dananaka.chatsome.login.data_model.Authentication> userSubscriber = new Subscriber<Authentication>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Authentication authentication) {

                //If need to show only online users the replace the syncUsers() to syncOnlineUsers();
                userSubscription = userService.syncUsers()
                        .subscribe(usersSubscriber);
            }
        };

        loginSubscription = loginService.getAuthentication()
                .filter(successfullyAuthenticated())
                .doOnNext(new Action1<Authentication>() {
                    @Override
                    public void call(Authentication authentication) {
                        self = authentication.getUser();
                    }
                })
                .subscribe(userSubscriber);
    }

    public void stopPresenting() {
        usersDisplayer.detach(conversationInteractionListener);
        loginSubscription.unsubscribe();
        if (userSubscription != null)
            userSubscription.unsubscribe();
    }

    private Func1<Authentication, Boolean> successfullyAuthenticated() {
        return new Func1<Authentication, Boolean>() {
            @Override
            public Boolean call(Authentication authentication) {
                return authentication.isSuccess();
            }
        };
    }

    private final UsersDisplayer.UserInteractionListener conversationInteractionListener = new UsersDisplayer.UserInteractionListener() {

        @Override
        public void onUserSelected(User user) {
            Bundle bundle = new Bundle();
            bundle.putString(SENDER, self.getUid());
            bundle.putString(DESTINATION,user.getUid());
            navigator.toSelectedConversation(bundle);
        }
    };

    public void filterUsers(String text) {
        usersDisplayer.filter(text);
    }

}
