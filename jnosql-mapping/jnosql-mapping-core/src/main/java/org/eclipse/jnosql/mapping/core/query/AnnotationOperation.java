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
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public enum AnnotationOperation {
    INSERT {
        @Override
        public Object invoke(Operation operation)  {
            checkParameterNumber(operation);
            Object param = operation.params[0];
            boolean isVoid = operation.method.getReturnType().equals(Void.TYPE);
            if (param instanceof Iterable entities) {
                Iterable<?> result = operation.repository.insertAll(entities);
                return isVoid ? Void.TYPE : result;
            } else if (param.getClass().isArray()) {
                Iterable<?> result = operation.repository.insertAll(Arrays.asList((Object[]) param));
                return isVoid ? Void.TYPE : result;
            } else {
                Object result = operation.repository.insert(param);
                return isVoid ? Void.TYPE : result;
            }
        }
    }, UPDATE {
        @Override
        public Object invoke(Operation operation) {
            checkParameterNumber(operation);
            Object param = operation.params[0];
            boolean isVoid = operation.method.getReturnType().equals(Void.TYPE);
            boolean isBoolean = operation.method.getReturnType().equals(Boolean.class)
                    || operation.method.getReturnType().equals(Boolean.TYPE);
            boolean isInt = operation.method.getReturnType().equals(Integer.class)
                    || operation.method.getReturnType().equals(Integer.TYPE);
            if (param instanceof Iterable entities) {
                return executeIterable(operation, entities, isVoid, isBoolean, isInt, false, null);
            } else if (param.getClass().isArray()) {
                List<Object> entities = Arrays.asList((Object[]) param);
                return executeIterable(operation, entities, isVoid, isBoolean, isInt, true, param);
            } else {
                return executeSingleEntity(operation, param, isVoid, isBoolean, isInt);
            }
        }

        private static Object executeIterable(Operation operation, Iterable entities, boolean isVoid, boolean isBoolean,
                                              boolean isInt, boolean isArray, Object param) {
            int count = operation.repository.updateAll(entities);
            if (isVoid) {
                return Void.TYPE;
            } else if (isBoolean) {
                return true;
            } else if (isInt) {
                return count;
            }  else if(isArray){
                return param;
            } else {
                return entities;
            }
        }
    };

    private static Object executeSingleEntity(Operation operation, Object param, boolean isVoid, boolean isBoolean, boolean isInt) {
        boolean result = operation.repository.update(param);
        if(isVoid) {
            return Void.TYPE;
        } else if(isBoolean) {
            return result;
        } else if(isInt) {
            return 1;
        } else {
            return param;
        }
    }

    private static void checkParameterNumber(Operation operation) {
        if (operation.params.length != 1) {
            throw new UnsupportedOperationException("The method operation requires one parameter, please check the method: "
                    + operation.method);
        }
    }

    public abstract Object invoke(Operation operation);




    public record Operation(Method method, Object[] params, AbstractRepository repository){

    }
}
