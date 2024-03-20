package main;

import main.models.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormatter {

    // генерация FileBackedTaskManager из файла
    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        try {
            // считываем целиком текст из файла в строковую переменную
            String data = Files.readString(file.toPath());
            // создаем на основе файла массив строк
            String[] lines = data.split("\n");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
            int size = lines.length;

            for (int i = 1; i < size - 2; i++) {
                var task = fromString(lines[i]);
                if (task instanceof Epic) {
                    fileBackedTaskManager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    fileBackedTaskManager.addSubtask((Subtask) task);
                } else {
                    fileBackedTaskManager.addTask(task);
                }
            }

            List<Integer> history = historyFromString(lines[size - 1]);
            if (!history.isEmpty()) {
                for (int idTask : history) {
                    if (fileBackedTaskManager.getTaskHashMap().containsKey(idTask))
                        fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getTask(idTask));
                    if (fileBackedTaskManager.getSubtaskHashMap().containsKey(idTask))
                        fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getSubtask(idTask));
                    if (fileBackedTaskManager.getEpicHashMap().containsKey(idTask))
                        fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getEpic(idTask));
                }
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка работы с файлом", e);
        }

    }


    // Метод подготавливает строку для сохранения в файл
    static String makeDataToSave(List<Task> tasks, HistoryManager historyManager) {

        StringBuilder history = new StringBuilder();
        history.append("id,type,name,status,description,epic or subtasks\n");

        for (var task : tasks) {
            history.append(task.toStringForFile()).append("\n");
        }
        history.append("\n");
        if (historyManager.getHistory().isEmpty())
            history.append("empty history");
        else {
            history.append(historyToString(historyManager));
        }
        return history.toString();
    }

    // Возвращает строку из id задач через запятую, которые есть в истории
    static String historyToString(HistoryManager manager) {
        String result = "";
        int i = 0;
        for (var task : manager.getHistory()) {
            if (i == 0) {
                result = result + task.getId();
            } else {
                result = result + "," + task.getId();
            }
            i++;
        }
        return result;
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> result = new ArrayList<>();
        if (value.equals("empty history"))
            return result;
        String[] historyIds = value.split(",");
        for (String id : historyIds) {
            result.add(Integer.parseInt(id));
        }
        return result;
    }

    // Создание task, subtask or epic в зависимости, какая строка передана
    public static Task fromString(String value) {
        //в качестве результата создает таск определенного типа
        String[] data = value.split(",");
        int id = Integer.parseInt(data[0]);
        String title = data[2];
        Status status = Status.valueOf(data[3]);
        String description = data[4];
        TaskType type = TaskType.valueOf(data[1]);
        switch (type) {
            case TASK: {
                return new Task(id, title, description, status);
            }
            case SUBTASK: {
                int epicId = Integer.parseInt(data[5]);
                return new Subtask(id, title, description, status, epicId);
            }
            case EPIC: {
                return new Epic(id, title, description, status, null);
            }
        }
        return null;
    }
}
