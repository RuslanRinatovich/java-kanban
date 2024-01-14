import java.util.ArrayList;
import java.util.Scanner;

public class EpicMenu {
    static Scanner scanner = new Scanner(System.in);

    public static void showEpicMenu() {
        while (true) {
            printMenu();
            String command = scanner.nextLine();
            switch (command) {
                case "1":
                    addEpic();
                    break;
                case "2":
                    showAllEpics();
                    break;
                case "3":
                    clearAllEpics();
                    break;
                case "4":
                    getEpicById();
                    break;
                case "5":
                    updateEpic();
                    break;
                case "6":
                    deleteEpic();
                    break;
                case "7":
                    return;
            }
        }
    }

    public static void printMenu() {
        System.out.println("Выберите команду:");
        System.out.println("1 - Создать новый эпик");
        System.out.println("2 - Получение списка всех эпиков");
        System.out.println("3 - Удаление всех эпиков");
        System.out.println("4 - Получение эпика по идентификатору.");
        System.out.println("5 - Обновить эпик");
        System.out.println("6 - Удалить эпик");
        System.out.println("7 - Назад");
    }

    //1 - Создать новую задачу
    public static void addEpic() {
        System.out.println("Введите название эпика:");
        String title = scanner.nextLine();
        System.out.println("Введите описание эпика:");
        String description = scanner.nextLine();
        ArrayList<Integer> subtasksIds =  new ArrayList<>();
        Epic newEpic = new Epic(title, description, TaskManager.getNewEpiсId(), Status.NEW, subtasksIds);
        TaskManager.addEpic(newEpic);
        System.out.println("Новый эпик добавлен");
    }

    //2 - Получение списка всех подзадач
    public static void showAllEpics() {
        System.out.println("Список всех эпиков");
        System.out.println("--------------------------");
        for (Epic t : TaskManager.getAllEpics()) {
            System.out.println(t);

            if (t.getSubtasksIds().isEmpty())
            {
                System.out.println("---Список подзадач пуст");
            } else {
                System.out.println("---Подзадачи");
                for (int i: t.getSubtasksIds()) {
                    Subtask subtask = TaskManager.getSubtaskById(i);
                    System.out.println("\t" + subtask);
                }
                System.out.println("----------");
            }
        }
        System.out.println("--------------------------");

    }

    // 3 - Удаление всех подзадач
    public static void clearAllEpics() {
        System.out.println("Вы действительно хотите очистить список эпиков с подзадачами'? Y/N");
        String answer = scanner.nextLine();
        if (answer.equals("Y") || answer.equals("y")) {
            TaskManager.deleteAllEpics();
            System.out.println("Cписок эпиков очищен");
        } else {
            System.out.println("Очистка прервана");
        }
    }

    // 4 - Получение по идентификатору.
    public static void getEpicById() {
        System.out.println("Введите идентификатор эпика");
        int id = scanner.nextInt();
        scanner.nextLine();
        Epic t = TaskManager.getEpicById(id);
        if (t != null) {
            System.out.println(t);
            if (t.getSubtasksIds().isEmpty())
            {
                System.out.println("---Список подзадач пуст");
            } else {
                System.out.println("---Подзадачи");
                for (int i: t.getSubtasksIds()) {
                    Subtask subtask = TaskManager.getSubtaskById(i);
                    System.out.println("\t" + subtask);
                }
                System.out.println("----------");
            }
        } else {
            System.out.println("Ошибка, не верный идентификатор");
        }
    }

    // 5 - Обновить задачу"
    public static void updateEpic() {
        System.out.println("Введите идентификатор эпика:");
        int id = scanner.nextInt();
        scanner.nextLine();
        Epic t = TaskManager.getEpicById(id);
        if (t != null) {
            System.out.println("Введите название эпика:");
            String title = scanner.nextLine();
            System.out.println("Введите описание эпика:");
            String description = scanner.nextLine();
            t.setTitle(title);
            t.setDescription(description);
            System.out.println("Эпик обновлен");
        } else {
            System.out.println("Ошибка, не верный идентификатор");
        }

    }

    //6 - Удалить задачу
    public static void deleteEpic() {
        System.out.println("Введите идентификатор эпика");
        int id = scanner.nextInt();
        Epic t = TaskManager.getEpicById(id);
        if (t != null) {
            TaskManager.deleteEpic(id);
            System.out.println("Эпик удален");
        } else {
            System.out.println("Ошибка, не верный идентификатор");
        }
    }



}

