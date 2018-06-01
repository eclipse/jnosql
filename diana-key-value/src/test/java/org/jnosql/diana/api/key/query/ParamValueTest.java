package org.jnosql.diana.api.key.query;

import org.hamcrest.MatcherAssert;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.key.KeyValueEntity;
import org.jnosql.query.QueryException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParamsTest {


    @Test
    public void shouldAddParamter() {
        Params params = new Params();
        Value name = params.add("name");
        assertNotNull(name);
        MatcherAssert.<List<String>>assertThat(params.getParametersNames(), containsInAnyOrder("name"));
    }

    @Test
    public void shouldNotUseValueWhenIsInvalid() {
        Params params = new Params();
        Value name = params.add("name");
        assertThrows(QueryException.class, () ->{
            name.get();
        });

        assertThrows(QueryException.class, () ->{
            name.get(String.class);
        });
    }

    @Test
    public void shouldSetParameter() {
        Params params = new Params();
        Value name = params.add("name");
        KeyValueEntity<String> entity = KeyValueEntity.of("name", name);
        params.bind("name", "Ada Lovelace");

        assertEquals("Ada Lovelace", entity.get());

        params.bind("name", "Diana");
        assertEquals("Diana", entity.get());
    }

}