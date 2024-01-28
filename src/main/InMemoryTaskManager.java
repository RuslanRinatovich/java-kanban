package main;


import main.models.Epic;
import main.models.Status;
import main.models.Subtask;
import main.models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int idTask = 0;
    private final HistoryManager historyManager;
    // я так не делал, потому что один из ревьюеров написал студентам, что при такой
    // реализации менеджер истории в оперативной памяти жестко зашивается в менеджер задач.
    // Стоит использовать подход к инициализации поля через аргумент конструктора, это называется
    // внедрением зависимости. Я реализовал так. Как лучше то?
    // private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Map<Integer, Task> taskHashMap = new HashMap<>();
    private final Map<Integer, Epic> epicHashMap = new HashMap<>();
    private final Map<Integer, Subtask> subtaskHashMap = new HashMap<>();

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public Map<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    @Override
    public Map<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    @Override
    public Map<Integer, Subtask> getSubtaskHashMap() {
        return subtaskHashMap;
    }


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // -----------------------------------------------------------
    // Методы для работы с задачами
    public int getId() {
        return idTask;
    }

    // Формирование нового индентификатора для задачи
    public int getNewId() {
        idTask++;
        return idTask;
    }

    //  a. Получение списка всех задач.
    @Override
    public List<Task> getTasks() {
        return new ArrayList<Task>(taskHashMap.values());
    }

    // b. Удаление всех задач.
    @Override
    public void deleteTasks() {
        taskHashMap.clear();
    }

    //  c. Получение по идентификатору.
    // Взято с метанита. объекты классов представляют ссылочные типы, то есть указывают на какой-то объект, расположенный в памяти
    // Взято техзадания по покрытию тестами
    // (убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.)
    // по заданию в истории я должен хранить разные версии одного и того же объекта под указанным Id
    // я хочу реализовать клонирование(копирование) экзмепляра класса, чтобы сохранить в истории текущую версию
    // для этой реализации есть два подхода: реализовать интерфейс Cloneable или
    // создать новый экземпляр этого класса со значениями полей текущего экзмепляра классса(я выбрал второй вариант)
    // могу убрать "копирование". Как тогда будет работать сохранение в менеджере предыдущих версии задач в Истории?
    // я так понимаю, тогда в истории будут лежать просто ссылки, которые будут указывать на один и тот же участок памяти
    //

    @Override
    public Task getTask(int id) {
        if (taskHashMap.containsKey(id)) {
            Task task = taskHashMap.get(id);

            Task clonedTaskFoHistory = new Task(task.getTitle(), task.getDescription(), task.getId(), task.getStatus());
            historyManager.add(clonedTaskFoHistory);
            return task;
        }
        return null;
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public void addTask(Task newTask) {
        int id = getNewId();
        newTask.setId(id);
        taskHashMap.put(id, newTask);
    }

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateTask(Task newTask) {
        taskHashMap.replace(newTask.getId(), newTask);
    }

    // f. Удаление по идентификатору.
    @Override
    public void deleteTask(int id) {
        if (taskHashMap.containsKey(id)) {
            taskHashMap.remove(id);
        }
    }

    // g. изменение статуса задачи
    @Override
    public void changeTaskStatus(Task task, Status status) {
        task.setStatus(status);
    }


    // -----------------------------------------------------------
    // Методы для работы с подзадачами
    // -----------------------------------------------------------

    //  a. Получение списка всех подзадач.
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<Subtask>(subtaskHashMap.values());
    }

    //b. Удаление всех подзадач
    @Override
    public void deleteSubtasks() {
        // во всех эпиках очищаем список индентификаторов его подзадач
        for (Epic e : epicHashMap.values()) {
            e.clearAllSubtasks();
        }
        subtaskHashMap.clear();
    }

    //  c. Получение подзадачи по идентификатору.
    @Override
    public Subtask getSubtask(int id) {

        if (subtaskHashMap.containsKey(id)) {
            Subtask subtask = subtaskHashMap.get(id);
            Subtask clonedTaskFoHistory = new Subtask(subtask.getTitle(), subtask.getDescription(), subtask.getId(), subtask.getStatus(), subtask.getEpicId());
            historyManager.add(clonedTaskFoHistory);
            return subtask;
        }
        return null;
    }

    // d. Создание подзадачи. Сам объект должен передаваться в качестве параметра.
    @Override
    public void addSubtask(Subtask newSubtask) {
        int id = getNewId();
        newSubtask.setId(id);
        subtaskHashMap.put(id, newSubtask);
        Epic epic = epicHashMap.get(newSubtask.getEpicId());
        epic.addSubtaskId(id);
        updateEpicStatus(epic);
    }

    //e. Обновление подзадачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateSubtask(Subtask newSubtask) {
        subtaskHashMap.replace(newSubtask.getId(), newSubtask);
        Epic epic = epicHashMap.get(newSubtask.getEpicId());
        updateEpicStatus(epic);
    }

    // f. Удаление подзадачи по идентификатору.
    @Override
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

    @Override
    public void changeSubtaskStatus(Subtask subtask, Status status) {
        // при изменении статуса подзадачи надо пересмотреть статус Эпика
        subtask.setStatus(status);
        Epic epic = epicHashMap.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }


    //метод для проверки имеют ли все подзадачи эпика один и тот же статус
    @Override
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
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<Epic>(epicHashMap.values());
    }


    // b. Удаление всех эпиков и их подзадач.
    @Override
    public void deleteEpics() {
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    //  c. Получение по идентификатору.
    @Override
    public Epic getEpic(int id) {
        if (epicHashMap.containsKey(id)) {
            Epic t = epicHashMap.get(id);
            Epic clonedTaskFoHistory = new Epic(t.getTitle(), t.getDescription(), t.getId(), t.getStatus(), t.getSubtasksIds());
            historyManager.add(clonedTaskFoHistory);

            return t;
        }
        return null;
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public void addEpic(Epic newEpic) {

        int id = getNewId();
        newEpic.setId(id);
        epicHashMap.put(id, newEpic);
    }

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateEpic(Epic newEpic) {
        epicHashMap.replace(newEpic.getId(), newEpic);
    }


    @Override
    public void removeEpicSubtask(Epic epic, int id) {
        epic.removeSubtask(id);
        updateEpicStatus(epic);
    }

    // f. Удаление по идентификатору.
    @Override
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
    @Override
    public void updateEpicStatus(Epic epic) {
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

