package com.example.deansponholz.sye_project;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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

    Bitmap bitmap_Logo;
    ImageView iv_Logo;

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
                getActivity().startActivity(intent);
            }
        });
        fragment_menu = (RelativeLayout) root.findViewById(R.id.fragment_menu);
        displayMenuScene(root);
        return root;
    }



    private void displayMenuScene(View root){

        /*
        button_play_test = new Button(root.getContext());
        button_play_test.setWidth(constants_display.width / 20);
        button_play_test.setHeight(constants_display.height / 20);
        button_play_test.setBackgroundResource(R.drawable.button_menu);
        button_play_test.setX((float) (constants_display.width / 2));
        button_play_test.setY((float) (constants_display.height / 2));
        button_play_test.setText("");
        button_play_test.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        fragment_menu.addView(button_play_test);
        */

        //Efficiently load drawables into imageViews for Menu
        bitmap_Logo = constants_display.loadBitmapEfficiently(root.getContext(),
                getResources(),
                R.drawable.trademark,
                (int) (constants_display.width * 0.025),
                (int) (constants_display.height * 0.008));


        final float logoX = (float)(constants_display.height * 0.8);
        final float logoY = (float)(constants_display.width * 0.020);

        iv_Logo = new ImageView(root.getContext());
        iv_Logo.setImageBitmap(bitmap_Logo);
        iv_Logo.setY(logoX);
        iv_Logo.setX(logoY);

        fragment_menu.addView(iv_Logo);

    }

}
