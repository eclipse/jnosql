/*
 *   Copyright (c) 2023 Contributors to the Eclipse Foundation
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    and Apache License v2.0 which accompanies this distribution.
 *    The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *    and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *    You may elect to redistribute this code under either of these licenses.
 *
 *    Contributors:
 *
 *    Otavio Santana
 */
package jakarta.nosql.tck.entities;


import jakarta.nosql.Column;
import jakarta.nosql.Entity;

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
