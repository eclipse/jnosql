package org.eclipse.jnosql.communication.query;

import jakarta.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class JSONQueryValueTest {

    private JsonObject json;

    @BeforeEach
    public void setUp() {
        this.json = JsonObject.TRUE.asJsonObject();
    }


}