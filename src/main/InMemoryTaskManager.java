package main;

import main.models.*;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private static Logger LOGGER;

    static {
        try (FileInputStream ins = new FileInputStream("log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Main.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    protected final HistoryManager historyManager;
    protected final Map<Integer, Task> taskHashMap = new HashMap<>();
    protected final Map<Integer, Epic> epicHashMap = new HashMap<>();
    protected final Map<Integer, Subtask> subtaskHashMap = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected int idTask = 0;

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


    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }


    private boolean isTasksIntersected(Task firstTask, Task secondTask) {
        return (firstTask.getEndTime().isAfter(secondTask.getStartTime()) && firstTask.getEndTime().isBefore(secondTask.getEndTime()) || secondTask.getEndTime().isAfter(firstTask.getStartTime()) && secondTask.getEndTime().isBefore(firstTask.getEndTime()) || firstTask.getStartTime().isEqual(secondTask.getStartTime()) && firstTask.getEndTime().isEqual(secondTask.getEndTime()));
    }

    private void checkIfIntersectedTaskExist(Task currentTask) throws ManagerSaveException {
        List<Task> intersectedTask = prioritizedTasks.stream().filter(task -> isTasksIntersected(currentTask, task)).collect(Collectors.toList());
        if (!intersectedTask.isEmpty()) {

            if (intersectedTask.size() > 1) {
                LOGGER.log(Level.INFO, currentTask.toString());
                for (Task t : intersectedTask) {
                    LOGGER.log(Level.INFO, t.toString());
                }
                LOGGER.log(Level.WARNING, "Пересечение при добавлении");
                throw new ManagerSaveException("Пересечение при добавлении", new Exception());
            }
            // если вы выборке только одна задача
            // случай пересечения с самим собой при обновлении
            int id = intersectedTask.get(0).getId();
            if (id != currentTask.getId()) {
                LOGGER.log(Level.INFO, currentTask.toString());
                for (Task t : intersectedTask) {
                    LOGGER.log(Level.INFO, t.toString());
                }
                LOGGER.log(Level.WARNING, "Пересечение при обновлении");
                throw new ManagerSaveException("Пересечение при обновлении", new Exception());
            }
        }
    }

    // проверяет существует ли указанный айди среди айди всех задач, подзадач и эпиков
    private boolean isCurrentIdExist(int id) {
        return (taskHashMap.containsKey(id) || subtaskHashMap.containsKey(id) || epicHashMap.containsKey(id));
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
    public void deleteTasks() throws ManagerSaveException {
        prioritizedTasks.removeAll(taskHashMap.values());
        taskHashMap.clear();
    }


    @Override
    public Task getTask(int id) throws ManagerSaveException {
        if (taskHashMap.containsKey(id)) {
            Task task = taskHashMap.get(id);
            Task clonedTaskFoHistory = new Task(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getDuration(), task.getStartTime());
            historyManager.add(clonedTaskFoHistory);
            return task;
        }
        return null;
    }


    // d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public void addTask(Task newTask) throws ManagerSaveException {
        int id = newTask.getId();
        if (id == 0) {
            id = getNewId();
            newTask.setId(id);
        }
        if (isCurrentIdExist(id)) {
            throw new ManagerSaveException("Задача с указанным id уже существует", new Exception());
        }
        if (newTask.getStartTime() != null) {
            checkIfIntersectedTaskExist(newTask);
            prioritizedTasks.add(newTask);
        }
        taskHashMap.put(id, newTask);
    }

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateTask(Task newTask) throws ManagerSaveException {
        checkIfIntersectedTaskExist(newTask);
        if (prioritizedTasks.contains(newTask)) {
            prioritizedTasks.remove(taskHashMap.get(newTask.getId()));
        }
        taskHashMap.replace(newTask.getId(), newTask);
        if (newTask.getStartTime() != null) prioritizedTasks.add(newTask);
    }

    // f. Удаление по идентификатору.
    @Override
    public void deleteTask(int id) throws ManagerSaveException {

        if (taskHashMap.containsKey(id)) {
            prioritizedTasks.remove(taskHashMap.get(id));
            taskHashMap.remove(id);
        } else {
            throw new ManagerSaveException("Указанного id не существует", new Exception());
        }
    }

    // g. изменение статуса задачи
    @Override
    public void changeTaskStatus(Task task, Status status) throws ManagerSaveException {
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
    public void deleteSubtasks() throws ManagerSaveException {
        // во всех эпиках очищаем список индентификаторов его подзадач
        for (Epic e : epicHashMap.values()) {
            e.clearAllSubtasks();
            updateEpicStatus(e);
            updateEpicDurationAndTime(e);
        }
        prioritizedTasks.removeAll(subtaskHashMap.values());
        subtaskHashMap.clear();
    }

    //  c. Получение подзадачи по идентификатору.
    @Override
    public Subtask getSubtask(int id) throws ManagerSaveException {

        if (subtaskHashMap.containsKey(id)) {
            Subtask subtask = subtaskHashMap.get(id);
            Subtask clonedTaskFoHistory = new Subtask(subtask.getId(), subtask.getTitle(), subtask.getDescription(), subtask.getStatus(), subtask.getDuration(), subtask.getStartTime(), subtask.getEpicId());
            historyManager.add(clonedTaskFoHistory);
            return subtask;
        }
        return null;
    }

    // d. Создание подзадачи. Сам объект должен передаваться в качестве параметра.
    @Override
    public void addSubtask(Subtask newSubtask) throws ManagerSaveException {
        int id = newSubtask.getId();
        if (id == 0) {
            id = getNewId();
            newSubtask.setId(id);
        }
        checkIfIntersectedTaskExist(newSubtask);
        if (isCurrentIdExist(id)) {
            throw new ManagerSaveException("Задача с указанным id уже существует", new Exception());
        }
        subtaskHashMap.put(id, newSubtask);
        if (newSubtask.getStartTime() != null) prioritizedTasks.add(newSubtask);

        Epic epic = epicHashMap.getOrDefault(newSubtask.getEpicId(), null);
        if (epic != null) {
            epic.addSubtaskId(id);
            updateEpicStatus(epic);
            updateEpicDurationAndTime(epic);
        }

    }

    //e. Обновление подзадачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateSubtask(Subtask newSubtask) throws ManagerSaveException {
        checkIfIntersectedTaskExist(newSubtask);
        prioritizedTasks.remove(subtaskHashMap.get(newSubtask.getId()));
        subtaskHashMap.replace(newSubtask.getId(), newSubtask);
        if (newSubtask.getStartTime() != null) prioritizedTasks.add(newSubtask);
        Epic epic = epicHashMap.get(newSubtask.getEpicId());
        updateEpicStatus(epic);
        updateEpicDurationAndTime(epic);
    }

    // f. Удаление подзадачи по идентификатору.
    @Override
    public void deleteSubtask(int id) throws ManagerSaveException {
        if (subtaskHashMap.containsKey(id)) {
            Subtask subtask = subtaskHashMap.get(id);
            Epic epic = epicHashMap.get(subtask.getEpicId());
            // удалить идентификатор у эпика
            prioritizedTasks.remove(subtask);
            epic.removeSubtask(id);
            subtaskHashMap.remove(id);
            updateEpicStatus(epic);
            updateEpicDurationAndTime(epic);
        } else {
            throw new ManagerSaveException("Указанного id не существует", new Exception());
        }
    }

    @Override
    public void changeSubtaskStatus(Subtask subtask, Status status) throws ManagerSaveException {
        // при изменении статуса подзадачи надо пересмотреть статус Эпика
        subtask.setStatus(status);
        Epic epic = epicHashMap.get(subtask.getEpicId());
        updateEpicStatus(epic);

    }


    //метод для проверки имеют ли все подзадачи эпика один и тот же статус
    @Override
    public boolean hasAllSubtaskSameStatus(Status status, Epic epic) {
        for (int i : epic.getSubtasksIds()) {
            if (subtaskHashMap.get(i).getStatus() != status) return false;
        }
        return true;
    }


    // ЭПИКИ
    //Формирование нового индентификатора для эпика
    //  a. Получение списка всех эпиков.
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicHashMap.values());
    }


    // b. Удаление всех эпиков и их подзадач.
    @Override
    public void deleteEpics() throws ManagerSaveException {

        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    //  c. Получение по идентификатору.
    @Override
    public Epic getEpic(int id) throws ManagerSaveException {
        if (epicHashMap.containsKey(id)) {
            Epic epic = epicHashMap.get(id);
            Epic clonedTaskFoHistory = new Epic(epic.getId(), epic.getTitle(), epic.getDescription(), epic.getStatus(), epic.getDuration(), epic.getStartTime(), epic.getSubtasksIds());
            historyManager.add(clonedTaskFoHistory);
            return epic;
        }
        return null;
    }

    // d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public void addEpic(Epic newEpic) throws ManagerSaveException {
        int id = newEpic.getId();
        if (id == 0) {
            id = getNewId();
            newEpic.setId(id);
        }
        if (isCurrentIdExist(id)) {
            throw new ManagerSaveException("Задача с указанным id уже существует", new Exception());
        }
        epicHashMap.put(id, newEpic);
    }

    //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public void updateEpic(Epic newEpic) throws ManagerSaveException {
        epicHashMap.replace(newEpic.getId(), newEpic);
    }


    @Override
    public void removeEpicSubtask(Epic epic, int id) throws ManagerSaveException {
        epic.removeSubtask(id);
        updateEpicStatus(epic);
        updateEpicDurationAndTime(epic);
    }

    // f. Удаление по идентификатору.
    @Override
    public void deleteEpic(int id) throws ManagerSaveException {
        if (epicHashMap.containsKey(id)) {
            Epic epic = epicHashMap.get(id);
            // удаляем подзадачи из subtaskHashMap
            for (int i : epic.getSubtasksIds()) {
                subtaskHashMap.remove(i);
            }
            epicHashMap.remove(id);
        }
    }

    // метод обновления статуса Эпика
    @Override
    public void updateEpicStatus(Epic epic) throws ManagerSaveException {
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

    // Метод для рассчёта длительности, времени начала и окончания эпика
    public void updateEpicDurationAndTime(Epic epic) throws ManagerSaveException {
        // список подзадач пуст
        if (epic.getSubtasksIds().isEmpty()) {
            epic.setDuration(Duration.ofMinutes(0));
            epic.setStartTime(null);
            epic.setEndTime(null);
            return;
        }
        // получаем первую по времени начала подзадачу
        Optional<Subtask> firstSubtask = subtaskHashMap.values().stream().filter(s -> epic.getSubtasksIds().contains(s.getId())).min(Comparator.comparing(Task::getStartTime)).stream().findFirst();
        firstSubtask.ifPresent(value -> epic.setStartTime(value.getStartTime()));
        // получаем последнюю по времени начала подзадачу
        Optional<Subtask> lastSubtask = subtaskHashMap.values().stream().filter(s -> epic.getSubtasksIds().contains(s.getId())).max(Comparator.comparing(Task::getEndTime)).stream().findFirst();
        lastSubtask.ifPresent(value -> epic.setEndTime(value.getStartTime()));
        long totalDuration = 0;
        for (int i : epic.getSubtasksIds())
            totalDuration = totalDuration + subtaskHashMap.get(i).getDuration().toMinutes();
        epic.setDuration(Duration.ofMinutes(totalDuration));
    }
}

