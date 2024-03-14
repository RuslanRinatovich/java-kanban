package main;

import main.models.Node;
import main.models.Task;

import java.util.List;

public interface HistoryManager {
    static final int HISTORY_SIZE = 10;

    void add(Task task);
    void remove(int id);
    List<Task> getHistory();

    Node getTail();
    Node getHead();
}
