package com.hben.needo.comments.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ben on 09/02/2017.
 */

public class Comment implements Parcelable {

    private String userName;
    private String comment;


    public Comment() {
    }

    public Comment(String userName, String comment) {
        this.userName = userName;
        this.comment = comment;
    }

    protected Comment(Parcel in) {
        userName = in.readString();
        comment = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(comment);
    }
}
