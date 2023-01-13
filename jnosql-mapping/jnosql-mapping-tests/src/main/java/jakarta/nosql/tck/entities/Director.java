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


import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Entity;

import java.util.List;
import java.util.Objects;

@Entity
public final class Director extends Person {

    @Column
    private Movie movie;


    Director() {
    }

    public Director(long id, String name, int age, List<String> phones, String ignore, Movie movie) {
        super(id, name, age, phones, ignore);
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Director director = (Director) o;
        return Objects.equals(movie, director.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie);
    }

    public static DirectorBuilder builderDirector() {
        return new DirectorBuilder();
    }

    public static class DirectorBuilder {
        private long id;
        private String name;
        private int age;
        private List<String> phones;
        private String ignore;
        private Movie movie;

        private DirectorBuilder() {
        }

        public DirectorBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public DirectorBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DirectorBuilder withAge(int age) {
            this.age = age;
            return this;
        }

        public DirectorBuilder withPhones(List<String> phones) {
            this.phones = phones;
            return this;
        }

        public DirectorBuilder withIgnore(String ignore) {
            this.ignore = ignore;
            return this;
        }

        public DirectorBuilder withMovie(Movie movie) {
            this.movie = movie;
            return this;
        }

        public Director build() {
            return new Director(id, name, age, phones, ignore, movie);
        }
    }

}
