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
package org.eclipse.jnosql.metamodel;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMod;
import java.util.List;
import javax.lang.model.element.Element;

public abstract class AbstractFieldBuilder {

    protected JInvocation buildInvocation(
            JClass jClass,
            List<JExpression> arguments
    ) {
        JInvocation invocation = JExpr._new(
                jClass
        );
        for (JExpression argument : arguments) {
            invocation.arg(argument);
        }
        return invocation;
    }

    protected void buildField(JDefinedClass jClass, Element element, JClass type, JInvocation value) {
        jClass.field(
                JMod.PUBLIC | JMod.STATIC | JMod.VOLATILE,
                type,
                element.getSimpleName().toString(),
                value
        );
    }

}
