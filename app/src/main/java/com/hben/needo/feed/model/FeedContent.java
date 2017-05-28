package com.hben.needo.feed.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hben.needo.comments.model.Comment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ben on 23/01/2017.
 */

public class FeedContent implements Parcelable{

   private String title;
   private String content;
   private List<Comment> comments;
   private String userProfilePicture;

    public FeedContent() {
    }

    public FeedContent(String title, String content, String userProfilePicture, List<Comment> comments) {
        this.title = title;
        this.content = content;
        this.userProfilePicture = userProfilePicture;
        this.comments = comments;
    }

    public FeedContent(String title, String content, String userProfilePicture) {
        this.title = title;
        this.content = content;
        this.userProfilePicture = userProfilePicture;
    }

    protected FeedContent(Parcel in) {
        title = in.readString();
        content = in.readString();
        userProfilePicture = in.readString();
        convertParcelableArrayToComments(in.readParcelableArray(ClassLoader.getSystemClassLoader()));
    }

    public static final Creator<FeedContent> CREATOR = new Creator<FeedContent>() {
        @Override
        public FeedContent createFromParcel(Parcel in) {
            return new FeedContent(in);
        }

        @Override
        public FeedContent[] newArray(int size) {
            return new FeedContent[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String mContent) {
        this.content = mContent;
    }

    public String getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(String mUserProfilePicture) {
        this.userProfilePicture = mUserProfilePicture;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(userProfilePicture);
        dest.writeParcelableArray(convertCommentsToParcelableArray(), 0);
    }

    private Parcelable[] convertCommentsToParcelableArray() {

        Parcelable[] result = new Parcelable[comments.size()];
        for (int i = 0; i < comments.size(); i++) {
            result[i] = comments.get(i);
        }
        return result;
    }

    private void convertParcelableArrayToComments(Parcelable[] parcelables) {

        comments = new ArrayList<>(parcelables.length);
        for (Parcelable parcelable : parcelables) {
            comments.add((Comment) parcelable);
        }
    }
}
