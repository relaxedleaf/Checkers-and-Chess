package com.lemon.check.Evacheck.Chess;

import com.lemon.check.Evacheck.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private int id;
    private Player player1;
    private Player player2;
    private boolean isBlacksTurn = false;
    private Boolean availability = true;
    private List<List<Piece>> chessList;

    public Room(){
    }

    public Room(int id, Player player1){
        this.id = id;
        this.player1 = player1;
        this.chessList = new ArrayList<List<Piece>>();
        Piece nullc = new NullChess();
        Piece [][] chessArray = new Piece[][]
                        {{new Rook(true,0,0), new Knight(true,0,1), new Bishop(true,0,2), new Queen(true,0,3), new King(true,0,4), new Bishop(true,0,5), new Knight(true,0,6), new Rook(true,0,7)},
                        {new Pawn(true,1,0,true), new Pawn(true,1,1,true), new Pawn(true,1,2,true),new Pawn(true,1,3,true), new Pawn(true,1,4,true), new Pawn(true,1,5,true), new Pawn(true,1,6,true), new Pawn(true,1,7,true)},
                        {nullc, nullc, nullc, nullc,nullc, nullc,nullc, nullc},
                        {nullc, nullc, nullc, nullc,nullc, nullc,nullc, nullc},
                        {nullc, nullc, nullc, nullc,nullc, nullc,nullc, nullc},
                        {nullc, nullc, nullc, nullc,nullc, nullc,nullc, nullc},
                        {new Pawn(false,6,0,true), new Pawn(false,6,1,true), new Pawn(false,6,2,true), new Pawn(false,6,3,true), new Pawn(false,6,4,true), new Pawn(false,6,5,true), new Pawn(false,6,6,true), new Pawn(false,6,7,true)},
                        {new Rook(false,7,0), new Knight(false,7,1), new Bishop(false,7,2),new Queen(false,7,3), new King(false,7,4), new Bishop(false,7,5), new Knight(false,7,6), new Rook(false,7,7)}
                        };

        for(int r = 0; r < chessArray.length; r++) {
            List<Piece> row = new ArrayList<>();
            for (int c = 0; c < chessArray[r].length; c++) {
                row.add(chessArray[r][c]);
            }
            this.chessList.add(row);
        }

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public List<List<Piece>> getChessList() {
        return chessList;
    }

    public void setCheckerList(List<List<Piece>> checkerList) {
        this.chessList = checkerList;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public boolean isBlacksTurn() {
        return isBlacksTurn;
    }

    public void setBlacksTurn(boolean blacksTurn) {
        isBlacksTurn = blacksTurn;
    }

}
