package engine.world;

import schema.generated.PRDRange;

import javax.xml.bind.annotation.XmlAttribute;

public class Range {

    protected double to;
    protected double from;

    public Range(PRDRange range){
        this.to = range.getTo();
        this.from = range.getFrom();
    }

    public double getTo() {
        return to;
    }

    public void setTo(double value) {
        this.to = value;
    }

    public double getFrom() {
        return from;
    }

    public void setFrom(double value) {
        this.from = value;
    }
}
