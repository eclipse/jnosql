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
 *   Daniel Cunha <dcunha@tomitribe.com>
 */
package org.jnosql.artemis.configuration.yaml;

import org.jnosql.artemis.configuration.ConfigurableImpl;

import java.util.List;

class ConfigurablesYAML {
    private List<ConfigurableImpl> configurations;

    public List<ConfigurableImpl> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<ConfigurableImpl> configurations) {
        this.configurations = configurations;
    }
}
