package search_solutions;

import core_search.BaseSearch;
import core_search.Node;
import core_search.SortedQueue;
import search_problems.Square;
import search_problems.maze;
import search_problems.mazeProblem;


import java.util.ArrayList;
import java.util.Comparator;

public class mazeAstar extends BaseSearch<Square, String> {
    public mazeAstar(mazeProblem problem) {
        super(problem, new SortedQueue<>(new CalculateHeuristic(problem)));
    }

    public static void main(String[] args) {

        maze maze = new maze(3, 3);
        maze.generateMaze();
        maze.writeMaze();

        mazeProblem m = new mazeProblem(maze.getMazeGrid());
        m.setMazeGrid(maze.getMazeGrid());
        maze.initializeWindow(maze.getMazeGrid());

        mazeAstar t = new mazeAstar(m);

        double startTime = System.nanoTime();
        ArrayList<Square> pathList = t.search();
        double endTime = System.nanoTime();

        m.changePathInGrid(pathList);
        maze.initializeWindow(m.getMazeGrid());

        System.out.printf(
                "-------------------------\nTime taken: %.3f secs\n-------------------------",
                (endTime - startTime) / 1_000_000_000
        );
    }

    @SuppressWarnings("ClassCanBeRecord")
    public static class CalculateHeuristic implements Comparator<Node<Square, String>> {
        public final mazeProblem problem;
        public CalculateHeuristic(mazeProblem problem) {
            this.problem = problem;
        }
        public int compare(Node<Square, String> o1, Node<Square, String> o2) {
            return Integer.compare(problem.getEstimatedDistance(o1.getState()) + o1.getPathCost(), problem.getEstimatedDistance(o2.getState()) + o2.getPathCost());
        }
    }
}
