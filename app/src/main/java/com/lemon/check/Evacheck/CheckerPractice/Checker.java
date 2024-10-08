package com.lemon.check.Evacheck.CheckerPractice;

import java.util.ArrayList;

public class Checker {
    private int row;
    private int column;
    private boolean crownStatus;

    public Checker(int row, int column){
        this.row = row;
        this.column = column;
        this.crownStatus = false;
    }

    public Checker(Checker checker){
        this.row = checker.getRow();
        this.column = checker.getColumn();
        this.crownStatus = checker.isCrownStatus();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isCrownStatus() {
        return crownStatus;
    }

    public void setCrownStatus(boolean crownStatus) {
        this.crownStatus = crownStatus;
    }

    public ArrayList<int[]> getMove(Checker[][] checkerList) {
        return null;
    }

    public ArrayList<int[]> getMove2(Checker[][] checkerList) {
        return null;
    }

    public ArrayList<int[]> getKillList() {
        return null;
    }
}
