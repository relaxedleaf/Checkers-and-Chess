package com.lemon.check.checkesandchess;

import java.util.ArrayList;

public class PlayerManager {
    private Boolean duplicateUsername = true;
    private ArrayList <Player> playerList = new ArrayList<>();
    private Player signedInPlayer = new Player();

    private ArrayList <String> usernameList = new ArrayList<>();

    public PlayerManager(){

    }
    public Player createPlayer(String username, String email){
        Player player = new Player(username, email);
        playerList.add(player);
        usernameList.add(username);

        return player;
    }

    public ArrayList<Player> getPlayerList(){
        return playerList;
    }



    public Player getSignedInPlayer(String email){
        for(int i = 0; i < playerList.size(); i++){
            if(playerList.get(i).getEmail().equals(email)){
                signedInPlayer.setEmail(playerList.get(i).getEmail());
                signedInPlayer.setUsername(playerList.get(i).getUsername());
                break;
            }
        }
        return signedInPlayer;
    }

    public boolean getDuplicateUsername(String username){
        if(usernameList.size() == 0){
            duplicateUsername = false;
        }
        else {
            for (int i = 0; i < usernameList.size(); i++) {
                if (usernameList.get(i).equals(username)) {
                    duplicateUsername = true;
                    break;
                } else {
                    duplicateUsername = false;
                }
            }
        }
        return duplicateUsername;
    }

    public ArrayList<String> getUsernameList() {
        return usernameList;
    }

    public void setUsernameList(ArrayList<String> usernameList) {
        this.usernameList = usernameList;
    }

}
