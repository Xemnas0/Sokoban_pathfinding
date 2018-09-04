package com.company;

public class Main {

    public static void main(String[] args) {

        // Get file name for arguments
        //String filename = args[0];
        String[] filename = {"map1.txt", "map2.txt", "map3.txt", "map4.txt"};

        Labyrinth labyrinth = new Labyrinth(filename[2]);

        System.out.println(labyrinth.getMap());

        // If a '+' element has been found, the player is already on goal, and the game is ended
        if (labyrinth.checkEnd()) {
            System.out.println("");
            return;
        }


        System.out.println(labyrinth.findSolution());
    }
}
