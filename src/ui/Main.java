package ui;

public class Main {
    public static int choice;

    public static void main(String[] args) {

        try {
            userInterface ui = new UserInterfaceImpl();
            ui.printMenu();
            choice = ui.getUserChoice();
            ui.choiceHandler(choice);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
