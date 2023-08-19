package engine.rule;

import java.util.Random;

public class ActivationImpl implements Activation{

    private int ticks;
    double probability;
    private final Random random = new Random();

    public ActivationImpl() {
        this.ticks = 1;
        this.probability = 1;
    }

    public ActivationImpl(int ticks) {
        this.ticks = ticks;
        this.probability = 1;
    }

    public ActivationImpl(float probability) {
        this.ticks = 1;
        this.probability = probability;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    @Override
    public boolean isActive(int tickNumber) {
        if (tickNumber % ticks == 0) {
            //TODO : validate
            return random.nextDouble() < probability;
        }
        return false;
    }
}
