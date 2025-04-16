package core_search;

/** this interface represents a generic PriorityQueue
 *
 *  Type parameters:
 *      S: the data type of states
 *      A: the data type of actions
 */
public interface MyPriorityQueue<S,A> {
    Node<S,A> pop();

    void add(Node<S,A> e);

    boolean isEmpty();

}
