package com.dananaka.chatsome.conversation_list.data_model;

import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;

/**
 * Created by Vicknesh on 04/01/17.
 */

public class Conversation implements Parcelable {

    private String uid;
    private String name;
    private String image;
    private String message;
    private String time;
    private Integer unread;

    public Conversation() {}

    public Conversation(String uid, String name, String image, String message, String time, Integer unread) {
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.message = message;
        this.time = time;
        this.unread = unread;
    }

    public Conversation(Parcel in){
        this.uid = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.message = in.readString();
        this.time = in.readString();
        this.unread = in.readInt();
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }

    public Integer getUnread() {
        return unread;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Conversation conversation = (Conversation) o;

        return uid != null && uid.equals(conversation.uid);
    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try {
            parcel.writeString(this.uid);
            parcel.writeString(this.name);
            parcel.writeString(this.image);
            parcel.writeString(this.message);
            parcel.writeString(this.time);
            parcel.writeInt(this.unread);
        } catch (ParcelFormatException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<? extends Object> CREATOR = new Parcelable.Creator() {
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };
}