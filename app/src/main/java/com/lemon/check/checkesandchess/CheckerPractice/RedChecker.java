package com.lemon.check.checkesandchess.CheckerPractice;

import android.util.Log;

import java.util.ArrayList;

public class RedChecker extends Checker {
    private ArrayList <int[]> possibleMove;
    private ArrayList <int[]> killList;

    public RedChecker(int row, int column){
        super(row,column);
    }

    public RedChecker (Checker redChecker){
        super(redChecker);
    }


    @Override
    public ArrayList<int[]> getMove(Checker[][] checkerList){
        possibleMove = new ArrayList<>();
        killList = new ArrayList<>();

        recursiveSearch(getRow(), getColumn(), checkerList);
        Log.d("PossibleMoves",String.valueOf(possibleMove.size()));
        return possibleMove;
    }

    public void recursiveSearch(int r, int c, Checker[][]checkerList){
        if(!isCrownStatus()){
            recursiveSearchNotCrown(r, c, checkerList);
        }
        else{
            recursiveSearchNotCrown(r, c, checkerList);
            recursiveSearchCrown(r, c, checkerList);
        }
    }

    public void recursiveSearchNotCrown(int r, int c, Checker[][]checkerList){
        if(c == 0 && r < 7) {
            if (!(checkerList[r + 1][c + 1] instanceof RedChecker)) { //if the redChecker is at column 0 and there is not one redChecker at lower right
                if(checkerList[r + 1][c + 1] instanceof BlackChecker){
                    if(r + 2 <= 7) {
                        if (checkerList[r + 2][c + 2] == null) {
                            possibleMove.add(new int[]{r + 2, c + 2});
                            killList.add(new int[]{r + 1, c + 1});
                            //recursiveSearch(r + 2, c + 2, checkerList);
                        }
                    }
                }
                else{
                    possibleMove.add(new int[]{r + 1, c + 1});
                    killList.add(null);
                }
            }

        }
        else if(c > 0 && c < 7 && r < 7){
            if(!(checkerList[r + 1][c - 1] instanceof RedChecker)){//if the lower left is not a redChecker
                if(checkerList[r + 1][c - 1] instanceof BlackChecker){//if the lower left is a black checker
                    if ((r + 2 <= 7) && (c - 2 >= 0)) {
                        if (checkerList[r + 2][c - 2] == null) {//if the lower left of the lower left is not null
                            possibleMove.add(new int[]{r + 2, c - 2});//add the position to the possibleMove
                            killList.add(new int[]{r + 1, c - 1});
                            //recursiveSearch(r + 2, c - 2, checkerList);//start a new search
                        }
                    }

                }
                else{
                    possibleMove.add(new int[]{r + 1, c - 1});
                    killList.add(null);
                }

            }
            if(!(checkerList[r + 1][c + 1] instanceof RedChecker)){//if the lower right is not a redChecker
                if(checkerList[r + 1][c + 1] instanceof BlackChecker){//if the lower right is a black checker
                    if ((r + 2 <= 7) && (c + 2 <= 7)) {
                        if (checkerList[r + 2][c + 2] == null) {//if the lower right of the lower right is not null
                            possibleMove.add(new int[]{r + 2, c + 2});//add the position to the possibleMove
                            killList.add(new int[]{r + 1, c + 1});
                            //recursiveSearch(r + 2, c + 2, checkerList);//start a new search
                        }
                    }

                }
                else{
                    possibleMove.add(new int[]{r + 1, c + 1});
                    killList.add(null);
                }

            }
        }
        else{
            if(c == 7 && r < 7) {
                if (!(checkerList[r + 1][c - 1] instanceof RedChecker)) { //if the redChecker is at column 0 and there is not one redChecker at lower right
                    if (checkerList[r + 1][c - 1] instanceof BlackChecker) {
                        if (r + 2 <= 7) {
                            if (checkerList[r + 2][c - 2] == null) {
                                possibleMove.add(new int[]{r + 2, c - 2});
                                killList.add(new int[]{r + 1, c - 1});
                                //recursiveSearch(r + 2, c - 2, checkerList);
                            }
                        }
                    } else {
                        possibleMove.add(new int[]{r + 1, c - 1});
                        killList.add(null);
                    }
                }
            }
        }
    }

