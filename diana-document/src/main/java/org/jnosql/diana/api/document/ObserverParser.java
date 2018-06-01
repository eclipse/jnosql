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
package org.jnosql.diana.api.document;

/**
 * This is an observer to a parser
 */
public interface ObserverParser {

    /**
     * Fire an event to entity name
     *
     * @param document the document
     * @return the field result
     */
    String fireEntity(String document);

    /**
     * Fire an event to each field in case of mapper process
     *
     * @param document the document
     * @return the field result
     */
    String fireField(String document);
}
