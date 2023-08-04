package engine.world;

import schema.generated.PRDRange;

public class EnvProperty {

    enum Type { STRING, BOOLEAN, FLOAT, DECIMAL }
    private String name;
    private Range range;
    private Type type;



    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public Range getRange() {
        return this.range;
    }

    public void setRange(PRDRange range) {
        this.range = new Range(range);
    }


    public Type getType() {
        return type;
    }

    public void setType(String type) {
        switch(type){
            case "string":
                this.type = Type.STRING;
                break;
            case "boolean":
                this.type = Type.BOOLEAN;
                break;
            case "float":
                this.type = Type.FLOAT;
                break;
            case "decimal":
                this.type = Type.DECIMAL;
                break;
        }
    }

}
