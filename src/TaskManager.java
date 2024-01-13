import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private static int idTask = 0;
    private static int idSubTask = 0;
    private static int idEpic = 0;

    public static int getNewIdTask()
    {
        idTask++;
        return idTask;
    }
    static HashMap<Integer, Task> taskHashMap = new HashMap<>();
    static HashMap<Integer, Epic> epicHashMap = new HashMap<>();

    static HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();

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

    public static void changeTaskStatus(Task task, Status status)
    {
        task.setStatus(status);
    }



}
