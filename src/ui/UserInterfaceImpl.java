package ui;
import dto.impl.MessageDTO;
import engine.impl.Engine;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterfaceImpl implements userInterface {

    private final Engine engine;
    private int userChoice;
    private boolean systemLoaded;

    private boolean exit;

    public UserInterfaceImpl() {
        engine = new Engine();
        userChoice = 0;
        systemLoaded = false;
        exit = false;
    }

    @Override
    public void printMenu() {
        System.out.println("Please select one of the following options:");
        System.out.println("1. load system xml file");
        System.out.println("2. show simulation state");
        System.out.println("3. run simulation");
        System.out.println("4. show past simulation results");
        System.out.println("5. exit");

    }
    @Override
    public int getUserChoice() {
        while (true) {
        Scanner scanner = new Scanner(System.in);
            try {
                System.out.print("Please enter your choice: ");
                userChoice = scanner.nextInt();
                if (userChoice >= 1 && userChoice <= 5) {
                    return userChoice;
                }
                System.out.println("Invalid choice - number should be between 1 to 5");

            } catch (InputMismatchException e) {

                System.out.println("Invalid input - please select a valid number");
            }
        }
    }

    @Override
    public void choiceHandler(int choice) {
        switch (choice) {
            case 1:
                SendLoadXmlFileRequest();
                break;
            case 2:
//                System.out.println("show simulation state");
                break;
            case 3:
//                System.out.println("run simulation");
                break;
            case 4:
//                System.out.println("show past simulation results");
                break;
            case 5:
                exit = true;
                break;
            default:
//                System.out.println("Invalid choice");
                break;
        }

    }

    @Override
    public void startInterface() {
        while (!exit) {
            printMenu();
            int choice = getUserChoice();
            choiceHandler(choice);
            if (choice == 5) {
                exit = true;
            }
        }
    }

    public void SendLoadXmlFileRequest() {
        Scanner scanner = new Scanner(System.in);
        systemLoaded = false;
        do {
            System.out.println("Please enter system XML file path or enter 5 to exit: ");
            String path = scanner.nextLine();
            if (path.equals("5")) {
                exit = true;
                return;
            }
            MessageDTO dto = engine.loadSystemWorldFromXmlFile(path);
            if (dto.isSuccess()) {
                System.out.println(("XML file loaded successfully !"));
                systemLoaded = true;
            } else {
                if (dto.getMessage() != null) {
                    System.out.println(("Failed to load XML file - " + dto.getMessage()));
                }
            }
        }
        while (!systemLoaded);
    }
}
