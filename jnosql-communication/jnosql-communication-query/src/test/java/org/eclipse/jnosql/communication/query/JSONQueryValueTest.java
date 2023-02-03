package org.eclipse.jnosql.communication.query;

import jakarta.json.JsonObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JSONQueryValueTest {

    private JsonObject json;

    @BeforeEach
    public void setUp() {
        this.json = JsonObject.TRUE.asJsonObject();
    }


    @Test
    public void shouldGetType() {
        JSONQueryValue queryValue = new JSONQueryValue(json);
        assertThat(queryValue).isNotNull()
                .extracting(JSONQueryValue::type)
                .isEqualTo(ValueType.JSON);
    }

    @Test
    public void shouldGet() {
        JSONQueryValue queryValue = new JSONQueryValue(json);
        assertThat(queryValue).isNotNull()
                .extracting(JSONQueryValue::get)
                .isEqualTo(json);
    }

    @Test
    public void shouldEquals(){
        JSONQueryValue queryValue = new JSONQueryValue(json);
        assertEquals(queryValue, new JSONQueryValue(json));
    }

    @Test
    public void shouldHashCode() {
        JSONQueryValue queryValue = new JSONQueryValue(json);
        assertEquals(queryValue.hashCode(), new JSONQueryValue(json).hashCode());
    }

}