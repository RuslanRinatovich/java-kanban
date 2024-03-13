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

        if (historyTask.containsKey(task.getId()))
        {
            removeNode(historyTask.get(task.getId()));
        }
        linkLast(task);
        historyTask.put(task.getId(), tail);
        //historyTask2.linkLast(task);
    }

    @Override
    public void remove(int id) {

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
        if (oldTail == null)
            tail = newNode;
        else
            oldTail.next = newNode;
        size++;
        return newNode;
    }

    public List<Task> getTasks()
    {
        ArrayList<Task> result = new ArrayList<>();
        Node current = head;
        while (current != null)
        {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    public void removeNode(Node node)
    {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = null;
        node.next = null;
    }


    public int size() {
        return this.size;
    }





}
