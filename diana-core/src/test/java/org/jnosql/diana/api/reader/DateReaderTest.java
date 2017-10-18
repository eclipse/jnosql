/*
 *
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
 *
 */

package org.jnosql.diana.api.reader;


import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DateReaderTest {

    private DateValueReader dateReader;

    @Before
    public void init() {
        dateReader = new DateValueReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(dateReader.isCompatible(Date.class));
    }

    @Test
    public void shouldConvert() {
        long milliseconds = new Date().getTime();
        Date date = new Date();
        assertEquals(milliseconds, dateReader.read(Date.class, milliseconds).getTime());
        assertEquals(date, dateReader.read(Date.class, date));
    }
}
