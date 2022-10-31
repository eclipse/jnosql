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
package org.eclipse.jnosql.mapping.graph;

/**
 * After the {@link EdgeRepeatTraversal} condition the next step is
 * {@link EdgeRepeatStepTraversal#times(int)} and {@link EdgeRepeatStepTraversal#until()}
 */
public interface EdgeRepeatStepTraversal {

    /**
     * Starts a loop traversal n times
     * @param times the repeat times that is required
     * @return a {@link EdgeTraversal} instance
     */
    EdgeTraversal times(int times);

    /**
     * Define the loop traversal until a defined condition
     * @return {@link EdgeUntilTraversal}
     */
    EdgeUntilTraversal until();

}
