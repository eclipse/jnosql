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
import jakarta.nosql.mapping.Convert;
import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.MoneyConverter;

@Entity
public class Computer {

    @Id
    private final Long id;

    @Column
    private final String name;

    @Column
    private final int age;

    @Column
    private final String model;

    @Column
    @Convert(MoneyConverter.class)
    private final Money price;

    public Computer(@Id Long id, @Column("name") String name,
                    @Column("age") int age, @Column("model") String model,
                    @Column("price") @Convert(MoneyConverter.class) Money price) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.model = model;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getModel() {
        return model;
    }

    public Money getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", model='" + model + '\'' +
                ", price=" + price +
                '}';
    }
}
