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

import jakarta.nosql.criteria.CriteriaFunction;
import jakarta.nosql.criteria.CriteriaQuery;
import jakarta.nosql.criteria.Expression;
import jakarta.nosql.criteria.FunctionQuery;
import jakarta.nosql.criteria.Root;
import jakarta.nosql.criteria.SelectQuery;
import org.eclipse.jnosql.AbstractGenericType;

public class DefaultCriteriaQuery<T extends Object> extends AbstractGenericType<T> implements CriteriaQuery<T> {
    
    private final Root<T> from;
    
    public DefaultCriteriaQuery(Class<T> type) {
        super(type);
        this.from = new DefaultRoot<>(type);
    }
    
    @Override
    public Root<T> from() {
        return this.from;
    }    

    @Override
    public FunctionQuery<T> select(CriteriaFunction<T, ?, ?>... functions) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public SelectQuery<T> select(Expression<T, ?>... expressions) {
        return new DefaultSelectQuery<>(this.getType(), expressions);
    }
    
}
