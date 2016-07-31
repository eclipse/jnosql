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
package org.jnosql.diana.hbase.column;


import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnFamilyEntity;

import java.util.ArrayList;
import java.util.List;

import static org.apache.hadoop.hbase.CellUtil.*;
import static org.jnosql.diana.hbase.column.HBaseUtils.getKey;

class EntityUnit {

    private String rowKey;

    private String columnFamily;

    private final List<Column> columns = new ArrayList<>();

    EntityUnit(Result result) {

        for (Cell cell : result.rawCells()) {

            String name = new String(cloneQualifier(cell));
            String value = new String(cloneValue(cell));
            if (this.rowKey == null) {
                this.rowKey = new String(cloneRow(cell));
            }
            if (this.columnFamily == null) {
                this.columnFamily = new String(cloneFamily(cell));
            }
            columns.add(Column.of(name, value));
        }
    }


    public boolean isNotEmpty() {
        return !columns.isEmpty();
    }

    public ColumnFamilyEntity toEntity() {
        ColumnFamilyEntity entity = ColumnFamilyEntity.of(columnFamily);
        entity.addAll(columns);
        entity.add(getKey(rowKey));
        return entity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntityUnit{");
        sb.append("rowKey='").append(rowKey).append('\'');
        sb.append(", columnFamily='").append(columnFamily).append('\'');
        sb.append(", columns=").append(columns);
        sb.append('}');
        return sb.toString();
    }
}
