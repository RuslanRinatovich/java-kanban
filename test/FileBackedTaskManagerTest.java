import main.Managers;
import main.TaskManager;
import main.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FileBackedTaskManagerTest {
    private static TaskManager fileBackedTaskManager;


//    @BeforeEach
//    public void setUp() throws ManagerSaveException {
//
//        fileBackedTaskManager = Managers.getDefault();
//
//        Task task1 = new Task("Задача 1", "Описание задачи 1",  Status.NEW);
//        Task task2 = new Task("Задача 2", "Описание задачи 2",  Status.NEW);
//        Task task3 = new Task("Задача 3", "Описание задачи 3",  Status.NEW);
//
//        fileBackedTaskManager.addTask(task1); //1
//        fileBackedTaskManager.addTask(task2);//2
//        fileBackedTaskManager.addTask(task3);//3
//
//        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1", Status.NEW, new ArrayList<>());
//        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2",  Status.NEW, new ArrayList<>());
//        Epic epic3 = new Epic("Эпик 3", "Описание Эпика 3",  Status.NEW, new ArrayList<>());
//
//        fileBackedTaskManager.addEpic(epic1);//4
//        fileBackedTaskManager.addEpic(epic2);//5
//        fileBackedTaskManager.addEpic(epic3);//6
//
//        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1",  Status.NEW, epic1.getId());
//        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2",  Status.NEW, epic1.getId());
//        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3",  Status.NEW, epic2.getId());
//        Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4",  Status.NEW, epic2.getId());
//        Subtask subtask5 = new Subtask("Подзадача 5", "Описание подзадачи 5",  Status.NEW, epic3.getId());
//        Subtask subtask6 = new Subtask("Подзадача 6", "Описание подзадачи 6",  Status.NEW, epic3.getId());
//        fileBackedTaskManager.addSubtask(subtask1);//7
//        fileBackedTaskManager.addSubtask(subtask2);//8
//        fileBackedTaskManager.addSubtask(subtask3);//9
//        fileBackedTaskManager.addSubtask(subtask4);//10
//        fileBackedTaskManager.addSubtask(subtask5);//11
//        fileBackedTaskManager.addSubtask(subtask6);//12
//    }
    @BeforeEach
    public void setUp() throws ManagerSaveException {

        fileBackedTaskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание задачи 1",  Status.NEW);


        fileBackedTaskManager.addTask(task1); //1


        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1", Status.NEW, new ArrayList<>());


        fileBackedTaskManager.addEpic(epic1);//2


        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1",  Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2",  Status.NEW, epic1.getId());

        fileBackedTaskManager.addSubtask(subtask1);//3
        fileBackedTaskManager.addSubtask(subtask2);//4

    }

    @Test
    public void getNewId() {
        int id = fileBackedTaskManager.getNewId();
        assertEquals(id, 13, "Идентификатор некорректный");
    }

    @Test
    public void getTaskById() throws ManagerSaveException {

        Task expected = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        Task actual = fileBackedTaskManager.getTask(1);
        assertEquals(expected, actual, "Задачи не совпадают");
    }

    @Test
    public void addTask() throws ManagerSaveException {
        int id = fileBackedTaskManager.getId() + 1;
        Task newTask = new Task(id, "Задача 1", "Описание задачи 1", Status.NEW);
        fileBackedTaskManager.addTask(newTask);
        Task actualTask = fileBackedTaskManager.getTask(fileBackedTaskManager.getId());
        assertEquals(newTask, actualTask, "Задачи не совпадают");
    }

    @Test
    public void updateTask() throws ManagerSaveException {
        Task newTask = new Task(1, "Задача 1. Обновленная", "Описание задачи 1",  Status.NEW);
        fileBackedTaskManager.updateTask(newTask);
        Task actualTask = fileBackedTaskManager.getTask(1);
        assertEquals(newTask, actualTask, "Задачи не совпадают");
    }

    @Test
    public void deleteTask() throws ManagerSaveException {
        fileBackedTaskManager.deleteTask(1);
        Task expected = fileBackedTaskManager.getTask(1);
        assertNull(expected, "Задача не удалена.");
    }

    @Test
    public void deleteAllSubtasks() throws ManagerSaveException {
        fileBackedTaskManager.deleteSubtasks();
        assertEquals(0, fileBackedTaskManager.getSubtaskHashMap().size(), "Подзадачи не очищены");
    }

    @Test
    public void getSubtaskById() throws ManagerSaveException {
        Subtask expected = new Subtask(7,"Подзадача 1", "Описание подзадачи 1",  Status.NEW, 4);
        Subtask actual = fileBackedTaskManager.getSubtask(7);
        assertEquals(expected, actual, "Подзадачи не совпадают");
    }

    @Test
    public void addSubtask() throws ManagerSaveException {
        int id = fileBackedTaskManager.getId() + 1;
        Subtask newSubtask = new Subtask(id,"Подзадача 7", "Описание подзадачи 7",  Status.NEW, 4);
        fileBackedTaskManager.addSubtask(newSubtask);
        Subtask actualSubtask = fileBackedTaskManager.getSubtask(fileBackedTaskManager.getId());
        assertEquals(newSubtask, actualSubtask, "Задачи не совпадают");

    }

    @Test
    public void updateSubtask() throws ManagerSaveException {
        Subtask newSubtask = new Subtask(7,"Подзадача 1, Обновлена", "Описание подзадачи 7",  Status.NEW, 4);
        fileBackedTaskManager.updateSubtask(newSubtask);
        Subtask actualSubtask = fileBackedTaskManager.getSubtask(7);
        assertEquals(newSubtask, actualSubtask, "Задачи не совпадают");
    }

    @Test
    public void deleteSubtask() throws ManagerSaveException {
        fileBackedTaskManager.deleteSubtask(7);
        Subtask expected = fileBackedTaskManager.getSubtask(1);
        assertNull(expected, "Подзадача не удалена.");
    }

    @Test
    public void getEpicById() throws ManagerSaveException {
        ArrayList<Integer> subtasksIds = new ArrayList<>();
        subtasksIds.add(4);
        subtasksIds.add(5);
        Epic expected = new Epic(4,"Эпик 1", "Описание Эпика 1",  Status.NEW, subtasksIds);
        Epic actual = fileBackedTaskManager.getEpic(4);
        assertEquals(expected, actual, "Эпики не совпадают");
    }

    @Test
    public void addEpic() throws ManagerSaveException {
        int id = fileBackedTaskManager.getId() + 1;
        Epic newEpic = new Epic( id,"Эпик 4", "Описание Эпика 4", Status.NEW, new ArrayList<>());
        fileBackedTaskManager.addEpic(newEpic);
        Epic actualEpic = fileBackedTaskManager.getEpic(fileBackedTaskManager.getId());
        assertEquals(newEpic, actualEpic, "Эпики не совпадают");
    }

    @Test
    public void updateEpic() throws ManagerSaveException {
        ArrayList<Integer> subtasksIds = new ArrayList<>();
        subtasksIds.add(4);
        subtasksIds.add(5);
        Epic newEpic = new Epic(4,"Эпик 1. Обновлен", "Описание Эпика 1",  Status.NEW, subtasksIds);
        fileBackedTaskManager.updateEpic(newEpic);
        Epic actualEpic = fileBackedTaskManager.getEpic(4);
        assertEquals(newEpic, actualEpic, "Эпики не совпадают");
    }

    @Test
    public void deleteEpic() throws ManagerSaveException {
        fileBackedTaskManager.deleteEpic(4);
        Epic expected = fileBackedTaskManager.getEpic(4);
        assertNull(expected, "Эпик удален.");
    }
}
