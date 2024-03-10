package org.eclipse.jnosql.mapping.semistructured.entities.constructor;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.util.List;

@Entity
public class SocialMediaFollowers {

    @Id
    private String id;

    @Column
    private List<String> followers;

    public SocialMediaFollowers(@Id String id, @Column List<String> followers) {
        this.id = id;
        this.followers = followers;
    }

    public String getId() {
        return id;
    }

    public List<String> getFollowers() {
        return followers;
    }
}
