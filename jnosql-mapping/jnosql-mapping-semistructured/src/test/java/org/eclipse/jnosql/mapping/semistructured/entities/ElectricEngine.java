package org.eclipse.jnosql.mapping.semistructured.entities;


import jakarta.json.bind.annotation.JsonbVisibility;

@JsonbVisibility(FieldAccessStrategy.class)
public class ElectricEngine extends Engine {

    public ElectricEngine() {
    }

    public ElectricEngine(int horsepower) {
        super(horsepower);
    }

    @Override
    public String getFuelType() {
        return "Electric";
    }

    @Override
    public String toString() {
        return "ElectricEngine{" +
                "horsepower=" + horsepower +
                '}';
    }
}