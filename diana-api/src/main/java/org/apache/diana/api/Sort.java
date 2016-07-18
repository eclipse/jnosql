package org.apache.diana.api;


import java.util.Objects;

public class Sort {

    private final String name;

    private final SortType type;

    private Sort(String name, SortType type) {
        this.name = name;
        this.type = type;
    }

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

    public static enum SortType{
        ASC, DESC;
    }
}
