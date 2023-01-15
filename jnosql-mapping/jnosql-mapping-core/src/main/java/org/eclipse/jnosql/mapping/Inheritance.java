/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the inheritance strategy to be used for an entity class hierarchy.
 * It is specified on the entity class that is the root of the entity class hierarchy.
 * This class can be either a regular or an abstract;
 * The table/column-family/document-collection will have a column for every attribute
 * of every class in the hierarchy.
 * The subclass will use the {@link Entity} name from that class with this annotation.
 *
 * <pre>
 *
 *   Example:
 *   &#064;Entity
 *   &#064;Inheritance
 *   public class Notification { ... }
 *
 *   &#064;Entity
 *   public class SMSNotification extends Notification { ... }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Inheritance {
}
