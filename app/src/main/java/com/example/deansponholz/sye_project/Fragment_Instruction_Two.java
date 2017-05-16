package com.example.deansponholz.sye_project;

/**
 * Created by deansponholz on 5/16/17.
 */

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Fragment_Instruction_Two extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_instruction_two, container, false);



        return v;

    }
    public static Fragment_Instruction_Two newInstance() {

        Fragment_Instruction_Two f = new Fragment_Instruction_Two();

        return f;
    }
}