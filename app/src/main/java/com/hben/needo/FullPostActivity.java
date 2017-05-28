package com.hben.needo;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hben.needo.comments.control.CommentsAdapter;
import com.hben.needo.comments.model.Comment;
import com.hben.needo.feed.model.FeedContent;
import com.hben.needo.firebase.FireBaseDatabaseListener;
import com.hben.needo.firebase.FireBaseManager;

public class FullPostActivity extends AppCompatActivity implements FireBaseDatabaseListener {

    //Ui
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    //RecyclerView
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapterRecycleView;

    //FireBase
    private FireBaseManager fireBaseManager;

    private Comment comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_post);

        fireBaseManager = FireBaseManager.newInstance();
        initViews();
        setupUI();
        setupRecyclerView();


    }

    private void initViews() {
        commentsRecyclerView = (RecyclerView) findViewById(R.id.commentRecyclerView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.full_post_toolbar_layout);
    }

    @Override
    protected void onStart() {

        super.onStart();
        fireBaseManager.database.registerListener(this);
    }

    private void setupUI() {
        setSupportActionBar(toolbar);

        TextView fullContent = (TextView) findViewById(R.id.full_post_textView);

        TextView title = new TextView(this);
        title.setText(getIntent().getStringExtra("title"));
        collapsingToolbarLayout.setTitle(title.getText().toString());

        fullContent.setText(getIntent().getStringExtra("content"));


        setupFAB();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onStop() {

        super.onStop();
        fireBaseManager.database.unregisterListener(this);
    }

    private void setupRecyclerView() {
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        commentsAdapterRecycleView = new CommentsAdapter();
        commentsRecyclerView.setAdapter(commentsAdapterRecycleView);

    }

    private void setupFAB() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void postsFetched(FeedContent feedContent) {
        commentsAdapterRecycleView.insert(comment);
        commentsRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

