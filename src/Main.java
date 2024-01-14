import java.util.Scanner;


public class Main {
    static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        System.out.println("Поехали!");
        while (true) {
            printMainMenu();
            String command = scanner.nextLine();
            switch (command) {
                case "1":
                    TaskMenu.showTaskMenu();
                    break;
                case "2":
                    EpicMenu.showEpicMenu();
                    break;
                case "3":
                    SubtaskMenu.showSubtaskMenu();
                    break;
                case "4":
                    return;
            }
        }

    }

    public static void printMainMenu() {
        System.out.println("Выберите команду:");
        System.out.println("1 - Работа с задачами");
        System.out.println("2 - Работа с эпиками");
        System.out.println("3 - Работа с подзадачами");
        System.out.println("4 - Выход");
    }


}
