package org.eclipse.jnosql.mapping.semistructured.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.util.Map;

@Entity
public class Form {

    @Id
    private String id;

    @Column
    private Map<String, Object> questions;

    public String getId() {
        return id;
    }

    public Map<String, Object> getQuestions() {
        return questions;
    }
}
