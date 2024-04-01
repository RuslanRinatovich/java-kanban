import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import main.HistoryManager;
import main.Managers;
import main.TaskManager;
import main.httphandlers.LocalDateTimeTypeAdapter;
import main.models.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserListTypeToken extends TypeToken<List<Task>> {
    // здесь ничего не нужно реализовывать
}


public class HttpTaskServerTest {
    private static HttpTaskServer httpTaskServer = new HttpTaskServer();
    ;

    @BeforeEach
    public void setUp() throws IOException {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.setUp();
        httpTaskServer.createServer();
        httpTaskServer.start();
    }

    @AfterAll
    public static void destroy() throws IOException {
        httpTaskServer.stop();
    }

    private Gson getDefaultGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }
    // получение всех задач
    @Test
    void CheckGetTasksReturnCode200AndTasksCountEqualToThree() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri).header("Accept", "application/json;charset=UTF-8")
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        List<Task> taskList = getDefaultGson().fromJson(response.body(), new UserListTypeToken().getType());
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(3, taskList.size(), "Не верное количество задач");
    }
    // получение одной задачи с id 1
    @Test
    void CheckGetTasksWithId1ReturnCode200AndActualTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri).header("Accept", "application/json;charset=UTF-8")
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        Task expected = new Task(1,"Задача 1", "Описание задачи 1", Status.NEW, Duration.ofMinutes(30), null);
        Task actual = getDefaultGson().fromJson(response.body(), Task.class);
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(expected, actual, "Задачи не совпали");
    }
    // получение всех подзадач
    @Test
    void CheckGetSubtasksReturnCode200AndSubtasksCountEqualToThree() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri).header("Accept", "application/json;charset=UTF-8")
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        List<Subtask> taskList = getDefaultGson().fromJson(response.body(), new UserListTypeToken().getType());
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(6, taskList.size(), "Не верное количество задач");
    }
    // получение одной подзадачи с id 7
    @Test
    void CheckGetSubtasksWithId1ReturnCode200AndActualTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/subtasks/7");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri).header("Accept", "application/json;charset=UTF-8")
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int expectedResponseCode = 200;
        int actualResponseCode = response.statusCode();
        Subtask expected = new Subtask(7,"Подзадача 1", "Описание подзадачи 1",  Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2024,3,28,13,0),4);
        Subtask actual = getDefaultGson().fromJson(response.body(), Subtask.class);
        assertEquals(expectedResponseCode, actualResponseCode, "Коды не совпадают");
        assertEquals(expected, actual, "Задачи не совпали");
    }
}

