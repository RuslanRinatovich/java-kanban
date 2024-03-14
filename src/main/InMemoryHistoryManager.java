package main;

import java.util.*;

import main.models.Node;
import main.models.Task;


public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    private int size = 0;
    private final Map<Integer, Node> historyTask = new HashMap<>();


    @Override
    public void add(Task task) {

        if (historyTask.containsKey(task.getId())) {
            int id = task.getId();
            removeNode(historyTask.get(id));
            remove(id);
        }
        historyTask.put(task.getId(), linkLast(task));
    }

    @Override
    public Node getHead() {
        return head;
    }

    @Override
    public Node getTail() {
        return tail;
    }

    @Override
    public void remove(int id) {
        historyTask.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public Node linkLast(Task element) {

        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, element, null);
        if (size == 0)
            head = newNode;
        tail = newNode;
        if (oldTail != null)
            oldTail.next = newNode;
        size++;
        return newNode;
    }

    public List<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    public void removeNode(Node node) {
        // в списке один элемент
        if (size == 1) {
            head = null;
            tail = null;
        } else {
            if (node.prev == null) {
                node.next.prev = null;
                head = node.next;
            } else if (node.next == null) {
                node.prev.next = null;
                tail = node.prev;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
        }
        size--;
    }

    public int size() {
        return this.size;
    }
}
