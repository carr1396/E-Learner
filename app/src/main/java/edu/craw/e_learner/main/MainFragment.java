package edu.craw.e_learner.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import edu.craw.e_learner.R;
import edu.craw.e_learner.app.LearnerApplication;
import edu.craw.e_learner.dao.History;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MainFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private LearnerApplication learnerApp;
    private ViewSwitcher vs;
    private Animation slide_in_left, slide_out_right;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MainFragment newInstance(int columnCount) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        learnerApp = (LearnerApplication) getActivity().getApplicationContext();
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);
        vs = (ViewSwitcher) view.findViewById(R.id.list_view_switcher);
        slide_in_left = AnimationUtils.loadAnimation(MainFragment.this.getActivity(),
                android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(MainFragment.this.getActivity(),
                android.R.anim.slide_out_right);

        vs.setInAnimation(slide_in_left);
        vs.setOutAnimation(slide_out_right);
        ArrayList<History> ITEMS = new ArrayList<>();
        if (learnerApp!=null)
        {
            ITEMS=new ArrayList<>(learnerApp.getDbHelper().getHistory());
        }
        if(ITEMS.isEmpty())
        {
            if(vs.getCurrentView().getId() == R.id.list)
            {
                vs.showNext();
            }
        }else{
            if(vs.getCurrentView().getId() != R.id.list)
            {
                vs.showNext();
            }
            if (view.findViewById(R.id.list)!=null) {

                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                }
                recyclerView.setAdapter(new MyHistoryRecyclerViewAdapter(ITEMS, mListener));
            }
        }
        // Set the adapter

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(History item);
    }
}
