package org.eclipse.jnosql.mapping.semistructured.entities;


import jakarta.json.bind.annotation.JsonbVisibility;

@JsonbVisibility(FieldAccessStrategy.class)
public class GasEngine extends Engine {

    public GasEngine() {
    }

    public GasEngine(int horsepower) {
        super(horsepower);
    }

    @Override
    public String getFuelType() {
        return "Gasoline";
    }


    @Override
    public String toString() {
        return "GasEngine{" +
                "horsepower=" + horsepower +
                '}';
    }
}