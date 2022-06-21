/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
module org.eclipse.jnosql.communication.key.value {
    requires jakarta.nosql.communication.query;
    requires jakarta.nosql.communication.core;
    requires jakarta.nosql.communication.key.value;
    requires java.json;

    opens org.eclipse.jnosql.communication.keyvalue.query;
    opens org.eclipse.jnosql.communication.keyvalue;

    provides jakarta.nosql.keyvalue.KeyValueEntity.KeyValueEntityProvider with org.eclipse.jnosql.communication.keyvalue.DefaultKeyValueEntityProvider;
    provides jakarta.nosql.keyvalue.KeyValueQueryParser with org.eclipse.jnosql.communication.keyvalue.query.DefaultKeyValueQueryParser;
}