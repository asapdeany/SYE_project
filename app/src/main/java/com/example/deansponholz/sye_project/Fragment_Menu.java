package com.example.deansponholz.sye_project;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by deansponholz on 1/20/17.
 */

public class Fragment_Menu extends Fragment {

    Button button_play;
    Bitmap bitmapTrademark;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

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

        return root;
    }

    private void loadBitmaps(){
        bitmapTrademark = BitmapFactory.decodeResource(getResources(), R.drawable.trademark);
        //bitmapTrademark.setWidth();
    }
}
