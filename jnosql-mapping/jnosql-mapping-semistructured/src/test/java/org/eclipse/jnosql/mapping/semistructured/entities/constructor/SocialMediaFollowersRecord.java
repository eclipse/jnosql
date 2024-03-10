package org.eclipse.jnosql.mapping.semistructured.entities.constructor;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.util.List;

@Entity
public record SocialMediaFollowersRecord (@Id String id, @Column List<String> followers){
}
