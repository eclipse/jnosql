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

/**
 * This package contains all objects to use a Column Family API. This API gonna focus in domain, in other words,
 * ubiquitous language. A column family is a NoSQL object that contains columns of related data. It is a tuple (pair)
 * that consists of a key-value pair, where the key is mapped to a value that is a set of columns.
 * In analogy with relational databases, a column family is as a "table", each key-value pair being a "row".
 * Each column is a tuple (triplet) consisting of a column name, a value, and a timestamp. In a relational
 * database table, this data would be grouped together within a table with other non-related data.
 * Ref: https://en.wikipedia.org/wiki/Column_family
 *
 */
package org.eclipse.jnosql.mapping.column;