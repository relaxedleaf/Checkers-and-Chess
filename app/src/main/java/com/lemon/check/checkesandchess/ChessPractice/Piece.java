package com.lemon.check.checkesandchess.ChessPractice;

import java.util.List;

public abstract class Piece implements Cloneable {
    protected int moveDistance;
    protected int row;
    protected int column;
    //int[][] moveDirection = new int[3][3];
    protected boolean isBlack;
    protected boolean firstMove;


    public Piece(Boolean isBlack, int row, int column){
        //this.moveDistance = moveDistance;
        this.row = row;
        this.column = column;
        this.isBlack = isBlack;
    }

    public Piece(Boolean isBlack, int row, int column, Boolean firstMove){
        //this.moveDistance = moveDistance;
        this.row = row;
        this.column = column;
        this.isBlack = isBlack;
        this.firstMove = firstMove;
    }

    public boolean isBlack(){
        return isBlack;
    }

    public Piece getcopy() throws CloneNotSupportedException
    {
        return (Piece) this.clone();
    }

    public abstract List<Point> getMoves(Piece[][] board);

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }
    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }
}
