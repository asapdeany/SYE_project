package com.example.deansponholz.sye_project;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by deansponholz on 5/15/17.
 */

public class Fragment_Instruction extends Fragment {

    InstructionDrawView instructionDrawView;
    RelativeLayout fragment_instruction;
    public SensorHandler sensorHandler = null;
    System_UI_Manager system_ui_manager;
    private static final int LINE_SPACING = 100;
    Button button_instruction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_instruction, container, false);
        system_ui_manager = new System_UI_Manager(getActivity());
        sensorHandler = new SensorHandler(root.getContext());
        instructionDrawView = new InstructionDrawView(this.getActivity());
        fragment_instruction = (RelativeLayout) root.findViewById(R.id.fragment_instruction);

        button_instruction = (Button) root.findViewById(R.id.button_instruction);

        button_instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Instruction.class);
                getActivity().startActivity(intent);
            }
        });


        return root;
    }

    public class InstructionDrawView extends View {

        //onDraw
        Canvas canvas;
        float fishX, fishY;
        Paint paintLine = new Paint();


        public InstructionDrawView(Context context) {
            super(context);
            initMyView();
        }

        public void initMyView() {
            //Drawing Tools
            canvas = new Canvas();

            paintLine = new Paint();
            paintLine.setColor(Color.WHITE);
            paintLine.setStrokeWidth(3.25f);
            paintLine.setStyle(Paint.Style.STROKE);

        }

        @Override
        public void onDraw(Canvas canvas) {


            int yOffset = (Constants_Display.height / 2) - 60;
            int xOffset = (Constants_Display.width / 2) - 55;


            fishX = (float) (-sensorHandler.xPos * 5) + xOffset;
            fishY = (float) (sensorHandler.yPos * 5) + yOffset;


            //Loop through to create 10 vertical lines
            for (int i = 1; i < 17; i++) {
                canvas.drawLine(fishX + (i * LINE_SPACING), -Constants_Display.height, fishX + (i * LINE_SPACING), +Constants_Display.height, paintLine);

            }
            for (int i = 1; i < 17; i++) {
                canvas.drawLine(fishX + (i * -LINE_SPACING), -Constants_Display.height, fishX + (i * -LINE_SPACING), +Constants_Display.height, paintLine);

            }

            //Loop through to create 10 horizontal lines
            for (int i = 1; i < 17; i++) {
                canvas.drawLine(0, fishY + (i * LINE_SPACING), Constants_Display.width, fishY + (i * LINE_SPACING), paintLine);

            }
            for (int i = 1; i < 17; i++) {
                canvas.drawLine(0, fishY - (i * LINE_SPACING), Constants_Display.width, fishY - (i * LINE_SPACING), paintLine);
            }
            //middle vertical line from landscape point of view
            canvas.drawLine(fishX, -Constants_Display.height, fishX, Constants_Display.height, paintLine);
            //middle horizontal line from landscape point of view
            canvas.drawLine(0, fishY, Constants_Display.width, fishY, paintLine);
            invalidate();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        system_ui_manager.hideView();
        fragment_instruction.addView(instructionDrawView);
    }

    @Override
    public void onPause() {
        super.onPause();
        fragment_instruction.removeView(instructionDrawView);
    }
}
