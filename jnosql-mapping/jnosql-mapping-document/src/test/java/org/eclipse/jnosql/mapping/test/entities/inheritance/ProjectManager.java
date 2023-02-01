/*
 *   Copyright (c) 2023 Contributors to the Eclipse Foundation
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    and Apache License v2.0 which accompanies this distribution.
 *    The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *    and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *    You may elect to redistribute this code under either of these licenses.
 *
 *    Contributors:
 *
 *    Otavio Santana
 */
package org.eclipse.jnosql.mapping.test.entities.inheritance;


import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class ProjectManager {

    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private List<Project> projects;

    @Deprecated
    ProjectManager() {
    }

    private ProjectManager(Long id, String name, List<Project> projects) {
        this.id = id;
        this.name = name;
        this.projects = projects;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Project> getProjects() {
        if(projects == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(projects);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectManager that = (ProjectManager) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectManager{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", projects=" + projects +
                '}';
    }

    public static ProjectManager of(Long id, String name, List<Project> projects) {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(projects, "projects is required");
        return new ProjectManager(id, name, projects);
    }
}
