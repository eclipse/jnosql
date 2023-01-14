/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.mapping.graph.entities;


import jakarta.nosql.Column;
import jakarta.nosql.Entity;

import java.util.List;
import java.util.Map;

@Entity
public class Actor extends Person {

    @Column
    private Map<String, String> movieCharacter;

    @Column
    private Map<String, Integer> movieRating;

    Actor(long id, String name, int age, List<String> phones, String ignore, Map<String, String> movieCharacter, Map<String, Integer> movieRating) {
        super(id, name, age, phones, ignore);
        this.movieCharacter = movieCharacter;
        this.movieRating = movieRating;
    }

    Actor() {
    }

    public Map<String, String> getMovieCharacter() {
        return movieCharacter;
    }

    public Map<String, Integer> getMovieRating() {
        return movieRating;
    }

    public static ActorBuilder actorBuilder() {
        return new ActorBuilder();
    }


}
