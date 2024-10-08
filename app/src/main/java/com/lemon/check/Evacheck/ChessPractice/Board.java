package com.lemon.check.Evacheck.ChessPractice;

public class Board {
    Piece[][] board = new Piece[8][8];
    public Board(){
        setup();
    }







    public void setup(){
        //Black
        board[0][0] = new Rook(true,0,0);
        board[0][7] = new Rook(true,0,7);
        board[0][1] = new Knight(true,0,1);
        board[0][6] = new Knight(true,0,6);
        board[0][2] = new Bishop(true,0,3);
        board[0][5] = new Bishop(true,0,5);
        board[0][3] = new Queen(true,0,3);
        board[0][4] = new King(true,0,4);

        for(int i = 0; i < 8; i++){
            board[1][i] = new Pawn(true,1,i,true);
        }

        //White
        board[7][0] = new Rook(false,0,0);
        board[7][7] = new Rook(false,0,7);
        board[7][1] = new Knight(false,0,1);
        board[7][6] = new Knight(false,0,6);
        board[7][2] = new Bishop(false,0,3);
        board[7][5] = new Bishop(false,0,5);
        board[7][3] = new Queen(false,0,3);
        board[7][4] = new King(false,0,4);

        for(int i = 0; i < 8; i++){
            board[7][i] = new Pawn(false,7,i,true);
        }
    }
}
