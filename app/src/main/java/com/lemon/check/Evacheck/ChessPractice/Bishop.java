package com.lemon.check.Evacheck.ChessPractice;

//import Piece;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Boolean isBlack, int row, int column) {
        super(isBlack, row, column);
    }

    @Override
    public List getMoves(Piece[][] board) {
        List<Point> moves = new ArrayList<>();

        //up right
        int tempRow = row;
        int tempColumn = column;
        for (;;){
            tempRow++;
            tempColumn++;
            if(tempRow >= 0 && tempRow < 8 && tempColumn >= 0 && tempColumn < 8){
                if(board[tempRow][tempColumn] == null) {
                    moves.add(new Point(tempRow, tempColumn));
                }
                else if(board[tempRow][tempColumn].isBlack() != this.isBlack()){
                    moves.add(new Point(tempRow,tempColumn));
                    break;
                }
                else{
                    break;
                }
            }
            else{
                break;
            }

        }


        // up left
        tempRow = row;
        tempColumn = column;
        for (;;){
            tempRow++;
            tempColumn--;
            if(tempRow >= 0 && tempRow < 8 && tempColumn >= 0 && tempColumn < 8){
                if(board[tempRow][tempColumn] == null) {
                    moves.add(new Point(tempRow, tempColumn));
                }
                else if(board[tempRow][tempColumn].isBlack() != this.isBlack()){
                    moves.add(new Point(tempRow,tempColumn));
                    break;
                }
                else{
                    break;
                }
            }
            else{
                break;
            }

        }

        //down right
        tempRow = row;
        tempColumn = column;
        for (;;){
            tempRow--;
            tempColumn++;
            if(tempRow >= 0 && tempRow < 8 && tempColumn >= 0 && tempColumn < 8){
                if(board[tempRow][tempColumn] == null) {
                    moves.add(new Point(tempRow, tempColumn));
                }
                else if(board[tempRow][tempColumn].isBlack() != this.isBlack()){
                    moves.add(new Point(tempRow,tempColumn));
                    break;
                }
                else{
                    break;
                }
            }
            else{
                break;
            }

        }


        //down left
        tempRow = row;
        tempColumn = column;
        for (;;){
            tempRow--;
            tempColumn--;
            if(tempRow >= 0 && tempRow < 8 && tempColumn >= 0 && tempColumn < 8){
                if(board[tempRow][tempColumn] == null) {
                    moves.add(new Point(tempRow, tempColumn));
                }
                else if(board[tempRow][tempColumn].isBlack() != this.isBlack()){
                    moves.add(new Point(tempRow,tempColumn));
                    break;
                }
                else{
                    break;
                }
            }
            else{
                break;
            }

        }

        return moves;
    }
}
