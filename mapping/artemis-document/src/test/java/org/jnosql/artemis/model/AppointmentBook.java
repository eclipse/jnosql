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

import org.jnosql.artemis.Column;
import org.jnosql.artemis.Entity;
import org.jnosql.artemis.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class AppointmentBook {


    @Id
    private String id;


    @Column
    private List<Contact> contacts = new ArrayList<>();


    AppointmentBook() {
    }

    public AppointmentBook(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public List<Contact> getContacts() {
        return contacts;
    }

    public void add(Contact contact) {
        this.contacts.add(contact);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppointmentBook appointmentBook = (AppointmentBook) o;
        return Objects.equals(id, appointmentBook.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AppointmentBook{" + "id='" + id + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}
