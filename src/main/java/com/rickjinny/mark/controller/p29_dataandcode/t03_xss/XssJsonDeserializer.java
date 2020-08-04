package com.rickjinny.mark.controller.p29_dataandcode.t03_xss;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

public class XssJsonDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        String value = jsonParser.getValueAsString();
        if (value != null) {
            return HtmlUtils.htmlEscape(value);
        }
        return value;
    }

    @Override
    public Class<?> handledType() {
        return String.class;
    }
}
