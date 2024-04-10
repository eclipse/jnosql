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

/**
 * This package contains classes and interfaces related to the mapper layer of the semistructured database model.
 * The semistructured mapper layer is responsible for mapping between Java objects and the data model used in semistructured databases,
 * providing a seamless integration between application code and database operations.
 * <p>
 * The main components of this package include:
 * <ul>
 *     <li>{@link org.eclipse.jnosql.mapping.semistructured.AbstractSemiStructuredTemplate}:
 *     An abstract template class that serves as the foundation for implementing semistructured database operations.</li>
 *     <li>{@link org.eclipse.jnosql.mapping.semistructured.EventPersistManager}:
 *     A manager class responsible for firing events before and after entity persistence operations.</li>
 *     <li>{@link org.eclipse.jnosql.mapping.semistructured.EntityConverter}:
 *     An interface representing the converter between entity objects and communication entities in the semistructured model.</li>
 *     <li>{@link org.eclipse.jnosql.mapping.semistructured.AttributeFieldValue}:
 *     An interface representing specialized field values for columns in the semistructured model.</li>
 *     <li>{@link org.eclipse.jnosql.mapping.semistructured.MappingQuery}:
 *     A mapping implementation of the {@link org.eclipse.jnosql.communication.semistructured.SelectQuery} interface,
 *     specifically designed for the semistructured database model.</li>
 * </ul>
 * <p>
 * Clients can utilize the classes and interfaces provided in this package to interact with semistructured databases
 * in a consistent and efficient manner, abstracting away the complexities of the underlying database operations.
 * </p>
 *
 * @see org.eclipse.jnosql.mapping.semistructured.AbstractSemiStructuredTemplate
 * @see org.eclipse.jnosql.mapping.semistructured.EventPersistManager
 * @see org.eclipse.jnosql.mapping.semistructured.EntityConverter
 * @see org.eclipse.jnosql.mapping.semistructured.AttributeFieldValue
 * @see org.eclipse.jnosql.mapping.semistructured.MappingQuery
 */
package org.eclipse.jnosql.mapping.semistructured;