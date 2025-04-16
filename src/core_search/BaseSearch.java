package core_search;

import search_problems.Square;

import java.util.*;

/**
 *  WARNING:
 *     1. This class will NOT work if S (i.e., the data type of states) is an array type (e.g., int[], String[], etc.)
 *     2. The data type of states must provide the correct equals method
 *
 *  An implementation of the general best-first search algorithm
 *
 *  To implement a specific search algorithm (such as BFS, DFS, etc.),
 *  extend this class and provide an implementation of the PriorityQueue
 *
 *  Type parameters:
 *      S:  the data type of states
 *      A: the data type of actions
 *  */

public class BaseSearch<S,A> {
    private final Problem<S,A> p;
    private final MyPriorityQueue<S,A> frontier;
    private final Map<S, Node<S,A>> reached = new HashMap<>();

    public BaseSearch(Problem<S, A> p, MyPriorityQueue<S, A> frontier) {
        this.p = p;
        this.frontier = frontier;
    }

    public ArrayList<S> search(){
        Node<S,A> start = new Node<>(p.initialState(), null, 0, null);
        frontier.add(start);
        reached.put(p.initialState(), start);
        while(!frontier.isEmpty()) {
            Node<S,A> node = frontier.pop();
            if(node.getState().equals(p.goalState())) {
                return printPath(node);
            }
            for(Tuple<S,A> t : p.execution(node.getState())) {
                Node<S,A> child = new Node<>(
                        t.getState(),
                        t.getAction(),
                        node.getPathCost() + t.getCost(),
                        node);
                if(!reached.containsKey(child.getState()) || (child.getPathCost() < reached.get(child.getState()).getPathCost())) {
                    reached.put(child.getState(), child);
                    frontier.add(child);
                }
            }
        }
        return null;
    }

    public ArrayList<S> printPath(Node<S,A> node){
        int pathCost = node.getPathCost();
        Stack<S> path = new Stack<>();
        do {
            path.add(node.getState());
            node = node.getParent();
        } while(node!=null);
        ArrayList<S> pathList = new ArrayList<>();
        System.out.println("Path (from initial state to goal state): ");
        while(!path.isEmpty()){
            S state = path.pop();
            pathList.add(state);
            p.printState(state);
            if(!path.isEmpty()) {
                System.out.println("\n\t   ↓\n");
            }
        }
        System.out.println("\nPath cost: "+pathCost);
        return pathList;
    }
}
