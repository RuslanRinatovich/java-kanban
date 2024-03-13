package main;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import main.models.Node;
import main.models.Task;
import java.util.LinkedList;
import java.util.List;




public class InMemoryHistoryManager implements HistoryManager {



    //private final List<Task> historyTask = new LinkedList<>();

    private final HandMadeLinkedList historyTask = new HandMadeLinkedList();

    @Override
    public void add(Task task) {

        historyTask.linkLast(task);
        //historyTask2.linkLast(task);
    }

    @Override
    public void remove(int id) {

    }
    @Override
    public List<Task> getHistory() {
        return historyTask.getTasks();
    }

    static class HandMadeLinkedList {
        /**
         * Указатель на первый элемент списка. Он же first
         */
        private Node head;
        /**
         * Указатель на последний элемент списка. Он же last
         */
        private Node tail;
        private int size = 0;


        public void linkLast(Task element) {

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
}
