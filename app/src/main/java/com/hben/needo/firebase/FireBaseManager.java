package com.hben.needo.firebase;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hben.needo.comments.model.Comment;
import com.hben.needo.feed.model.FeedContent;

import java.util.ArrayList;
import java.util.List;

import static com.hben.needo.firebase.FireBaseFeedConstants.POSTS;

/**
 * Created by Ben on 31/01/2017.
 */

public class FireBaseManager {

    private static FireBaseManager fireBaseManager;
    public Database database;
    public Auth auth;
    private FireBaseManager() {

        database = new Database();
        auth = new Auth();
    }

    public static FireBaseManager newInstance() {

        if (fireBaseManager == null)
            fireBaseManager = new FireBaseManager();
        return fireBaseManager;
    }

    ////////////////////////////////////////////////////////////////////////////

    public class Database {

        private List<FireBaseDatabaseListener> listeners;

        private FirebaseDatabase database;
        private ChildEventListener childEventListener;
        private DatabaseReference postsDatabaseReference;
        private List<Comment> comments;

        private Database() {

            listeners = new ArrayList<>();
            database = FirebaseDatabase.getInstance();
            postsDatabaseReference = database.getReference().child(POSTS);
            comments = new ArrayList<>();
        }

        public void registerListener(FireBaseDatabaseListener listener) {

            listeners.add(listener);
        }

        public void unregisterListener(FireBaseDatabaseListener listener) {

            listeners.remove(listener);
        }

        private void detachDatabaseReadListener() {

            if (childEventListener == null)
                return;
            postsDatabaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }

        private void attachDatabaseReadListener() {

            if (childEventListener != null)
                return;
            childEventListener = new PostsChildEventListener();
            postsDatabaseReference.addChildEventListener(childEventListener);
        }

        public void addNewPost(String title, String postContent) {
            comments.add(new Comment(auth.getUserName(), "1234567489"));
            final FeedContent feedContent = new FeedContent(title, postContent, auth.getUserProfilePicUri().toString(), comments);
            postsDatabaseReference.push().setValue(feedContent);
        }
        public void getCommentForPost(){

        }



        private class PostsChildEventListener implements ChildEventListener {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                FeedContent feedContent = dataSnapshot.getValue(FeedContent.class);
                for (FireBaseDatabaseListener fireBaseDatabaseListener : listeners)
                    fireBaseDatabaseListener.postsFetched(feedContent);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////


    public class Auth {

        private List<FireBaseAuthListener> listeners;
        private FirebaseAuth firebaseAuth;
        private FirebaseAuth.AuthStateListener authStateListener;

        private Auth() {

            listeners = new ArrayList<>();
            firebaseAuth = FirebaseAuth.getInstance();
            authStateListener = new AuthStateListener();
        }

        public void registerListener(FireBaseAuthListener listener) {

            listeners.add(listener);
        }

        public void unregisterListener(FireBaseAuthListener listener) {

            listeners.remove(listener);
        }

        public void start() {

            firebaseAuth.addAuthStateListener(authStateListener);
        }

        public void stop() {

            if (authStateListener != null)
                firebaseAuth.removeAuthStateListener(authStateListener);
            signUserOut();
        }

        private void signUserOut() {

            database.detachDatabaseReadListener();
            for (FireBaseAuthListener listener : listeners)
                listener.userSignedOut();

           // firebaseAuth.getInstance().signOut();

        }

        private void signUserIn(String user) {

            database.attachDatabaseReadListener();
            for (FireBaseAuthListener listener : listeners)
                listener.userSignedIn();
        }

        public String getUserName() {

            FirebaseUser user = auth.firebaseAuth.getCurrentUser();
            if (user != null) {
                return user.getDisplayName();
            }
            return "There is no user connected to the app.";
        }

        public String getUserEmail() {

            FirebaseUser user = auth.firebaseAuth.getCurrentUser();
            if (user != null) {
                return user.getEmail();
            }
            return "There is no user connected to the app.";
        }

        public Uri getUserProfilePicUri() {

            FirebaseUser user = auth.firebaseAuth.getCurrentUser();
            if (user != null) {
                return user.getPhotoUrl();
            }
            return null;
        }

        public boolean isUserSignedIn() {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                // already signed in
                return true;
            } else {
                // not signed in
                return false;
            }
        }

        private class AuthStateListener implements FirebaseAuth.AuthStateListener {


            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    auth.signUserOut();
                } else {
                    auth.signUserIn(user.getDisplayName());
                }

            }
        }

    }


}

