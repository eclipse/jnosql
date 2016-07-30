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


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import org.jnosql.diana.api.ExecuteAsyncQueryException;
import org.jnosql.diana.api.column.ColumnFamilyEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class CassandraReturnQueryAsync implements Runnable {

    private final ResultSetFuture resultSet;

    private final Consumer<List<ColumnFamilyEntity>> consumer;


    CassandraReturnQueryAsync(ResultSetFuture resultSet, Consumer<List<ColumnFamilyEntity>> consumer) {
        this.resultSet = resultSet;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            ResultSet resultSet = this.resultSet.get();
            List<ColumnFamilyEntity> entities = resultSet.all().stream()
                    .map(row -> CassandraConverter.toDocumentEntity(row)).collect(Collectors.toList());
            consumer.accept(entities);
        } catch (InterruptedException | ExecutionException e) {
            throw new ExecuteAsyncQueryException(e);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CassandraReturnQueryAsync{");
        sb.append("resultSet=").append(resultSet);
        sb.append(", consumer=").append(consumer);
        sb.append('}');
        return sb.toString();
    }
}
