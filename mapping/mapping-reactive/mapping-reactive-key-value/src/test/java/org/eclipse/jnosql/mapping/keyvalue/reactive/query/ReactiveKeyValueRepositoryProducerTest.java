/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.mapping.keyvalue.reactive.query;

import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.eclipse.jnosql.mapping.reactive.ReactiveRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
class ReactiveKeyValueRepositoryProducerTest {

    @Inject
    private ReactiveKeyValueRepositoryProducer producer;

    @Test
    public void shouldCreateFromTemplate() {
        KeyValueTemplate template = Mockito.mock(KeyValueTemplate.class);
        PersonRepository personRepository = producer.get(PersonRepository.class, template);
        assertNotNull(personRepository);
    }

    public interface PersonRepository extends ReactiveRepository<Person, Long> {
    }


}