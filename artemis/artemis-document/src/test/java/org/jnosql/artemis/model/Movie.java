/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis.model;


import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Entity;

import java.util.Objects;
import java.util.Set;

@Entity("movie")
public class Movie {

    @Column
    private String title;

    @Column
    private long year;

    @Column
    private Set<String> actors;

    Movie() {
    }

    public Movie(String title, long year, Set<String> actors) {
        this.title = title;
        this.year = year;
        this.actors = actors;
    }

    public String getTitle() {
        return title;
    }

    public long getYear() {
        return year;
    }

    public Set<String> getActors() {
        return actors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        return year == movie.year &&
                Objects.equals(title, movie.title) &&
                Objects.equals(actors, movie.actors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year, actors);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", actors=" + actors +
                '}';
    }
}
