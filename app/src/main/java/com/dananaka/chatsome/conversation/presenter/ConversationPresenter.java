package com.dananaka.chatsome.conversation.presenter;

import android.util.Log;

import com.dananaka.chatsome.conversation.data_model.Message;
import com.dananaka.chatsome.conversation.service.ConversationService;
import com.dananaka.chatsome.conversation.view.ConversationDisplayer;
import com.dananaka.chatsome.login.service.LoginService;
import com.dananaka.chatsome.navigation.Navigator;
import com.dananaka.chatsome.user.data_model.User;
import com.dananaka.chatsome.user.service.UserService;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Vicknesh on 29/12/16.
 */

public class ConversationPresenter {

    private final LoginService loginService;
    private final ConversationService conversationService;
    private final ConversationDisplayer conversationDisplayer;
    private final UserService userService;
    private final String self;
    private final String destination;
    private final Navigator navigator;

    private Subscription subscription;
    private Subscription chatSubscription;
    private Subscription typingSubscription;
    private Subscription unreadSubscription;

    private Integer unreadCount = 0;

    public ConversationPresenter(
            LoginService loginService,
            ConversationService conversationService,
            ConversationDisplayer conversationDisplayer,
            UserService userService,
            String self,
            String destination,
            Navigator navigator
    ) {
        this.loginService = loginService;
        this.conversationService = conversationService;
        this.conversationDisplayer = conversationDisplayer;
        this.userService = userService;
        this.self = self;
        this.destination = destination;
        this.navigator = navigator;
    }

    public void startPresenting() {
        conversationDisplayer.attach(actionListener);
        conversationDisplayer.disableInteraction();
        Subscriber<com.dananaka.chatsome.user.data_model.User> conversationSubscriber = new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final User user) {
                conversationDisplayer.setupToolbar(user.getName(),user.getImage(),user.getLastSeen());
                conversationService.setMessageEnabled(self,destination,false);
                if(userIsAuthenticated()) {
                    chatSubscription = conversationService.syncMessages(self,destination)
                        .subscribe(new Subscriber<Message>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Message message) {
                                    conversationDisplayer.addToDisplay(message, self);
                                    unreadSubscription = conversationService.getUnreadCount(self, destination)
                                            .subscribe(new Subscriber<Integer>() {
                                                @Override
                                                public void onCompleted() {

                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    unreadCount = 0;
                                                }

                                                @Override
                                                public void onNext(Integer value) {
                                                    unreadCount = value;
                                                }
                                            });
                                }
                        });
                }
            }
        };

        subscription = userService.getUser(destination)
                .subscribe(conversationSubscriber);

        typingSubscription = conversationService.getTyping(self,destination)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        conversationService.setTyping(self,destination,false);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean)
                            conversationDisplayer.showTyping();
                        else
                            conversationDisplayer.hideTyping();
                    }
                });
    }


    public void stopPresenting() {
        conversationDisplayer.detach(actionListener);
        if(unreadCount != null) {
            conversationService.setUnreadCount(destination,self,0);
        }
        conversationService.setMessageEnabled(self,destination,true);
        conversationService.setTyping(self,destination,false);
        subscription.unsubscribe();
        if (typingSubscription != null)
            typingSubscription.unsubscribe();
        if (chatSubscription != null)
            chatSubscription.unsubscribe();
        if(unreadSubscription !=null){
            unreadSubscription.unsubscribe();
        }
    }

    private boolean userIsAuthenticated() {
        return self != null;
    }

    private final ConversationDisplayer.ConversationActionListener actionListener = new ConversationDisplayer.ConversationActionListener() {

        @Override
        public void onUpPressed() {
            navigator.toParent();
        }

        @Override
        public void onMessageLengthChanged(int messageLength) {
            if (userIsAuthenticated() && messageLength > 0) {
                conversationDisplayer.enableInteraction();
                conversationService.setTyping(self,destination,true);
            } else {
                conversationDisplayer.disableInteraction();
                conversationService.setTyping(self,destination,false);
            }
        }

        @Override
        public void onSubmitMessage(final String message) {
            if(userIsAuthenticated()) {
            conversationService.sendMessage(self, new Message(self, destination, message));
                if(unreadCount != null){
                    conversationService.setUnreadCount(self, destination, unreadCount+1);
                }else{
                    conversationService.setUnreadCount(self, destination, 1);
                }
            }



        }

    };

}
