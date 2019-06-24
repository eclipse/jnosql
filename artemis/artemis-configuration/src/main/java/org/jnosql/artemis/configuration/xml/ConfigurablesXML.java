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
package org.jnosql.artemis.configuration.xml;

import org.jnosql.artemis.configuration.DefaultConfigurable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "configurations")
@XmlAccessorType(XmlAccessType.FIELD)
class ConfigurablesXML {

    @XmlElement(name = "configuration")
    private List<DefaultConfigurable> configurations;

    public List<DefaultConfigurable> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<DefaultConfigurable> configurations) {
        this.configurations = configurations;
    }
}
