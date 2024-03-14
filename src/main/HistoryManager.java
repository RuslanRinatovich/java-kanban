package main;

import main.models.Node;
import main.models.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

    Node getTail();

    Node getHead();
}
