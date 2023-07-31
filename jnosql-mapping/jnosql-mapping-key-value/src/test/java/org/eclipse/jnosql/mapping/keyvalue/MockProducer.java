/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.keyvalue;


import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.interceptor.Interceptor;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.keyvalue.BucketManager;
import org.eclipse.jnosql.mapping.Database;
import org.eclipse.jnosql.mapping.DatabaseType;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.User;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.function.Supplier;

import static org.mockito.Mockito.when;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
@ApplicationScoped
public class MockProducer implements Supplier<BucketManager> {

    @Produces
    public BucketManager get() {
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
