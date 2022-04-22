/*
 *  Copyright (c) 2022 Otávio Santana and others
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
 *   Alessandro Moscatelli
 */
package org.eclipse.jnosql.communication.criteria;

import jakarta.nosql.criteria.Expression;

public abstract class AbstractBinaryPredicate<X extends Object, T extends Object, LHS extends Expression<X, T>, RHS> extends AbstractPredicate<X> {

    private final LHS left;
    private final RHS right;
    
    public AbstractBinaryPredicate(LHS left, RHS right) {
        this.left = left;
        this.right = right;
    }

    public LHS getLeft() {
        return left;
    }

    public RHS getRight() {
        return right;
    }
    
}
