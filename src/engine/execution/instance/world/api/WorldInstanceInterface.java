package engine.execution.instance.world.api;

import engine.schema.generated.PRDWorld;

public interface WorldInstanceInterface {
    void run();
    void stop();

    void setWorld(PRDWorld prdWorld);
}
