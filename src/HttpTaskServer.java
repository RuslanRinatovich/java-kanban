import com.sun.net.httpserver.HttpServer;
import main.Managers;
import main.TaskManager;
import main.httphandlers.TasksHandler;
import main.models.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {

    private static final int PORT = 8080;
    public static TaskManager taskManager = Managers.getDefault();
    public static void main(String[] args) throws IOException {
        // инициализация начальных данных
        setUp();
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        // завершаем работу сервера для корректной работы тренажёра
       // httpServer.stop(1);
    }

   static void setUp() throws ManagerSaveException {
        Task task1 = new Task("Задача 1", "Описание задачи 1",  Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024,3,25,10,0));
        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024,3,26,11,0));
        Task task3 = new Task("Задача 3", "Описание задачи 3",  Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024,3,28,12,0));

        taskManager.addTask(task1); //1
        taskManager.addTask(task2);//2
        taskManager.addTask(task3);//3

        Epic epic1 = new Epic("Эпик 1", "Описание Эпика 1",  Status.NEW, Duration.ofMinutes(0), LocalDateTime.of(2024,1,1,0,0), new ArrayList<>());
        Epic epic2 = new Epic("Эпик 2", "Описание Эпика 2",  Status.NEW,Duration.ofMinutes(0), LocalDateTime.of(2024,1,1,0,0), new ArrayList<>());
        Epic epic3 = new Epic("Эпик 3", "Описание Эпика 3",  Status.NEW, Duration.ofMinutes(0),LocalDateTime.of(2024,1,1,0,0), new ArrayList<>());

        taskManager.addEpic(epic1);//4
        taskManager.addEpic(epic2);//5
        taskManager.addEpic(epic3);//6

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1",  Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024,3,28,13,0),epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2",    Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024,3,28,14,0),epic1.getId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3",  Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024,3,28,15,0),epic2.getId());
        Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4",   Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024,3,28,16,0),epic2.getId());
        Subtask subtask5 = new Subtask("Подзадача 5", "Описание подзадачи 5",   Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024,3,28,17,0),epic3.getId());
        Subtask subtask6 = new Subtask("Подзадача 6", "Описание подзадачи 6",   Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024,3,28,18,0),epic3.getId());
        taskManager.addSubtask(subtask1);//7
        taskManager.addSubtask(subtask2);//8
        taskManager.addSubtask(subtask3);//9
        taskManager.addSubtask(subtask4);//10
        taskManager.addSubtask(subtask5);//11
        taskManager.addSubtask(subtask6);//12

    }
}
