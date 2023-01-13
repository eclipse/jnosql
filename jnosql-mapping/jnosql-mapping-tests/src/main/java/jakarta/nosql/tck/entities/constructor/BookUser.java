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
package jakarta.nosql.tck.entities.constructor;


import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;
import jakarta.nosql.tck.entities.Book;

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
