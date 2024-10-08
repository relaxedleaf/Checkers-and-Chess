package com.lemon.check.Evacheck.Chess;

//import Piece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece implements Serializable {
    private List<Point> moves = new ArrayList<>();

    public King(Boolean isBlack, int row, int column){
        super(isBlack,row,column,"King");
        this.row = row;
        this.column = column;

    }

    @Override
    public List getMoves(List<List<Piece>> board) {
        moves.clear();
        moves = new ArrayList<>();
        if(column == 0){
            if(row == 0){
                right(board);
                bottomRight(board);
                bottom(board);
            }
            else if(row == 7){
                up(board);
                upperRight(board);
                right(board);
            }
            else{
                up(board);
                upperRight(board);
                right(board);
                bottomRight(board);
                bottom(board);
            }
        }
        else if(column == 7){
            if(row == 0){
                left(board);
                bottomLeft(board);
                bottom(board);
            }
            else if(row == 7){
                up(board);
                upperLeft(board);
                left(board);
            }
            else{
                up(board);
                upperLeft(board);
                left(board);
                bottomLeft(board);
                bottom(board);
            }
        }
        else{
            if(row == 0){
                left(board);
                bottomLeft(board);
                bottom(board);
                bottomRight(board);
                right(board);
            }
            else if(row == 7){
                left(board);
                upperLeft(board);
                up(board);
                upperRight(board);
                right(board);

            }
            else{
                left(board);
                upperLeft(board);
                up(board);
                upperRight(board);
                right(board);
                bottomLeft(board);
                bottomRight(board);
                bottom(board);
                bottomLeft(board);
            }
        }



        return moves;
    }

    public void left(List<List<Piece>> board){
        //left
        if(board.get(row).get(column - 1) instanceof NullChess || board.get(row).get(column - 1).isBlack() != this.isBlack()){
            moves.add(new Point(row, column - 1));
        }
    }

    public void upperLeft(List<List<Piece>> board){
        //upper left
        if(board.get(row - 1).get(column - 1) instanceof NullChess || board.get(row - 1).get(column - 1).isBlack() != this.isBlack()){
            moves.add(new Point(row - 1, column - 1));
        }
    }

    public void up(List<List<Piece>> board){
        //up
        if(board.get(row - 1).get(column) instanceof NullChess || board.get(row - 1).get(column).isBlack() != this.isBlack()){
            moves.add(new Point(row - 1, column));
        }
    }

    public void upperRight(List<List<Piece>> board){
        //upper right
        if(board.get(row - 1).get(column + 1) instanceof NullChess || board.get(row - 1).get(column + 1).isBlack() != this.isBlack()){
            moves.add(new Point(row - 1, column + 1));
        }
    }

    public void right(List<List<Piece>> board){
        //right
        if(board.get(row).get(column + 1) instanceof NullChess || board.get(row).get(column + 1).isBlack() != this.isBlack()){
            moves.add(new Point(row, column + 1));
        }
    }

    public void bottomRight(List<List<Piece>> board){
        //bottom right
        if(board.get(row + 1).get(column + 1) instanceof NullChess || board.get(row + 1).get(column + 1).isBlack() != this.isBlack()){
            moves.add(new Point(row + 1, column + 1));
        }
    }
    public void bottom(List<List<Piece>> board){
        //bottom
        if(board.get(row + 1).get(column) instanceof NullChess || board.get(row + 1).get(column).isBlack() != this.isBlack()){
            moves.add(new Point(row + 1, column));
        }
    }

    public void bottomLeft(List<List<Piece>> board){
        //bottom left
        if(board.get(row + 1).get(column - 1) instanceof NullChess || board.get(row + 1).get(column - 1).isBlack() != this.isBlack()){
            moves.add(new Point(row + 1, column - 1));
        }
    }

}
