package com.dananaka.chatsome.conversation.view;


import androidx.recyclerview.widget.RecyclerView;

import com.dananaka.chatsome.conversation.data_model.Message;

/**
 * Created by Vicknesh on 29/12/16.
 */

class ConversationMessageViewHolder extends RecyclerView.ViewHolder {

    private final ConversationMessageView conversationMessageView;

    public ConversationMessageViewHolder(ConversationMessageView messageView) {
        super(messageView);
        this.conversationMessageView = messageView;
    }

    public void bind(Message message) {
        conversationMessageView.display(message);
    }
}
