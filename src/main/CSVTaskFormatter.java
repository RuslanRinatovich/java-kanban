package main;

import main.models.*;

import java.util.List;

public class CSVTaskFormatter {

    static FileBackedTaskManager loadFromFile(File file)
    {

    }

    static String makeDataToSave(List<Task> tasks, HistoryManager historyManager) {

        StringBuilder history = new StringBuilder();
        history.append("id,type,name,status,description,epic");

        for (var task:tasks) {
            history.append(task.toStringForFile()).append("\n");
        }
        history.append("\n");
        history.append(historyToString(historyManager));
        return history.toString();
    }


    static String historyToString(HistoryManager manager){
        String result = "";
        for (var task: manager.getHistory()) {
            result =result +"," + task.getId();
        }
        return result;
    }

    static List<Integer> historyFromString(String value)
    {


    }


    public Task fromString(String value) {
        //в качестве результата создает таск определенного типа
        String[] data = value.split(",");
        TaskType type = TaskType.valueOf(data[1]);
        switch (type) {
            case TASK: {
                int id = Integer.parseInt(data[0]);
                String title = data[2];
                Status status = Status.valueOf(data[3]);
                String description = data[4];
                return new Task(title, description, id, status);
            }
            case SUBTASK: {
                int id = Integer.parseInt(data[0]);
                String title = data[2];
                Status status = Status.valueOf(data[3]);
                String description = data[4];
                int epicId = Integer.parseInt(data[5]);

                return new Subtask(title, description, id, status, epicId);
            }
            case EPIC:
            {
                int id = Integer.parseInt(data[0]);
                String title = data[2];
                Status status = Status.valueOf(data[3]);
                String description = data[4];
                return new Epic(title, description, id, status, null);
            }
        }
        return null;
    }
}
