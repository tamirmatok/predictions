package engine.world;

import schema.generated.PRDWorld;

public class World {
    private Entities entities;
    private Environment environment;
    private Rules rules;
    private Termination termination;


    public void setWorld(PRDWorld jaxbPrdWorld) {
//        this.entities = new Entities(jaxbPrdWorld.getPRDEntities());
        this.environment = new Environment(jaxbPrdWorld.getPRDEvironment());
//        this.rules = new Rules(jaxbPrdWorld.getPRDRules());
//        this.termination = new Termination(jaxbPrdWorld.getPRDTermination());
    }
}
