package Fragments;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by mraokorni on 11/6/2016.
 */
public class Constants {
    public static String BASE_URL = "https://dl.dropboxusercontent.com/u/6890301/tasks.json";

    public static void showSnack(View view, String stringMsg, String ok) {
        Snackbar snackbar = Snackbar
                .make(view, stringMsg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
