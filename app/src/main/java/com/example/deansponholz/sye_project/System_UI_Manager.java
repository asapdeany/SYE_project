package com.example.deansponholz.sye_project;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import static android.R.attr.uiOptions;

/**
 * Created by deansponholz on 2/22/17.
 */

public class System_UI_Manager {


    View decorView;
    Integer uiOptions;
    Activity callingActivity;
    public System_UI_Manager(Activity activity){
        callingActivity = activity;

    }

    public void hideView(){

        decorView = callingActivity.getWindow().getDecorView();
        uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                // Note that system bars will only be "visible" if none of the
                // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // TODO: The system bars are visible. Make any desired
                    // adjustments to your UI, such as showing the action bar or
                    // other navigational controls.

                    //decorView.setSystemUiVisibility(uiOptions);

                } else {
                    // TODO: The system bars are NOT visible. Make any desired
                    // adjustments to your UI, such as hiding the action bar or
                    // other navigational controls.

                }
            }
        });
    }
}
