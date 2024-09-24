package com.lemon.check.checkesandchess.ChessPractice;

public class Point {
    private int row;
    private int column;

    public Point(int row, int column){
        this.row = row;
        this.column = column;
    }
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean equals(Point p){
        if(p.row == row && p.column == column){
            return true;
        }
        return false;
    }
}
