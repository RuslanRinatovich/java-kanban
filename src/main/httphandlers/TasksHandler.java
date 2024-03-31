package main.httphandlers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.TaskManager;
import main.models.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TasksHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;
    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), body);

        JsonElement jsonElement = JsonParser.parseString(body);
        switch (endpoint) {
            case GET_COLLECTION: {
                handleGetTasks(exchange);
                break;
            }
            case GET_ONE: {
                handleGetTask(exchange);

                break;
            }
            case ADD: {
                handleAddTask(exchange);
                break;
            }
            case DELETE: {
                handleDeleteTask(exchange);
                break;
            }
            case UPDATE: {
                handleUpdateTask(exchange);
                break;
            }
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter());
        Gson gson = gsonBuilder.create();
        String response = gson.toJson(taskManager.getTasks());
        writeResponse(exchange, response, 200);
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {

    }

    private void handleAddTask(HttpExchange exchange) throws IOException {

    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {

    }

    private void handleUpdateTask(HttpExchange exchange) throws IOException {

    }

    private String TaskToJson(Task task)
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter());
        Gson gson = gsonBuilder.create();
        return gson.toJson(task);
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
    private Endpoint getEndpoint(String requestPath, String requestMethod, String body) {
        String[] pathParts = requestPath.split("/");
        // анализируем какой метод TaskManagera нужен
        switch (requestMethod)
        {
            case "GET":
            {
                // вернуть json задач
                if (pathParts.length == 2)
                    return Endpoint.GET_COLLECTION;
                if (pathParts.length == 3) {
                    try {
                        int id = Integer.parseInt(pathParts[2]);
                    } catch (NumberFormatException e) {
                        return Endpoint.UNKNOWN;
                    }
                    // вернуть одну задачу
                    return Endpoint.GET_ONE;
                }
            }
            case "POST":
            {
                JsonElement jsonElement = JsonParser.parseString(body);
                if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
                    return Endpoint.UNKNOWN;
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("id"))
                    return  Endpoint.UPDATE;
                else
                    return Endpoint.ADD;
            }
            case "DELETE":
            {
                JsonElement jsonElement = JsonParser.parseString(body);
                if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
                    return Endpoint.UNKNOWN;
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("id"))
                    return  Endpoint.DELETE;
                else
                    return Endpoint.UNKNOWN;
            }
            default:
                return Endpoint.UNKNOWN;

        }


    }
}

class TaskListTypeToken extends TypeToken<List<Task>> {
}

class LocalTimeTypeAdapter extends TypeAdapter<LocalTime> {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalTime localTime) throws IOException {
        jsonWriter.value(localTime.format(timeFormatter));
    }

    @Override
    public LocalTime read(final JsonReader jsonReader) throws IOException {
        return LocalTime.parse(jsonReader.nextString(), timeFormatter);
    }
}