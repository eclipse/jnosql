package org.eclipse.jnosql.mapping.semistructured.entities;

import jakarta.json.bind.annotation.JsonbVisibility;
import jakarta.nosql.Column;
import jakarta.nosql.Convert;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.util.Objects;

@Entity
@JsonbVisibility(FieldAccessStrategy.class)
public class Machine {

    @Id
    private String id;

    @Column
    @Convert(EngineConverter.class)
    private Engine engine;

    @Column
    private String manufacturer;

    @Column
    private int year;

    public String getId() {
        return id;
    }

    public Engine getEngine() {
        return engine;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Machine machine = (Machine) o;
        return Objects.equals(id, machine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id='" + id + '\'' +
                ", engine=" + engine +
                ", manufacturer='" + manufacturer + '\'' +
                ", year=" + year +
                '}';
    }
}
