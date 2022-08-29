package org.eclipse.jnosql.mapping.validation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Computer {

    private final String name;

    private final int version;

    private final String model;

    public Computer(@NotBlank String name, @Min(2020) int version,
                    @NotBlank @Size(min = 2, max = 4) String model) {
        this.name = name;
        this.version = version;
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public String getModel() {
        return model;
    }
}
