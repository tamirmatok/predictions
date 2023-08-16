package engine.execution.instance.world.api;

import dto.impl.MessageDTO;
import engine.definition.environment.api.EnvVariablesManager;
import engine.execution.instance.enitty.manager.EntityInstanceManager;
import engine.rule.Rule;
import engine.schema.generated.PRDWorld;

import java.util.ArrayList;

public interface WorldInstanceInterface {
    MessageDTO run();
    void stop();

}
