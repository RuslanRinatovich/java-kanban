package main;


import main.models.Epic;
import main.models.Status;
import main.models.Subtask;
import main.models.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String autoSaveFileName;

    public FileBackedTaskManager(HistoryManager historyManager, String filename) {
        super(historyManager);
        autoSaveFileName = filename;
    }

    static String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();
        history.append("id,type,name,status,description,epic");
        for (var task : manager.getHistory()) {
            history.append(task.toStringForFile() + "\n");
        }
        return history.toString();
    }

    public void save() {
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(autoSaveFileName, StandardCharsets.UTF_8))) {
            wr.write(historyToString(this.historyManager));
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void addTask(Task newTask) {
        super.addTask(newTask);
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void changeTaskStatus(Task task, Status status) {
        super.changeTaskStatus(task, status);
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void addSubtask(Subtask newSubtask) {
        super.addTask(newSubtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void changeSubtaskStatus(Subtask subtask, Status status) {
        super.changeSubtaskStatus(subtask, status);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void addEpic(Epic newEpic) {
        super.addEpic(newEpic);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void removeEpicSubtask(Epic epic, int id) {
        super.removeEpicSubtask(epic, id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

}
