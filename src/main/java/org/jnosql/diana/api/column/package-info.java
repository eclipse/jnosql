/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
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
package org.jnosql.diana.api.column;