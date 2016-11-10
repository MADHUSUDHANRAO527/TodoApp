package application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by mraokorni on 11/6/2016.
 */
public class MyApplication extends Application {
    public static MyApplication instance;
    public static RequestQueue queue;

    public void onCreate() {
        super.onCreate();
        instance = this;
        queue = Volley.newRequestQueue(instance);
    }
}
