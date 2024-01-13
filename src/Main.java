import java.util.Scanner;


public class Main {
    static Scanner scanner;
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        System.out.println("Поехали!");
        while (true) {
            TaskMenu.printMenu();
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    TaskMenu.addTask();
                    break;
                case "2":
                    TaskMenu.showAllTasks();
                    break;
                case "3":
                    TaskMenu.clearAllTasks();
                    break;
                case "4":
                    TaskMenu.getTaskById();
                    break;
                case "5":
                    TaskMenu.updateTask();
                    break;
                case "6":
                    TaskMenu.deleteTask();
                    break;
                case "7":
                    TaskMenu.changeTaskStatus();
                    break;
                case "8":
                    return;
            }
        }

    }


}
