package com.example.guanghuili.checkesandchess.Chess;

public class Point {
    int row;
    int column;

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

    public boolean equals(Point p){
        if(p.row == row && p.column == column){
            return true;
        }
        return false;
    }
}
