package main;


import main.models.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.CSVTaskFormatter.historyToString;
import static main.CSVTaskFormatter.makeDataToSave;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File autoSaveFileName;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        autoSaveFileName = file;
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(autoSaveFileName, StandardCharsets.UTF_8))) {
            List<Task> tasks = new ArrayList<>();
            tasks.addAll(getTasks());
            tasks.addAll(getSubtasks());
            tasks.addAll(getEpics());
            wr.write(makeDataToSave(tasks, this.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка работы с файлом", e);
        }
    }


    @Override
    public Task getTask(int id) throws ManagerSaveException {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int id) throws ManagerSaveException {
        Subtask task = super.getSubtask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) throws ManagerSaveException {
        Epic task = super.getEpic(id);
        save();
        return task;
    }

    @Override
    public void deleteTasks() throws ManagerSaveException {
        super.deleteTasks();
        save();
    }

    @Override
    public void addTask(Task newTask) throws ManagerSaveException {
        super.addTask(newTask);
        save();
    }

    @Override
    public void updateTask(Task newTask) throws ManagerSaveException {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void deleteTask(int id) throws ManagerSaveException {
        super.deleteTask(id);
        save();
    }

    @Override
    public void changeTaskStatus(Task task, Status status) throws ManagerSaveException {
        super.changeTaskStatus(task, status);
        save();
    }

    @Override
    public void deleteSubtasks() throws ManagerSaveException {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void addSubtask(Subtask newSubtask) throws ManagerSaveException {
        super.addSubtask(newSubtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) throws ManagerSaveException {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) throws ManagerSaveException {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void changeSubtaskStatus(Subtask subtask, Status status) throws ManagerSaveException {
        super.changeSubtaskStatus(subtask, status);
        save();
    }

    @Override
    public void deleteEpics() throws ManagerSaveException {
        super.deleteEpics();
        save();
    }

    @Override
    public void addEpic(Epic newEpic) throws ManagerSaveException {
        super.addEpic(newEpic);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) throws ManagerSaveException {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void removeEpicSubtask(Epic epic, int id) throws ManagerSaveException {
        super.removeEpicSubtask(epic, id);
        save();
    }

    @Override
    public void deleteEpic(int id) throws ManagerSaveException {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void updateEpicStatus(Epic epic) throws ManagerSaveException {
        super.updateEpicStatus(epic);
        save();
    }

}
