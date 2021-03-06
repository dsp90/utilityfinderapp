package com.pawardushyant.foodfinderapp.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Commons {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getImageUrl(String photoReference){
        return Constants.BASE_IMAGE_URL + Constants.IMAGE_QUERY_PARAMS_1 + photoReference +
                Constants.IMAGE_QUERY_PARAMS_2 + Constants.API_KEY;
    }
}
