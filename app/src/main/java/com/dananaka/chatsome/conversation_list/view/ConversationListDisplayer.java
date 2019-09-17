package com.dananaka.chatsome.conversation_list.view;

import com.dananaka.chatsome.conversation_list.data_model.Conversation;
import com.dananaka.chatsome.conversation_list.data_model.Conversations;

/**
 * Created by Vicknesh on 29/12/16.
 */

public interface ConversationListDisplayer {

    void display(Conversations conversations);

    void addToDisplay(Conversation conversation);

    void attach(ConversationInteractionListener conversationInteractionListener);

    void detach(ConversationInteractionListener conversationInteractionListener);

    interface ConversationInteractionListener {

        void onConversationSelected(Conversation conversation);

    }

}
