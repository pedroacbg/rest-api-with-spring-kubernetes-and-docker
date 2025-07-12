package com.pedroacbg.rest_with_spring_boot.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class GenderSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String gender, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String formatedGender = "Male".equals(gender) ? "M" : "F";
        jsonGenerator.writeString(formatedGender);
    }
}
