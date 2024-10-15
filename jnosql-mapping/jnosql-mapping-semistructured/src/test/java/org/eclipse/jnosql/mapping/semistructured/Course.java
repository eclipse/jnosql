/*
 *   Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.semistructured;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

@Entity
public class Course {

    @Id
    private String id;

    @Column
    private Student student;

    public String getId() {
        return id;
    }

    public Course setId(String id) {
        this.id = id;
        return this;
    }

    public Student getStudent() {
        return student;
    }

    public Course setStudent(Student student) {
        this.student = student;
        return this;
    }
}
