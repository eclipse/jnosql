package org.apache.diana.cassandra.document;


import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Row;
import org.apache.diana.api.DefaultValue;
import org.apache.diana.api.Value;
import org.apache.diana.api.column.Column;
import org.apache.diana.api.column.ColumnEntity;
import org.apache.diana.api.document.DocumentEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.*;

final class CassandraConverter {

    private CassandraConverter() {
    }

    public static ColumnEntity toDocumentEntity(Row row) {
        List<Column> columns = new ArrayList<>();
        String columnFamily = "";
        for (ColumnDefinitions.Definition definition : row.getColumnDefinitions().asList()) {
            DataType type = definition.getType();
            columnFamily = definition.getKeyspace();
            Value value = DefaultValue.of(CassandraConverter.get(definition, row));
            Column column = Column.of(definition.getName(), value);
            columns.add(column);
        }
        return ColumnEntity.of(columnFamily, columns);
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
