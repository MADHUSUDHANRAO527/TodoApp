package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import Adapter.DoneTaskAdapter;
import Db.DatabaseHandler;
import Models.PendingPojo;
import workspace.mobile.android.task.R;

/**
 * Created by mraokorni on 11/6/2016.
 */
public class DoneFragment extends Fragment {
    Context context;
    View v;
    RecyclerView recyclerView;
    List<PendingPojo> pendingList = new ArrayList<>();
    Gson gson;
    DatabaseHandler db;
    DoneTaskAdapter adapter;
    static SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(
                R.layout.pending_layout, container, false);
        context = getActivity();
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        gson = new Gson();
        //APIHelper.callApi(v, getActivity());
        //callPendingList();
        db = new DatabaseHandler(getActivity());
        Log.d("DONE ADAPTER SIZE = ", db.getAllDonePojos().size()+"");
        adapter = new DoneTaskAdapter(db.getAllDonePojos(), getActivity());
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Constants.showSnack(v, "Refreshed", "");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }
        });

        return v;
    }
    public static void stopRefresh(){
        swipeRefreshLayout.setRefreshing(false);
    }
}