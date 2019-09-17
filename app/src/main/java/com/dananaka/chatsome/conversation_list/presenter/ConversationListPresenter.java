package com.dananaka.chatsome.conversation_list.presenter;

import android.os.Bundle;

import com.dananaka.chatsome.conversation.data_model.Message;
import com.dananaka.chatsome.conversation_list.data_model.Conversation;
import com.dananaka.chatsome.conversation_list.service.ConversationListService;
import com.dananaka.chatsome.conversation_list.view.ConversationListDisplayer;
import com.dananaka.chatsome.login.data_model.Authentication;
import com.dananaka.chatsome.login.service.LoginService;
import com.dananaka.chatsome.navigation.AndroidConversationsNavigator;
import com.dananaka.chatsome.user.data_model.User;
import com.dananaka.chatsome.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Vicknesh on 29/12/16.
 */

public class ConversationListPresenter {

    private static final String SENDER = "sender";
    private static final String DESTINATION = "destination";

    private ConversationListDisplayer conversationListDisplayer;
    private ConversationListService conversationListService;
    private AndroidConversationsNavigator navigator;
    private LoginService loginService;
    private UserService userService;

    private Subscription loginSubscription;
    private Subscription userSubscription;
    private Subscription messageSubscription;
    private Subscription unreadSubscription;

    private List<String> uids;
    private User self;

    private Integer unreadCount = 0;

    public ConversationListPresenter(
            ConversationListDisplayer conversationListDisplayer,
            ConversationListService conversationListService,
            AndroidConversationsNavigator navigator,
            LoginService loginService,
            UserService userService) {
        this.conversationListDisplayer = conversationListDisplayer;
        this.conversationListService = conversationListService;
        this.navigator = navigator;
        this.loginService = loginService;
        this.userService = userService;
    }



    public void startPresenting() {
        conversationListDisplayer.attach(conversationInteractionListener);

        final Subscriber<com.dananaka.chatsome.user.data_model.User> userSubscriber = new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final User user) {
                if(userIsAuthenticated()) {
                    messageSubscription = conversationListService.getLastMessageFor(self, user)
                            .subscribe(new Action1<Message>() {
                                @Override
                                public void call(final Message message) {
                                    unreadSubscription = conversationListService.getUnreadCount(self.getUid(), user.getUid())
                                            .subscribe(new Subscriber<Integer>() {
                                                @Override
                                                public void onCompleted() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }

                                                @Override
                                                public void onNext(Integer value) {
                                                    unreadCount = value;
                                                    conversationListDisplayer.addToDisplay(
                                                            new Conversation(user.getUid(), user.getName(), user.getImage(), message.getMessage(), message.getTimestamp(), unreadCount));
                                                }
                                            });

                                }
                            });
                }
            }

        };
        Subscriber<List<String>> usersSubscriber = new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> strings) {
                uids = new ArrayList<>(strings);
                for (String uid: uids) {
                    userSubscription = userService.getUser(uid)
                            .subscribe(userSubscriber);
                }
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
                .flatMap(conversationsForUser())
                .subscribe(usersSubscriber);
    }

    public void stopPresenting() {
        conversationListDisplayer.detach(conversationInteractionListener);
        loginSubscription.unsubscribe();
        if (userSubscription != null)
            userSubscription.unsubscribe();
        if (messageSubscription != null)
            messageSubscription.unsubscribe();
        if(unreadSubscription != null)
            unreadSubscription.unsubscribe();
    }
    private boolean userIsAuthenticated() {
        return self != null;
    }

    private Func1<Authentication, Observable<List<String>>> conversationsForUser() {
        return new Func1<Authentication, Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call(Authentication authentication) {
                return conversationListService.getConversationsFor(self);
            }
        };
    }

    private Func1<Authentication, Boolean> successfullyAuthenticated() {
        return new Func1<Authentication, Boolean>() {
            @Override
            public Boolean call(Authentication authentication) {
                return authentication.isSuccess();
            }
        };
    }

    private final ConversationListDisplayer.ConversationInteractionListener conversationInteractionListener = new ConversationListDisplayer.ConversationInteractionListener() {

        @Override
        public void onConversationSelected(final Conversation conversation) {
            Bundle bundle = new Bundle();
            bundle.putString(SENDER, self.getUid());
            bundle.putString(DESTINATION,conversation.getUid());
            conversationListService.setUnreadCount(self.getUid(),conversation.getUid(),0);
            navigator.toSelectedConversation(bundle);
        }

    };

}
