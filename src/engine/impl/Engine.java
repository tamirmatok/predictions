package engine.impl;

import dto.impl.MessageDTO;
import dto.impl.PRDEnvDTO;
import dto.impl.PrdWorldDTO;
import dto.impl.PropertiesDTO;
import dto.impl.simulation.SimulationDTO;
import dto.impl.simulation.SimulationReport;
import engine.api.EngineInterface;
import engine.definition.environment.api.EnvVariablesManager;
import engine.definition.property.api.PropertyDefinition;
import engine.definition.world.impl.WorldDefinitionImpl;
import engine.execution.instance.environment.api.ActiveEnvironment;
import engine.execution.instance.property.PropertyInstance;
import engine.execution.instance.property.PropertyInstanceImpl;
import engine.execution.instance.world.impl.WorldInstance;
import engine.schema.generated.PRDEvironment;
import engine.simulator.impl.Simulator;
import engine.system.file.impl.FileSystem;
import engine.schema.generated.PRDWorld;

import java.util.ArrayList;
import java.util.HashMap;

public class Engine implements EngineInterface {

    private FileSystem fileSystem;
    private  WorldDefinitionImpl worldDefinition;
    private WorldInstance worldInstance;
    private boolean isWorldLoaded = false;
    private PRDWorld prdWorld;

    private final ArrayList<SimulationReport> simulationReports;


    public Engine() {
        this.worldDefinition = new WorldDefinitionImpl();
        this.simulationReports = new ArrayList<SimulationReport>();
    };

    // option number 1 - load world from xml file
    @Override
    public MessageDTO loadSystemWorldFromXmlFile(String xmlFilePath){
        try {
            this.resetWorld();
            this.fileSystem = new FileSystem(xmlFilePath);
            this.fileSystem.loadXmlFile();
            this.prdWorld = this.fileSystem.loadJaxbWorldFromXmlFile();
            this.worldDefinition.loadWorldDefintion(prdWorld);
            isWorldLoaded = true;

        } catch (Exception e) {
            this.resetWorld();
            return new MessageDTO(false, e.getMessage());
        }
        return new MessageDTO(true, "Xml file loaded successfully");
    }

    // option number 2 - get simulation state
    @Override
    public PrdWorldDTO getSimulationState() {
        if (!isWorldLoaded) {
            return new PrdWorldDTO(false, new PRDWorld());
        }
        return new PrdWorldDTO(true, prdWorld);
    }

    @Override
    public PRDEnvDTO getEnvState() {
        if (!isWorldLoaded) {
            return new PRDEnvDTO(false, new PRDEvironment());
        }
        else{
            return new PRDEnvDTO(true, prdWorld.getPRDEvironment());
        }
    }

    @Override
    public MessageDTO setEnvVariable(String envVariableName, Object value) {
        return this.worldInstance.setEnvVariable(envVariableName, value);
    }

    @Override
    public PropertiesDTO setEnvVariables() {
        try {
            HashMap<String, String> propertyNameToPropertyValue = new HashMap<String, String>();
            EnvVariablesManager envVariablesManager = worldInstance.getWorldDefinition().getEnvVariablesManager();
            ActiveEnvironment activeEnvironment = worldInstance.getActiveEnvironment();

            for (PropertyDefinition propertyDefinition : envVariablesManager.getEnvVariables()) {
                String envPropertyName = propertyDefinition.getName();
                try {
                    PropertyInstance propertyInstance = activeEnvironment.getProperty(envPropertyName);
                    propertyNameToPropertyValue.put(envPropertyName, propertyInstance.getValue().toString());
                } catch (Exception e) {
                    PropertyInstance newPropertyInstance = new PropertyInstanceImpl(propertyDefinition, propertyDefinition.generateValue());
                    propertyNameToPropertyValue.put(envPropertyName, newPropertyInstance.getValue().toString());
                    activeEnvironment.addPropertyInstance(newPropertyInstance);
                }
            }
            return new PropertiesDTO(true, propertyNameToPropertyValue);
        }
        catch (Exception e){
            return new PropertiesDTO(false, new HashMap<String, String>());
        }
    }

    @Override
    public MessageDTO resetActiveEnvironment() {
        if (this.worldInstance == null){
            return new MessageDTO(false, "world did not load yet.");
        }
        try {
            this.worldInstance.resetActiveEnvironment();
            return new MessageDTO(true,"Active environment reset successfully !");
        }
        catch(Exception e){
            return  new MessageDTO(false, e.getMessage());
        }
    }

    @Override
    public MessageDTO startSimulation() {
        try {
            if(isWorldLoaded) {
                //TODO: change next line - we need to load all world definition again just because of the initial population
                ActiveEnvironment activeEnvironment = this.worldInstance.getActiveEnvironment();
                this.worldDefinition.loadWorldDefintion(prdWorld);
                this.worldInstance = new WorldInstance(this.worldDefinition);
                this.worldInstance.setWorldInstance(activeEnvironment);
                Simulator simulation = new Simulator(worldInstance);
                SimulationReport simulationReport = simulation.runSimulation();
                this.simulationReports.add(simulationReport);
                return new MessageDTO(true, simulationReport.getCauseOfTermination());
            }
            else{
                return new MessageDTO(false, "World did not load yet.");
            }
        }
        catch (Exception e){
            return new MessageDTO(false, e.getMessage());
        }
    }

    private void resetWorld(){
        this.prdWorld = new PRDWorld();
        this.worldDefinition = new WorldDefinitionImpl();
        this.worldInstance = new WorldInstance(worldDefinition);
    }


    public SimulationDTO getSimulationReports(){
        return new SimulationDTO(true, simulationReports);
    }



}
