/*
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
 */

/**
 * Each configuration has four information:
 * <ul>
 * <li><b>name</b>: the name of the configuration, it works as an </li>
 * <li><b>description</b>: a description of the configuration, it won’t be used</li>
 * <li><b>provider</b>: the classpath of a configuration implementation.</li>
 * <li><b>settings</b>: the entry list, as a Map, to be used when it creates the instances.</li>
 * </ul>
 *
 * <p>It converts it to properties on Eclipse Config. It uses the value to name at the ConfigProperty annotation as prefix configuration; therefore given the annotation:</p>
 *
 * <pre>
 * @Inject
 * @ConfigProperty(name = "database")
 * private ColumnFamilyManager manager;
 * </pre>
 * <p>The configuration should be on that structure:</p>
 * <pre>
 * database=prefix
 * prefix.provider=org.eclipse.jnosql.provider.MyClass
 * prefix.settings.key=value
 * prefix.settings.key2=value2
 * </pre>
 *
 * This project has support to:
 *
 * <ul>
 *   <li>Coffee</li>
 * </ul>
 */
package org.eclipse.jnosql.artemis.configuration;