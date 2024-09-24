package com.lemon.check.checkesandchess.Chess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece implements Serializable {
    public Queen(Boolean isBlack, int row, int column) {
        super(isBlack, row, column,"Queen");
    }

    @Override
    public List<Point> getMoves(List<List<Piece>> board) {
        List<Point> moves = new ArrayList<>();


        int tempRow = row;
        int tempColumn = column;
        //right
        for(;;){
            tempColumn++;
            if(tempColumn >= 0 && tempColumn < 8){
                if(board.get(tempRow).get(tempColumn) instanceof NullChess){
                    moves.add(new Point(tempRow,tempColumn));
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


        //Left moves
        tempRow = row;
        tempColumn = column;
        for(;;){
            tempColumn--;
            if(tempColumn >= 0 && tempColumn < 8){
                if(board.get(tempRow).get(tempColumn) instanceof NullChess){
                    moves.add(new Point(tempRow,tempColumn));
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

        //up moves
        tempRow = row;
        tempColumn = column;
        for(;;){
            tempRow++;
            if(tempRow >= 0 && tempRow < 8){
                if(board.get(tempRow).get(tempColumn) instanceof NullChess){
                    moves.add(new Point(tempRow,tempColumn));
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


        //down moves
        tempRow = row;
        tempColumn = column;
        for(;;){
            tempRow--;
            if(tempRow >= 0 && tempRow < 8){
                if(board.get(tempRow).get(tempColumn) instanceof NullChess){
                    moves.add(new Point(tempRow,tempColumn));
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


        //up right
        tempRow = row;
        tempColumn = column;
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
