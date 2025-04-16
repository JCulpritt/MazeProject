package core_search;

/** A generic implementation of the search tree node
 *
 *  Type parameters:
 *  S: the data type of states
 *  A: the data type of actions
 */
public class Node<S, A> {
    private final S STATE;
    private final A ACTION;
    private final int PATHCOST;
    private final Node<S,A> PARENT;

    public Node(S state, A action, int pathCost, Node<S,A> parent) {
        this.STATE = state;
        this.ACTION = action;
        this.PATHCOST = pathCost;
        this.PARENT = parent;
    }

    public S getState() {
        return STATE;
    }

    public A getAction() {
        return ACTION;
    }

    public int getPathCost() {
        return PATHCOST;
    }

    public Node<S,A> getParent() {
        return PARENT;
    }

    @Override
    public String toString() {
        return "{" +
                STATE +
                ", " + ACTION +
                ", " + PATHCOST +
                ", " + PARENT +
                '}';
    }
}
