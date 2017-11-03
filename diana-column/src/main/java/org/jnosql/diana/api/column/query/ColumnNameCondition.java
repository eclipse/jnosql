/*
 *
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
 *
 */
package org.jnosql.diana.api.column.query;

/**
 * The base to name condition
 */
public interface ColumnNameCondition {


    /**
     * Creates the equals condition {@link org.jnosql.diana.api.Condition#EQUALS}
     *
     * @param value the value to the condition
     * @param <T>   the type
     * @return the {@link ColumnWhere}
     * @throws NullPointerException when value is null
     */
    <T> ColumnWhere eq(T value) throws NullPointerException;

    /**
     * Creates the like condition {@link org.jnosql.diana.api.Condition#LIKE}
     *
     * @param value the value to the condition
     * @return the {@link ColumnWhere}
     * @throws NullPointerException when value is null
     */
    ColumnWhere like(String value) throws NullPointerException;

    /**
     * Creates the greater than condition {@link org.jnosql.diana.api.Condition#GREATER_THAN}
     *
     * @param value the value to the condition
     * @return the {@link ColumnWhere}
     * @throws NullPointerException when value is null
     */
    ColumnWhere gt(Number value) throws NullPointerException;

    /**
     * Creates the greater equals than condition {@link org.jnosql.diana.api.Condition#GREATER_EQUALS_THAN}
     *
     * @param value the value to the condition
     * @return the {@link ColumnWhere}
     * @throws NullPointerException when value is null
     */
    ColumnWhere gte(Number value) throws NullPointerException;

    /**
     * Creates the lesser than condition {@link org.jnosql.diana.api.Condition#LESSER_THAN}
     *
     * @param value the value to the condition
     * @return the {@link ColumnWhere}
     * @throws NullPointerException when value is null
     */
    ColumnWhere lt(Number value) throws NullPointerException;

    /**
     * Creates the lesser equals than condition {@link org.jnosql.diana.api.Condition#LESSER_EQUALS_THAN}
     *
     * @param value the value to the condition
     * @return the {@link ColumnWhere}
     * @throws NullPointerException when value is null
     */
    ColumnWhere lte(Number value) throws NullPointerException;

    /**
     * Creates the between condition {@link org.jnosql.diana.api.Condition#EQUALS}
     *
     * @param valueA the values within a given range
     * @param valueB the values within a given range
     * @return the {@link ColumnWhere}
     * @throws NullPointerException when either valueA or valueB are null
     */
    ColumnWhere between(Number valueA, Number valueB) throws NullPointerException;

    /**
     * Creates in condition {@link org.jnosql.diana.api.Condition#IN}
     * @param values the values
     * @param <T> the type
     * @return the {@link ColumnWhere}
     * @throws NullPointerException when value is null
     */
    <T> ColumnWhere in(Iterable<T> values) throws NullPointerException;

}
