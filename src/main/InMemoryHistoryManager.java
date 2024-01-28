package main;

import main.models.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> historyTask = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyTask.size() == HISTORY_SIZE)
            historyTask.remove(0);
        historyTask.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyTask;
    }
}
