package Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import Adapter.DoneTaskAdapter;
import Adapter.PendingTaskAdapter;
import Db.DatabaseHandler;
import Models.PendingPojo;
import workspace.mobile.android.task.R;

/**
 * Created by mraokorni on 11/6/2016.
 */
public class PendingFragment extends Fragment {
    Context context;
    View v;
    RecyclerView recyclerView;
    List<PendingPojo> pendingList = new ArrayList<>();
    Gson gson;
    DatabaseHandler db;
    PendingTaskAdapter pendingTaskAdapter;
    static SwipeRefreshLayout swipeRefreshLayout;
    DoneTaskAdapter doneTaskAdapter;

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
        db = new DatabaseHandler(getActivity());
        pendingTaskAdapter = new PendingTaskAdapter(db.getAllPendingPojos(), getActivity());
        recyclerView.setAdapter(pendingTaskAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Constants.showSnack(v, "Refreshed", "");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });


        return v;
    }

    public static void stopRefresh(){
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_todo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_todo:
                showAddTodoPopup();
                break;
            default:
                break;
        }
        return true;
    }

    private void showAddTodoPopup() {
        final Dialog m_dialog = new Dialog(context);
        m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_dialog.setContentView(R.layout.add_todo_popup);
        m_dialog.getWindow().getAttributes().windowAnimations = R.style.popup_login_dialog_animation;

        TextView createTxt = (TextView) m_dialog.findViewById(R.id.create_txt);
        final EditText createEdit = (EditText) m_dialog.findViewById(R.id.todo_name);

        createTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!createEdit.getText().toString().equalsIgnoreCase("")) {
                    PendingPojo pojo = new PendingPojo();
                    pojo.setName(createEdit.getText().toString());
                    pojo.setState(0);
                    db.addPendingPojo(pojo);
                    pendingTaskAdapter.getUpdateList(db.getAllPendingPojos());
                    m_dialog.dismiss();
                } else {
                    Constants.showSnack(v,
                            "Enter todo name!",
                            "");
                }
            }

        });
        m_dialog.show();
    }
}
