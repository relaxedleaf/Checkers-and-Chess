package com.lemon.check.checkesandchess.ChessPractice;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


import com.lemon.check.checkesandchess.R;

import java.util.List;


public class Chess extends AppCompatActivity implements View.OnClickListener{
    private ImageButton[][] imageButtonList;

    private Boolean firstClick;
    private Piece[][] board;
    private List<Point> moves;
    private Point start;

    Boolean isBlacksTurn;


    //row 1
    private ImageButton ibtn_0_0;
    private ImageButton ibtn_0_1;
    private ImageButton ibtn_0_2;
    private ImageButton ibtn_0_3;
    private ImageButton ibtn_0_4;
    private ImageButton ibtn_0_5;
    private ImageButton ibtn_0_6;
    private ImageButton ibtn_0_7;
    //row 2
    private ImageButton ibtn_1_0;
    private ImageButton ibtn_1_1;
    private ImageButton ibtn_1_2;
    private ImageButton ibtn_1_3;
    private ImageButton ibtn_1_4;
    private ImageButton ibtn_1_5;
    private ImageButton ibtn_1_6;
    private ImageButton ibtn_1_7;
    //row 3
    private ImageButton ibtn_2_0;
    private ImageButton ibtn_2_1;
    private ImageButton ibtn_2_2;
    private ImageButton ibtn_2_3;
    private ImageButton ibtn_2_4;
    private ImageButton ibtn_2_5;
    private ImageButton ibtn_2_6;
    private ImageButton ibtn_2_7;
    //row 4
    private ImageButton ibtn_3_0;
    private ImageButton ibtn_3_1;
    private ImageButton ibtn_3_2;
    private ImageButton ibtn_3_3;
    private ImageButton ibtn_3_4;
    private ImageButton ibtn_3_5;
    private ImageButton ibtn_3_6;
    private ImageButton ibtn_3_7;

    //row 5
    private ImageButton ibtn_4_0;
    private ImageButton ibtn_4_1;
    private ImageButton ibtn_4_2;
    private ImageButton ibtn_4_3;
    private ImageButton ibtn_4_4;
    private ImageButton ibtn_4_5;
    private ImageButton ibtn_4_6;
    private ImageButton ibtn_4_7;

    //row 6
    private ImageButton ibtn_5_0;
    private ImageButton ibtn_5_1;
    private ImageButton ibtn_5_2;
    private ImageButton ibtn_5_3;
    private ImageButton ibtn_5_4;
    private ImageButton ibtn_5_5;
    private ImageButton ibtn_5_6;
    private ImageButton ibtn_5_7;

    //row 7
    private ImageButton ibtn_6_0;
    private ImageButton ibtn_6_1;
    private ImageButton ibtn_6_2;
    private ImageButton ibtn_6_3;
    private ImageButton ibtn_6_4;
    private ImageButton ibtn_6_5;
    private ImageButton ibtn_6_6;
    private ImageButton ibtn_6_7;

    //row 8
    private ImageButton ibtn_7_0;
    private ImageButton ibtn_7_1;
    private ImageButton ibtn_7_2;
    private ImageButton ibtn_7_3;
    private ImageButton ibtn_7_4;
    private ImageButton ibtn_7_5;
    private ImageButton ibtn_7_6;
    private ImageButton ibtn_7_7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);



        //row 1
        ibtn_0_0 = findViewById(R.id.ibtn_0_0);
        ibtn_0_1 = findViewById(R.id.ibtn_0_1);
        ibtn_0_2 = findViewById(R.id.ibtn_0_2);
        ibtn_0_3 = findViewById(R.id.ibtn_0_3);
        ibtn_0_4 = findViewById(R.id.ibtn_0_4);
        ibtn_0_5 = findViewById(R.id.ibtn_0_5);
        ibtn_0_6 = findViewById(R.id.ibtn_0_6);
        ibtn_0_7 = findViewById(R.id.ibtn_0_7);


