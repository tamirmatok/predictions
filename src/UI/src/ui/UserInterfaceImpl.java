package ui;
import dto.impl.MessageDTO;
import dto.impl.PRDEnvDTO;
import dto.impl.PrdWorldDTO;
import dto.impl.PropertiesDTO;
import dto.impl.simulation.EntityReport;
import dto.impl.simulation.PropertyValueCount;
import dto.impl.simulation.SimulationDTO;
import dto.impl.simulation.SimulationReport;
import engine.impl.Engine;
import engine.schema.generated.*;

import java.util.*;

public class UserInterfaceImpl implements userInterface {

    private final Engine engine;
    private int userChoice;
    private boolean systemLoaded;
    private boolean exit;

    private boolean backToMainMenuPressed;

    int backToMainMenuIndex;


    public UserInterfaceImpl() {
        engine = new Engine();
        userChoice = 0;
        systemLoaded = false;
        exit = false;
        backToMainMenuPressed = false;
        backToMainMenuIndex = -1;
    }

    public int getUserChoice(int from, int to) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            try {
                System.out.print("Please enter your choice: ");
                userChoice = scanner.nextInt();
                if (userChoice >= from && userChoice <= to) {
                    return userChoice;
                }
                System.out.println("Invalid choice - number should be between " + from + " to " + to);

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
                this.sendRunSimulationRequest();
                break;
            case 4:
                this.presentSimulationReports();
                break;
            case 5:
                exit = true;
                break;
        }
    }

    private SimulationDTO sendGetSimulationReportRequest() {
        return engine.getSimulationReports();
    }

    private void sendRunSimulationRequest() {
        this.sendSetEnvVariablesRequests();
        if (!backToMainMenuPressed) {
            System.out.println("Simulation is running... please wait !");
            MessageDTO messageDTO = engine.startSimulation();
            if (messageDTO.isSuccess()) {
                System.out.println("Simulation run finish successfully!");
                System.out.println("Termination cause : " + messageDTO.getMessage());
            } else {
                System.out.println("Engine Error: " + messageDTO.getMessage());
            }
        }
        else {
            backToMainMenuPressed = false;
        }
    }


    private void sendSetEnvVariablesRequests() {
        Scanner scanner = new Scanner(System.in);
        MessageDTO messageDTO = engine.resetActiveEnvironment();
        PRDEnvDTO prdEnvDTO = engine.getEnvState();
        if (!prdEnvDTO.isSuccess() || !messageDTO.isSuccess()) {
            System.out.println("Failed to get environment state - please load system XML file first");
            backToMainMenuPressed = true;
        }
        else {
            PRDEvironment prdEnvironment = prdEnvDTO.getPRDEnv();
            System.out.println("\n------------------------Set env properties------------------------");
            System.out.println("Please select env property to set a value (default is random initialize)");
            int index;
            boolean donePressed = false;
            backToMainMenuPressed = false;
            do {
                index = 0;
                for (PRDEnvProperty prdEnvProperty : prdEnvironment.getPRDEnvProperty()) {
                    System.out.println((index + 1) + " - Env property name: " + prdEnvProperty.getPRDName());
                    index++;
                }
                System.out.println((prdEnvironment.getPRDEnvProperty().size() + 1) + " - Save and done");
                System.out.println((prdEnvironment.getPRDEnvProperty().size() + 2) + " - Cancel and get back to main menu");
                int doneIndex = prdEnvironment.getPRDEnvProperty().size() + 1;
                int getBackToMenuIndex = prdEnvironment.getPRDEnvProperty().size() + 2;
                int choice = getUserChoice(1, getBackToMenuIndex);
                if (choice == doneIndex) {
                    donePressed = true;
                    PropertiesDTO propertiesDTO = engine.setEnvVariables();
                    if (!propertiesDTO.isSuccess()) {
                        System.out.println("Failed to init env properties");
                    } else {
                        HashMap<String, String> data = propertiesDTO.getData();
                        System.out.println("\nEnv properties been set successfully");
                        System.out.println("\n------------------------Set env properties------------------------\n");
                        System.out.println("\n*********** Env properties initialize summarize ***********\n");
                        for (String properyName : data.keySet()) {
                            System.out.println("\tEnv property name: " + properyName + ", Env property value: " + data.get(properyName));
                        }
                        System.out.println("\n*********** Env properties initialize summarize ***********\n");
                    }
                }
                else if (choice == getBackToMenuIndex) {
                    backToMainMenuPressed = true;
                    System.out.println("Env properties didn't initialized.");
                    System.out.println("\n------------------------Set env properties------------------------\n");
                } else {
                    PRDEnvProperty prdEnvProperty = prdEnvironment.getPRDEnvProperty().get(choice - 1);
                    System.out.println("\n---------------- Env properties initialize - " + prdEnvProperty.getPRDName() +  " ----------------");
                    System.out.println("You chose to set property " + prdEnvProperty.getPRDName() + ".");
                    System.out.println("\tProperty type: " + prdEnvProperty.getType());
                    if (prdEnvProperty.getPRDRange() != null) {
                        String from;
                        String to;
                        if (prdEnvProperty.getType().equals("decimal")){
                            from = String.valueOf((int) prdEnvProperty.getPRDRange().getFrom());
                            to = String.valueOf((int) prdEnvProperty.getPRDRange().getTo());
                        }
                        else {
                            from =  String.valueOf(prdEnvProperty.getPRDRange().getFrom());
                            to =  String.valueOf(prdEnvProperty.getPRDRange().getTo());
                        }
                        System.out.println("\tProperty range: " + from + "-" + to);
                        System.out.println("Please enter a value of type " + prdEnvProperty.getType() +  " between the range: ");
                    } else {
                        System.out.println("Please enter a value: ");
                    }
                    Object value = scanner.nextLine();
                    messageDTO = engine.setEnvVariable(prdEnvProperty.getPRDName(), value);
                    if (!messageDTO.isSuccess()) {
                        System.out.println("\n*********** Set " + prdEnvProperty.getPRDName() + " problem" + " ***********\n");
                        System.out.println("Failed to set environment property " + prdEnvProperty.getPRDName() + " to " + value);
                        System.out.println(messageDTO.getMessage());
                        System.out.println("Please try again (: ");
                        System.out.println("\n*********** Set " + prdEnvProperty.getPRDName() + " problem" + " ***********");
                        System.out.println("\n---------------- Env properties initialize - " + prdEnvProperty.getPRDName() +  " ----------------\n");
                    } else {
                        System.out.println(messageDTO.getMessage());
                        System.out.println("\n---------------- Env properties initialize - " + prdEnvProperty.getPRDName() +  "----------------\n");
                    }
                }
            }
            while (!donePressed && !backToMainMenuPressed);
        }
    }


    private void presentSimulationReports() {
        SimulationDTO simulationDTO = this.sendGetSimulationReportRequest();
        if (!simulationDTO.isSuccess()) {
            System.out.println(simulationDTO.getErrorMessage());

        } else if (simulationDTO.getData().size() == 0) {
            System.out.println("No simulation records found.");
            System.out.println("\n--------------Simulation reports----------------");

        } else {
            do {
                int backToMenuIndex = simulationDTO.getSimulations().size() + 1;
                System.out.println("Please select number of simulation: ");
                for (SimulationReport simulationReport : simulationDTO.getSimulations()) {
                    System.out.println(simulationReport.getSimulationId() + " - " + simulationReport.getTimestamp());
                }
                System.out.println(backToMenuIndex + " - Back to main menu");
                int userChoice = getUserChoice(1, simulationDTO.getSimulations().size() + 1);
                if (userChoice != backToMenuIndex) {
                    SimulationReport simulationReport = simulationDTO.getSimulations().get(userChoice - 1);
                    System.out.println("\n-------------Simulation view-------------");
                    System.out.println("Please select view method:\n1. Quantity\n2. Histogram of property\n3. Back to main menu");
                    userChoice = getUserChoice(1, 3);
                    System.out.println("\n-------------Simulation view-------------");

                    switch(userChoice){
                        case 1:
                            this.printQuantityViewReport(simulationReport);
                            break;
                        case 2:
                            this.HistogramViewReport(simulationReport);
                            break;
                        case 3:
                            backToMainMenuPressed = true;
                    }
                } else {
                    backToMainMenuPressed = true;
                }
            }
            while (!backToMainMenuPressed);
        }
    }


    private void HistogramViewReport(SimulationReport simulationReport) {
        System.out.println("\n----------- Simulation Histogram view -----------");
        PropertyValueCount chosenPropertyValueCount = getPropertyToView(simulationReport);

        if (!backToMainMenuPressed && chosenPropertyValueCount != null) {
            String propertyName = chosenPropertyValueCount.getPropertyName();
            System.out.println("\n--- " + propertyName + " Histogram" + " ---");
            HashMap<String, Integer> valueCounts = chosenPropertyValueCount.getPropertyValueCount();

            // Sort By value count to present it
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(valueCounts.entrySet());
            entryList.sort(Map.Entry.comparingByValue());

            LinkedHashMap<String, Integer> sortedValueCounts = new LinkedHashMap<>();
            for (Map.Entry<String, Integer> entry : entryList) {
                sortedValueCounts.put(entry.getKey(), entry.getValue());
            }

            // Print the sorted HashMap
            for (Map.Entry<String, Integer> entry : sortedValueCounts.entrySet()) {
                int count = entry.getValue();
                String propertyValue = entry.getKey();
                System.out.print(propertyValue + " |");
                for (int i = count; i > 0; i--) {
                    System.out.print("*");
                }
                System.out.println("--> " + count);
            }
        }
        else{
            System.out.println("\n----------- Simulation Histogram view -----------");
        }
    }


    private PropertyValueCount getPropertyToView(SimulationReport simulationReport) {
        System.out.println("Please select entity name you would like to view");
        backToMainMenuIndex = simulationReport.getEntityReports().size() + 1;

        for (int i = 1; i <= simulationReport.getEntityReports().size() + 1; i++) {
            if (i == backToMainMenuIndex) {
                System.out.println(i + ". Back to main menu");
            }
            else {
                EntityReport entityReport = simulationReport.getEntityReports().get(i-1);
                System.out.println(i + ". " + entityReport.getEntityName());
            }
        }
        int userChoice = getUserChoice(1, simulationReport.getEntityReports().size() + 1);

        if (userChoice != backToMainMenuIndex) {
            EntityReport chosenEntityReport = simulationReport.getEntityReports().get(userChoice - 1);
            if (chosenEntityReport.getFinalPopulation() == 0) {
                System.out.println("Entity final population is 0, no histogram could be made.");
                return null;
            }
            System.out.println("Please select property name: ");
            backToMainMenuIndex = chosenEntityReport.getPropertyValueCounts().size() + 1;
            for (int i = 1; i <= chosenEntityReport.getPropertyValueCounts().size() + 1; i++) {
                if (i == backToMainMenuIndex) {
                    System.out.println(i + ". Back to main menu");
                }
                else {
                    PropertyValueCount propertyValueCount = chosenEntityReport.getPropertyValueCounts().get(i-1);
                    System.out.println(i + ". " + propertyValueCount.getPropertyName());
                }
            }
            userChoice = getUserChoice(1, chosenEntityReport.getPropertyValueCounts().size() + 1);
            if (userChoice != backToMainMenuIndex) {
                return chosenEntityReport.getPropertyValueCounts().get(userChoice - 1);
            }
        }
        backToMainMenuPressed = true;
        return null;
    }




    private void printQuantityViewReport(SimulationReport simulationReport){
        System.out.println("\n--- Simulation quantity view ---");
        for (EntityReport entityReport : simulationReport.getEntityReports()) {
            System.out.println("Entity name: " + entityReport.getEntityName());
            System.out.println("\tInitial Population: " + entityReport.getInitialPopulation());
            System.out.println("\tFinal population: " + entityReport.getFinalPopulation());
        }
        System.out.println("\n--- Simulation Report ---");
    }


    private void sendGetSimulationStateRequest() {
        PrdWorldDTO prdWorldDTO = engine.getSimulationState();
        if (prdWorldDTO.isSuccess()) {
            this.printPRDWorld(prdWorldDTO.getPrdWorld());
        } else {
            System.out.println("no world loaded");
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


    public void sendLoadXmlFileRequest() {
        Scanner scanner = new Scanner(System.in);
        systemLoaded = false;
        do {
            System.out.println("Please enter system XML file path or enter 5 to get back to main menu: ");
            String path = scanner.nextLine();
            if (path.equals("5")) {
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
