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

import java.util.List;
import java.util.Map;

public class ActorBuilder {
    private long id;
    private String name;
    private int age;
    private List<String> phones;
    private String ignore;
    private Map<String, String> movieCharacter;
    private Map<String, Integer> movieRating;

    ActorBuilder() {
    }

    public ActorBuilder withId() {
        this.id = 12;
        return this;
    }

    public ActorBuilder withName() {
        this.name = "Otavio";
        return this;
    }

    public ActorBuilder withAge() {
        this.age = 10;
        return this;
    }

    public ActorBuilder withPhones(List<String> phones) {
        this.phones = phones;
        return this;
    }

    public ActorBuilder withIgnore(String ignore) {
        this.ignore = ignore;
        return this;
    }

    public ActorBuilder withMovieCharacter(Map<String, String> movieCharacter) {
        this.movieCharacter = movieCharacter;
        return this;
    }

    public ActorBuilder withMovieRating(Map<String, Integer> movieRating) {
        this.movieRating = movieRating;
        return this;
    }

    public Actor build() {
        return new Actor(id, name, age, phones, ignore, movieCharacter, movieRating);
    }
}