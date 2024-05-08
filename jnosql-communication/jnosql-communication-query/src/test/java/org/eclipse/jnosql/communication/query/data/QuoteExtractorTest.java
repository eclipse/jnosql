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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuoteExtractorTest {

    @Test
    @DisplayName("Test extraction from single quotes")
    void shoudlExtractSingleQuotes() {
        var input = "WHERE name LIKE 'A%'";
        var expected = "A%";
        var actual = QuoteExtractor.INSTANCE.extract(input);
        assertEquals(expected, actual, "Should extract the string within single quotes correctly.");
    }

    @Test
    @DisplayName("Test extraction from double quotes")
    void shouldExtractDoubleQuotes() {
        String input = "WHERE name LIKE \"A%\"";
        String expected = "A%";
        String actual = QuoteExtractor.INSTANCE.extract(input);
        assertEquals(expected, actual, "Should extract the string within double quotes correctly.");
    }

    @Test
    @DisplayName("Test no quotes present")
    void shouldNoQuotes() {
        String input = "WHERE name LIKE A%";
        String actual = QuoteExtractor.INSTANCE.extract(input);
        assertNull(actual, "Should return null when no quotes are present.");
    }

    @Test
    @DisplayName("Test empty quotes")
    void shouldEmptyQuotes() {
        String input = "WHERE name LIKE \"\"";
        String expected = "";
        String actual = QuoteExtractor.INSTANCE.extract(input);
        assertEquals(expected, actual, "Should handle empty double quotes correctly.");
    }

    @Test
    @DisplayName("Test mixed quotes with escaped characters")
    void shouldMixedAndEscapedQuotes() {
        String input = "Text \"some \\\"quoted\\\" text\" and 'other \\'quoted\\' text'";
        String expectedDouble = "some \\\"quoted\\\" text";
        String expectedSingle = "other \\'quoted\\' text";
        String actualDouble = QuoteExtractor.INSTANCE.extract(input);
        assertEquals(expectedDouble, actualDouble, "Should correctly handle escaped double quotes.");

        // Assuming calling extractQuotedString again should find the next quote
        String actualSingle = QuoteExtractor.INSTANCE.extract(input.substring(input.indexOf(expectedDouble) + expectedDouble.length() + 1));
        assertEquals(expectedSingle, actualSingle, "Should correctly handle escaped single quotes.");
    }
}