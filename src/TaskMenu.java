import java.util.Scanner;

public class TaskMenu {
    static Scanner scanner = new Scanner(System.in);
    public static void printMenu() {
        System.out.println("Выберите команду:");
        System.out.println("1 - Создать новую задачу");
        System.out.println("2 - Получение списка всех задач");
        System.out.println("3 - Удаление всех задач");
        System.out.println("4 - Получение по идентификатору.");
        System.out.println("5 - Обновить задачу");
        System.out.println("6 - Удалить задачу");
        System.out.println("7 - Изменить статус задачи");
        System.out.println("8 - Выход");
    }
    //1 - Создать новую задачу
    public static void addTask() {
        System.out.println("Введите название задачи:");
        String title = scanner.nextLine();
        System.out.println("Введите описание задачи:");
        String description = scanner.nextLine();
        Task newTask = new Task(title, description, TaskManager.getNewIdTask(), Status.NEW);
        TaskManager.addTask(newTask);
    }
    //2 - Получение списка всех задач
    public static void showAllTasks() {
        System.out.println("Список всех задач");
        System.out.println("--------------------------");
        for (Task t: TaskManager.getAllTasks()) {
            System.out.println(t);
        }
        System.out.println("--------------------------");

    }
    // 3 - Удаление всех задач
    public static void clearAllTasks() {
        System.out.println("Вы действительно хотите очистить список задач? Y/N");
        String answer = scanner.nextLine();
        if (answer.equals("Y") || answer.equals("y")) {
            TaskManager.deleteAllTasks();
            System.out.println("Cписок задач очищен");
        } else {
            System.out.println("Очистка прервана");
        }
    }
    // 4 - Получение по идентификатору.
    public static void getTaskById() {
        System.out.println("Введите идентификатор задачи");
        int id = scanner.nextInt();
        scanner.nextLine();
        Task t = TaskManager.getTaskById(id);
        if (t != null) {
            System.out.println(t);
        } else {
            System.out.println("Ошибка, не верный идентификатор");
        }
    }
    // 5 - Обновить задачу"
    public static void updateTask() {
        System.out.println("Введите идентификатор задачи:");
        int id = scanner.nextInt();
        scanner.nextLine();
        Task t = TaskManager.getTaskById(id);
        if (t != null) {
            System.out.println("Введите название задачи:");
            String title = scanner.nextLine();
            System.out.println("Введите описание задачи:");
            String description = scanner.nextLine();
            System.out.println("Введите статус задачи:\n1 - NEW,\n" +
                    "2 - IN_PROGRESS,\n" +
                    "3 - DONE");
            int statusNumber = scanner.nextInt();
            scanner.nextLine();
            Status status;
            switch (statusNumber)
            {
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
            Task newTask = new Task(title, description, id, status);
            TaskManager.updateTask(newTask);
            System.out.println("Задача обновлена");
        } else {
            System.out.println("Ошибка, не верный идентификатор");
        }

    }

    //6 - Удалить задачу
    public static void deleteTask() {
        System.out.println("Введите идентификатор задачи");
        int id = scanner.nextInt();
        Task t = TaskManager.getTaskById(id);
        if (t != null) {
            TaskManager.deleteTask(id);
            System.out.println("Задача удалена");
        } else {
            System.out.println("Ошибка, не верный идентификатор");
        }
    }

    //7 - Изменить статус задачи
    public static void changeTaskStatus() {
        System.out.println("Введите идентификатор задачи");
        int id = scanner.nextInt();
        Task t = TaskManager.getTaskById(id);
        if (t != null) {
            System.out.println("Введите статус задачи: 1 - NEW,\n" +
                    " 2 - IN_PROGRESS,\n" +
                    " 3 - DONE");
            int statusNumber = scanner.nextInt();
            scanner.nextLine();
            Status status;
            switch (statusNumber)
            {
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
            TaskManager.changeTaskStatus(t, status);
            System.out.println("Статус обновлен");
        } else {
            System.out.println("Ошибка, не верный идентификатор");
        }
    }
}
