package com.lemon.check.Evacheck.Chess;

//import Piece;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece implements Serializable {

    public Pawn(Boolean isBlack, int row, int column, Boolean firstMove) {
        super(isBlack, row, column, firstMove,"Pawn");
    }

    @Override
    public List getMoves(List<List<Piece>> board) {

        List<Point> moves = new ArrayList<>();

        if(this.isBlack()){
            Log.d("isBlack", String.valueOf(this.isBlack()));
            if(column == 0){
                if(board.get(row + 1).get(column) instanceof NullChess) {
                    moves.add(new Point(row + 1, column));
                    if(firstMove && board.get(row + 2).get(column) instanceof NullChess){
                        moves.add(new Point(row + 2, column));
                    }
                }
                if(!(board.get(row + 1).get(column + 1)instanceof NullChess) && !board.get(row + 1).get(column + 1).isBlack()){
                    moves.add(new Point(row + 1, column + 1));
                }

            }
            else if(column == 7) {
                if(board.get(row + 1).get(column) instanceof NullChess) {
                    moves.add(new Point(row + 1, column));
                    if(firstMove && board.get(row + 2).get(column) instanceof NullChess){
                        moves.add(new Point(row + 2, column));
                    }
                }
                if(!(board.get(row + 1).get(column - 1)instanceof NullChess) && !board.get(row + 1).get(column - 1).isBlack()){
                    moves.add(new Point(row + 1, column - 1));
                }
            }
            else {
                if(board.get(row + 1).get(column) instanceof NullChess) {
                    moves.add(new Point(row + 1, column));
                    if(firstMove && board.get(row + 2).get(column) instanceof NullChess){
                        moves.add(new Point(row + 2, column));
                    }
                }
                if(!(board.get(row + 1).get(column + 1)instanceof NullChess) && !board.get(row + 1).get(column + 1).isBlack()){
                    moves.add(new Point(row + 1, column + 1));
                }

                if(!(board.get(row + 1).get(column - 1)instanceof NullChess) && !board.get(row + 1).get(column - 1).isBlack()){
                    moves.add(new Point(row + 1, column - 1));
                }
            }
        }
        //red's turn
        else{
            Log.d("Clicked","pawn");
            Log.d("Clicked",String.valueOf(getRow()));
            Log.d("Clicked",String.valueOf(getColumn()));

            if(column == 0){
                if(board.get(row - 1).get(column) instanceof NullChess) {
                    moves.add(new Point(row - 1, column));
                    if(firstMove && board.get(row - 2).get(column) instanceof NullChess){
                        moves.add(new Point(row - 2, column));
                    }
                }
                if(!(board.get(row - 1).get(column + 1)instanceof NullChess) && board.get(row - 1).get(column + 1).isBlack()){
                    moves.add(new Point(row - 1, column + 1));
                }

            }
            else if(column == 7) {
                if(board.get(row - 1).get(column) instanceof NullChess) {
                    moves.add(new Point(row - 1, column));
                    if(firstMove && board.get(row - 2).get(column) instanceof NullChess){
                        moves.add(new Point(row - 2, column));
                    }
                }
                if(!(board.get(row - 1).get(column - 1)instanceof NullChess) && board.get(row - 1).get(column - 1).isBlack()){
                    moves.add(new Point(row - 1, column - 1));
                }
            }
            else {
                if(board.get(row - 1).get(column) instanceof NullChess) {
                    moves.add(new Point(row - 1, column));
                    if(firstMove && board.get(row - 2).get(column) instanceof NullChess){
                        moves.add(new Point(row - 2, column));
                    }
                }
                if(!(board.get(row - 1).get(column + 1)instanceof NullChess) && board.get(row - 1).get(column + 1).isBlack()){
                    moves.add(new Point(row - 1, column + 1));
                }

                if(!(board.get(row - 1).get(column - 1)instanceof NullChess) &&board.get(row - 1).get(column - 1).isBlack()){
                    moves.add(new Point(row - 1, column - 1));
                }
            }
        }

        return moves;
    }
}
