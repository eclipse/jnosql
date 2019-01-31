/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.jnosql.aphrodite.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

abstract class AbstractArgumentProvider implements ArgumentsProvider {

    private static final Predicate<String> IS_COMMENT = s -> s.startsWith("#");

    protected abstract String getFile();

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        URL url = AbstractArgumentProvider.class.getResource(getFile());
        Path path = Paths.get(url.toURI());
        return Files.readAllLines(path).stream()
                .filter(IS_COMMENT.negate())
                .map(Arguments::of);
    }
}
