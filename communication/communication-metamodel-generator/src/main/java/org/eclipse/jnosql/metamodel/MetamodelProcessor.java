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

import com.google.auto.service.AutoService;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.ElementKind;
import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Embeddable;
import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;
import jakarta.nosql.mapping.MappedSuperclass;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MetamodelProcessor extends AbstractProcessor {

    private final Predicate<Element> isField = element -> ElementKind.FIELD.equals(element.getKind());
    private final Predicate<Element> isId = element -> Objects.nonNull(element.getAnnotation(Id.class));
    private final Predicate<Element> isColumn = element -> Objects.nonNull(element.getAnnotation(Column.class));

    private final AbstractSimpleFieldBuilder stringFieldBuilder = new StringFieldBuilder();
    private final AbstractSimpleFieldBuilder numberFieldBuilder = new NumberFieldBuilder();
    private final AbstractSimpleFieldBuilder comparableFieldBuilder = new ComparableFieldBuilder();
    private final AbstractSimpleFieldBuilder pluralFieldBuilder = new PluralFieldBuilder();
    private final ValueFieldBuilder valueFieldBuilder = new ValueFieldBuilder();
    private final EntityFieldBuilder entityFieldBuilder = new EntityFieldBuilder();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(
                Arrays.asList(
                        Entity.class,
                        Embeddable.class,
                        MappedSuperclass.class
                ).stream().map(
                        Class::getName
                ).collect(
                        Collectors.toList()
                )
        );
    }

    private void buildField(JCodeModel codeModel, JDefinedClass jClass, TypeElement typeElement, Element element) {
        TypeMirror asType = element.asType();
        if (asType instanceof DeclaredType) {
            DeclaredType declaredType = DeclaredType.class.cast(asType);
            Element asElement = declaredType.asElement();
            if (asElement instanceof TypeElement) {
                String toString = TypeElement.class.cast(asElement).toString();
                try {
                    Class<?> forName = Class.forName(
                            toString
                    );
                    if (forName.isAssignableFrom(String.class)) {
                        stringFieldBuilder.buildField(codeModel, jClass, typeElement, element);
                    } else if (forName.isAssignableFrom(Number.class)) {
                        numberFieldBuilder.buildField(codeModel, jClass, typeElement, element);
                    } else if (forName.isAssignableFrom(Comparable.class)) {
                        comparableFieldBuilder.buildField(codeModel, jClass, typeElement, element);
                    } else if (forName.isAssignableFrom(Collection.class)) {
                        pluralFieldBuilder.buildField(codeModel, jClass, typeElement, element);
                    } else {
                        valueFieldBuilder.buildField(codeModel, jClass, typeElement, element, forName);
                    }
                } catch (ClassNotFoundException exception) {
                    entityFieldBuilder.buildField(codeModel, jClass, typeElement, element, toString);
                }
            }
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment re) {
        for (TypeElement annotationElement : set) {
            for (Element element : re.getElementsAnnotatedWith(annotationElement)) {
                if (element instanceof TypeElement) {
                    TypeElement typeElement = TypeElement.class.cast(element);

                    String name = typeElement.getQualifiedName() + "_";

                    JCodeModel codeModel = new JCodeModel();

                    try {

                        JDefinedClass definedClass = codeModel._class(name);

                        element.getEnclosedElements().stream().filter(
                                isId.or(isColumn).and(isField)
                        ).forEach(enclosedElement
                                -> MetamodelProcessor.this.buildField(
                                        codeModel,
                                        definedClass,
                                        typeElement,
                                        enclosedElement
                                )
                        );

                        codeModel.build(
                                new OutputStreamCodeWriter(
                                        processingEnv.getFiler().createSourceFile(
                                                name
                                        ).openOutputStream()
                                )
                        );
                    } catch (JClassAlreadyExistsException | IOException ex) {
                        Logger.getLogger(MetamodelProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }

        return true;
    }

}
