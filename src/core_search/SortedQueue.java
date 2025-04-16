package core_search;
import java.util.*;

public class SortedQueue<S,A> implements MyPriorityQueue<S,A> {
    private final PriorityQueue<Node<S,A>> queue;

    public SortedQueue(Comparator<Node<S,A>> c) {
        this.queue = new PriorityQueue<>(c);
    }
    public Node<S, A> pop() {
        return queue.poll();
    }

    public void add(Node<S,A> e) {
        queue.add(e);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

}
