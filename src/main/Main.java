package main;

import main.models.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static TaskManager taskManager = Managers.getDefault();
    static Scanner scanner;

    public static void main(String[] args) throws IOException {

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.main();
    }


}
