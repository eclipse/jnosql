package org.eclipse.jnosql.mapping.semistructured.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

@Entity
public record Room (@Id int number, @Column Guest guest) {
}
