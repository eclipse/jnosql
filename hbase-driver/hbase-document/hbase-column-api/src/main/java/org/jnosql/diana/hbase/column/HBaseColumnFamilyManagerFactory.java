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


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.jnosql.diana.api.column.ColumnFamilyManagerFactory;

public class HBaseColumnFamilyManagerFactory implements ColumnFamilyManagerFactory<HBaseColumnFamilyManager> {

    private final Configuration configuration;

    private final List<String> families;

    HBaseColumnFamilyManagerFactory(Configuration configuration, List<String> families) {
        this.configuration = configuration;
        this.families = families;
    }

    @Override
    public HBaseColumnFamilyManager getColumnEntityManager(String database) {
        try {
            Connection connection = ConnectionFactory.createConnection(configuration);
            Admin admin = connection.getAdmin();
            TableName tableName = TableName.valueOf(database);
            if (admin.tableExists(tableName)) {
                existTable(admin, tableName);
            } else {
                createTable(admin, tableName);
            }
            Table table = connection.getTable(tableName);
            return new HBaseColumnFamilyManager(connection, table);
        } catch (IOException e) {
            throw new DianaHBaseException("A error happened when try to create ColumnFamilyManager", e);
        }
    }

    private void existTable(Admin admin, TableName tableName) throws IOException {
        HTableDescriptor tableDescriptor = admin.getTableDescriptor(tableName);
        HColumnDescriptor[] columnFamilies = tableDescriptor.getColumnFamilies();
        List<String> familiesExist = Arrays.stream(columnFamilies).map(HColumnDescriptor::getName).map(String::new).collect(Collectors.toList());
        families.stream().filter(s -> !familiesExist.contains(s)).map(HColumnDescriptor::new).forEach(tableDescriptor::addFamily);
        admin.modifyTable(tableName, tableDescriptor);
    }

    private void createTable(Admin admin, TableName tableName) throws IOException {
        HTableDescriptor desc = new HTableDescriptor(tableName);
        families.forEach(HColumnDescriptor::new);
        admin.createTable(desc);
    }

    @Override
    public void close() {

    }


}
