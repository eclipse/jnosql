/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Maximillian Arruda
 */
package org.eclipse.jnosql.mapping.semistructured.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.util.Objects;

@Entity
public class Task {

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    @Id
    private String id;

    @Column
    private String description;

    @Column
    private boolean active;

    Task(){
    }

    Task(String id, String description, boolean active) {
        this.id = id;
        this.description = description;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return active == task.active && Objects.equals(id, task.id) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, active);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                '}';
    }
}
