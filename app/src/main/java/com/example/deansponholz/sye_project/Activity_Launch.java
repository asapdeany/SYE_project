package com.example.deansponholz.sye_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by deansponholz on 2/21/17.
 */

public class Activity_Launch extends AppCompatActivity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    System_UI_Manager system_ui_manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        //hide status bar
        system_ui_manager = new System_UI_Manager(this);
        Constants_Display constants_display = new Constants_Display(getApplicationContext());


    }

    @Override
    protected void onResume() {
        super.onResume();
        system_ui_manager.hideView();
                /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(getApplicationContext(), Activity_Menu.class);
                startActivity(mainIntent);

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

