import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private static int idTask = 0;
    private static int idSubTask = 0;
    private static int idEpic = 0;



    static HashMap<Integer, Task> taskHashMap = new HashMap<>();
    static HashMap<Integer, Epic> epicHashMap = new HashMap<>();

    static HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();


    // -----------------------------------------------------------
    // Методы для работы с задачами
    //Формирование нового инденетификатора для задачи
    public static int getNewTaskId()
    {
        idTask++;
        return idTask;
    }
    //  a. Получение списка всех задач.
    public static Collection<Task> getAllTasks() {
        return taskHashMap.values();
    }
    //b. Удаление всех задач.
    public static void deleteAllTasks() {
        taskHashMap.clear();
        idTask = 0;
    }
    //  c. Получение по идентификатору.
    public static Task getTaskById(int id) {
        if (taskHashMap.containsKey(id)) {
            return taskHashMap.get(id);
        }
        return null;
    }
    // d. Создание. Сам объект должен передаваться в качестве параметра.
    public static void addTask(Task newTask) {

        taskHashMap.put(newTask.getId(), newTask);
    }

   //e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
   public static void updateTask(Task newTask) {

       taskHashMap.replace(newTask.getId(), newTask);
   }
   // f. Удаление по идентификатору.
    public static boolean deleteTask(int id) {
        if (taskHashMap.containsKey(id)) {
            taskHashMap.remove(id);
            return true;
        }
        return false;
    }
    // g. изменение статуса задачи
    public static void changeTaskStatus(Task task, Status status)
    {
        task.setStatus(status);
    }


    // -----------------------------------------------------------
    // Методы для работы с подзадачами
    // -----------------------------------------------------------


    public static int getNewSubtaskId()
    {
        idSubTask++;
        return idSubTask;
    }
    //  a. Получение списка всех подзадач.
    public static Collection<Subtask> getAllSubtasks() {
        return subtaskHashMap.values();
    }
    //b. Удаление всех подзадач
    public static void deleteAllSubtasks() {
        // во всех эпиках очищаем список индентификаторов его подзадач
        for (Epic e: epicHashMap.values()) {
            e.clearAllSubtasks();
        }
        subtaskHashMap.clear();
        idSubTask = 0;
    }
    //  c. Получение подзадачи по идентификатору.
    public static Subtask getSubtaskById(int id) {
        if (subtaskHashMap.containsKey(id)) {
            return subtaskHashMap.get(id);
        }
        return null;
    }
    // d. Создание подзадачи. Сам объект должен передаваться в качестве параметра.
    public static void addSubtask(Subtask newSubtask) {
        subtaskHashMap.put(newSubtask.getId(), newSubtask);
        Epic epic = epicHashMap.get(newSubtask.getEpicId());
        epic.addSubtaskId(newSubtask.getId());
        updateEpicStatus(epic);
    }

    //e. Обновление подзадачи. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public static void updateSubtask(Subtask newSubtask) {
        addSubtask(newSubtask);
    }

    // f. Удаление подзадачи по идентификатору.
    public static void deleteSubtask(int id) {
        if (subtaskHashMap.containsKey(id)) {
            Subtask subtask = subtaskHashMap.get(id);
            Epic epic = epicHashMap.get(subtask.getEpicId());
            // удалить идентификатор у эпика
            epic.removeSubtask(id);
            subtaskHashMap.remove(id);
            updateEpicStatus(epic);
        }
    }

    public static void changeSubtaskStatus(Subtask subtask, Status status)
    {
        // при изменении статуса подзадачи надо пересмотреть статус Эпика
        subtask.setStatus(status);
        Epic epic = epicHashMap.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }

    // метод обновления статуса Эпика
    public static void updateEpicStatus(Epic epic)
    {
        // список подзадач пуст
        if (epic.getSubtasksIds().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        // все подзадачи имеют статус NEW
        if (hasAllSubtaskSameStatus(Status.NEW, epic))
        {
            epic.setStatus(Status.NEW);
            return;
        }

        // все подзадачи имеют статус DONE
        if (hasAllSubtaskSameStatus(Status.DONE, epic))
        {
            epic.setStatus(Status.DONE);
            return;
        }
        epic.setStatus(Status.IN_PROGRESS);
    }
   //метод для проверки имеют ли все подзадачи эпика один и тот же статус
    public static boolean hasAllSubtaskSameStatus(Status status, Epic epic)
    {
        for (int i: epic.getSubtasksIds()) {
            if (subtaskHashMap.get(i).status != status)
                return false;
        }
        return true;
    }


    // ЭПИКИ
    //  c. Получение подзадачи по идентификатору.
    public static Epic getEpicById(int id) {
        if (epicHashMap.containsKey(id)) {
            return epicHashMap.get(id);
        }
        return null;
    }

}
