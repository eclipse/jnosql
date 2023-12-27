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
package org.eclipse.jnosql.mapping.core.query;

import java.lang.reflect.Method;

@SuppressWarnings({"rawtypes", "unchecked"})
public enum AnnotationOperation {
    INSERT {
        @Override
        public Object invoke(Operation operation) throws Throwable {
            if (operation.params.length != 1) {
                throw new UnsupportedOperationException("The insert method requires one parameter");
            }
            Object entity = operation.params[0];
            boolean isVoid = operation.method.getReturnType().equals(Void.TYPE);
            if(entity instanceof Iterable entities){
                Iterable<?> result = operation.repository.insertAll(entities);
                return isVoid? Void.TYPE:result;
            } else {
                Object result = operation.repository.insert(entity);
                return isVoid ? Void.TYPE : result;
            }
        }
    };

    public abstract Object invoke(Operation operation) throws Throwable;




    public record Operation(Object instance, Method method, Object[] params, AbstractRepository repository){

    }
}
