package org.jnosql.diana.hbase.column;

import org.jnosql.diana.api.column.ColumnConfiguration;
import org.jnosql.diana.api.column.ColumnFamilyManagerFactory;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class HBaseColumnFamilyManagerFactoryTest {

    private ColumnConfiguration configuration = new HBaseColumnConfiguration();


    @Test
    public void shouldCreateColumnFamilyManager() {
        ColumnFamilyManagerFactory managerFactory = configuration.getManagerFactory();
        assertNotNull(managerFactory);
    }

}