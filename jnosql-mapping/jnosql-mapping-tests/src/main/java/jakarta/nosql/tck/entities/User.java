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

import jakarta.nosql.mapping.Id;

import java.util.Objects;


public class User {

    @Id
    private String nickname;

    private String name;

    private int age;

    public User(String nickname, String name, int age) {
        this.nickname = nickname;
        this.name = name;
        this.age = age;
    }

    User() {}

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nickname);
    }

    @Override
    public String toString() {
        return "User{" + "nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
