/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.nosql.tck.entities;

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
        this.id = (long) 12;
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