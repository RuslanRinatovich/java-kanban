package main;

import main.models.Epic;
import main.models.Status;
import main.models.Subtask;
import main.models.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private int idTask = 0;
    private HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public HashMap<Integer, Subtask> getSubtaskHashMap() {
        return subtaskHashMap;
    }

    // -----------------------------------------------------------
    // Методы для работы с задачами
    // Формирование нового индентификатора для задачи
    private int getNewId() {
        idTask++;
        return idTask;
    }

    //  a. Получение списка всех задач.
    public ArrayList<Task> getAllTasks() {
        return (ArrayList<Task>) taskHashMap.values();
    }

    // b. Удаление всех задач.
    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    //  c. Получение по идентификатору.
    public Task getTaskById(int id) {
        return taskHashMap.get(id);
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    public void addTask(Task newTask) {
        int id = getNewId();
        newTask.setId(id);
        taskHashMap.put(id, newTask);
    }

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task newTask) {
        taskHashMap.replace(newTask.getId(), newTask);
    }

    // f. Удаление по идентификатору.
    public void deleteTask(int id) {
        if (taskHashMap.containsKey(id)) {
            taskHashMap.remove(id);
        }
    }

    // g. изменение статуса задачи
    public void changeTaskStatus(Task task, Status status) {
        task.setStatus(status);
    }


    // -----------------------------------------------------------
    // Методы для работы с подзадачами
    // -----------------------------------------------------------

    //  a. Получение списка всех подзадач.
    public ArrayList<Subtask> getAllSubtasks() {
        return (ArrayList<Subtask>) subtaskHashMap.values();
    }

    //b. Удаление всех подзадач
    public void deleteAllSubtasks() {
        // во всех эпиках очищаем список индентификаторов его подзадач
        for (Epic e : epicHashMap.values()) {
            e.clearAllSubtasks();
        }
        subtaskHashMap.clear();
    }

    //  c. Получение подзадачи по идентификатору.
    public Subtask getSubtaskById(int id) {
        return subtaskHashMap.get(id);
    }

    // d. Создание подзадачи. Сам объект должен передаваться в качестве параметра.
    public void addSubtask(Subtask newSubtask) {
        int id = getNewId();
        newSubtask.setId(id);
        subtaskHashMap.put(id, newSubtask);
        Epic epic = epicHashMap.get(newSubtask.getEpicId());
        epic.addSubtaskId(id);
        updateEpicStatus(epic);
    }

    //e. Обновление подзадачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateSubtask(Subtask newSubtask) {
        subtaskHashMap.replace(newSubtask.getId(), newSubtask);
        Epic epic = epicHashMap.get(newSubtask.getEpicId());
        updateEpicStatus(epic);
    }

    // f. Удаление подзадачи по идентификатору.
    public void deleteSubtask(int id) {
        if (subtaskHashMap.containsKey(id)) {
            Subtask subtask = subtaskHashMap.get(id);
            Epic epic = epicHashMap.get(subtask.getEpicId());
            // удалить идентификатор у эпика
            epic.removeSubtask(id);
            subtaskHashMap.remove(id);
            updateEpicStatus(epic);
        }
    }

    public void changeSubtaskStatus(Subtask subtask, Status status) {
        // при изменении статуса подзадачи надо пересмотреть статус Эпика
        subtask.setStatus(status);
        Epic epic = epicHashMap.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }


    //метод для проверки имеют ли все подзадачи эпика один и тот же статус
    public boolean hasAllSubtaskSameStatus(Status status, Epic epic) {
        for (int i : epic.getSubtasksIds()) {
            if (subtaskHashMap.get(i).getStatus() != status)
                return false;
        }
        return true;
    }


    // ЭПИКИ
    //Формирование нового индентификатора для эпика
    //  a. Получение списка всех эпиков.
    public ArrayList<Epic> getAllEpics() {
        return (ArrayList<Epic>) epicHashMap.values();
    }

    // b. Удаление всех эпиков и их подзадач.
    public void deleteAllEpics() {
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    //  c. Получение по идентификатору.
    public Epic getEpicById(int id) {
        return epicHashMap.get(id);
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    public void addEpic(Epic newEpic) {
        int id = getNewId();
        newEpic.setId(id);
        epicHashMap.put(id, newEpic);
    }

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateEpic(Epic newEpic) {
        epicHashMap.replace(newEpic.getId(), newEpic);
    }


    public void removeSubtask(Epic epic, int id) {
        epic.removeSubtask(id);
        updateEpicStatus(epic);
    }

    // f. Удаление по идентификатору.
    public void deleteEpic(int id) {
        if (epicHashMap.containsKey(id)) {
            Epic epic = epicHashMap.get(id);
            // удаляем подзадачи из subtaskHashMap
            for (int i : epic.getSubtasksIds()) {
                subtaskHashMap.remove(id);
            }
            epicHashMap.remove(id);
        }
    }

    // метод обновления статуса Эпика
    private void updateEpicStatus(Epic epic) {
        // список подзадач пуст
        if (epic.getSubtasksIds().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        // все подзадачи имеют статус NEW
        if (hasAllSubtaskSameStatus(Status.NEW, epic)) {
            epic.setStatus(Status.NEW);
            return;
        }

        // все подзадачи имеют статус DONE
        if (hasAllSubtaskSameStatus(Status.DONE, epic)) {
            epic.setStatus(Status.DONE);
            return;
        }
        epic.setStatus(Status.IN_PROGRESS);
    }

}
