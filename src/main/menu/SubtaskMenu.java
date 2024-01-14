package main.menu;

import main.models.Epic;
import main.models.Status;
import main.models.Subtask;
import main.TaskManager;

import java.util.Scanner;

public class SubtaskMenu {

    static Scanner scanner = new Scanner(System.in);

    public static void showSubtaskMenu() {
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            switch (command) {
                case "1":
                    addSubtask();
                    break;
                case "2":
                    showAllSubtasks();
                    break;
                case "3":
                    clearAllSubtasks();
                    break;
                case "4":
                    getSubtaskById();
                    break;
                case "5":
                    updateSubtask();
                    break;
                case "6":
                    deleteSubtask();
                    break;
                case "7":
                    changeSubtaskStatus();
                    break;
                case "8":
                    return;
            }
        }
    }

    public static void printMenu() {
        System.out.println("Выберите команду:");
        System.out.println("1 - Создать новую подзадачу");
        System.out.println("2 - Получение списка всех подзадач");
        System.out.println("3 - Удаление всех подзадач");
        System.out.println("4 - Получение подзадачи по идентификатору.");
        System.out.println("5 - Обновить подзадачу");
        System.out.println("6 - Удалить подзадачу");
        System.out.println("7 - Изменить статус подзадачи");
        System.out.println("8 - Выход");
    }

    //1 - Создать новую задачу
    public static void addSubtask() {
        System.out.println("Введите название подзадачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание подзадачи:");
        String description = scanner.nextLine();
        System.out.println("Введите идентификатор эпика:");
        int id = scanner.nextInt();
        scanner.nextLine();
        if (TaskManager.epicHashMap.containsKey(id)) {
            Subtask newSubtask = new Subtask(title, description, TaskManager.getNewSubtaskId(), Status.NEW, id);
            TaskManager.addSubtask(newSubtask);
        } else {
            System.out.println("Ошибка. Неправильный идентификатор эпика");
        }
    }

    //2 - Получение списка всех подзадач
    public static void showAllSubtasks() {
        System.out.println("Список всех подзадач");
        System.out.println("--------------------------");
        for (Subtask t : TaskManager.getAllSubtasks()) {
            Epic epic = TaskManager.getEpicById(t.getEpicId());
            System.out.println("Эпик: " + epic.getTitle() + "\n" + t);
        }
        System.out.println("--------------------------");

    }

    // 3 - Удаление всех подзадач
    public static void clearAllSubtasks() {
        System.out.println("Вы действительно хотите очистить список подзадач? Y/N");
        String answer = scanner.nextLine();
        if (answer.equals("Y") || answer.equals("y")) {
            TaskManager.deleteAllSubtasks();
            System.out.println("Cписок подзадач очищен");
        } else {
            System.out.println("Очистка прервана");
        }
    }

    // 4 - Получение по идентификатору.
    public static void getSubtaskById() {
        System.out.println("Введите идентификатор подзадачи");
        int id = scanner.nextInt();
        scanner.nextLine();
        Subtask t = TaskManager.getSubtaskById(id);
        if (t != null) {
            Epic epic = TaskManager.getEpicById(t.getEpicId());
            System.out.println("Эпик: " + epic.getTitle());
            System.out.println(t);
        } else {
            System.out.println("Ошибка, не верный идентификатор");
        }
    }

    // 5 - Обновить задачу"
    public static void updateSubtask() {
        System.out.println("Введите идентификатор подзадачи:");
        int id = scanner.nextInt();
        scanner.nextLine();
        Subtask t = TaskManager.getSubtaskById(id);
        if (t != null) {
            System.out.println("Введите название подзадачи:");
            String title = scanner.nextLine();
            System.out.println("Введите описание подзадачи:");
            String description = scanner.nextLine();
            System.out.println("Введите статус подзадачи:\n1 - NEW,\n" +
                    "2 - IN_PROGRESS,\n" +
                    "3 - DONE");
            int statusNumber = scanner.nextInt();
            scanner.nextLine();
            Status status;
            switch (statusNumber) {
                case 1:
                    status = Status.NEW;
                    break;
                case 2:
                    status = Status.IN_PROGRESS;
                    break;
                case 3:
                    status = Status.DONE;
                    break;
                default:
                    System.out.println("Ошибка, не корректный статус");
                    return;
            }
            // при обновлении задачи, возможно нужно будет поменять эпик
            System.out.println("Введите идентификатор эпика:");
            int epicId = scanner.nextInt();
            scanner.nextLine();
            Epic epic = TaskManager.getEpicById(id);
            if (epic != null) {
                TaskManager.deleteSubtask(t.getId());
                Subtask newTask = new Subtask(title, description, id, status, epicId);
                TaskManager.updateSubtask(newTask);
                System.out.println("Подзадача обновлена");
            } else {
                System.out.println("Ошибка, не верный идентификатор эпика");
                return;
            }
        } else {
            System.out.println("Ошибка, не верный идентификатор");
        }

    }

    //6 - Удалить задачу
    public static void deleteSubtask() {
        System.out.println("Введите идентификатор подзадачи");
        int id = scanner.nextInt();
        Subtask t = TaskManager.getSubtaskById(id);
        if (t != null) {
            TaskManager.deleteSubtask(id);
            System.out.println("Подзадача удалена");
        } else {
            System.out.println("Ошибка, не верный идентификатор");
        }
    }

    //7 - Изменить статус задачи
    public static void changeSubtaskStatus() {
        System.out.println("Введите идентификатор подзадачи");
        int id = scanner.nextInt();
        Subtask t = TaskManager.getSubtaskById(id);
        if (t != null) {
            System.out.println("Введите статус подзадачи:\n1 - NEW,\n" +
                    " 2 - IN_PROGRESS,\n" +
                    " 3 - DONE");
            int statusNumber = scanner.nextInt();
            scanner.nextLine();
            Status status;
            switch (statusNumber) {
                case 1:
                    status = Status.NEW;
                    break;
                case 2:
                    status = Status.IN_PROGRESS;
                    break;
                case 3:
                    status = Status.DONE;
                    break;
                default:
                    System.out.println("Ошибка, не корректный статус");
                    return;
            }
            TaskManager.changeSubtaskStatus(t, status);
            System.out.println("Статус обновлен");
        } else {
            System.out.println("Ошибка, не верный идентификатор");
        }
    }
}

