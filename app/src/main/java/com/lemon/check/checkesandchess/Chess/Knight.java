package com.lemon.check.checkesandchess.Chess;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece implements Serializable {
    public Knight(Boolean isBlack, int row, int column) {
        super(isBlack, row, column, "Knight");
    }

    @Override
    public List<Point> getMoves(List<List<Piece>> board) {
        List<Point> moves = new ArrayList<>();

        moves.add(new Point(row+2,column+1));
        moves.add(new Point(row+1,column+2));

        moves.add(new Point(row-2,column+1));
        moves.add(new Point(row-1,column+2));

        moves.add(new Point(row+2,column-1));
        moves.add(new Point(row+1,column-2));

        moves.add(new Point(row-2,column-1));
        moves.add(new Point(row-1,column-2));

        for(int i = (moves.size()-1); i >= 0 ; i--){
            if(moves.get(i).getRow() < 0 || moves.get(i).getRow() >= 8 || moves.get(i).getColumn() < 0 || moves.get(i).getColumn() >= 8){
                moves.remove(i);
            }
            else if(!(board.get(moves.get(i).getRow()).get(moves.get(i).getColumn())instanceof NullChess)){
                if(board.get(moves.get(i).getRow()).get(moves.get(i).getColumn()).isBlack() == this.isBlack()) {
                    moves.remove(i);
                }
            }
        }

        return moves;
    }
}
