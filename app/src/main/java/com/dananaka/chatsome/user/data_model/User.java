package com.dananaka.chatsome.user.data_model;

import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;

/**
 * Created by Vicknesh on 27/12/16.
 */

public class User implements Parcelable {

    private String uid;
    private String name;
    private String status;
    private String placeName;
    private String placeLat;
    private String placeLong;
    private String image;
    private String email;
    private long lastSeen;
    private String fcm;

    @SuppressWarnings("unused") //Used by Firebase
    public User() {
    }

    public User(String id, String name, String status, String placeName, String placeLat, String placeLong, String image, String email) {
        this.uid = id;
        this.name = name;
        this.status = status;
        this.placeName = placeName;
        this.placeLat = placeLat;
        this.placeLong = placeLong;
        this.image = image;
        this.email = email;
    }

    public User(Parcel in){
        this.uid = in.readString();
        this.name = in.readString();
        this.status = in.readString();
        this.placeName = in.readString();
        this.placeLat = in.readString();
        this.placeLong = in.readString();
        this.image = in.readString();
        this.email = in.readString();
        this.lastSeen = in.readLong();
        this.fcm = in.readString();
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceLat() {
        return placeLat;
    }

    public String getPlaceLong() {
        return placeLong;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public String getFcm() {
        return fcm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (uid != null ? !uid.equals(user.uid) : user.uid != null) {
            return false;
        }
        if (name != null ? !name.equals(user.name) : user.name != null) {
            return false;
        }
        if (status != null ? !status.equals(user.status) : user.status != null) {
            return false;
        }
        return image != null ? image.equals(user.image) : user.image == null;

    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (placeName != null ? placeName.hashCode() : 0);
        result = 31 * result + (placeLat != null ? placeLat.hashCode() : 0);
        result = 31 * result + (placeLong != null ? placeLong.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
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
            parcel.writeString(this.status);
            parcel.writeString(this.placeName);
            parcel.writeString(this.placeLat);
            parcel.writeString(this.placeLong);
            parcel.writeString(this.image);
            parcel.writeString(this.email);
            parcel.writeLong(this.lastSeen);
            parcel.writeString(this.fcm);
        } catch (ParcelFormatException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<? extends Object> CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
