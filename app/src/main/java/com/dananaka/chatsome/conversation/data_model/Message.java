package com.dananaka.chatsome.conversation.data_model;

import com.dananaka.chatsome.Utils;

/**
 * Created by Vicknesh on 08/02/17.
 */

public class Message {

    private String sender;
    private String destination;
    private String message;
    private String timestamp;

    public Message() {}

    public Message(String sender, String destination, String message) {
        this.sender = sender;
        this.destination = destination;
        this.message = message;
        this.timestamp = Utils.getCurrentTimestamp();
    }

    public String getSender() {
        return sender;
    }

    public String getDestination() {
        return destination;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Message message = (Message) o;

        return this.message != null && this.message.equals(message.message)
                && timestamp != null && timestamp.equals(message.timestamp);
    }
}
