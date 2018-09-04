package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Labyrinth {

    private List<String> strLabyrinth = new ArrayList<>();
    private char[][] gridLabyrinth;
    private int nrows, ncols;

    // Flag
    private boolean solved = false;

    private char[] obstacles = {'#', '$', '*'};
    private char[] directions = {'U', 'L', 'D', 'R'};

    public Labyrinth(String filename) {
        // This will reference one line at a time
        String line;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(filename);

            // Wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                strLabyrinth.add(line);
            }

            // Close files
            bufferedReader.close();

            // Initialize grid
            nrows = strLabyrinth.size();
            ncols = strLabyrinth.get(0).length();

            System.out.println("Map dimensions: [" + nrows + ", " + ncols + "]");

            gridLabyrinth = new char[nrows][ncols];

            // From strings to char matrix
            for (int i = 0; i < nrows; i++) {
                String row = strLabyrinth.get(i);
                gridLabyrinth[i] = row.toCharArray();
//                for (int j = 0; j < ncols-1; j++) {
//                    gridLabyrinth[i][j] = row.charAt(j);
                //}
            }


        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            filename + "'");
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + filename + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }

    /***
     * Print the matrix of chars representing the map
     */
    public String getMap() {
        return String.join("\n", strLabyrinth);
    }

    /***
     * Print the matrix of chars representing the map
     */
    public void printGridLabyrinth() {
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                System.out.print(gridLabyrinth[i][j]);
            }
            System.out.println();
        }
    }

    /***
     * Checks if there is a "+" inside the map, so the Sokoban player is on goal
     * and the game is ended
     * @return True if ended
     */
    public boolean checkEnd() {

        for (String s : strLabyrinth) {
            boolean ended = s.contains("+");

            if (ended)
                return true;
        }
        return false;
    }


    /***
     *
     * @return A string with the final solution, if exists.
     */
    public String findSolution() {
        // Representing (x,y) coordinates of the player
        int[] playerPosition = findPlayerPosition();

        // Matrix for keeping track of which cell I've visited
        boolean[][] visited = new boolean[nrows][ncols];
        boolean finished = false;
        LinkedList<Character> path_solution = new LinkedList<Character>();

        System.out.println("Player found at position: " + Arrays.toString(playerPosition));

        rPath(visited, playerPosition[0], playerPosition[1], path_solution);

        return "";
    }


    private void rPath(boolean[][] visited, int current_row, int current_col, LinkedList<Character> current_solution) {
        visited[current_row][current_col] = true;

        //DEBUG
        System.out.println("I'm in [" + current_row + ", " + current_col + "]");

        if (noPossibleMoves(visited, current_row, current_col)) {
            visited[current_row][current_col] = false;
            return;
        }

        if (isGoal(current_row, current_col)) {
            solved = true;
            printSolution(current_solution);
            return;
        }


        for (char direction : directions) {
            if(solved)
                return;


            int destination_row = current_row + rowMove(direction);
            int destination_col = current_col + colMove(direction);

            //if accessible and not yet visited
            if (isAccessible(destination_row, destination_col) && !visited[destination_row][destination_col]) {
                // Add direction to the solution
                current_solution.add(direction);
                // Visit the destination
                rPath(visited, destination_row, destination_col, current_solution);
                // Remove direction from the solution
                current_solution.removeLast();

            }


        }

    }

    private void printSolution(LinkedList<Character> current_solution) {

        System.out.println(current_solution.stream().map(x -> x.toString()).collect(Collectors.joining(" ")));

    }

    /***
     *
     * @param direction A char -> {'U', 'D', 'L', 'R'}
     * @return Int, +1 if right, -1 if left, 0 if up or down
     */
    private int colMove(char direction) {

        if (direction == 'U' || direction == 'D')
            return 0;
        if (direction == 'R')
            return 1;
        return -1;
    }

    /***
     *
     * @param direction A char -> {'U', 'D', 'L', 'R'}
     * @return Int, +1 if up, -1 if down, 0 if right or left
     */
    private int rowMove(char direction) {

        if (direction == 'L' || direction == 'R')
            return 0;
        if (direction == 'U')
            return -1;
        return 1;
    }

    private boolean isAccessible(int row, int col) {

        //If the cell is out of the map, it is not accessible
        if (outOfMap(row, col))
            return false;

        //If the cell is blocked by an obstacle, it is not accessible
        for (char c : obstacles) {
            if (gridLabyrinth[row][col] == c) {
                //DEBUG
                System.out.println("Obstacle " + c + " in [" + row + ", " + col + "]");
                return false;
            }
        }

        return true;

    }

    private boolean isGoal(int row, int col) {
        return (gridLabyrinth[row][col] == '.');
    }

    private boolean noPossibleMoves(boolean[][] visited, int current_row, int current_col) {

        int blocked_directions = 0;

        // Check if above is blocked
        for (char direction : directions) {

            int destination_row = current_row + rowMove(direction);
            int destination_col = current_col + colMove(direction);

            if (!isAccessible(destination_row, destination_col) || visited[destination_row][destination_col])
                ++blocked_directions;

        }

        return (blocked_directions == 4);

    }

    private boolean outOfMap(int row, int col) {
        return (row < 0 || row >= nrows || col < 0 || col >= ncols);
    }


    private int[] findPlayerPosition() {
        int[] pos = new int[2];
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                if (gridLabyrinth[i][j] == '@') {
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }

}
