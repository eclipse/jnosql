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
import jakarta.nosql.mapping.Id;

import java.util.Objects;

@Entity
public final class Book {

    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private Integer age;


    Book() {
    }

    Book(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public static BookBuilder builder() {
        return new BookBuilder();
    }

    public static class BookBuilder {
        private String name;
        private Integer age;
        private Long id;

        private BookBuilder() {
        }

        public BookBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public BookBuilder withAge(Integer age) {
            this.age = age;
            return this;
        }

        public BookBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public Book build() {
            return new Book(id, name, age);
        }
    }
}