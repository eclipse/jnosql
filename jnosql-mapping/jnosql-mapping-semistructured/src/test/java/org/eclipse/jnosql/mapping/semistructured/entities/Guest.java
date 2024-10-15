package org.eclipse.jnosql.mapping.semistructured.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Embeddable;

@Embeddable
public record Guest (@Column String documentNumber, @Column String name) {
}
