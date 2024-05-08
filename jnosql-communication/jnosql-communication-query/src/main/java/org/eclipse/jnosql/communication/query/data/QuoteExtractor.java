/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query.data;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

enum QuoteExtractor {

    INSTANCE;

    private static final Pattern QUOTE_PATTERN = Pattern.compile(
            "\"((?:\\\\.|[^\"])*)\"|'((?:\\\\.|[^'])*)'"
    );

    public String extract(String input) {
        Matcher matcher = QUOTE_PATTERN.matcher(input);

        if (matcher.find()) {
            // Check which group was matched, double or single quotes
            if (matcher.group(1) != null) {
                return matcher.group(1);  // Return text inside double quotes
            } else if (matcher.group(2) != null) {
                return matcher.group(2);  // Return text inside single quotes
            }
        }
        return null;  // Return null if no quoted string is found
    }
}
