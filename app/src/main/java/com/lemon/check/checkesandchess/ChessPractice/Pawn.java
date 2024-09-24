package com.lemon.check.checkesandchess.ChessPractice;

//import Piece;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(Boolean isBlack, int row, int column, Boolean firstMove) {
        super(isBlack, row, column, firstMove);
    }

    @Override
    public List getMoves(Piece[][] board) {

        List<Point> moves = new ArrayList<>();

        if(this.isBlack()){
            Log.d("isBlack", String.valueOf(this.isBlack()));
            if(column == 0){
                if(board[row+1][column] == null) {
                    moves.add(new Point(row + 1, column));
                    if(firstMove && board[row + 2][column] == null){
                        moves.add(new Point(row + 2, column));
                    }
                }
                if(board[row + 1][column + 1] != null && !board[row + 1][column + 1].isBlack()){
                    moves.add(new Point(row + 1, column + 1));
                }

            }
            else if(column == 7) {
                if(board[row + 1][column] == null) {
                    moves.add(new Point(row + 1, column));
                    if(firstMove && board[row + 2][column] == null){
                        moves.add(new Point(row + 2, column));
                    }
                }
                if(board[row + 1][column - 1] != null && !board[row + 1][column - 1].isBlack()){
                    moves.add(new Point(row + 1, column - 1));
                }
            }
            else {
                if(board[row + 1][column] == null) {
                    moves.add(new Point(row + 1, column));
                    if(firstMove && board[row + 2][column] == null){
                        moves.add(new Point(row + 2, column));
                    }
                }
                if(board[row + 1][column + 1] != null && !board[row + 1][column + 1].isBlack()){
                    moves.add(new Point(row + 1, column + 1));
                }

                if(board[row + 1][column - 1] != null && !board[row + 1][column - 1].isBlack()){
                    moves.add(new Point(row + 1, column - 1));
                }
            }
        }
        //red's turn
        else{
            if(column == 0){
                if(board[row-1][column] == null) {
                    moves.add(new Point(row - 1, column));
                    if(firstMove && board[row-2][column] == null){
                        moves.add(new Point(row - 2, column));
                    }
                }
                if(board[row - 1][column + 1] != null && board[row - 1][column + 1].isBlack()){
                    moves.add(new Point(row - 1, column + 1));
                }

            }
            else if(column == 7) {
                if(board[row-1][column] == null) {
                    moves.add(new Point(row - 1, column));
                    if(firstMove && board[row-2][column] == null){
                        moves.add(new Point(row - 2, column));
                    }
                }
                if(board[row - 1][column - 1] != null && board[row - 1][column - 1].isBlack()){
                    moves.add(new Point(row - 1, column - 1));
                }
            }
            else {
                if(board[row-1][column] == null) {
                    moves.add(new Point(row - 1, column));
                    if(firstMove && board[row-2][column] == null){
                        moves.add(new Point(row - 2, column));
                    }
                }
                if(board[row - 1][column + 1] != null && board[row - 1][column + 1].isBlack()){
                    moves.add(new Point(row - 1, column + 1));
                }

                if(board[row - 1][column - 1] != null &&board[row - 1][column - 1].isBlack()){
                    moves.add(new Point(row - 1, column - 1));
                }
            }
        }

        return moves;
    }
}
