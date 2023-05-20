package org.eclipse.jnosql.mapping.column.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

@Entity
public class Hero {

    @Id
    private final String id;

    @Column
    private final String name;

    public Hero(@Id String id, @Column("name") String name) {
        this.id = id;
        this.name = name;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }
}
