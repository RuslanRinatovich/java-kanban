package main;

import main.models.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static main.Managers.getDefaultHistory;

public class CSVTaskFormatter {

    // генерация FileBackedTaskManager из файла
    static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        try {
            // считываем целиком текст из файла в строковую переменную
            String data = Files.readString(file.toPath());
            // создаем на основе файла массив строк
            String[] lines = data.split("\n");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), "tasks.csv");
            int index = -1;
            for (String line : lines) {
                // если строка пустая, значит мы завершили чтение тасков
                index++;
                if (line.isEmpty() || line.isBlank()) {
                    continue;
                }
                var task = fromString(line);
                if (task instanceof Task) {
                    fileBackedTaskManager.addTask(task);
                }
                if (task instanceof Epic) {
                    fileBackedTaskManager.addEpic((Epic) task);
                }
                if (task instanceof Subtask) {
                    fileBackedTaskManager.addSubtask((Subtask) task);
                }
            }
            index++;
            for (int idTask : historyFromString(lines[index])) {
                if (fileBackedTaskManager.getTaskHashMap().containsKey(idTask))
                    fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getTask(idTask));
                if (fileBackedTaskManager.getSubtaskHashMap().containsKey(idTask))
                    fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getSubtask(idTask));
                if (fileBackedTaskManager.getEpicHashMap().containsKey(idTask))
                    fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getEpic(idTask));
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка работы с файлом", e);
        }

    }


    // Метод подготавливает строку для сохранения в файл
    static String makeDataToSave(List<Task> tasks, HistoryManager historyManager) {

        StringBuilder history = new StringBuilder();
        history.append("id,type,name,status,description,epic");

        for (var task : tasks) {
            history.append(task.toStringForFile()).append("\n");
        }
        history.append("\n");
        history.append(historyToString(historyManager));
        return history.toString();
    }

    // Возвращает строку из id задач через запятую, которые есть в истории
    static String historyToString(HistoryManager manager) {
        String result = "";
        for (var task : manager.getHistory()) {
            result = result + "," + task.getId();
        }
        return result;
    }

    static List<Integer> historyFromString(String value) {
        String[] historyIds = value.split(",");
        List<Integer> result = new ArrayList<>();
        for (String id : historyIds) {
            result.add(Integer.parseInt(id));
        }
        return result;
    }

    // Создание task, subtask or epic в зависимости, какая строка передана
    public static Task fromString(String value) {
        //в качестве результата создает таск определенного типа
        String[] data = value.split(",");
        TaskType type = TaskType.valueOf(data[1]);
        switch (type) {
            case TASK: {
                int id = Integer.parseInt(data[0]);
                String title = data[2];
                Status status = Status.valueOf(data[3]);
                String description = data[4];
                return new Task(id, title, description, status);
            }
            case SUBTASK: {
                int id = Integer.parseInt(data[0]);
                String title = data[2];
                Status status = Status.valueOf(data[3]);
                String description = data[4];
                int epicId = Integer.parseInt(data[5]);

                return new Subtask(id, title, description, status, epicId);
            }
            case EPIC: {
                int id = Integer.parseInt(data[0]);
                String title = data[2];
                Status status = Status.valueOf(data[3]);
                String description = data[4];
                return new Epic(id, title, description, status, null);
            }
        }
        return null;
    }
}
