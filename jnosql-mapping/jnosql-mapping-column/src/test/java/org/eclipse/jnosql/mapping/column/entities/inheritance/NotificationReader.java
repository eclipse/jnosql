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
package org.eclipse.jnosql.mapping.column.entities.inheritance;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.util.Objects;

@Entity
public class NotificationReader {

    @Id
    private String nickname;

    @Column
    private String name;

    @Column
    private Notification notification;

    @Deprecated
    NotificationReader() {
    }

    public NotificationReader(String nickname, String name, Notification notification) {
        this.nickname = nickname;
        this.name = name;
        this.notification = notification;
    }

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public Notification getNotification() {
        return notification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationReader that = (NotificationReader) o;
        return Objects.equals(nickname, that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nickname);
    }

    @Override
    public String toString() {
        return "NotificationReader{" +
                "nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", notification=" + notification +
                '}';
    }
}
