package workspace.mobile.android.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Db.DatabaseHandler;
import Fragments.Constants;
import Fragments.DoneFragment;
import Fragments.PendingFragment;
import Models.PendingPojo;
import application.MyApplication;
import workspace.mobile.android.task.R;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    DatabaseHandler db;
    private Context mContext;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        callPendingList();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PendingFragment.stopRefresh();
                DoneFragment.stopRefresh();
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PendingFragment(), "Pending");
        adapter.addFragment(new DoneFragment(), "Done");
        viewPager.setAdapter(adapter);
    }

    public void callPendingList() {
        final Gson gson = new Gson();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.BASE_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    db = new DatabaseHandler(mContext);

                    List<PendingPojo> dataList = gson.fromJson(response.getJSONArray("data").toString(), new TypeToken<List<PendingPojo>>() {
                    }.getType());
                    Log.d("Response = ", dataList.size() + " " + response.toString());
                    List<PendingPojo> doneList = dataList;
                    if (dataList.size() > 3) {

                        List<PendingPojo> pendingPojos = db.getAllPendingPojos();
                        List<PendingPojo> donePojos = db.getAllDonePojos();
                        for (int i = 0; i < dataList.size(); i++) {
                            if (dataList.get(i).getState() == 0) {
                                if (!pendingPojos.get(i).getName().equalsIgnoreCase(dataList.get(i).getName())) {
                                    db.addPendingPojo(dataList.get(i));
                                }
                            } else {
                                if (!donePojos.get(i).getName().equalsIgnoreCase(doneList.get(i).getName())) {
                                    db.addDonePojo(doneList.get(i));
                                }
                            }
                        }
                    } else {
                        List<PendingPojo> existingPPojos = db.getAllPendingPojos();
                        List<PendingPojo> existingDPojos = db.getAllDonePojos();
                        if (existingPPojos.size() == 0) {
                            for (int i = 0; i < dataList.size(); i++) {
                                if (dataList.get(i).getState() == 0) {
                                    db.addPendingPojo(dataList.get(i));
                                }
                            }
                        }
                        if (existingDPojos.size() == 0) {
                            for (int i = 0; i < doneList.size(); i++) {
                                if (doneList.get(i).getState() == 1) {
                                    db.addDonePojo(doneList.get(i));
                                }
                            }
                        }
                    }
                    setupViewPager(viewPager);
                    tabLayout = (TabLayout) findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(viewPager);
                    tabLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error = ", error.toString());
                Constants.showSnack(viewPager,
                        "Oops! Something went wrong. Please check internet connection!",
                        "OK");
                setupViewPager(viewPager);
                tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
        MyApplication.queue.add(jsonObjectRequest);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}