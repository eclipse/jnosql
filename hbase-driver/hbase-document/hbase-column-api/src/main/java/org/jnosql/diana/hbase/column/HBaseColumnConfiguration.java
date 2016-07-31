package org.jnosql.diana.hbase.column;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.jnosql.diana.api.column.ColumnConfiguration;
import org.jnosql.diana.api.column.ColumnFamilyManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Configuration to HBase
 *
 * @author Otávio Santana
 */
public class HBaseColumnConfiguration implements ColumnConfiguration {

    private final Configuration configuration;

    private final List<String> families = new ArrayList<>();

    /**
     * creates an {@link HBaseColumnConfiguration} instance with {@link HBaseConfiguration#create()}
     */
    public HBaseColumnConfiguration() {
        this.configuration = HBaseConfiguration.create();
    }

    /**
     * Creates hbase configuration
     *
     * @param configuration to be used
     * @throws NullPointerException when configuration is null
     */
    public HBaseColumnConfiguration(Configuration configuration) throws NullPointerException {
        this.configuration = requireNonNull(configuration, "configuration is required");
    }

    /**
     * Creates hbase configuration
     *
     * @param configuration to be used
     * @param families      families to be used
     * @throws NullPointerException when configuration is null
     */
    public HBaseColumnConfiguration(Configuration configuration, List<String> families) throws NullPointerException {
        this.configuration = requireNonNull(configuration, "configuration is required");
        requireNonNull(families, "families is required");
        this.families.addAll(families);
    }

    public void add(String family) {
        this.families.add(requireNonNull(family, "family is required"));
    }

    @Override
    public ColumnFamilyManagerFactory getManagerFactory(Map<String, String> configurations) {
        throw new IllegalArgumentException("It isn't implemented yet");
    }

    @Override
    public ColumnFamilyManagerFactory getManagerFactory() {
        return new HBaseColumnFamilyManagerFactory(configuration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HBaseColumnConfiguration that = (HBaseColumnConfiguration) o;
        return Objects.equals(configuration, that.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configuration);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HBaseColumnConfiguration{");
        sb.append("configuration=").append(configuration);
        sb.append('}');
        return sb.toString();
    }
}
