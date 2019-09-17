package com.dananaka.chatsome.conversation_list.service;

import com.dananaka.chatsome.conversation.data_model.Message;
import com.dananaka.chatsome.conversation.database.ConversationDatabase;
import com.dananaka.chatsome.conversation_list.database.ConversationListDatabase;
import com.dananaka.chatsome.user.data_model.User;
import com.dananaka.chatsome.user.data_model.Users;
import com.dananaka.chatsome.user.database.UserDatabase;

import java.util.List;

import rx.Observable;

/**
 * Created by Vicknesh on 29/12/16.
 */

public class PersistedConversationListService implements  ConversationListService {

    private final ConversationListDatabase conversationListDatabase;
    private final ConversationDatabase conversationDatabase;
    private final UserDatabase userDatabase;

    public PersistedConversationListService(ConversationListDatabase conversationListDatabase, ConversationDatabase conversationDatabase, UserDatabase userDatabase) {
        this.conversationListDatabase = conversationListDatabase;
        this.conversationDatabase = conversationDatabase;
        this.userDatabase = userDatabase;
    }

    @Override
    public Observable<Message> getLastMessageFor(User self, User destination) {
        return conversationDatabase.observeLastMessage(self.getUid(),destination.getUid());
    }

    @Override
    public Observable<List<String>> getConversationsFor(User user) {
        return conversationListDatabase.observeConversationsFor(user);
    }

    @Override
    public Observable<Users> getUsers(List<String> usersId) {
        return userDatabase.singleObserveUsers();
    }

    @Override
    public Observable<Integer> getUnreadCount(String self, String destination) {
        return conversationListDatabase.observeUnreadCount(self,destination);
    }

    @Override
    public void setUnreadCount(String self, String destination, int value) {
        conversationListDatabase.setUnreadCount(self,destination,value);
    }

}
