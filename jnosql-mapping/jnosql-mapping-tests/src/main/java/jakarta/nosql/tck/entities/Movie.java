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
