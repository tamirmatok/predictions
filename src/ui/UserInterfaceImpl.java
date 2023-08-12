package ui;
import dto.impl.MessageDTO;
import dto.impl.PrdWorldDTO;
import engine.impl.Engine;
import engine.schema.generated.PRDBySecond;
import engine.schema.generated.PRDByTicks;
import engine.schema.generated.PRDWorld;

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
        System.out.println("\nPlease select one of the following options:");
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
                this.sendLoadXmlFileRequest();
                break;
            case 2:
                this.sendGetSimulationStateRequest();
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

    private void sendGetSimulationStateRequest() {
        PrdWorldDTO prdWorldDTO = engine.getSimulationState();
        if (prdWorldDTO.isSuccess()) {
            this.printPRDWorld(prdWorldDTO.getPrdWorld());
        } else {
            System.out.println("Failed to get simulation state");
        }
    }

    private void printPRDWorld(PRDWorld prdWorld) {
        System.out.println("\n------------------------Simulation Definition------------------------");
        System.out.println("Entities:");
        prdWorld.getPRDEntities().getPRDEntity().forEach(entity -> {
            System.out.println("\tEntity name: " + entity.getName());
            System.out.println("\t\tEntity population: " + entity.getPRDPopulation());
            System.out.println("\t\tEntity properties:");
            entity.getPRDProperties().getPRDProperty().forEach(property -> {
                System.out.println("\t\tProperty name: " + property.getPRDName());
                System.out.println("\t\t\tProperty value: " + property.getType());
                if (property.getPRDRange() != null){
                    System.out.println("\t\t\tProperty range: " + property.getPRDRange().getFrom() + "-" + property.getPRDRange().getTo());
                }
                System.out.println("\t\t\tRandom initialize: " + property.getPRDValue().isRandomInitialize());
            });
        });
        System.out.println("Rules:");
        prdWorld.getPRDRules().getPRDRule().forEach(rule -> {
            String tickCount = "1 default";
            String probability = "1 default";
            int ruleActionCount = rule.getPRDActions().getPRDAction().size();

            if (rule.getPRDActivation() != null) {
                if (rule.getPRDActivation().getTicks() != null)
                    tickCount = rule.getPRDActivation().getTicks().toString();
                if (rule.getPRDActivation().getProbability() != null) {
                    probability = rule.getPRDActivation().getProbability().toString();
                }
            }
            System.out.println("\tRule name: " + rule.getName());
            System.out.println("\t\tActivation: ");
            System.out.println("\t\t\tTick Counts: " + tickCount);
            System.out.println("\t\t\tProbability: " + probability);
            System.out.println("\t\tNumber of actions: " + ruleActionCount);
            System.out.println("\t\tAction names:");
            rule.getPRDActions().getPRDAction().forEach(action -> {
                System.out.println("\t\t\t" + action.getType());
            });
        });

        System.out.println("Termination:");
        prdWorld.getPRDTermination().getPRDByTicksOrPRDBySecond().forEach(termination -> {
            if (termination instanceof PRDByTicks) {
                System.out.println("\tBy ticks: " + ((PRDByTicks) termination).getCount());
            } else if (termination instanceof PRDBySecond){
                System.out.println("\tBy seconds: " + ((PRDBySecond) termination).getCount());
            }
        });
        System.out.println("------------------------Simulation Definition------------------------");

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

    public void sendLoadXmlFileRequest() {
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
