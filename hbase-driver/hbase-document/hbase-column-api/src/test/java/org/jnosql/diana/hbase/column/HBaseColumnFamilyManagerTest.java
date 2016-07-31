package org.jnosql.diana.hbase.column;

import org.hamcrest.Matchers;
import org.jnosql.diana.api.column.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;


public class HBaseColumnFamilyManagerTest {

    private static final String DATA_BASE = "database";
    public static final String FAMILY = "person";

    private ColumnFamilyManagerFactory managerFactory;

    private ColumnFamilyManager columnFamilyManager;

    @Before
    public void setUp() {
        HBaseColumnConfiguration configuration = new HBaseColumnConfiguration();
        configuration.add(FAMILY);
        managerFactory = configuration.getManagerFactory();
        columnFamilyManager = managerFactory.getColumnEntityManager(DATA_BASE);
    }


    @Test
    public void shouldSave() {
        ColumnFamilyEntity entity = createEntity();
        columnFamilyManager.save(entity);
    }

    @Test(expected = DianaHBaseException.class)
    public void shouldReturnErrorWhenKeyIsNotDefined() {
        ColumnFamilyEntity entity = ColumnFamilyEntity.of(FAMILY);
        entity.add(Column.of("id", "otaviojava"));
        entity.add(Column.of("age", 26));
        entity.add(Column.of("country", "Brazil"));
        columnFamilyManager.save(entity);
    }

    @Test
    public void shouldFind() {
        columnFamilyManager.save(createEntity());
        ColumnQuery query = ColumnQuery.of(FAMILY);
        query.addCondition(ColumnCondition.eq(Column.of("", "otaviojava")));
        List<ColumnFamilyEntity> columnFamilyEntities = columnFamilyManager.find(query);
        assertNotNull(columnFamilyEntities);
        assertFalse(columnFamilyEntities.isEmpty());
        ColumnFamilyEntity entity = columnFamilyEntities.get(0);
        assertEquals(FAMILY, entity.getName());
        assertThat(entity.getColumns(), containsInAnyOrder(Column.of("", "otaviojava"), Column.of("age", "26"), Column.of("country", "Brazil")));
    }

    @Test
    public void shouldFindInBatch() {
        columnFamilyManager.save(createEntity());
        columnFamilyManager.save(createEntity2());

        ColumnQuery query = ColumnQuery.of(FAMILY);
        query.addCondition(ColumnCondition.eq(Column.of("", "otaviojava")));
        query.addCondition(ColumnCondition.eq(Column.of("", "poliana")));
        List<ColumnFamilyEntity> entities = columnFamilyManager.find(query);
        assertEquals(Integer.valueOf(2), Integer.valueOf(entities.size()));

    }

    @Test
    public void shouldDeleteEntity() {
        columnFamilyManager.save(createEntity());
        ColumnQuery query = ColumnQuery.of(FAMILY);
        query.addCondition(ColumnCondition.eq(Column.of("", "otaviojava")));
        columnFamilyManager.delete(query);
        List<ColumnFamilyEntity> entities = columnFamilyManager.find(query);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void shouldDeleteEntities() {
        columnFamilyManager.save(createEntity());
        columnFamilyManager.save(createEntity2());
        ColumnQuery query = ColumnQuery.of(FAMILY);
        query.addCondition(ColumnCondition.eq(Column.of("", "otaviojava")));
        query.addCondition(ColumnCondition.eq(Column.of("", "poliana")));
        columnFamilyManager.delete(query);
        List<ColumnFamilyEntity> entities = columnFamilyManager.find(query);
        assertTrue(entities.isEmpty());
    }

    private ColumnFamilyEntity createEntity() {
        ColumnFamilyEntity entity = ColumnFamilyEntity.of(FAMILY);
        entity.add(Column.of("", "otaviojava"));
        entity.add(Column.of("age", 26));
        entity.add(Column.of("country", "Brazil"));
        return entity;
    }

    private ColumnFamilyEntity createEntity2() {
        ColumnFamilyEntity entity = ColumnFamilyEntity.of(FAMILY);
        entity.add(Column.of("", "poliana"));
        entity.add(Column.of("age", 24));
        entity.add(Column.of("country", "Brazil"));
        return entity;
    }


}