//row 2
        ibtn_1_0 = findViewById(R.id.ibtn_1_0);
        ibtn_1_1 = findViewById(R.id.ibtn_1_1);
        ibtn_1_2 = findViewById(R.id.ibtn_1_2);
        ibtn_1_3 = findViewById(R.id.ibtn_1_3);
        ibtn_1_4 = findViewById(R.id.ibtn_1_4);
        ibtn_1_5 = findViewById(R.id.ibtn_1_5);
        ibtn_1_6 = findViewById(R.id.ibtn_1_6);
        ibtn_1_7 = findViewById(R.id.ibtn_1_7);
//row 3
        ibtn_2_0 = findViewById(R.id.ibtn_2_0);
        ibtn_2_1 = findViewById(R.id.ibtn_2_1);
        ibtn_2_2 = findViewById(R.id.ibtn_2_2);
        ibtn_2_3 = findViewById(R.id.ibtn_2_3);
        ibtn_2_4 = findViewById(R.id.ibtn_2_4);
        ibtn_2_5 = findViewById(R.id.ibtn_2_5);
        ibtn_2_6 = findViewById(R.id.ibtn_2_6);
        ibtn_2_7 = findViewById(R.id.ibtn_2_7);
//row 4
        ibtn_3_0 = findViewById(R.id.ibtn_3_0);
        ibtn_3_1 = findViewById(R.id.ibtn_3_1);
        ibtn_3_2 = findViewById(R.id.ibtn_3_2);
        ibtn_3_3 = findViewById(R.id.ibtn_3_3);
        ibtn_3_4 = findViewById(R.id.ibtn_3_4);
        ibtn_3_5 = findViewById(R.id.ibtn_3_5);
        ibtn_3_6 = findViewById(R.id.ibtn_3_6);
        ibtn_3_7 = findViewById(R.id.ibtn_3_7);

//row 5
        ibtn_4_0 = findViewById(R.id.ibtn_4_0);
        ibtn_4_1 = findViewById(R.id.ibtn_4_1);
        ibtn_4_2 = findViewById(R.id.ibtn_4_2);
        ibtn_4_3 = findViewById(R.id.ibtn_4_3);
        ibtn_4_4 = findViewById(R.id.ibtn_4_4);
        ibtn_4_5 = findViewById(R.id.ibtn_4_5);
        ibtn_4_6 = findViewById(R.id.ibtn_4_6);
        ibtn_4_7 = findViewById(R.id.ibtn_4_7);

//row 6
        ibtn_5_0 = findViewById(R.id.ibtn_5_0);
        ibtn_5_1 = findViewById(R.id.ibtn_5_1);
        ibtn_5_2 = findViewById(R.id.ibtn_5_2);
        ibtn_5_3 = findViewById(R.id.ibtn_5_3);
        ibtn_5_4 = findViewById(R.id.ibtn_5_4);
        ibtn_5_5 = findViewById(R.id.ibtn_5_5);
        ibtn_5_6 = findViewById(R.id.ibtn_5_6);
        ibtn_5_7 = findViewById(R.id.ibtn_5_7);

//row 7
        ibtn_6_0 = findViewById(R.id.ibtn_6_0);
        ibtn_6_1 = findViewById(R.id.ibtn_6_1);
        ibtn_6_2 = findViewById(R.id.ibtn_6_2);
        ibtn_6_3 = findViewById(R.id.ibtn_6_3);
        ibtn_6_4 = findViewById(R.id.ibtn_6_4);
        ibtn_6_5 = findViewById(R.id.ibtn_6_5);
        ibtn_6_6 = findViewById(R.id.ibtn_6_6);
        ibtn_6_7 = findViewById(R.id.ibtn_6_7);

