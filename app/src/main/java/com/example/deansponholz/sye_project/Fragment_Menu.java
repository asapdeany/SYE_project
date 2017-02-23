package com.example.deansponholz.sye_project;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by deansponholz on 1/20/17.
 */

public class Fragment_Menu extends Fragment {

    System_UI_Manager system_ui_manager;
    Constants_Display constants_display;
    Button button_play;
    RelativeLayout fragment_menu;

    Bitmap testBitmap;
    ImageView testImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        //hide status bar
        system_ui_manager = new System_UI_Manager(getActivity());

        constants_display = new Constants_Display(root.getContext());

        //Initialization
        button_play = (Button) root.findViewById(R.id.button_play);
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Calibrate.class);
                //Intent intent = new Intent(getActivity(), Activity_Play.class);
                getActivity().startActivity(intent);
            }
        });
        fragment_menu = (RelativeLayout) root.findViewById(R.id.fragment_menu);
        displayImages(root);

        return root;
    }

    private void displayImages(View root){


        testBitmap = constants_display.loadBitmapEfficiently(getContext(),
                getResources(),
                R.drawable.trademark,
                (int) (constants_display.width * 0.025),
                (int) (constants_display.height * 0.008));


        final float test = (float)(constants_display.height * 0.8);
        final float test1 = (float)(constants_display.width * 0.020);

        testImageView = new ImageView(root.getContext());
        testImageView.setImageBitmap(testBitmap);
        testImageView.setX(test1);
        testImageView.setY(test);
        fragment_menu.addView(testImageView);

    }

}
