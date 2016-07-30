/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.cassandra.column;


import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Row;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnFamilyEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.*;

final class CassandraConverter {

    private CassandraConverter() {
    }

    public static ColumnFamilyEntity toDocumentEntity(Row row) {
        List<Column> columns = new ArrayList<>();
        String columnFamily = "";
        for (ColumnDefinitions.Definition definition : row.getColumnDefinitions().asList()) {
            DataType type = definition.getType();
            columnFamily = definition.getKeyspace();
            Value value = Value.of(CassandraConverter.get(definition, row));
            Column column = Column.of(definition.getName(), value);
            columns.add(column);
        }
        return ColumnFamilyEntity.of(columnFamily, columns);
    }

    public static Object get(ColumnDefinitions.Definition definition, Row row) {
        String name = definition.getName();
        Class classType = get(definition.getType().getName());
        if (classType == List.class) {
            DataType dataType = definition.getType().getTypeArguments().get(0);
            return row.getList(name, get(dataType.getName()));
        }

        if (classType == Set.class) {
            DataType dataType = definition.getType().getTypeArguments().get(0);
            return row.getSet(name, get(dataType.getName()));
        }

        if (classType == Map.class) {
            DataType keyType = definition.getType().getTypeArguments().get(0);
            DataType valueType = definition.getType().getTypeArguments().get(1);
            return row.getMap(name, get(keyType.getName()), get(valueType.getName()));
        }
        return row.get(name, classType);

    }

    private static Class get(DataType.Name name) {
        switch (name) {
            case ASCII:
            case TEXT:
            case VARCHAR:
                return String.class;
            case BIGINT:
            case COUNTER:
                return Long.class;
            case INT:
            case SMALLINT:
            case TINYINT:
                return Integer.class;
            case BLOB:
                return ByteBuffer.class;
            case BOOLEAN:
                return Boolean.class;
            case DECIMAL:
                return BigDecimal.class;
            case VARINT:
                return BigInteger.class;
            case DOUBLE:
                return Double.class;
            case FLOAT:
                return Float.class;
            case INET:
                return InetAddress.class;

            case UUID:
            case TIMEUUID:
                return UUID.class;
            case LIST:
                return List.class;
            case SET:
                return Set.class;
            case MAP:
                return Map.class;
            case TIMESTAMP:
            case DATE:
            case TIME:
                return Date.class;

            case TUPLE:
            case CUSTOM:
            case UDT:
            default:
                throw new IllegalArgumentException("The type is not supported " + name);

        }
    }
}
