/*
 *   Copyright (c) 2023 Contributors to the Eclipse Foundation
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    and Apache License v2.0 which accompanies this distribution.
 *    The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *    and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *    You may elect to redistribute this code under either of these licenses.
 *
 *    Contributors:
 *
 *    Otavio Santana
 */
module org.eclipse.jnosql.mapping.test {
    requires jakarta.nosql.core;
    requires jakarta.data.api;
    requires jakarta.cdi;
    requires org.junit.jupiter.api;
    exports org.eclipse.jnosql.mapping.test.jupiter;
    opens org.eclipse.jnosql.mapping.test.entities;
    opens org.eclipse.jnosql.mapping.test.entities.inheritance;
    opens org.eclipse.jnosql.mapping.test.jupiter;
}