package main;

import main.models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> historyTask = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (historyTask.size() == 10)
            historyTask.remove(0);
        historyTask.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyTask;
    }
}
