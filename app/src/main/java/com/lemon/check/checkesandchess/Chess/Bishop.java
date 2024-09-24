package com.lemon.check.checkesandchess.Chess;

//import Piece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece implements Serializable {
    public Bishop(Boolean isBlack, int row, int column) {
        super(isBlack, row, column, "Bishop");
    }

    @Override
    public List getMoves(List<List<Piece>> board) {
        List<Point> moves = new ArrayList<>();

        //up right
        int tempRow = row;
        int tempColumn = column;
        for (;;){
            tempRow++;
            tempColumn++;
            if(tempRow >= 0 && tempRow < 8 && tempColumn >= 0 && tempColumn < 8){
                if(board.get(tempRow).get(tempColumn) instanceof NullChess) {
                    moves.add(new Point(tempRow, tempColumn));
                }
                else if(board.get(tempRow).get(tempColumn).isBlack() != this.isBlack()){
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
                if(board.get(tempRow).get(tempColumn) instanceof NullChess) {
                    moves.add(new Point(tempRow, tempColumn));
                }
                else if(board.get(tempRow).get(tempColumn).isBlack() != this.isBlack()){
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
                if(board.get(tempRow).get(tempColumn) instanceof NullChess) {
                    moves.add(new Point(tempRow, tempColumn));
                }
                else if(board.get(tempRow).get(tempColumn).isBlack() != this.isBlack()){
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
                if(board.get(tempRow).get(tempColumn) instanceof NullChess) {
                    moves.add(new Point(tempRow, tempColumn));
                }
                else if(board.get(tempRow).get(tempColumn).isBlack() != this.isBlack()){
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
