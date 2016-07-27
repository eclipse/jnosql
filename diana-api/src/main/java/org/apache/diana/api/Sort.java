package org.apache.diana.api;


import java.util.Objects;

/**
 * This element represents a required order to be used in a query, it's has two attributes:
 * -- The name - the field's name to be sorted
 * -- The type - the way to be sorted
 *
 * @author Otávio Santana
 * @see Sort#of(String, SortType)
 * @see SortType
 */
public class Sort {

    private final String name;

    private final SortType type;

    private Sort(String name, SortType type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Creates a bew Sort instance to be used in a NoSQL query.
     *
     * @param name - the field name be used in a sort process
     * @param type - the way to be sorted
     * @return a sort instance
     */
    public static Sort of(String name, SortType type) {
        return new Sort(name, type);
    }

    public String getName() {
        return name;
    }

    public SortType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sort sort = (Sort) o;
        return Objects.equals(name, sort.name) &&
                type == sort.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Sort{");
        sb.append("name='").append(name).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }

    /**
     * The way to be sorted.
     *
     * @author Otávio Santana
     * @see Sort
     */
    public enum SortType {
        /**
         * The ascending way
         */
        ASC,
        /**
         * The descending way
         */
        DESC
    }
}
