/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.jnosql.query;

/**
 * Condition performs different computations or actions depending on whether a boolean query
 * condition evaluates to true or false.
 * The conditions are composed of three elements.
 */
public interface Condition {

    /**
     * the data source or target, to apply the operator
     * @return the name
     */
    String getName();

    /**
     * that defines comparing process between the name and the value.
     * @return the operator
     */
    Operator getOperator();

    /**
     * that data that receives the operation.
     * @return the value
     */
    Value<?> getValue();
}
