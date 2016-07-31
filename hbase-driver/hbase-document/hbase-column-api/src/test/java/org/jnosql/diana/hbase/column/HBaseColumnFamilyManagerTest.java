package org.jnosql.diana.hbase.column;

import org.jnosql.diana.api.column.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class HBaseColumnFamilyManagerTest {

    private static final String DATA_BASE = "database";

    private ColumnFamilyManagerFactory managerFactory = new HBaseColumnConfiguration().getManagerFactory();

    private ColumnFamilyManager columnFamilyManager;

    @Before
    public void setUp() {
        columnFamilyManager = managerFactory.getColumnEntityManager(DATA_BASE);
    }


    @Test
    public void shouldSave() {
        ColumnFamilyEntity entity = ColumnFamilyEntity.of("person");
        entity.add(Column.of("nickName", "otaviojava"));
        entity.add(Column.of("age", 26));
        entity.add(Column.of("country", "Brazil"));

        columnFamilyManager.save(entity);
    }

}