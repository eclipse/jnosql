package org.apache.diana.api.column;

import org.apache.diana.api.Value;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;


public class ColumnFamilyEntityTest {

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenNameIsNull() {
        ColumnFamilyEntity.of(null);
    }
    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenDocumentsIsNull() {
        ColumnFamilyEntity.of("entity", null);
    }

    @Test
    public void shouldReturnOneDocument() {
        ColumnFamilyEntity entity = ColumnFamilyEntity.of("entity");
        assertEquals(Integer.valueOf(0), Integer.valueOf(entity.size()));
        assertTrue(entity.isEmpty());

        entity.add(Column.of("name", "name"));
        entity.add(Column.of("name2", Value.of("name2")));
        assertFalse(entity.isEmpty());
        assertEquals(Integer.valueOf(2), Integer.valueOf(entity.size()));
        assertFalse(ColumnFamilyEntity.of("entity", singletonList(Column.of("name", "name"))).isEmpty());
    }

    @Test
    public void shouldDoCopy() {
        ColumnFamilyEntity entity = ColumnFamilyEntity.of("entity", singletonList(Column.of("name", "name")));
        ColumnFamilyEntity copy = entity.copy();
        assertFalse(entity == copy);
        assertEquals(entity, copy);

    }

    @Test
    public void shouldFindDocument() {
        Column column = Column.of("name", "name");
        ColumnFamilyEntity entity = ColumnFamilyEntity.of("entity", singletonList(column));
        Optional<Column> name = entity.find("name");
        Optional<Column> notfound = entity.find("not_found");
        assertTrue(name.isPresent());
        assertFalse(notfound.isPresent());
        assertEquals(column, name.get());
    }

    @Test
    public void shouldRemoveDocument() {
        Column column = Column.of("name", "name");
        ColumnFamilyEntity entity = ColumnFamilyEntity.of("entity", singletonList(column));
        assertTrue(entity.remove("name"));
        assertTrue(entity.isEmpty());
    }

    @Test
    public void shouldConvertToMap() {
        Column column = Column.of("name", "name");
        ColumnFamilyEntity entity = ColumnFamilyEntity.of("entity", singletonList(column));
        Map<String, Object> result = entity.toMap();
        assertFalse(result.isEmpty());
        assertEquals(Integer.valueOf(1), Integer.valueOf(result.size()));
        assertEquals(column.getName(), result.keySet().stream().findAny().get());

    }

}