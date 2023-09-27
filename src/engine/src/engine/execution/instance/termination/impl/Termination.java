package engine.execution.instance.termination.impl;

import engine.execution.instance.termination.api.terminationInterface;

public class Termination implements terminationInterface {

    int ticksCount;
    int bySecondsCount;

    boolean byUser = false;

    public Termination(int ticksCount, int bySecondsCount) {
        this.ticksCount = ticksCount;
        this.bySecondsCount = bySecondsCount;
    }

    public Termination(boolean byUser){
        this.byUser = true;
    }

    public Termination() {
        this.ticksCount = 0;
        this.bySecondsCount = 0;
    }

    @Override
    public int getByTicks() {
        return this.ticksCount;
    }

    @Override
    public int getBySeconds() {
        return this.bySecondsCount;
    }

    @Override
    public void setByTicks(int ticksCount) {
        this.ticksCount = ticksCount;

    }
    @Override
    public void setBySeconds(int bySecondsCount) {
        this.bySecondsCount = bySecondsCount;
    }

    public boolean isByUser() {
        return byUser;
    }
    @Override
    public void setTerminationCondition(int ticks, int seconds) {

    }
}
