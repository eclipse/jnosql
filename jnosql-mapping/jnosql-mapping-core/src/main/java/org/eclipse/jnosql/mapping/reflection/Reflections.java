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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * Utilitarian class to reflection
 */
public interface Reflections {

    /**
     * Return The Object from the Column.
     *
     * @param object the object
     * @param field  the field to return object
     * @return - the field value in Object
     */
    Object getValue(Object object, Field field);

    /**
     * Set the field in the Object.
     *
     * @param object the object
     * @param field  the field to return object
     * @param value  the value to object
     * @return - if the operation was execute with success
     */
    boolean setValue(Object object, Field field, Object value);

    /**
     * Create new instance of this class.
     *
     * @param constructor the constructor
     * @param <T>         the instance type
     * @return the new instance that class
     */
    <T> T newInstance(Constructor<T> constructor);


    /**
     * Create new instance of this class.
     *
     * @param clazz the clazz
     * @param <T>   the instance type
     * @return the new instance that class
     */
    <T> T newInstance(Class<T> type);

    /**
     * Find the Column from the name field.
     *
     * @param string the name of field
     * @param type  the class
     * @return the field from the name
     */
    Field getField(String string, Class<?> type);

    /**
     * returns the generic type of field.
     *
     * @param field the field
     * @return a generic type
     */
    Class<?> getGenericType(Field field);

    /**
     * return the key and value of field.
     *
     * @param field the field
     * @return the types of the type
     */
    KeyValueClass getGenericKeyValue(Field field);


    /**
     * Make the given field accessible, explicitly setting it accessible
     * if necessary. The setAccessible(true) method is only
     * called when actually necessary, to avoid unnecessary
     * conflicts with a JVM SecurityManager (if active).
     *
     * @param field field the field to make accessible
     */
    void makeAccessible(Field field);

    /**
     * Make the given a constructor class accessible, explicitly setting it accessible
     * if necessary. The setAccessible(true) method is only
     * called when actually necessary, to avoid unnecessary
     * conflicts with a JVM SecurityManager (if active).
     *
     * @param type the class constructor acessible
     * @param <T>   the entity type
     * @return the constructor class
     * @throws ConstructorException when the constructor has public and default
     */
    <T> Constructor<T> getConstructor(Class<T> type);

    /**
     * Returns the name of the entity. So it tries to read the {@link Entity} otherwise
     * {@link Class#getSimpleName()}
     *
     * @param entity the class to read
     * @return the {@link Entity} when is not blank otherwise {@link Class#getSimpleName()}
     * @throws NullPointerException when entity is null
     */
    String getEntityName(Class<?> entity);

    /**
     * Returns the fields from the entity class
     *
     * @param type the entity class
     * @return the list of fields that is annotated with either {@link Column} or
     * {@link Id}
     * @throws NullPointerException when class entity is null
     */
    List<Field> getFields(Class<?> type);

    /**
     * Checks if the class is annotated with {@link jakarta.nosql.mapping.MappedSuperclass} or
     * {@link jakarta.nosql.mapping.Inheritance}
     *
     * @param type the entity class
     * @return if the class is annotated
     * @throws NullPointerException when type is null
     */
    boolean isMappedSuperclass(Class<?> type);

    /**
     * Checks if the field is annotated with {@link Column}
     *
     * @param field the field
     * @return if the field is annotated with {@link Column}
     * @throws NullPointerException when the field is null
     */
    boolean isIdField(Field field);

    /**
     * Gets the name from the field, so it reads the {@link Column#value()}
     * otherwise {@link Field#getName()}
     *
     * @param field the fields
     * @return the column name
     * @throws NullPointerException when the field is null
     */
    String getColumnName(Field field);

    /**
     * Gets the id name, so it reads the {@link Id#value()} otherwise {@link Field#getName()}
     *
     * @param field the field
     * @return the column name
     * @throws NullPointerException when the field is null
     */
    String getIdName(Field field);

    /**
     * Reads the type annotation and checks if the inheritance has an
     * {@link jakarta.nosql.mapping.Inheritance} annotation.
     * If it has, it will return the {@link InheritanceMetadata} otherwise it will return
     * {@link Optional#empty()}
     *
     * @param type the type class
     * @return the {@link InheritanceMetadata} or {@link Optional#empty()}
     * @throws NullPointerException when type is null
     */
    Optional<InheritanceMetadata> getInheritance(Class<?> type);

    /**
     * Check if the entity has the {@link jakarta.nosql.mapping.Inheritance} annotation
     *
     * @param entity the entity
     * @return true if it has the {@link jakarta.nosql.mapping.Inheritance} annotation
     */
    boolean hasInheritanceAnnotation(Class<?> entity);

    /**
     * data structured to store key and value class to map collection.
     *
     * @author otaviojava
     */
    class KeyValueClass {
        private final Class<?> keyClass;
        private final Class<?> valueClass;

        public KeyValueClass(Class<?> keyClass, Class<?> valueClass) {
            this.keyClass = keyClass;
            this.valueClass = valueClass;
        }

        public Class<?> getKeyClass() {
            return keyClass;
        }

        public Class<?> getValueClass() {
            return valueClass;
        }

    }


}
