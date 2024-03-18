package main;

import main.models.*;

import java.util.List;
import java.util.Map;

public interface TaskManager {
    Map<Integer, Task> getTaskHashMap();

    Map<Integer, Epic> getEpicHashMap();

    Map<Integer, Subtask> getSubtaskHashMap();

    List<Task> getHistory();

    // -----------------------------------------------------------
    // Методы для работы с задачами
    int getNewId();

    int getId();

    //  a. Получение списка всех задач.
    List<Task> getTasks();


    // b. Удаление всех задач.
    void deleteTasks();

    //  c. Получение по идентификатору.
    Task getTask(int id);

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    void addTask(Task newTask);

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateTask(Task newTask);

    // f. Удаление по идентификатору.
    void deleteTask(int id);

    // g. изменение статуса задачи
    void changeTaskStatus(Task task, Status status);

    //  a. Получение списка всех подзадач.
    List<Subtask> getSubtasks();

    //b. Удаление всех подзадач
    void deleteSubtasks();

    //  c. Получение подзадачи по идентификатору.
    Subtask getSubtask(int id);

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
    List<Epic> getEpics();

    // b. Удаление всех эпиков и их подзадач.
    void deleteEpics();

    //  c. Получение по идентификатору.
    Epic getEpic(int id);

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    void addEpic(Epic newEpic);

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    void updateEpic(Epic newEpic);

    void removeEpicSubtask(Epic epic, int id);

    // f. Удаление по идентификатору.
    void deleteEpic(int id);

    // метод обновления статуса Эпика
    void updateEpicStatus(Epic epic);


}
