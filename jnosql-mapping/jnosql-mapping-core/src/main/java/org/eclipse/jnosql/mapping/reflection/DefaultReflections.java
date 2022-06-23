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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;
import jakarta.nosql.mapping.MappedSuperclass;
import org.eclipse.jnosql.mapping.util.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class DefaultReflections implements Reflections {

    @Override
    public Object getValue(Object object, Field field) {

        try {
            return field.get(object);
        } catch (Exception exception) {
            Logger.getLogger(Reflections.class.getName()).log(Level.SEVERE, null, exception);
        }
        return null;
    }

    @Override
    public boolean setValue(Object object, Field field, Object value) {
        try {

            field.set(object, value);

        } catch (Exception exception) {
            Logger.getLogger(Reflections.class.getName()).log(Level.SEVERE, null, exception);
            return false;
        }
        return true;
    }

    @Override
    public <T> T newInstance(Constructor<T> constructor) {
        try {
            return constructor.newInstance();
        } catch (Exception exception) {
            Logger.getLogger(Reflections.class.getName()).log(Level.SEVERE, null, exception);
            return null;
        }
    }


    @Override
    public <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = makeAccessible(clazz);
            return newInstance(constructor);
        } catch (Exception exception) {
            Logger.getLogger(Reflections.class.getName()).log(Level.SEVERE, null, exception);
            return null;
        }
    }

    @Override
    public Field getField(String string, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(string)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public Class<?> getGenericType(Field field) {
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        return (Class<?>) genericType.getActualTypeArguments()[0];

    }

    @Override
    public Reflections.KeyValueClass getGenericKeyValue(Field field) {
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Class<?> keyClass = (Class<?>) genericType.getActualTypeArguments()[0];
        Class<?> valueClass = (Class<?>) genericType.getActualTypeArguments()[1];
        return new Reflections.KeyValueClass(keyClass, valueClass);
    }


    @Override
    public void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier
                .isPublic(field.getDeclaringClass().getModifiers()))
                && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    @Override
    public <T> Constructor<T> makeAccessible(Class<T> clazz) {
        List<Constructor<?>> constructors = Stream.
                of(clazz.getDeclaredConstructors())
                .filter(c -> c.getParameterCount() == 0)
                .collect(toList());

        if (constructors.isEmpty()) {
            throw new ConstructorException(clazz);
        }
        Optional<Constructor<?>> publicConstructor = constructors
                .stream()
                .filter(c -> Modifier.isPublic(c.getModifiers()))
                .findFirst();
        if (publicConstructor.isPresent()) {
            return (Constructor<T>) publicConstructor.get();
        }

        Constructor<?> constructor = constructors.get(0);
        constructor.setAccessible(true);
        return (Constructor<T>) constructor;
    }

    @Override
    public String getEntityName(Class classEntity) {
        requireNonNull(classEntity, "class entity is required");

        return Optional.ofNullable((Entity) classEntity.getAnnotation(Entity.class))
                .map(Entity::value)
                .filter(StringUtils::isNotBlank)
                .orElse(classEntity.getSimpleName());
    }

    @Override
    public List<Field> getFields(Class classEntity) {
        requireNonNull(classEntity, "class entity is required");

        List<Field> fields = new ArrayList<>();

        if (isMappedSuperclass(classEntity)) {
            fields.addAll(getFields(classEntity.getSuperclass()));
        }
        Predicate<Field> hasColumnAnnotation = f -> f.getAnnotation(Column.class) != null;
        Predicate<Field> hasIdAnnotation = f -> f.getAnnotation(Id.class) != null;

        Stream.of(classEntity.getDeclaredFields())
                .filter(hasColumnAnnotation.or(hasIdAnnotation))
                .forEach(fields::add);
        return fields;
    }

    @Override
    public boolean isMappedSuperclass(Class<?> classEntity) {
        requireNonNull(classEntity, "class entity is required");
        return classEntity.getSuperclass().getAnnotation(MappedSuperclass.class) != null;
    }

    @Override
    public boolean isIdField(Field field) {
        requireNonNull(field, "field is required");
        return field.getAnnotation(Id.class) != null;
    }

    @Override
    public String getColumnName(Field field) {
        requireNonNull(field, "field is required");
        return Optional.ofNullable(field.getAnnotation(Column.class))
                .map(Column::value)
                .filter(StringUtils::isNotBlank)
                .orElse(field.getName());
    }

    @Override
    public String getIdName(Field field) {
        requireNonNull(field, "field is required");
        return Optional.ofNullable(field.getAnnotation(Id.class))
                .map(Id::value)
                .filter(StringUtils::isNotBlank)
                .orElse(field.getName());
    }


}
