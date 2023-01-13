/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.entities.inheritance;


import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;

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