//row 7
        ibtn_7_0 = findViewById(R.id.ibtn_7_0);
        ibtn_7_1 = findViewById(R.id.ibtn_7_1);
        ibtn_7_2 = findViewById(R.id.ibtn_7_2);
        ibtn_7_3 = findViewById(R.id.ibtn_7_3);
        ibtn_7_4 = findViewById(R.id.ibtn_7_4);
        ibtn_7_5 = findViewById(R.id.ibtn_7_5);
        ibtn_7_6 = findViewById(R.id.ibtn_7_6);
        ibtn_7_7 = findViewById(R.id.ibtn_7_7);



        //row 1
        ibtn_0_0.setOnClickListener(this);
        ibtn_0_1.setOnClickListener(this);
        ibtn_0_2.setOnClickListener(this);
        ibtn_0_3.setOnClickListener(this);
        ibtn_0_4.setOnClickListener(this);
        ibtn_0_5.setOnClickListener(this);
        ibtn_0_6.setOnClickListener(this);
        ibtn_0_7.setOnClickListener(this);


//row 2
        ibtn_1_0.setOnClickListener(this);
        ibtn_1_1.setOnClickListener(this);
        ibtn_1_2.setOnClickListener(this);
        ibtn_1_3.setOnClickListener(this);
        ibtn_1_4.setOnClickListener(this);
        ibtn_1_5.setOnClickListener(this);
        ibtn_1_6.setOnClickListener(this);
        ibtn_1_7.setOnClickListener(this);
//row 3
        ibtn_2_0.setOnClickListener(this);
        ibtn_2_1.setOnClickListener(this);
        ibtn_2_2.setOnClickListener(this);
        ibtn_2_3.setOnClickListener(this);
        ibtn_2_4.setOnClickListener(this);
        ibtn_2_5.setOnClickListener(this);
        ibtn_2_6.setOnClickListener(this);
        ibtn_2_7.setOnClickListener(this);
//row 4
        ibtn_3_0.setOnClickListener(this);
        ibtn_3_1.setOnClickListener(this);
        ibtn_3_2.setOnClickListener(this);
        ibtn_3_3.setOnClickListener(this);
        ibtn_3_4.setOnClickListener(this);
        ibtn_3_5.setOnClickListener(this);
        ibtn_3_6.setOnClickListener(this);
        ibtn_3_7.setOnClickListener(this);

//row 5
        ibtn_4_0.setOnClickListener(this);
        ibtn_4_1.setOnClickListener(this);
        ibtn_4_2.setOnClickListener(this);
        ibtn_4_3.setOnClickListener(this);
        ibtn_4_4.setOnClickListener(this);
        ibtn_4_5.setOnClickListener(this);
        ibtn_4_6.setOnClickListener(this);
        ibtn_4_7.setOnClickListener(this);

//row 6
        ibtn_5_0.setOnClickListener(this);
        ibtn_5_1.setOnClickListener(this);
        ibtn_5_2.setOnClickListener(this);
        ibtn_5_3.setOnClickListener(this);
        ibtn_5_4.setOnClickListener(this);
        ibtn_5_5.setOnClickListener(this);
        ibtn_5_6.setOnClickListener(this);
        ibtn_5_7.setOnClickListener(this);

//row 7
        ibtn_6_0.setOnClickListener(this);
        ibtn_6_1.setOnClickListener(this);
        ibtn_6_2.setOnClickListener(this);
        ibtn_6_3.setOnClickListener(this);
        ibtn_6_4.setOnClickListener(this);
        ibtn_6_5.setOnClickListener(this);
        ibtn_6_6.setOnClickListener(this);
        ibtn_6_7.setOnClickListener(this);

