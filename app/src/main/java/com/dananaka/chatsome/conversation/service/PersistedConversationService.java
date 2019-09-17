package com.dananaka.chatsome.conversation.service;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.dananaka.chatsome.conversation.data_model.Chat;
import com.dananaka.chatsome.conversation.data_model.Message;
import com.dananaka.chatsome.conversation.database.ConversationDatabase;
import com.dananaka.chatsome.database.DatabaseResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Vicknesh on 29/12/16.
 */

public class PersistedConversationService implements ConversationService {

    private final ConversationDatabase conversationDatabase;

    public PersistedConversationService(ConversationDatabase conversationDatabase) {
        this.conversationDatabase = conversationDatabase;
    }

    @Override
    public Observable<Message> syncMessages(String self, String destination) {
        return conversationDatabase.observeAddMessage(self,destination);
    }

    @Override
    public Observable<DatabaseResult<Chat>> getChat(String self, String destination) {
        return conversationDatabase.observeChat(self,destination)
                .map(asDatabaseResult())
                .onErrorReturn(DatabaseResult.<Chat>errorAsDatabaseResult());
    }

    private static Func1<Chat, DatabaseResult<Chat>> asDatabaseResult() {
        return new Func1<Chat, DatabaseResult<Chat>>() {
            @Override
            public DatabaseResult<Chat> call(Chat chat) {
                return new DatabaseResult<>(chat);
            }
        };
    }

    @Override
    public void sendMessage(String user, Message message) {
        conversationDatabase.sendMessage(user, message);
    }

    @Override
    public Observable<Boolean> getTyping(String self, String destination) {
        return conversationDatabase.observeTyping(self,destination);
    }

    @Override
    public void setTyping(String self, String destination, Boolean value) {
        conversationDatabase.setTyping(self,destination,value);
    }

    @Override
    public Observable<Integer> getUnreadCount(String self, String destination) {
        return conversationDatabase.observeUnreadCount(self,destination);
    }

    @Override
    public void setUnreadCount(String self, String destination, int value) {
        conversationDatabase.setUnreadCount(self,destination,value);
    }

    @Override
    public void setMessageEnabled(String self, String destination, Boolean value) {
        conversationDatabase.setMessageEnabled(self,destination,value);
    }
}
