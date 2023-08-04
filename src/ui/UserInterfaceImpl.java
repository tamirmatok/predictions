package ui;
import engine.Engine;
import schema.generated.PRDWorld;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterfaceImpl implements userInterface {

    private final Engine engine;
    private int userChoice;


    public UserInterfaceImpl() {
        engine = new Engine();
        userChoice = 0;
    }

    @Override
    public void printMenu() {
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
                System.out.println("Invalid number, please select valid number");

            } catch (InputMismatchException e) {
                System.out.println("Invalid choice, please select valid number");
            }
        }
    }

    @Override
    public void choiceHandler(int choice) {
        switch (choice) {
            case 1:
                try {
                    loadXmlFile();
                    //TODO: DELERE -> JUST VALIDATE THAT jaxbWorld load successfully
                    System.out.println("jaxbWorld loaded successfully");
                }
                catch (JAXBException | IOException e) {
                    System.out.println(e.getMessage());
                }
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
//                System.out.println("exit");
                break;
            default:
//                System.out.println("Invalid choice");
                break;
        }

    }

    public void  loadXmlFile() throws JAXBException, IOException {
        Scanner scanner = new Scanner(System.in);
        Xml xml = new Xml();
        do {
            System.out.println("please enter system xml file path");
            xml.setPath(scanner.nextLine());

            if (xml.isValidXMLPath()){
                engine.loadSystemFromXmlFile(xml.getFile());
            }
            else{
                System.out.println("Invalid path. please try again");
            }
        }
        while (!xml.isValidXMLPath());

        System.out.println("xml file load successfully");
    }
}
