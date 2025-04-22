package search_problems;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.List;

public class maze {

    static class Cell {
        int x, y;
        boolean visited = false;
        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Edge {
        Cell from, to;
        int weight;
        Edge(Cell from, Cell to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    private final int width, height;
    private final Cell[][] grid;
    private final Random rand = new Random();
    private final int[][] mazeGrid; // 1 = wall, 0 = path

    public maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height][width];
        this.mazeGrid = new int[height * 2 + 1][width * 2 + 1]; // visual grid

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                grid[y][x] = new Cell(x, y);

        for (int[] row : mazeGrid)
            Arrays.fill(row, 1);
    }

    public void generateMaze() {
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        Cell start = grid[0][rand.nextInt(width)];
        start.visited = true;

        carve(start);

        for (Cell neighbor : getNeighbors(start))
            edgeQueue.add(new Edge(start, neighbor, rand.nextInt(100)));

        while (!edgeQueue.isEmpty()) {
            Edge edge = edgeQueue.poll();
            if (edge.to.visited) continue;

            edge.to.visited = true;
            carve(edge.to);
            carveWallBetween(edge.from, edge.to);

            for (Cell neighbor : getNeighbors(edge.to)) {
                if (!neighbor.visited) {
                    edgeQueue.add(new Edge(edge.to, neighbor, rand.nextInt(100)));
                }
            }
        }

        // Carve entrance (top row) and exit (bottom row)
        mazeGrid[0][start.x * 2 + 1] = 0; // entry at top


        List<Cell> bottomRowVisited = new ArrayList<>();
        for(int i = 0; i < width; i++) {
            Cell candidate = grid[height-1][i];
            if(candidate.visited){
                bottomRowVisited.add(candidate);
            }
        }
        if (!bottomRowVisited.isEmpty()) {
            Cell end = bottomRowVisited.get(rand.nextInt(bottomRowVisited.size()));
            mazeGrid[height * 2][end.x * 2 + 1] = 0;
        }
    }

    private void carve(Cell cell) {
        mazeGrid[cell.y * 2 + 1][cell.x * 2 + 1] = 0;
    }

    private void carveWallBetween(Cell a, Cell b) {
        int wallX = a.x + b.x + 1;
        int wallY = a.y + b.y + 1;
        mazeGrid[wallY][wallX] = 0;
    }

    private List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[][] dirs = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
        for (int[] d : dirs) {
            int nx = cell.x + d[0];
            int ny = cell.y + d[1];
            if (nx >= 0 && ny >= 0 && nx < width && ny < height) {
                neighbors.add(grid[ny][nx]);
            }
        }
        return neighbors;
    }

    public void printMaze() {
        for (int[] ints : mazeGrid) {
            for (int anInt : ints) {
                System.out.print(anInt);
            }
            System.out.println();
        }
    }


    public void writeMaze() {
        File file = new File("testCases/TestMaze.txt");
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (int[] ints : mazeGrid) {
                for (int anInt : ints) {
                    fileWriter.write(Integer.toString(anInt));
                }
                fileWriter.write("\n");
            }
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (!file.exists() || file.length() == 0) {
            try {
                Thread.sleep(10); // short delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void initializeWindow(int[][] grid) {
        JFrame mainFrame = new JFrame("Maze Solver");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new GridLayout(grid.length, grid[0].length));// avoid null layouts
        mainFrame.setLocationRelativeTo(null);

        for (int[] ints : grid) {
            for (int col = 0; col < grid[0].length; col++) {
                JLabel label = makeLabel(ints[col]);
                mainFrame.add(label);
            }
        }
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private JLabel makeLabel(int cellValue) {
        JLabel label= new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(40, 40));
        if (cellValue == 1) {
            label.setBackground(Color.BLACK);
        }
        else if(cellValue == 0){
            label.setBackground(Color.WHITE);
        }
        else{
            label = new JLabel(new ImageIcon("path.png"));
        }
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        return label;
    }
    public int[][] getMazeGrid() {
        return mazeGrid;
    }

    public static void main(String[] args) {
        maze generator = new maze(4,4);
        generator.generateMaze();
        generator.initializeWindow(generator.mazeGrid);
        generator.writeMaze();
    }
}
