/*
 *   Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.semistructured.entities.constructor;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.util.List;

@Entity
public class SocialMediaFollowers {

    @Id
    private String id;

    @Column
    private List<String> followers;

    public SocialMediaFollowers(@Id String id, @Column List<String> followers) {
        this.id = id;
        this.followers = followers;
    }

    public String getId() {
        return id;
    }

    public List<String> getFollowers() {
        return followers;
    }
}
