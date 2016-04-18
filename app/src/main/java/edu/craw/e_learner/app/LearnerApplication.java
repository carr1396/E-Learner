package edu.craw.e_learner.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

import edu.craw.e_learner.dao.History;
import edu.craw.e_learner.data.LeanerDBSQLiteHelper;

/**
 * Created by farid on 3/14/2016.
 */
public class LearnerApplication extends Application {
    private static final String TAG = "ELEARNERAPPLICATIONLOG";
    //https://www.mobomo.com/2011/05/how-to-use-application-object-of-android/

    private static LearnerApplication singleton;
    private RequestQueue queue;
    private Typeface tf;
    private SharedPreferences settings;
    private static final String FONT_PATH="fonts/anonymous_pro.ttf";

    private String base_api_endpoint="/api/v1";

    private String autheticationToken="";

    public String getBase_server_url() {
        return base_server_url;
    }

    public void setBase_server_url(String base_server_url) {
        this.base_server_url = base_server_url;
    }

    private String base_server_url = "http://192.168.10.100:8080";

    public LeanerDBSQLiteHelper getDbHelper() {
        return dbHelper;
    }

    private void setDbHelper(LeanerDBSQLiteHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    private LeanerDBSQLiteHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        setSettings();
        LearnerApplication.this.tf = Typeface.createFromAsset(getApplicationContext().getAssets(), FONT_PATH);
        LearnerApplication.this.setQueue(Volley.newRequestQueue(this));;
        LearnerApplication.this.setDbHelper(new LeanerDBSQLiteHelper(LearnerApplication.this));
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public LearnerApplication getInstance()
    {
        return singleton;
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public void addRequestToQueue(Request req)
    {
        queue.add(req);
    }

    private void setQueue(RequestQueue queue) {
        this.queue = queue;
    }

    public Typeface getTf() {
        return tf;
    }

    public void setTf(String fontPath) {
        this.tf = Typeface.createFromAsset(getApplicationContext().getAssets(), fontPath);
    }

    public String getBase_api_endpoint() {
        return base_api_endpoint;
    }

    public void setBase_api_endpoint(String base_api_endpoint) {
        this.base_api_endpoint = base_api_endpoint;
    }

    public String getAutheticationToken() {
        return autheticationToken;
    }

    public void setAutheticationToken(String autheticationToken) {
        this.autheticationToken = autheticationToken;
    }

    public SharedPreferences getSettings() {
        return settings;
    }

    private void setSettings() {
        this.settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public boolean isApplicationIsAuthenticated() {
        return !settings.getString("_token", "").trim().isEmpty();
    }
}




