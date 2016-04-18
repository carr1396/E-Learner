package edu.craw.e_learner;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import edu.craw.e_learner.app.LearnerApplication;
import edu.craw.e_learner.dao.History;
import edu.craw.e_learner.gui.Toaster;
import edu.craw.e_learner.main.LoginFragment;
import edu.craw.e_learner.main.MainFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.IUserLoginListener, MainFragment.OnListFragmentInteractionListener{
    private static final String TAG = "MAINACTIVITYLOG";
    private static final String TAG_LOGIN_FRAGMENT ="MAINLOGINFRAGMENT";
    private static final String TAG_MAIN_FRAGMENT = "MAINMAINFRAGMENT";
    private LearnerApplication learnerApp;
    private SharedPreferences sharedPref;
    private FragmentManager manager;
    private String username;
    private String token;
    private LoginFragment loginFragment;
    private MainFragment mainFragment;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private int columnCount = 2;

    //https://www.versioneye.com/java to see lib info
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        learnerApp = (LearnerApplication) getApplicationContext();
        Typeface tf = learnerApp.getTf();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        sharedPref =  learnerApp.getSettings();
        username = sharedPref.getString("username", "");
        token = sharedPref.getString("_token", "");
        toolbar.setTitle("");

        View v = null;
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            v = toolbar.getChildAt(i);
            if (v != null) {
                if (v.getId() == R.id.tv_title && tf != null) {
                    ((TextView) v).setTypeface(tf);
                }
            }
        }
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (learnerApp.isApplicationIsAuthenticated()){
                    MainActivity.this.startActivity(new Intent(MainActivity.this, CourseListActivity.class));
                }else{
                    Snackbar.make(view, "You Are Not Logged In, Do So To See Your Courses", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        manager = getSupportFragmentManager();
        if(savedInstanceState == null)//http://code.hootsuite.com/orientation-changes-on-android/
        {
            // The Activity is NOT being re-created so we can instantiate a new Fragment
            // and add it to the Activity
            loginFragment = LoginFragment.newInstance(username, token);

            mainFragment = MainFragment.newInstance(columnCount);

            manager.beginTransaction()
                    // It's almost always a good idea to use .replace instead of .add so that
                    // you never accidentally layer multiple Fragments on top of each other
                    // unless of course that's your intention
                    .replace(R.id.layout_main_container, loginFragment, TAG_LOGIN_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        }else{
            loginFragment =  (LoginFragment) manager.findFragmentByTag(TAG_LOGIN_FRAGMENT);
            mainFragment = (MainFragment) manager.findFragmentByTag(TAG_MAIN_FRAGMENT);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.craw.e_learner/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.craw.e_learner/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    public void onUserLoggedIn(boolean isLoggedIn ) {
        username = sharedPref.getString("username", "");
        token = sharedPref.getString("_token", "");
        learnerApp.setAutheticationToken(token);
//        Log.d(TAG, token);
        Toaster.makeText(MainActivity.this, "You Are Logged In as, "+username, Toast.LENGTH_LONG).show();
        if (mainFragment==null)
        {
            mainFragment  = (MainFragment) manager.findFragmentByTag(TAG_MAIN_FRAGMENT);
            if(mainFragment==null)
            {
                mainFragment = MainFragment.newInstance(columnCount);
            }
        }
        manager.beginTransaction()
                // It's almost always a good idea to use .replace instead of .add so that
                // you never accidentally layer multiple Fragments on top of each other
                // unless of course that's your intention
                .replace(R.id.layout_main_container, mainFragment, TAG_MAIN_FRAGMENT)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onListFragmentInteraction(History item) {
        Toaster.makeText(MainActivity.this, "Item Clicked, ", Toast.LENGTH_LONG).show();
    }
}
