package com.lemon.check.checkesandchess.Checkers;


import com.lemon.check.checkesandchess.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private int id;
    private Player player1;
    private Player player2;
    private Boolean turn = true;
    private Boolean availability = true;
    private List<List<Checker>> checkerList;

    public Room(){
    }

    public Room(int id, Player player1){
        this.id = id;
        this.player1 = player1;
        this.checkerList = new ArrayList<List<Checker>>();
        Checker nullc = new NullChecker();
        Checker [][] checkerArray = new Checker[][]
                        {{nullc, new RedChecker(0,1), nullc, new RedChecker(0,3), nullc, new RedChecker(0,5), nullc, new RedChecker(0,7)},
                        {new RedChecker(1,0), nullc, new RedChecker(1,2),nullc, new RedChecker(1,4), nullc, new RedChecker(1,6), nullc},
                        {nullc, new RedChecker(2,1), nullc, new RedChecker(2,3), nullc, new RedChecker(2,5), nullc, new RedChecker(2,7)},
                        {nullc, nullc, nullc, nullc,nullc, nullc,nullc, nullc},
                        {nullc, nullc, nullc, nullc,nullc, nullc,nullc, nullc},
                        {new BlackChecker(5,0), nullc, new BlackChecker(5,2),nullc, new BlackChecker(5,4), nullc, new BlackChecker(5,6), nullc},
                        {nullc, new BlackChecker(6,1), nullc, new BlackChecker(6,3), nullc, new BlackChecker(6,5), nullc, new BlackChecker(6,7)},
                        {new BlackChecker(7,0), nullc, new BlackChecker(7,2),nullc, new BlackChecker(7,4), nullc, new BlackChecker(7,6), nullc}
                        };

        for(int r = 0; r < checkerArray.length; r++) {
            List<Checker> row = new ArrayList<>();
            for (int c = 0; c < checkerArray[r].length; c++) {
                row.add(checkerArray[r][c]);
            }
            this.checkerList.add(row);
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


    public Boolean getTurn() {
        return turn;
    }

    public void setTurn(Boolean turn) {
        this.turn = turn;
    }

    public List<List<Checker>> getCheckerList() {
        return checkerList;
    }

    public void setCheckerList(List<List<Checker>> checkerList) {
        this.checkerList = checkerList;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

}
