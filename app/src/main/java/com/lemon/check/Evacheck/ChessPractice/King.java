package com.lemon.check.Evacheck.ChessPractice;

//import Piece;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private List<Point> moves = new ArrayList<>();

    public King(Boolean isBlack, int row, int column){
        super(isBlack,row,column);
        this.row = row;
        this.column = column;

    }
    public int[][] getDirections(){
        int[][] x = {{1,1,1},{1,0,1},{1,1,1}};
        return x;
    }

    //*************************************************************************
    //This method will let us check if the king is in danger(Checked)
    //*************************************************************************
    public boolean isChecked(Piece board[][]) {
        //Check up
        for (int i = row + 1; i < 8; i++) {
            if (board[i][column] == null)
                continue;
            else if (board[i][column].isBlack() == this.isBlack())
                break;
            else {
                if ((board[i][column] instanceof Rook) || (board[i][column] instanceof Queen))
                    return true;
                else
                    break;
            }
        }

        //Check down
        for (int i = row - 1; i >= 0; i--) {
            if (board[i][column] == null)
                continue;
            else if (board[i][column].isBlack() == this.isBlack())
                break;
            else {
                if ((board[i][column] instanceof Rook) || (board[i][column] instanceof Queen))
                    return true;
                else
                    break;
            }
        }
        //check right
        for (int i = column + 1; i < 8; i++) {
            if (board[row][i] == null)
                continue;
            else if (board[row][i].isBlack() == this.isBlack())
                break;
            else {
                if ((board[row][i] instanceof Rook) || (board[row][i] instanceof Queen))
                    return true;
                else
                    break;
            }
        }
        //check left
        for (int i = column - 1; i >= 0; i--) {
            if (board[row][i] == null)
                continue;
            else if (board[row][i].isBlack() == this.isBlack())
                break;
            else {
                if ((board[row][i] instanceof Rook) || (board[row][i] instanceof Queen))
                    return true;
                else
                    break;
            }
        }


        //*************************************************************************
        //still needs to check diagonal, knights, kings, and pawns
        //*************************************************************************

        //Check right-down
        int tempColumn = column + 1;
        int tempRow = row - 1;
        while (tempColumn < 8 && tempRow >= 0) {
            if (board[tempColumn][tempRow] == null) {
                tempColumn++;
                tempRow--;
            } else if (board[tempColumn][tempRow].isBlack() == this.isBlack())
                break;
            else {
                if (board[tempColumn][tempRow] instanceof Bishop || board[tempColumn][tempRow] instanceof Queen)
                    return true;
                else
                    break;
            }
        }

        //check left-up
        tempColumn = column - 1;
        tempRow = row + 1;
        while (tempColumn >= 0 && tempRow < 8) {
            if (board[tempColumn][tempRow] == null) {
                tempColumn--;
                tempRow++;
            } else if (board[tempColumn][tempRow].isBlack() == this.isBlack())
                break;
            else {
                if (board[tempColumn][tempRow] instanceof Bishop || board[tempColumn][tempRow] instanceof Queen)
                    return true;
                else
                    break;
            }
        }

        //check left-down
        tempColumn = column - 1;
        tempRow = row - 1;
        while (tempColumn >= 0 && tempRow >= 0) {
            if (board[tempColumn][tempRow] == null) {
                tempColumn--;
                tempRow--;
            } else if (board[tempColumn][tempRow].isBlack() == this.isBlack())
                break;
            else {
                if (board[tempColumn][tempRow] instanceof Bishop || board[tempColumn][tempRow] instanceof Queen)
                    return true;
                else
                    break;
            }
        }

        //check right-up
        tempColumn = column + 1;
        tempRow = row + 1;
        while (tempColumn < 8 && tempRow < 8) {
            if (board[tempColumn][tempRow] == null) {
                tempColumn++;
                tempRow++;
            } else if (board[tempColumn][tempRow].isBlack() == this.isBlack())
                break;
            else {
                if (board[tempColumn][tempRow] instanceof Bishop || board[tempColumn][tempRow] instanceof Queen)
                    return true;
                else
                    break;
            }
        }

        //*************************************************************************
        //still needs to check knights and pawns
        //*************************************************************************

        int columnPos[] = {column + 1, column + 1, column + 2, column + 2, column - 1, column - 1, column - 2, column - 2};
        int rowPos[] = {row-2, row+2, row-1, row+1, row-2, row+2, row-1, row+1};
        for (int i = 0; i < 8; i++){
            if ((columnPos[i] >= 0 && columnPos[i] < 8 && rowPos[i] >= 0 && rowPos[i] < 8)) {
                if (board[columnPos[i]][rowPos[i]] != null && board[columnPos[i]][rowPos[i]].isBlack() != this.isBlack() && (board[columnPos[i]][rowPos[i]] instanceof Knight)) {
                    return true;
                }
            }
        }


        //*************************************************************************
        //pawns
        //*************************************************************************

        if(this.isBlack()){
            if((row + 1) < 8){
                if(column - 1 >= 0){
                    if(board[row+1][column - 1] != null){
                        if(board[row+1][column - 1] instanceof Pawn && !(board[row+1][column - 1].isBlack())){
                            return true;
                        }
                    }
                }
                if(column+1 < 8){
                    if(board[row+1][column + 1] != null){
                        if(board[row+1][column + 1] instanceof Pawn && !(board[row+1][column + 1].isBlack())){
                            return true;
                        }
                    }
                }
            }
        }
        else{
            if((row - 1) >= 0){
                if(column - 1 >= 0){
                    if(board[row+1][column - 1] != null){
                        if(board[row+1][column - 1] instanceof Pawn && (board[row+1][column - 1].isBlack())){
                            return true;
                        }
                    }
                }
                if(column+1 < 8){
                    if(board[row+1][column + 1] != null){
                        if(board[row+1][column + 1] instanceof Pawn && (board[row+1][column + 1].isBlack())){
                            return true;
                        }
                    }
                }
            }
        }



        return false;


    }

    @Override
    public List getMoves(Piece[][] board) {
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

    public void left(Piece[][] board){
        //left
        if(board[row][column - 1] == null || board[row][column - 1].isBlack() != this.isBlack()){
            moves.add(new Point(row, column - 1));
        }
    }

    public void upperLeft(Piece[][]board){
        //upper left
        if(board[row - 1][column - 1] == null || board[row - 1][column - 1].isBlack() != this.isBlack()){
            moves.add(new Point(row - 1, column - 1));
        }
    }

    public void up(Piece[][]board){
        //up
        if(board[row - 1][column] == null || board[row - 1][column].isBlack() != this.isBlack()){
            moves.add(new Point(row - 1, column));
        }
    }

    public void upperRight(Piece[][]board){
        //upper right
        if(board[row - 1][column + 1] == null || board[row - 1][column + 1].isBlack() != this.isBlack()){
            moves.add(new Point(row - 1, column + 1));
        }
    }

    public void right(Piece[][]board){
        //right
        if(board[row][column + 1] == null || board[row][column + 1].isBlack() != this.isBlack()){
            moves.add(new Point(row, column + 1));
        }
    }

    public void bottomRight(Piece[][]board){
        //bottom right
        if(board[row + 1][column + 1] == null || board[row + 1][column + 1].isBlack() != this.isBlack()){
            moves.add(new Point(row + 1, column + 1));
        }
    }
    public void bottom(Piece[][]board){
        //bottom
        if(board[row + 1][column] == null || board[row + 1][column].isBlack() != this.isBlack()){
            moves.add(new Point(row + 1, column));
        }
    }

    public void bottomLeft(Piece[][]board){
        //bottom left
        if(board[row + 1][column - 1] == null || board[row + 1][column - 1].isBlack() != this.isBlack()){
            moves.add(new Point(row + 1, column - 1));
        }
    }

}
