package org.apache.diana.cassandra.document;

import com.datastax.driver.core.Session;
import org.apache.diana.api.column.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.diana.cassandra.document.Constants.COLUMN_FAMILY;
import static org.apache.diana.cassandra.document.Constants.KEY_SPACE;
import static org.junit.Assert.assertTrue;


public class CassandraDocumentEntityManagerTest {

    private ColumnEntityManager columnEntityManager;

    @Before
    public void setUp() {
        CassandraConfiguration cassandraConfiguration = new CassandraConfiguration();
        ColumnEntityManagerFactory entityManagerFactory = cassandraConfiguration.getManagerFactory();
        columnEntityManager = entityManagerFactory.getColumnEntityManager(KEY_SPACE);
    }

    @Test
    public void shouldClose() throws Exception {
        columnEntityManager.close();
        CassandraDocumentEntityManager cassandraDocumentEntityManager = CassandraDocumentEntityManager.class.cast(columnEntityManager);
        Session session = cassandraDocumentEntityManager.getSession();
        assertTrue(session.isClosed());
    }

    @Test
    public void shouldInsertJustKey() {
        Column key = Columns.of("id", 10L);
        ColumnEntity columnEntity = ColumnEntity.of(COLUMN_FAMILY);
        columnEntity.add(key);
        columnEntityManager.save(columnEntity);
    }

    @Test
    public void shouldInsertColumns() {
        ColumnEntity columnEntity = getColumnEntity();
        columnEntityManager.save(columnEntity);
    }



    @Test
    public void shouldFindById() {
        ColumnEntity columnEntity = getColumnEntity();
        columnEntityManager.save(columnEntity);
        Column key = Columns.of("id", 10L);
        ColumnEntity entity = ColumnEntity.of(COLUMN_FAMILY);
        entity.add(key);
        ColumnEntity columnEntity1 = columnEntityManager.find(entity);


    }

    private ColumnEntity getColumnEntity() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Cassandra");
        fields.put("version", 3.2);
        fields.put("options", Arrays.asList(1,2,3));
        List<Column> columns = Columns.of(fields);
        Column key = Columns.of("id", 10L);
        ColumnEntity columnEntity = ColumnEntity.of(COLUMN_FAMILY);
        columnEntity.add(key);
        columns.forEach(columnEntity::add);
        return columnEntity;
    }

}