package edu.craw.e_learner.main;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import edu.craw.e_learner.R;
import edu.craw.e_learner.app.LearnerApplication;
import edu.craw.e_learner.gui.Toaster;
import edu.craw.e_learner.helpers.CustomPostRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOKEN = "_token";
    private static final String TAG = "LOGINFRAGMENTLOG";

    private String mUsername;
    private String mToken;

    private EditText et_login_username;
    private EditText et_login_password;
    private Button btn_login;
    private TextView tv_registerLink;
    private TextView tv_forgotLink;

    private LearnerApplication learnerApp;
    private SharedPreferences sharedPref;

    private Typeface tf;
    private String baseURL;
    private IUserLoginListener mListener;
    private boolean isLoggedIn = false;
    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Parameter 1.
     * @param token Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String username, String token) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }


    public interface IUserLoginListener {
        void onUserLoggedIn(boolean isLoggedIn);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        learnerApp = (LearnerApplication) getActivity().getApplicationContext();
        baseURL = learnerApp.getBase_server_url();
        sharedPref =  learnerApp.getSettings();
        tf = learnerApp.getTf();
        if (getArguments() != null) {
            mUsername = getArguments().getString(ARG_USERNAME);
            mToken = getArguments().getString(ARG_TOKEN);

        }
        if(mToken!=null)
        {
            if (!mToken.isEmpty())
            {
                isLoggedIn = true;
            }
        }
        // Retain this Fragment so that it will not be destroyed when an orientation
        // change happens and we can keep our AsyncTask running
        LoginFragment.this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {//called immediately after onCreate
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_login, container, false);
        et_login_password = (EditText) root.findViewById(R.id.et_login_password);
        et_login_username = (EditText) root.findViewById(R.id.et_login_username);
        btn_login = (Button) root.findViewById(R.id.btn_login);
        tv_registerLink = (TextView) root.findViewById(R.id.btn_go_to_register);
        tv_forgotLink = (TextView) root.findViewById(R.id.btn_go_to_forgot);

        if (tf!=null)
        {
            btn_login.setTypeface(tf);
            et_login_password.setTypeface(tf);
            et_login_username.setTypeface(tf);
        }

        btn_login.setOnClickListener(this);
        tv_forgotLink.setOnClickListener(this);
        tv_registerLink.setOnClickListener(this);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Try to use the Activity as a listener
        if (context instanceof IUserLoginListener) {
            mListener = (IUserLoginListener)context;
        } else {
            throw new IllegalStateException("Parent activity must implement IUserLoginListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) { // will be called after onCreateView
        super.onActivityCreated(savedInstanceState);
        if (mListener!=null && isLoggedIn)
        {
            mListener.onUserLoggedIn(isLoggedIn);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // This is VERY important to avoid a memory leak (because mListener is really a reference to an Activity)
        // When the orientation change occurs, onDetach will be called and since the Activity is being destroyed
        // we don't want to keep any references to it
        // When the Activity is being re-created, onAttach will be called and we will get our listener back
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_go_to_register || v.getId() == R.id.btn_go_to_register) {
            ((TextView) v).setTextColor(Color.rgb(41, 121, 255));
            String link = v.getId() == R.id.btn_go_to_register ? learnerApp.getBase_server_url()+"/#/register" : learnerApp.getBase_server_url()+"/#/forgot";
            Uri uri = Uri.parse(link);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (v.getId() == R.id.btn_login) {
            String username = et_login_username.getText().toString().trim();
            String password = et_login_password.getText().toString().trim();
            if(username.isEmpty()|| password.isEmpty())
            {
                Toaster.makeText(LoginFragment.this.getActivity(), (username.isEmpty() ? "Username Or Email Is Required" : "Password Is Required") + "", Toast.LENGTH_SHORT).show();
            }else{
                doLoginPostRequest(username, password);
            }
        }
    }
    private   void doLoginPostRequest(final String username, String password)
    {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);


        CustomPostRequest postLoginRequest = new CustomPostRequest(Request.Method.POST, learnerApp.getBase_server_url()+"/login", params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        try {
                            editor.putString("_token", response.getString("token"));
                            editor.putString("username", response.getJSONObject("user").getString("username"));
                            editor.putString("user_id", String.valueOf(response.getJSONObject("user").getInt("id")));
                            editor.commit();
                            mListener.onUserLoggedIn(true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String responseBody = null;
                        //http://stackoverflow.com/questions/18345174/volley-not-parsing-404-response foralso adding headers
                        try {
                            if (error!= null && error.networkResponse!=null){
                                responseBody = new String(error.networkResponse.data, "utf-8");

                                if (responseBody != null) {
                                    Toaster.makeText(LoginFragment.this.getActivity(), responseBody, Toast.LENGTH_LONG).show();
                                }
                                JSONObject jsonObject = new JSONObject(responseBody);
                            }else {
                                Toast.makeText(LoginFragment.this.getActivity(), "Server Connection Error. Check Internet Connection Or Try Again Later", Toast.LENGTH_LONG).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginFragment.this.getActivity(), "Error!! Something Went Wrong", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginFragment.this.getActivity(), "Error!! Something Went Wrong", Toast.LENGTH_LONG).show();
                        }

                        Toast.makeText(LoginFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

        };
        learnerApp.addRequestToQueue(postLoginRequest);
    }

}
