package search_problems;

import core_search.Problem;
import core_search.Tuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class mazeProblem implements Problem<Square,String> {
    private int[][] mazeGrid;
    private final Map<Square, List<Tuple<Square, String>>> transitionModel = new HashMap<>();

    public mazeProblem(int [][] mazeGrid) {
        this.mazeGrid = mazeGrid;
        buildTransitionModel(initialState());

    }
    protected int[][] buildGrid(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("testCases/" + fileName))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            int rowCount = 0;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                rowCount++;
            }
            int colCount = stringBuilder.length() / rowCount;
            int[][] mazeGrid = new int[rowCount][colCount];
            String mazeString = stringBuilder.toString();
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    char ch = mazeString.charAt(i * colCount + j);
                    mazeGrid[i][j] = ch - '0';
                }
            }
            return mazeGrid;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return null;
    }

    private void buildTransitionModel(Square state) {
        List<Tuple<Square, String>> Transitions = new ArrayList<>();
        ArrayList<Square> neighbors = getNeighbors(state);
        Set<Square> seenStates = new HashSet<>();

        for(Square neighbor : neighbors) {
            if(seenStates.add(neighbor)) {
                Transitions.add(new Tuple<>(neighbor, getAction(state, neighbor), 1));
            }
        }
        transitionModel.put(state, Transitions);
    }
    private ArrayList<Square> getNeighbors(Square state) {
        ArrayList<Square> neighbors = new ArrayList<>();
        int row = state.row();
        int col = state.column();

        // Up
        if (row > 0 && mazeGrid[row - 1][col] == 0) {
            neighbors.add(new Square(row - 1, col));
        }
        // Down
        if (row < mazeGrid.length - 1 && mazeGrid[row + 1][col] == 0) {
            neighbors.add(new Square(row + 1, col));
        }
        // Left
        if (col > 0 && mazeGrid[row][col - 1] == 0) {
            neighbors.add(new Square(row, col - 1));
        }
        // Right
        if (col < mazeGrid[0].length - 1 && mazeGrid[row][col + 1] == 0) {
            neighbors.add(new Square(row, col + 1));
        }

        return neighbors;
    }
    private String getAction(Square state, Square neighbor) {
        if(state.row() - neighbor.row() == -1) {
            return "Down";
        }
        if(state.row() - neighbor.row() == 1) {
            return "Up";
        }
        if(state.column() - neighbor.column() == -1) {
            return "Right";
        }
        if(state.column() - neighbor.column() == 1) {
            return "Left";
        }
        return null;
    }
    public int getEstimatedDistance(Square state){
        //manhattan distance
        return Math.abs(state.row() - goalState().row()) + Math.abs(state.column() - goalState().column());
    }

    public Square initialState() {
        for(int i = 0; i < mazeGrid[0].length; i++) {
            if(mazeGrid[0][i] == 0) {
                return new Square(0, i);
            }
        }
        return null;
    }

    public Square goalState() {
        for(int i = 0; i < mazeGrid[0].length; i++) {
            if(mazeGrid[mazeGrid.length-1][i] == 0) {
                return new Square(mazeGrid.length-1, i);
            }
        }
        return null;
    }

    public int[][] getMazeGrid() {
        return mazeGrid;
    }
    public void setMazeGrid(int[][] mazeGrid){
        this.mazeGrid = mazeGrid;
    }

    public void changePathInGrid(ArrayList<Square> path) {
        for(Square node : path) {
            mazeGrid[node.row()][node.column()] = 3;
        }
    }

    public List<Tuple<Square, String>> execution(Square state) {
        buildTransitionModel(state);
        return transitionModel.get(state);
    }

    public void printState(Square state) {
        System.out.printf("Row: %d Column: %d\n\n", state.row(), state.column());
    }



    public static void main(String[] args){
        maze maze = new maze(5, 5);
        maze.generateMaze();

        int[][] grid = maze.getMazeGrid();
        mazeProblem S = new mazeProblem(grid);
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<Square,List<Tuple<Square, String>>> entry : S.transitionModel.entrySet()){
            builder.append(!builder.isEmpty() ? "\n" : "")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue());
        }
        System.out.println(builder);

    }
}
