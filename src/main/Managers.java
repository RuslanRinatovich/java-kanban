package main;

public class Managers {

    public static TaskManager getDefault() {return new FileBackedTaskManager(getDefaultHistory(), "tasks.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }



}
