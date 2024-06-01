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
 *   Otavio Santana
 */
package org.eclipse.jnosql.mapping.semistructured.query;

import jakarta.data.page.CursoredPage;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.By;
import jakarta.data.repository.Delete;
import jakarta.data.repository.Find;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Update;
import org.eclipse.jnosql.mapping.semistructured.entities.Person;

import java.util.List;
import java.util.Optional;

public interface People {

    @Insert
    List<Person> insert(List<Person> people);

    @Insert
    Person insert(Person person);

    @Insert
    Person[] insert(Person[] person);


    @Update
    List<Person> update(List<Person> people);

    @Update
    Person update(Person person);

    @Update
    Person[] update(Person[] person);


    @Delete
    void delete(List<Person> people);

    @Delete
    void delete(Person person);

    @Delete
    void delete(Person[] person);

    List<Person> findByAge(int age);

    Person findById(Long id);

    Page<Person> findByAge(int age, PageRequest pageRequest);

    CursoredPage<Person> findByName(String name, PageRequest pageRequest);

    Optional<Person> findByIdAndName(Long id, String name);

    @Find
    List<Person> name(@By("name") String name);

    default String defaultMethod() {
        return "default";
    }
}
