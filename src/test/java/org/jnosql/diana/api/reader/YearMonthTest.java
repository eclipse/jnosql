/*
 * Copyright 2017 Otavio Santana and others
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.YearMonth;

import org.jnosql.diana.api.ValueReader;
import org.junit.Before;
import org.junit.Test;

public class YearMonthTest {

	private ValueReader valueReader;

	@Before
	public void init() {
		valueReader = new YearMonthValueReader();
	}

	@Test
	public void shouldValidateCompatibility() {
		assertTrue(valueReader.isCompatible(YearMonth.class));
		assertFalse(valueReader.isCompatible(String.class));
		assertFalse(valueReader.isCompatible(Long.class));
	}

	@Test
	public void shouldConvert() {
		YearMonth yearMonth = YearMonth.parse("2016-08");

		assertEquals(yearMonth, valueReader.read(YearMonth.class, YearMonth.parse("2016-08")));
		assertEquals(yearMonth, valueReader.read(String.class,"2016-08"));
		assertEquals(yearMonth, valueReader.read(Integer.class,YearMonth.of(2016,8)));
		assertEquals(yearMonth, valueReader.read(Long.class,YearMonth.of(2016,8)));
	}

}
