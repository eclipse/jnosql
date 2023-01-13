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
        return  "AppointmentBook{" + "id='" + id + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}
