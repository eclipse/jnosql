/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.eclipse.jnosql.artemis;


import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.mapping.Database;
import jakarta.nosql.mapping.DatabaseType;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.User;
import org.mockito.Mockito;

import javax.enterprise.inject.Produces;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class MockProducer {

    @Produces
    public BucketManager getBucketManager() {
        BucketManager bucketManager = Mockito.mock(BucketManager.class);
        Person person = Person.builder().withName("Default").build();
        when(bucketManager.get("key")).thenReturn(Optional.ofNullable(Value.of(person)));
        when(bucketManager.get(10L)).thenReturn(Optional.ofNullable(Value.of(person)));
        when(bucketManager.get("user")).thenReturn(Optional.of(Value.of(new User("Default", "Default", 10))));
        return bucketManager;
    }

    @Produces
    @Database(value = DatabaseType.KEY_VALUE, provider = "keyvalueMock")
    public BucketManager getBucketManagerMock() {
        BucketManager bucketManager = Mockito.mock(BucketManager.class);
        Person person = Person.builder().withName("keyvalueMock").build();
        when(bucketManager.get("key")).thenReturn(Optional.ofNullable(Value.of(person)));
        when(bucketManager.get(10L)).thenReturn(Optional.ofNullable(Value.of(person)));
        when(bucketManager.get("user")).thenReturn(Optional.of(Value.of(new User("keyvalueMock", "keyvalueMock", 10))));
        return bucketManager;
    }


}