//row 7
        ibtn_7_0.setOnClickListener(this);
        ibtn_7_1.setOnClickListener(this);
        ibtn_7_2.setOnClickListener(this);
        ibtn_7_3.setOnClickListener(this);
        ibtn_7_4.setOnClickListener(this);
        ibtn_7_5.setOnClickListener(this);
        ibtn_7_6.setOnClickListener(this);
        ibtn_7_7.setOnClickListener(this);

        imageButtonList = new ImageButton[][]
                {{ibtn_0_0, ibtn_0_1, ibtn_0_2, ibtn_0_3, ibtn_0_4, ibtn_0_5, ibtn_0_6, ibtn_0_7},
                        {ibtn_1_0, ibtn_1_1, ibtn_1_2, ibtn_1_3, ibtn_1_4, ibtn_1_5, ibtn_1_6, ibtn_1_7},
                        {ibtn_2_0, ibtn_2_1, ibtn_2_2, ibtn_2_3, ibtn_2_4, ibtn_2_5, ibtn_2_6, ibtn_2_7},
                        {ibtn_3_0, ibtn_3_1, ibtn_3_2, ibtn_3_3, ibtn_3_4, ibtn_3_5, ibtn_3_6, ibtn_3_7},
                        {ibtn_4_0, ibtn_4_1, ibtn_4_2, ibtn_4_3, ibtn_4_4, ibtn_4_5, ibtn_4_6, ibtn_4_7},
                        {ibtn_5_0, ibtn_5_1, ibtn_5_2, ibtn_5_3, ibtn_5_4, ibtn_5_5, ibtn_5_6, ibtn_5_7},
                        {ibtn_6_0, ibtn_6_1, ibtn_6_2, ibtn_6_3, ibtn_6_4, ibtn_6_5, ibtn_6_6, ibtn_6_7},
                        {ibtn_7_0, ibtn_7_1, ibtn_7_2, ibtn_7_3, ibtn_7_4, ibtn_7_5, ibtn_7_6, ibtn_7_7}};




        firstClick = true;
        isBlacksTurn = false;


        board = new Piece[8][8];
        setup();
       // return false;
    }

    @Override
    public void onClick(View v) {
        if(firstClick) {
            int id = v.getId();
            if (id == R.id.ibtn_0_0) {
                processFirstClick(0, 0);
            } else if (id == R.id.ibtn_0_1) {
                processFirstClick(0, 1);
            } else if (id == R.id.ibtn_0_2) {
                processFirstClick(0, 2);
            } else if (id == R.id.ibtn_0_3) {
                processFirstClick(0, 3);
            } else if (id == R.id.ibtn_0_4) {
                processFirstClick(0, 4);
            } else if (id == R.id.ibtn_0_5) {
                processFirstClick(0, 5);
            } else if (id == R.id.ibtn_0_6) {
                processFirstClick(0, 6);
            } else if (id == R.id.ibtn_0_7) {
                processFirstClick(0, 7);
            } else if (id == R.id.ibtn_1_0) {
                processFirstClick(1, 0);
            } else if (id == R.id.ibtn_1_1) {
                processFirstClick(1, 1);
            } else if (id == R.id.ibtn_1_2) {
                processFirstClick(1, 2);
            } else if (id == R.id.ibtn_1_3) {
                processFirstClick(1, 3);
            } else if (id == R.id.ibtn_1_4) {
                processFirstClick(1, 4);
            } else if (id == R.id.ibtn_1_5) {
                processFirstClick(1, 5);
            } else if (id == R.id.ibtn_1_6) {
                processFirstClick(1, 6);
            } else if (id == R.id.ibtn_1_7) {
                processFirstClick(1, 7);
            } else if (id == R.id.ibtn_2_0) {
                processFirstClick(2, 0);
            } else if (id == R.id.ibtn_2_1) {
                processFirstClick(2, 1);
            } else if (id == R.id.ibtn_2_2) {
                processFirstClick(2, 2);
            } else if (id == R.id.ibtn_2_3) {
                processFirstClick(2, 3);
            } else if (id == R.id.ibtn_2_4) {
                processFirstClick(2, 4);
            } else if (id == R.id.ibtn_2_5) {
                processFirstClick(2, 5);
            } else if (id == R.id.ibtn_2_6) {
                processFirstClick(2, 6);
            } else if (id == R.id.ibtn_2_7) {
                processFirstClick(2, 7);
            } else if (id == R.id.ibtn_3_0) {
                processFirstClick(3, 0);
            } else if (id == R.id.ibtn_3_1) {
                processFirstClick(3, 1);
            } else if (id == R.id.ibtn_3_2) {
                processFirstClick(3, 2);
            } else if (id == R.id.ibtn_3_3) {
                processFirstClick(3, 3);
            } else if (id == R.id.ibtn_3_4) {
                processFirstClick(3, 4);
            } else if (id == R.id.ibtn_3_5) {
                processFirstClick(3, 5);
            } else if (id == R.id.ibtn_3_6) {
                processFirstClick(3, 6);
            } else if (id == R.id.ibtn_3_7) {
                processFirstClick(3, 7);
            } else if (id == R.id.ibtn_4_0) {
                processFirstClick(4, 0);
            } else if (id == R.id.ibtn_4_1) {
                processFirstClick(4, 1);
            } else if (id == R.id.ibtn_4_2) {
                processFirstClick(4, 2);
            } else if (id == R.id.ibtn_4_3) {
                processFirstClick(4, 3);
            } else if (id == R.id.ibtn_4_4) {
                processFirstClick(4, 4);
            } else if (id == R.id.ibtn_4_5) {
                processFirstClick(4, 5);
            } else if (id == R.id.ibtn_4_6) {
                processFirstClick(4, 6);
            } else if (id == R.id.ibtn_4_7) {
                processFirstClick(4, 7);
            } else if (id == R.id.ibtn_5_0) {
                processFirstClick(5, 0);
            } else if (id == R.id.ibtn_5_1) {
                processFirstClick(5, 1);
            } else if (id == R.id.ibtn_5_2) {
                processFirstClick(5, 2);
            } else if (id == R.id.ibtn_5_3) {
                processFirstClick(5, 3);
            } else if (id == R.id.ibtn_5_4) {
                processFirstClick(5, 4);
            } else if (id == R.id.ibtn_5_5) {
                processFirstClick(5, 5);
            } else if (id == R.id.ibtn_5_6) {
                processFirstClick(5, 6);
            } else if (id == R.id.ibtn_5_7) {
                processFirstClick(5, 7);
            } else if (id == R.id.ibtn_6_0) {
                processFirstClick(6, 0);
            } else if (id == R.id.ibtn_6_1) {
                processFirstClick(6, 1);
            } else if (id == R.id.ibtn_6_2) {
                processFirstClick(6, 2);
            } else if (id == R.id.ibtn_6_3) {
                processFirstClick(6, 3);
            } else if (id == R.id.ibtn_6_4) {
                processFirstClick(6, 4);
            } else if (id == R.id.ibtn_6_5) {
                processFirstClick(6, 5);
            } else if (id == R.id.ibtn_6_6) {
                processFirstClick(6, 6);
            } else if (id == R.id.ibtn_6_7) {
                processFirstClick(6, 7);
            } else if (id == R.id.ibtn_7_0) {
                processFirstClick(7, 0);
            } else if (id == R.id.ibtn_7_1) {
                processFirstClick(7, 1);
            } else if (id == R.id.ibtn_7_2) {
                processFirstClick(7, 2);
            } else if (id == R.id.ibtn_7_3) {
                processFirstClick(7, 3);
            } else if (id == R.id.ibtn_7_4) {
                processFirstClick(7, 4);
            } else if (id == R.id.ibtn_7_5) {
                processFirstClick(7, 5);
            } else if (id == R.id.ibtn_7_6) {
                processFirstClick(7, 6);
            } else if (id == R.id.ibtn_7_7) {
                processFirstClick(7, 7);
            }

        }
        else{

            int id = v.getId();
            if (id == R.id.ibtn_0_0) {
                processSecondClick(0, 0);
            } else if (id == R.id.ibtn_0_1) {
                processSecondClick(0, 1);
            } else if (id == R.id.ibtn_0_2) {
                processSecondClick(0, 2);
            } else if (id == R.id.ibtn_0_3) {
                processSecondClick(0, 3);
            } else if (id == R.id.ibtn_0_4) {
                processSecondClick(0, 4);
            } else if (id == R.id.ibtn_0_5) {
                processSecondClick(0, 5);
            } else if (id == R.id.ibtn_0_6) {
                processSecondClick(0, 6);
            } else if (id == R.id.ibtn_0_7) {
                processSecondClick(0, 7);
            } else if (id == R.id.ibtn_1_0) {
                processSecondClick(1, 0);
            } else if (id == R.id.ibtn_1_1) {
                processSecondClick(1, 1);
            } else if (id == R.id.ibtn_1_2) {
                processSecondClick(1, 2);
            } else if (id == R.id.ibtn_1_3) {
                processSecondClick(1, 3);
            } else if (id == R.id.ibtn_1_4) {
                processSecondClick(1, 4);
            } else if (id == R.id.ibtn_1_5) {
                processSecondClick(1, 5);
            } else if (id == R.id.ibtn_1_6) {
                processSecondClick(1, 6);
            } else if (id == R.id.ibtn_1_7) {
                processSecondClick(1, 7);
            } else if (id == R.id.ibtn_2_0) {
                processSecondClick(2, 0);
            } else if (id == R.id.ibtn_2_1) {
                processSecondClick(2, 1);
            } else if (id == R.id.ibtn_2_2) {
                processSecondClick(2, 2);
            } else if (id == R.id.ibtn_2_3) {
                processSecondClick(2, 3);
            } else if (id == R.id.ibtn_2_4) {
                processSecondClick(2, 4);
            } else if (id == R.id.ibtn_2_5) {
                processSecondClick(2, 5);
            } else if (id == R.id.ibtn_2_6) {
                processSecondClick(2, 6);
            } else if (id == R.id.ibtn_2_7) {
                processSecondClick(2, 7);
            } else if (id == R.id.ibtn_3_0) {
                processSecondClick(3, 0);
            } else if (id == R.id.ibtn_3_1) {
                processSecondClick(3, 1);
            } else if (id == R.id.ibtn_3_2) {
                processSecondClick(3, 2);
            } else if (id == R.id.ibtn_3_3) {
                processSecondClick(3, 3);
            } else if (id == R.id.ibtn_3_4) {
                processSecondClick(3, 4);
            } else if (id == R.id.ibtn_3_5) {
                processSecondClick(3, 5);
            } else if (id == R.id.ibtn_3_6) {
                processSecondClick(3, 6);
            } else if (id == R.id.ibtn_3_7) {
                processSecondClick(3, 7);
            } else if (id == R.id.ibtn_4_0) {
                processSecondClick(4, 0);
            } else if (id == R.id.ibtn_4_1) {
                processSecondClick(4, 1);
            } else if (id == R.id.ibtn_4_2) {
                processSecondClick(4, 2);
            } else if (id == R.id.ibtn_4_3) {
                processSecondClick(4, 3);
            } else if (id == R.id.ibtn_4_4) {
                processSecondClick(4, 4);
            } else if (id == R.id.ibtn_4_5) {
                processSecondClick(4, 5);
            } else if (id == R.id.ibtn_4_6) {
                processSecondClick(4, 6);
            } else if (id == R.id.ibtn_4_7) {
                processSecondClick(4, 7);
            } else if (id == R.id.ibtn_5_0) {
                processSecondClick(5, 0);
            } else if (id == R.id.ibtn_5_1) {
                processSecondClick(5, 1);
            } else if (id == R.id.ibtn_5_2) {
                processSecondClick(5, 2);
            } else if (id == R.id.ibtn_5_3) {
                processSecondClick(5, 3);
            } else if (id == R.id.ibtn_5_4) {
                processSecondClick(5, 4);
            } else if (id == R.id.ibtn_5_5) {
                processSecondClick(5, 5);
            } else if (id == R.id.ibtn_5_6) {
                processSecondClick(5, 6);
            } else if (id == R.id.ibtn_5_7) {
                processSecondClick(5, 7);
            } else if (id == R.id.ibtn_6_0) {
                processSecondClick(6, 0);
            } else if (id == R.id.ibtn_6_1) {
                processSecondClick(6, 1);
            } else if (id == R.id.ibtn_6_2) {
                processSecondClick(6, 2);
            } else if (id == R.id.ibtn_6_3) {
                processSecondClick(6, 3);
            } else if (id == R.id.ibtn_6_4) {
                processSecondClick(6, 4);
            } else if (id == R.id.ibtn_6_5) {
                processSecondClick(6, 5);
            } else if (id == R.id.ibtn_6_6) {
                processSecondClick(6, 6);
            } else if (id == R.id.ibtn_6_7) {
                processSecondClick(6, 7);
            } else if (id == R.id.ibtn_7_0) {
                processSecondClick(7, 0);
            } else if (id == R.id.ibtn_7_1) {
                processSecondClick(7, 1);
            } else if (id == R.id.ibtn_7_2) {
                processSecondClick(7, 2);
            } else if (id == R.id.ibtn_7_3) {
                processSecondClick(7, 3);
            } else if (id == R.id.ibtn_7_4) {
                processSecondClick(7, 4);
            } else if (id == R.id.ibtn_7_5) {
                processSecondClick(7, 5);
            } else if (id == R.id.ibtn_7_6) {
                processSecondClick(7, 6);
            } else if (id == R.id.ibtn_7_7) {
                processSecondClick(7, 7);
            }
        }
    }




    public void setup(){
        //Black
        board[0][0] = new Rook(true,0,0);
        board[0][7] = new Rook(true,0,7);
        board[0][1] = new Knight(true,0,1);
        board[0][6] = new Knight(true,0,6);
        board[0][2] = new Bishop(true,0,2);
        board[0][5] = new Bishop(true,0,5);
        board[0][3] = new Queen(true,0,3);
        board[0][4] = new King(true,0,4);

        for(int i = 0; i < 8; i++){
            board[1][i] = new Pawn(true,1,i,true);
        }

        //Red
        board[7][0] = new Rook(false,7,0);
        board[7][7] = new Rook(false,7,7);
        board[7][1] = new Knight(false,7,1);
        board[7][6] = new Knight(false,7,6);
        board[7][2] = new Bishop(false,7,2);
        board[7][5] = new Bishop(false,7,5);
        board[7][3] = new Queen(false,7,3);
        board[7][4] = new King(false,7,4);

        for(int i = 0; i < 8; i++){
            board[6][i] = new Pawn(false,6,i,true);
        }
    }

    public void resetBackgroundColor(){
        for(int r = 0; r < imageButtonList.length; r++){
            for(int c = 0; c < imageButtonList.length; c++){
                if(r%2 == 0){
                    if(c%2 == 0){
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#e1e4e9"));
                    }
                    else{
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#c9af98"));
                    }
                }
                else{
                    if(c%2 == 0){
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#c9af98"));
                    }
                    else{
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#e1e4e9"));
                    }
                }

            }
        }
    }

    public void resetBackgroundAll(){
        for(int r = 0; r < imageButtonList.length; r++){
            for(int c = 0; c < imageButtonList.length; c++){
                if(r%2 == 0){
                    if(c%2 == 0){
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#e1e4e9"));
                    }
                    else{
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#c9af98"));
                    }
                }
                else{
                    if(c%2 == 0){
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#c9af98"));
                    }
                    else{
                        imageButtonList[r][c].setBackgroundColor(Color.parseColor("#e1e4e9"));
                    }
                }
                if(board[r][c] != null) {
                    if (board[r][c].isBlack() && board[r][c] instanceof Bishop) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_bishop);
                    } else if (board[r][c].isBlack() && board[r][c] instanceof King) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_king);
                    } else if (board[r][c].isBlack() && board[r][c] instanceof Knight) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_knight);
                    } else if (board[r][c].isBlack() && board[r][c] instanceof Pawn) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_pawn);
                    } else if (board[r][c].isBlack() && board[r][c] instanceof Queen) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_queen);
                    } else if (board[r][c].isBlack() && board[r][c] instanceof Rook) {
                        imageButtonList[r][c].setImageResource(R.drawable.black_rook);
                    } else if (!board[r][c].isBlack() && board[r][c] instanceof Bishop) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_bishop);
                    } else if (!board[r][c].isBlack() && board[r][c] instanceof King) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_king);
                    } else if (!board[r][c].isBlack() && board[r][c] instanceof Knight) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_knight);
                    } else if (!board[r][c].isBlack() && board[r][c] instanceof Pawn) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_pawn);
                    } else if (!board[r][c].isBlack() && board[r][c] instanceof Queen) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_queen);
                    } else if (!board[r][c].isBlack() && board[r][c] instanceof Rook) {
                        imageButtonList[r][c].setImageResource(R.drawable.red_rook);
                    } else {
                        return;
                    }
                }
                else{
                    imageButtonList[r][c].setImageResource(0);
                }
            }
        }
    }

    public void resetStart(){
        start.setRow(-1);
        start.setColumn(-1);
    }

    public void processFirstClick(int row, int column){
        Log.d("Clicked","clicked");
        //if no piece is in this position
        if(board[row][column] == null){
            Log.d("Clicked","clicked1");
            return;
        }
        //else if the the piece is the wrong color
        else if(board[row][column].isBlack() != isBlacksTurn){
            Log.d("Clicked","clicked2");
            return;
        }
        //if there is a piece and its the right color
        else{
            Log.d("Clicked","clicked3");
            //get all possible moves
            moves = board[row][column].getMoves(board);
            Log.d("Clicked",String.valueOf(moves.size()));
            //record starting position
            start = new Point(row,column);
            for(int i = 0; i < moves.size(); i++){
                imageButtonList[moves.get(i).getRow()][moves.get(i).getColumn()].setBackgroundColor(Color.WHITE);
            }
            firstClick = false;
        }
    }

    public void processSecondClick(int row, int column){
        //if the users clicks the same position twice
        //deselect the piece
        if(start.equals(new Point(row,column))){
            resetBackgroundColor();
            firstClick = true;
            resetStart();
        }
        //else the users clicks other chess of the same color, possible moves, or null
        else{
            if(board[row][column] == null){
                for (int i = 0; i < moves.size(); i++) {
                    if (moves.get(i).equals(new Point(row, column))) {
                        board[row][column] = board[start.getRow()][start.getColumn()];
                        board[row][column].setRow(row);
                        board[row][column].setColumn(column);
                        if(board[row][column] instanceof Pawn){
                            board[row][column].setFirstMove(false);
                        }
                        board[start.getRow()][start.getColumn()] = null;
                        resetBackgroundAll();
                        firstClick = true;
                        isBlacksTurn = !isBlacksTurn;
                        resetStart();
                        break;
                    }
                }
            }
            else {
                //same color, processFirstClick
                if (board[row][column].isBlack() == isBlacksTurn) {
                    firstClick = true;
                    resetBackgroundColor();
                    processFirstClick(row, column);
                }
                //possible moves
                else {
                    Log.d("rowColumn",String.valueOf(board[row][column].row));
                    for (int i = 0; i < moves.size(); i++) {
                        if (moves.get(i).equals(new Point(row, column))) {
                            board[row][column] = board[start.getRow()][start.getColumn()];
                            board[row][column].setRow(row);
                            board[row][column].setColumn(column);
                            board[start.getRow()][start.getColumn()] = null;
                            resetBackgroundAll();
                            firstClick = true;
                            isBlacksTurn = !isBlacksTurn;
                            resetStart();
                            break;
                        }
                    }
                }
            }

        }
    }


}