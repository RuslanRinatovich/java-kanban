package main.httphandlers;

import com.google.gson.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.TaskManager;
import main.models.ManagerSaveException;
import main.models.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import static java.net.HttpURLConnection.*;

public class TasksHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    // метод для разбора строки из параметров на пары ключ и значение в HashMap
    public Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println(exchange.getRequestURI());
        Endpoint endpoint = getEndpoint(String.valueOf(exchange.getRequestURI()), exchange.getRequestMethod());
        System.out.println(endpoint.toString());

        switch (endpoint) {
            case GET_COLLECTION: {
                handleGetTasks(exchange);
                System.out.println("Get tasks");
                break;
            }
            case GET_ONE: {
                handleGetTask(exchange);
                System.out.println("Get one task");
                break;
            }
            case ADD: {
                System.out.println("try add task");
                handleAddTask(exchange);
                break;
            }
            case DELETE: {
                System.out.println("try delete task");
                handleDeleteTask(exchange);
                break;
            }
            case UPDATE: {
                System.out.println("try update task");
                handleUpdateTask(exchange);
                break;
            }
            case UNKNOWN: {
                writeResponse(exchange, "Not Found", HTTP_NOT_FOUND);
                break;
            }
            default:
                writeResponse(exchange, "Not Found", HTTP_NOT_FOUND);
        }
    }

    // обработчик запроса на получение всех задач
    private void handleGetTasks(HttpExchange exchange) throws IOException {
        Gson gson = getDefaultGson();
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        String response = gson.toJson(taskManager.getTasks());
        writeResponse(exchange, response, HTTP_OK);

    }

    // обработчик запроса на получение одной задачи
    private void handleGetTask(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        int taskId = Integer.parseInt(pathParts[2]);
        Gson gson = getDefaultGson();
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        Task task = taskManager.getTask(taskId);
        if (task != null) {
            String response = gson.toJson(taskManager.getTask(taskId));
            writeResponse(exchange, response, HTTP_OK);
        } else {
            writeResponse(exchange, "Not Found", HTTP_NOT_FOUND);
        }
    }

    // обработчик запроса на добавление одной задачи
    private void handleAddTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(body);

        if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
            writeResponse(exchange, "Not Acceptable", HTTP_NOT_ACCEPTABLE);
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Gson gson = getDefaultGson();
        Task task = gson.fromJson(jsonObject, Task.class);
        try {
            taskManager.addTask(task);
            writeResponse(exchange, "Added", HTTP_CREATED);
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
            writeResponse(exchange, "Not Acceptable", HTTP_NOT_ACCEPTABLE);
        }

    }

    // обработчик запроса на удаление одной задачи
    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
        int id = Integer.parseInt(params.get("id"));
        try {
            taskManager.deleteTask(id);
            writeResponse(exchange, "Deleted", HTTP_CREATED);
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
            writeResponse(exchange, "Not Found", HTTP_NOT_FOUND);
        }

    }

    // обработчик запроса на обновление одной задачи
    private void handleUpdateTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
            writeResponse(exchange, "Not Acceptable", HTTP_NOT_ACCEPTABLE);
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Gson gson = getDefaultGson();
        Task task = gson.fromJson(jsonObject, Task.class);

        try {
            System.out.println(task.toString());
            taskManager.updateTask(task);
            writeResponse(exchange, "Updated", HTTP_CREATED);
        } catch (ManagerSaveException ex) {
            System.out.println(ex.getMessage());
            writeResponse(exchange, "Not Acceptable", HTTP_NOT_ACCEPTABLE);
        }
    }

    // Настройка json
    private Gson getDefaultGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    private Endpoint getEndpoint(String requestURI, String requestMethod) {
        // анализируем какой метод TaskManagera нужен
        switch (requestMethod) {
            case "GET": {
                // вернуть json задач
                if (Pattern.matches("^/tasks$", requestURI))
                    return Endpoint.GET_COLLECTION;
                if (Pattern.matches("^/tasks/\\d+$", requestURI))
                    return Endpoint.GET_ONE;
                return Endpoint.UNKNOWN;
            }
            case "POST": {
                if (Pattern.matches("^/tasks$", requestURI))
                    return Endpoint.ADD;
                if (Pattern.matches("^/tasks\\?id=\\d+$", requestURI))
                    return Endpoint.UPDATE;
                return Endpoint.UNKNOWN;
            }
            case "DELETE": {
                if (Pattern.matches("^/tasks\\?id=\\d+$", requestURI))
                    return Endpoint.DELETE;
                return Endpoint.UNKNOWN;
            }
            default:
                return Endpoint.UNKNOWN;

        }


    }
}


