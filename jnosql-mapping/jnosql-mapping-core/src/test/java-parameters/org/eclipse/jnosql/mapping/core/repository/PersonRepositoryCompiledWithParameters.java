/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
 *   Ondro Mihalyi
 */
package org.eclipse.jnosql.mapping.core.repository;

import jakarta.data.Sort;
import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.By;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import java.util.List;
import org.eclipse.jnosql.mapping.core.entities.Person;

public interface PersonRepositoryCompiledWithParameters extends BasicRepository<Person, String> {

    @Query("FROM Person WHERE name = :name")
    List<Person> query(@Param("name") @By("name") String name, Sort sort);

    @Query("FROM Person WHERE age = ?1")
    List<Person> findAge(int age);

    @Query("FROM Person WHERE age = ?1 AND name = ?2")
    List<Person> findAgeAndName(int age, String name);

}