    public void recursiveSearchCrown(int r, int c, Checker[][]checkerList){
        if (c == 0 && r > 0) {
            if (!(checkerList[r - 1][c + 1] instanceof RedChecker)) {
                if (checkerList[r - 1][c + 1] instanceof BlackChecker) {
                    if (r - 2 >= 0) {
                        if (checkerList[r - 2][c + 2] == null) {
                            possibleMove.add(new int[]{r - 2, c + 2});
                            killList.add(new int[]{r - 1, c + 1});
                            //recursiveSearch(r - 2, c + 2, checkerList);
                        }
                    }
                } else {
                    possibleMove.add(new int[]{r - 1, c + 1});
                    killList.add(null);
                }
            }

        } else if (c > 0 && c < 7 && r > 0) {
            if (!(checkerList[r - 1][c - 1] instanceof RedChecker)) {
                if (checkerList[r - 1][c - 1] instanceof BlackChecker) {
                    if ((r - 2 >= 0) && (c - 2 >= 0)) {
                        if (checkerList[r - 2][c - 2] == null) {
                            possibleMove.add(new int[]{r - 2, c - 2});
                            killList.add(new int[]{r - 1, c - 1});
                            //recursiveSearch(r - 2, c - 2, checkerList);
                        }
                    }

                } else {
                    possibleMove.add(new int[]{r - 1, c - 1});
                    killList.add(null);
                }

            }
            if (!(checkerList[r - 1][c + 1] instanceof RedChecker)) {
                if (checkerList[r - 1][c + 1] instanceof BlackChecker) {
                    if ((r - 2 >= 0) && (c + 2 <= 7)) {
                        if (checkerList[r - 2][c + 2] == null) {
                            possibleMove.add(new int[]{r - 2, c + 2});
                            killList.add(new int[]{r - 1, c + 1});
                            //recursiveSearch(r - 2, c + 2, checkerList);//start a new search
                        }
                    }

                } else {
                    possibleMove.add(new int[]{r - 1, c + 1});
                    killList.add(null);
                }

            }
        } else {
            if (c == 7 && r > 0) {
                if (!(checkerList[r - 1][c - 1] instanceof RedChecker)) {
                    if (checkerList[r - 1][c - 1] instanceof BlackChecker) {
                        if (r + 2 >= 0) {
                            if (checkerList[r - 2][c - 2] == null) {
                                possibleMove.add(new int[]{r - 2, c - 2});
                                killList.add(new int[]{r - 1, c - 1});
                                //recursiveSearch(r - 2, c - 2, checkerList);
                            }
                        }
                    } else {
                        possibleMove.add(new int[]{r - 1, c - 1});
                        killList.add(null);
                    }
                }
            }
        }
    }



    @Override
    public ArrayList<int[]> getMove2(Checker[][] checkerList){

        possibleMove = new ArrayList<>();
        killList = new ArrayList<>();

        recursiveSearch2(getRow(), getColumn(), checkerList);

        return possibleMove;
    }

    public void recursiveSearch2(int r, int c, Checker[][]checkerList) {
        if(isCrownStatus() == false){
            recursiveSearch2NotCrown(r, c, checkerList);
        }
        else{
            recursiveSearch2NotCrown(r, c, checkerList);
            recursiveSearch2Crown(r, c, checkerList);
        }
    }

