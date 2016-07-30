/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
 *
 * @author Otávio Santana
 */
package org.apache.diana.api.key;