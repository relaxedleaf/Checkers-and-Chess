package com.lemon.check.checkesandchess.ChessPractice;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Boolean isBlack, int row, int column) {
        super(isBlack, row, column);
    }

    @Override
    public List<Point> getMoves(Piece[][] board) {
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
            else if(board[moves.get(i).getRow()][moves.get(i).getColumn()]!= null){
                if(board[moves.get(i).getRow()][moves.get(i).getColumn()].isBlack() == this.isBlack()) {
                    moves.remove(i);
                }
            }
        }

        return moves;
    }
}
