package engine.execution.instance.termination.api;

public interface terminationInterface {
    int getByTicks();
    int getBySeconds();
    void setByTicks(int ticks);
    void setBySeconds(int seconds);
    void setTerminationCondition(int ticks, int seconds);
}
