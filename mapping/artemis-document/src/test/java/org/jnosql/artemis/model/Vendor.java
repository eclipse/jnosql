package org.jnosql.artemis.model;

import org.jnosql.artemis.Column;
import org.jnosql.artemis.Entity;
import org.jnosql.artemis.Id;

import java.util.Collections;
import java.util.Set;

@Entity("vendors")
public class Vendor {

    @Id
    private String name;

    @Column
    private Set<String> prefixes;

    Vendor() {
    }

    public Vendor(String name) {
        this.name = name;
        prefixes = Collections.emptySet();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(Set<String> prefixes) {
        this.prefixes = prefixes;
    }

    public void add(String prefix) {
        this.prefixes.add(prefix);
    }
}
