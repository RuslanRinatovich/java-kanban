package main;

import main.models.Epic;
import main.models.Status;
import main.models.Subtask;
import main.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    HashMap<Integer, Task> getTaskHashMap();

    HashMap<Integer, Epic> getEpicHashMap();

    HashMap<Integer, Subtask> getSubtaskHashMap();

    List<Task> getHistoryTask();

    // -----------------------------------------------------------
    // Методы для работы с задачами
    // Формирование нового индентификатора для задачи
    int getNewId();

    //  a. Получение списка всех задач.
    List<Task> getAllTasks();

    // b. Удаление всех задач.
    void deleteAllTasks();

    //  c. Получение по идентификатору.
    Task getTaskById(int id);

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    void addTask(Task newTask);

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTask(Task newTask);

    // f. Удаление по идентификатору.
    void deleteTask(int id);

    // g. изменение статуса задачи
    void changeTaskStatus(Task task, Status status);

    //  a. Получение списка всех подзадач.
    List<Subtask> getAllSubtasks();

    //b. Удаление всех подзадач
    void deleteAllSubtasks();

    //  c. Получение подзадачи по идентификатору.
    Subtask getSubtaskById(int id);

    // d. Создание подзадачи. Сам объект должен передаваться в качестве параметра.
    void addSubtask(Subtask newSubtask);

    //e. Обновление подзадачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateSubtask(Subtask newSubtask);

    // f. Удаление подзадачи по идентификатору.
    void deleteSubtask(int id);

    void changeSubtaskStatus(Subtask subtask, Status status);

    //метод для проверки имеют ли все подзадачи эпика один и тот же статус
    boolean hasAllSubtaskSameStatus(Status status, Epic epic);

    // ЭПИКИ
    //Формирование нового индентификатора для эпика
    //  a. Получение списка всех эпиков.
    List<Epic> getAllEpics();

    // b. Удаление всех эпиков и их подзадач.
    void deleteAllEpics();

    //  c. Получение по идентификатору.
    Epic getEpicById(int id);

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    void addEpic(Epic newEpic);

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateEpic(Epic newEpic);

    void removeSubtask(Epic epic, int id);

    // f. Удаление по идентификатору.
    void deleteEpic(int id);

    // метод обновления статуса Эпика
    void updateEpicStatus(Epic epic);
}
