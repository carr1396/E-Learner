package edu.craw.e_learner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.craw.e_learner.app.LearnerApplication;
import edu.craw.e_learner.dao.Course;
import edu.craw.e_learner.gui.Toaster;

/**
 * An activity representing a list of Courses. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CourseDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CoursesAddListActivity extends AppCompatActivity {

    private static final String TAG = "CoursesAddActivityLOGS";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private LearnerApplication learnerApp;
    ArrayList<Course> ITEMS = new ArrayList<>();
    private ViewSwitcher vs;
    private Animation slide_in_left, slide_out_right;
    private String courseGetUrl = "";
    private SharedPreferences sharedPref;

    private View recyclerView;
    private SimpleItemRecyclerViewAdapter recyclerViewAdapter;
    private boolean recyclerViewhasBeenShown = false;
    private TextView tvEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        learnerApp = (LearnerApplication) CoursesAddListActivity.this.getApplicationContext();

        courseGetUrl = learnerApp.getBase_server_url() + learnerApp.getBase_api_endpoint() + "/courses/mine";

        sharedPref = learnerApp.getSettings();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        recyclerView = findViewById(R.id.course_list);
        tvEmptyList = (TextView) findViewById(R.id.text_empty);

        if (learnerApp != null) {

            ITEMS = new ArrayList<>(learnerApp.getDbHelper().getCourses());
        }

        tvEmptyList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getMyCoursesFromNetwork();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_view_courses);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        vs = (ViewSwitcher) findViewById(R.id.list_view_switcher);
        slide_in_left = AnimationUtils.loadAnimation(CoursesAddListActivity.this,
                android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(CoursesAddListActivity.this,
                android.R.anim.slide_out_right);

        vs.setInAnimation(slide_in_left);
        vs.setOutAnimation(slide_out_right);

        if (ITEMS.isEmpty()) {
            if (vs.getCurrentView().getId() == R.id.course_list) {
                vs.showNext();
            }
            getMyCoursesFromNetwork();
        } else {
            showListView();

        }


        if (findViewById(R.id.course_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void showListView() {
        if (vs.getCurrentView().getId() != R.id.course_list) {
            vs.showNext();
        }
        assert recyclerView != null;

        if(!recyclerViewhasBeenShown){
            if(recyclerView!=null){
                setupRecyclerView((RecyclerView) recyclerView);
                recyclerViewhasBeenShown = true;
            }
        }
    }

    private void getMyCoursesFromNetwork() {

        JsonArrayRequest coursesRequest = new JsonArrayRequest

                (Request.Method.GET, courseGetUrl, null, new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        ITEMS.clear();
                        int responseLength = response.length();
                        Toast.makeText(CoursesAddListActivity.this, "Network Course Fetch Successful. You Have " + responseLength+ " Course(s)", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject courseJSON = response.getJSONObject(i);
                                JSONObject addedJSON = courseJSON.getJSONObject("added");
                                JSONObject schoolJSON = courseJSON.getJSONObject("school");
                                Course c =  new Course(
                                        courseJSON.getLong("id"),
                                        courseJSON.getString("name"),
                                        courseJSON.getString("description"),
                                        courseJSON.getString("abbrev"),
                                        courseJSON.getString("code"),
                                        Timestamp.valueOf(courseJSON.getString("updated_at")),
                                        Timestamp.valueOf(courseJSON.getString("created_at")),
                                        courseJSON.getLong("added_id"),
                                        courseJSON.getLong("school_id"),
                                        (addedJSON!=null?addedJSON.getString("username"): null),
                                        (schoolJSON!=null?schoolJSON.getString("name"):null)
                                );
                                try{
                                    long entryId = learnerApp.getDbHelper().createCourseEntry(c);
                                    c.setId(entryId);
                                    ITEMS.add(c);
                                }catch (SQLiteException e){
                                    Log.d(TAG, e.getMessage());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (vs.getCurrentView().getId() == R.id.course_list){
                            showListView();
                        }
                        if (recyclerView!=null && recyclerViewAdapter!=null){
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String responseBody = null;
                        Log.d(TAG, error.toString());
                        try {
                            if (error != null && error.networkResponse != null) {
                                responseBody = new String(error.networkResponse.data, "utf-8");

                                if (responseBody != null) {
                                    JSONObject jsonObject = new JSONObject(responseBody);
                                    Toaster.makeText(CoursesAddListActivity.this, responseBody, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(CoursesAddListActivity.this, "Something Went Wrong While Parsing Network Data, Try Again Later", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(CoursesAddListActivity.this, "Server Connection Error. Check Internet Connection Or Try Again Later", Toast.LENGTH_LONG).show();

                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(CoursesAddListActivity.this, "Error!! Something Went Wrong", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CoursesAddListActivity.this, "Error!! Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //https://gist.github.com/jchernandez/5bec1913af80e2923da8
                HashMap<String, String> params = new HashMap<String, String>();
                String auth = "Bearer " + "";
                if (sharedPref!=null) {
                    auth +=sharedPref.getString("_token", "");
//                    Log.d(TAG, auth);

                }
                params.put("authorization", auth);
                return params;
            }
        };
        learnerApp.addRequestToQueue(coursesRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerViewAdapter = new SimpleItemRecyclerViewAdapter(ITEMS);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Course> mValues;

        public SimpleItemRecyclerViewAdapter(List<Course> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.course_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(Integer.parseInt(mValues.get(position).getId() + ""));
            holder.mContentView.setText(mValues.get(position).getDescription());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CourseDetailFragment.ARG_ITEM_ID, holder.mItem.getId() + "");
                        CourseDetailFragment fragment = new CourseDetailFragment();                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.course_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CourseAddDetailActivity.class);
                        intent.putExtra(CourseDetailFragment.ARG_ITEM_ID, (int) (holder.mItem.getId()));

                        context.startActivity(intent);
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Course mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
