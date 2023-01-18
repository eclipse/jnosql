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
package org.eclipse.jnosql.mapping.test.entities.constructor;


import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import org.eclipse.jnosql.mapping.test.entities.Book;

import java.util.List;
import java.util.Objects;

@Entity
public class BookUser {

    @Id
    private final String nickname;

    @Column
    private final String name;

    @Column
    private final List<Book> books;

    BookUser(@Id String nickname,
             @Column("native_name") String name,
             @Column("books") List<Book> books) {
        this.nickname = nickname;
        this.name = name;
        this.books = books;
    }

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookUser bookUser = (BookUser) o;
        return Objects.equals(nickname, bookUser.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nickname);
    }

    @Override
    public String toString() {
        return "BookUser{" +
                "nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }
}
