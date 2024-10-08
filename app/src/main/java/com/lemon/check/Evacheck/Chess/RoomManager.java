package com.lemon.check.Evacheck.Chess;

import com.lemon.check.Evacheck.Player;

import java.util.ArrayList;

public class RoomManager {
    private static ArrayList<Room> roomList = new ArrayList<>();
    private static ArrayList<Integer> idList = new ArrayList<>();

    public RoomManager() {

    }

    public Room createRoom(Player player1) {
        int id = idGenerator();
        idList.add(id);
        Room room = new Room(id, player1);
        roomList.add(room);
        return room;
    }

    public void joinRoom(int id, Player player2){
        for(int i = 0; i < roomList.size(); i++){
            if(id == roomList.get(i).getId()){
                roomList.get(i).setPlayer2(player2);
                break;
            }
        }
    }

    public int idGenerator() {
        boolean generating = true;
        int min = 1;
        int max = 5000;
        int range = (5000 - 1) + 1;
        int random = (int) (Math.random() * range) + min;
        while (generating){
            if(roomList.size() < 0) {
                for (int i = 0; i < roomList.size(); i++) {
                    if (random == roomList.get(i).getId()) {
                        random = (int) (Math.random() * range) + min;
                        generating = false;
                        break;
                    }
                }
            }
            else{
                generating = false;
            }
        }
        return random;
    }

    public static ArrayList<Room> getRoomList() {
        return roomList;
    }

    public static void setRoomList(ArrayList<Room> roomList) {
        RoomManager.roomList = roomList;
    }

    public static ArrayList<Integer> getIdList() {
        return idList;
    }

    public static void setIdList(ArrayList<Integer> idList) {
        RoomManager.idList = idList;
    }

}
