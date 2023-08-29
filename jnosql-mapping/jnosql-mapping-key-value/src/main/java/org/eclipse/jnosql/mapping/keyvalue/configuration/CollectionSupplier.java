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
 *   Otavio Santana
 */
package org.eclipse.jnosql.mapping.keyvalue.configuration;


import jakarta.data.exceptions.MappingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.keyvalue.BucketManagerFactory;
import org.eclipse.jnosql.mapping.keyvalue.KeyValue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Queue;
import java.util.Set;

@ApplicationScoped
public class CollectionSupplier {


    @Inject
    private BucketManagerFactory factory;

    @Produces
    @KeyValue("")
    public <T> List<T> getList(InjectionPoint injectionPoint) {
         CollectionElement<T> element = CollectionElement.of(injectionPoint);
         return factory.getList(element.bucketName, element.type);
    }

    @Produces
    @KeyValue("")
    public <T> Queue<T> getQueue(InjectionPoint injectionPoint) {
        CollectionElement<T> element = CollectionElement.of(injectionPoint);
        return factory.getQueue(element.bucketName, element.type);
    }

    @Produces
    @KeyValue("")
    public <T> Set<T> getSet(InjectionPoint injectionPoint) {
        CollectionElement<T> element = CollectionElement.of(injectionPoint);
        return factory.getSet(element.bucketName, element.type);
    }


    private record CollectionElement<T>(String bucketName, Class<T> type) {

        @SuppressWarnings("unchecked")
        private static <T> CollectionElement<T> of(InjectionPoint injectionPoint) {
            KeyValue keyValue = injectionPoint.getAnnotated().getAnnotation(KeyValue.class);
            String bucketName = keyValue.value();
            if (injectionPoint.getType() instanceof ParameterizedType param) {
                Type argument = param.getActualTypeArguments()[0];
                return new CollectionElement<>(bucketName, (Class<T>) argument);
            }
            throw new MappingException("There is an issue to load the Queue from the database");
        }
    }
}
