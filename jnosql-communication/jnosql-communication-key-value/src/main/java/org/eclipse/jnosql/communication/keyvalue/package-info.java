/*
 *
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
 *
 */

/**
 * This package contains all Key-value domain API.
 * <p>
 * Key-value (KV) stores use the associative array (also known as a map or dictionary) as their fundamental data model.
 * In this model, data is represented as a collection of key-value pairs, such that each possible key appears at most
 * once in the collection.
 * The key-value model is one of the simplest non-trivial data models, and richer data models are often implemented
 * as an extension of it.
 * The key-value model can be extended to a discretely ordered model that maintains keys in lexicographic order.
 * This extension is computationally powerful, in that it can efficiently retrieve selective key ranges.
 * Key-value stores can use consistency models ranging from eventual consistency to serializability.
 * Some databases support ordering of keys. There are various hardware implementations, and some users maintain
 * data in memory (RAM), while others employ solid-state drives or rotating disks.
 */
package org.eclipse.jnosql.communication.keyvalue;