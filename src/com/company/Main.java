package com.company;

public class Main {

    public static void main(String[] args) {

        // Get file name for arguments
        //String filename = args[0];
        String[] filename = {"map1.txt", "map2.txt", "map3.txt"};

        Labyrinth labyrinth = new Labyrinth(filename[0]);

        System.out.println(labyrinth.getMap());

        if (labyrinth.checkEnd()) {
            System.out.println("The game is already ended.");
            return;
        }


        System.out.println(labyrinth.findSolution());
    }
}