    public void recursiveSearch2NotCrown(int r, int c, Checker[][]checkerList){
        if (c == 0 && r < 7) {
            if (!(checkerList[r + 1][c + 1] instanceof RedChecker)) { //if the redChecker is at column 0 and there is not one redChecker at lower right
                if (checkerList[r + 1][c + 1] instanceof BlackChecker) {
                    if (r + 2 <= 7) {
                        if (checkerList[r + 2][c + 2] == null) {
                            possibleMove.add(new int[]{r + 2, c + 2});
                            killList.add(new int[]{r + 1, c + 1});
                            //recursiveSearch(r + 2, c + 2, checkerList);
                        }
                    }
                }
            }

        } else if (c > 0 && c < 7 && r < 7) {
            if (!(checkerList[r + 1][c - 1] instanceof RedChecker)) {//if the lower left is not a redChecker
                if (checkerList[r + 1][c - 1] instanceof BlackChecker) {//if the lower left is a black checker
                    if ((r + 2 <= 7) && (c - 2 >= 0)) {
                        if (checkerList[r + 2][c - 2] == null) {//if the lower left of the lower left is not null
                            possibleMove.add(new int[]{r + 2, c - 2});//add the position to the possibleMove
                            killList.add(new int[]{r + 1, c - 1});
                            //recursiveSearch(r + 2, c - 2, checkerList);//start a new search
                        }
                    }

                }

            }
            if (!(checkerList[r + 1][c + 1] instanceof RedChecker)) {//if the lower right is not a redChecker
                if (checkerList[r + 1][c + 1] instanceof BlackChecker) {//if the lower right is a black checker
                    if ((r + 2 <= 7) && (c + 2 <= 7)) {
                        if (checkerList[r + 2][c + 2] == null) {//if the lower right of the lower right is not null
                            possibleMove.add(new int[]{r + 2, c + 2});//add the position to the possibleMove
                            killList.add(new int[]{r + 1, c + 1});
                            //recursiveSearch(r + 2, c + 2, checkerList);//start a new search
                        }
                    }

                }

            }
        } else {
            if (c == 7 && r < 7) {
                if (!(checkerList[r + 1][c - 1] instanceof RedChecker)) { //if the redChecker is at column 0 and there is not one redChecker at lower right
                    if (checkerList[r + 1][c - 1] instanceof BlackChecker) {
                        if (r + 2 <= 7) {
                            if (checkerList[r + 2][c - 2] == null) {
                                possibleMove.add(new int[]{r + 2, c - 2});
                                killList.add(new int[]{r + 1, c - 1});
                                //recursiveSearch(r + 2, c - 2, checkerList);
                            }
                        }
                    }
                }
            }
        }
    }

    public void recursiveSearch2Crown(int r, int c, Checker[][]checkerList){
        if(c == 0 && r > 0) {
            if (!(checkerList[r - 1][c + 1] instanceof RedChecker)) {
                if(checkerList[r - 1][c + 1] instanceof BlackChecker){
                    if(r - 2 >= 0) {
                        if (checkerList[r - 2][c + 2] == null) {
                            possibleMove.add(new int[]{r - 2, c + 2});
                            killList.add(new int[]{r - 1, c + 1});
                            //recursiveSearch(r - 2, c + 2, checkerList);
                        }
                    }
                }
            }

        }
        else if(c > 0 && c < 7 && r > 0){
            if(!(checkerList[r - 1][c - 1] instanceof RedChecker)){
                if(checkerList[r - 1][c - 1] instanceof BlackChecker){
                    if ((r - 2 >= 0) && (c - 2 >= 0)) {
                        if (checkerList[r - 2][c - 2] == null) {
                            possibleMove.add(new int[]{r - 2, c - 2});
                            killList.add(new int[]{r - 1, c - 1});
                            //recursiveSearch(r - 2, c - 2, checkerList);
                        }
                    }

                }

            }
            if(!(checkerList[r - 1][c + 1] instanceof RedChecker)){
                if(checkerList[r - 1][c + 1] instanceof BlackChecker){
                    if ((r - 2 >= 0) && (c + 2 <= 7)) {
                        if (checkerList[r - 2][c + 2] == null) {
                            possibleMove.add(new int[]{r - 2, c + 2});
                            killList.add(new int[]{r - 1, c + 1});
                            //recursiveSearch(r - 2, c + 2, checkerList);//start a new search
                        }
                    }

                }

            }
        }
        else{
            if(c == 7 && r > 0) {
                if (!(checkerList[r - 1][c - 1] instanceof RedChecker)) {
                    if (checkerList[r - 1][c - 1] instanceof BlackChecker) {
                        if (r + 2 >= 0) {
                            if (checkerList[r - 2][c - 2] == null) {
                                possibleMove.add(new int[]{r - 2, c - 2});
                                killList.add(new int[]{r - 1, c - 1});
                                //recursiveSearch(r - 2, c - 2, checkerList);
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrayList <int[]> getKillList(){
        return killList;
    }
}


