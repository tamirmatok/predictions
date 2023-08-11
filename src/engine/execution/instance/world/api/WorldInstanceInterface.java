package engine.execution.instance.world.api;

import engine.definition.environment.api.EnvVariablesManager;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.rule.Rule;
import engine.schema.generated.PRDWorld;

import java.util.ArrayList;

public interface WorldInstanceInterface {
    void run();
    void stop();

}
