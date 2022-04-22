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

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import jakarta.nosql.metamodel.PluralAttribute;
import java.util.Arrays;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class PluralFieldBuilder extends AbstractSimpleFieldBuilder {

    @Override
    public void buildField(JCodeModel codeModel, JDefinedClass jClass, TypeElement typeElement, Element element) {
        super.buildField(
                jClass,
                element,
                codeModel.ref(
                        PluralAttribute.class
                ).narrow(
                        codeModel.ref(typeElement.getQualifiedName().toString())
                ),
                buildInvocation(
                        codeModel.ref(
                                DefaultPluralAttribute.class
                        ),
                        Arrays.asList(
                                codeModel.ref(typeElement.getQualifiedName().toString()).dotclass(),
                                JExpr.lit(element.getSimpleName().toString())
                        )
                )
        );
    }

}
