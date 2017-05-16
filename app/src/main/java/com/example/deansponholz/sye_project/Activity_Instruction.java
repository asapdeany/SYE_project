package com.example.deansponholz.sye_project;

/**
 * Created by deansponholz on 5/15/17.
 */


import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;


public class Activity_Instruction extends FragmentActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener{


    private RadioGroup radioGroup;
    private static final int NUMBER_OF_PAGES = 2;
    ViewPager pager;
    System_UI_Manager system_ui_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        system_ui_manager = new System_UI_Manager(this);
        system_ui_manager.hideView();
        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(this);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        switch(position) {
            case 0:
                radioGroup.check(R.id.radioButton1);
                break;
            case 1:
                radioGroup.check(R.id.radioButton2);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch(i) {
            case R.id.radioButton1:
                pager.setCurrentItem(0);
                break;
            case R.id.radioButton2:
                pager.setCurrentItem(1);
                break;
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {


                case 0: return Fragment_Instruction_Intro.newInstance();
                case 1: return Fragment_Instruction_Two.newInstance();



                default: return Fragment_Instruction_Intro.newInstance();

            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
