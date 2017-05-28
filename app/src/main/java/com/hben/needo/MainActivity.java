package com.hben.needo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.hben.needo.feed.control.FeedClickListener;
import com.hben.needo.feed.control.FeedContentAdapter;
import com.hben.needo.feed.model.FeedContent;
import com.hben.needo.feed.view.FeedContentViewHolder;
import com.hben.needo.firebase.FireBaseAuthListener;
import com.hben.needo.firebase.FireBaseDatabaseListener;
import com.hben.needo.firebase.FireBaseManager;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

import static com.hben.needo.AppConstants.NEW_POST_REQUEST_CODE;
import static com.hben.needo.AppConstants.SIGN_IN_REQUEST_CODE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FireBaseDatabaseListener, FireBaseAuthListener {

    //Ui
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    // private FiftyShadesOf fiftyShadesOf;
    private ProgressBar progressBar;
    private DrawerLayout drawer;

    //RecyclerView
    private RecyclerView feedRecyclerView;
    private FeedContentAdapter feedAdapterRecycleView;

    //FireBase
    private FireBaseManager fireBaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fireBaseManager = FireBaseManager.newInstance();

        initViews();
        setupUi();
        setupRecyclerView();
    }

    private void initViews() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        feedRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void setupUi() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFAB();
        initializeDrawer();

    }


    private void initializeDrawer() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        View header = navigationView.getHeaderView(0);
        TextView userName = (TextView) header.findViewById(R.id.drawerUserName);
        TextView userEmail = (TextView) header.findViewById(R.id.drawerEmail);
        ImageView userProfilePic = (ImageView) header.findViewById(R.id.drawerUserProfilePic);

        userName.setText(fireBaseManager.auth.getUserName());
        userEmail.setText(fireBaseManager.auth.getUserEmail());
        Glide.with(this)
                .load(fireBaseManager.auth.getUserProfilePicUri())
                .into(userProfilePic);


    }

    private void setupFAB() {

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddNewPostActivity.class);
                startActivityForResult(intent, NEW_POST_REQUEST_CODE);
            }
        });
    }

    private void setupRecyclerView() {

        feedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        feedAdapterRecycleView = new FeedContentAdapter();
        feedRecyclerView.setAdapter(feedAdapterRecycleView);
        feedRecyclerView.setItemAnimator(new LandingAnimator());

        feedAdapterRecycleView.setFeedClickListener(new FeedClickListener() {

            @Override
            public void onPostClicked(FeedContentViewHolder feedContentViewHolder) {

                TextView title = feedContentViewHolder.titleView;
                TextView content = feedContentViewHolder.contentView;
                Intent intent = new Intent(MainActivity.this, FullPostActivity.class);

                intent.putExtra("title", title.getText().toString());
                intent.putExtra("content", content.getText().toString());

                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NEW_POST_REQUEST_CODE:
                    String title = data.getStringExtra("title");
                    String content = data.getStringExtra("content");
                    fireBaseManager.database.addNewPost(title, content);
                    feedRecyclerView.smoothScrollToPosition(0);
                    break;
                case SIGN_IN_REQUEST_CODE:
                    Alerter.create(this).setText("Hello " + fireBaseManager.auth.getUserName() + "!").setIcon(R.drawable.ic_face).setBackgroundColor(R.color.colorPrimary).show();
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    private void showSignInScreen() {
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setProviders(getIdpConfigs())
                .setTheme(R.style.AppTheme)
                .setLogo(R.mipmap.ic_launcher)
                .build();
        startActivityForResult(intent, SIGN_IN_REQUEST_CODE);

    }

    @Override
    public void postsFetched(FeedContent feedContent) {

        feedAdapterRecycleView.insert(feedContent);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            Alerter.create(this).setText("Coming Soon :)").setIcon(R.drawable.ic_alert).setBackgroundColor(R.color.colorAccent).show();
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
//            if (){
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//            }
        } else if (id == R.id.nav_profile) {
            navigationView.setCheckedItem(R.id.nav_home);
            Alerter.create(this).setText("Coming Soon :)").setIcon(R.drawable.ic_alert).setBackgroundColor(R.color.colorAccent).show();
        } else if (id == R.id.nav_logout) {
            fireBaseManager.auth.unregisterListener(this);
            fireBaseManager.auth.stop();
            AuthUI.getInstance().signOut(this);
            showSignInScreen();
        } else if (id == R.id.nav_settings) {
            navigationView.setCheckedItem(R.id.nav_home);
            Alerter.create(this).setText("Coming Soon :)").setIcon(R.drawable.ic_alert).setBackgroundColor(R.color.colorAccent).show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireBaseManager.auth.registerListener(this);
        fireBaseManager.database.registerListener(this);
        if (!fireBaseManager.auth.isUserSignedIn()) {
            showSignInScreen();
        }
        if (fireBaseManager.auth.isUserSignedIn()) {
            fireBaseManager.auth.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireBaseManager.auth.unregisterListener(this);
        fireBaseManager.database.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fireBaseManager.auth.stop();
    }

    @NonNull
    private List<AuthUI.IdpConfig> getIdpConfigs() {

        List<AuthUI.IdpConfig> idpConfigs = new ArrayList<>(3);
        idpConfigs.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());
        idpConfigs.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
        return idpConfigs;
    }


    @Override
    public void userSignedOut() {
    }

    @Override
    public void userSignedIn() {
    }
}
