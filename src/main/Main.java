package main;

import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static TaskManager taskManager = Managers.getDefault();
    static Scanner scanner;

    public static void main(String[] args) throws IOException {

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.main();
    }
}
