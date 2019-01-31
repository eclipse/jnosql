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
 * The WHERE clause specifies a filter to the result. These filters are booleans operations that are composed of one or
 * more conditions appended with the and ({@link Operator#AND}) and or ({@link Operator#OR}) operators.
 */
public interface Where {

    /**
     * The condition
     * @return the condition
     */
    Condition getCondition();
}
