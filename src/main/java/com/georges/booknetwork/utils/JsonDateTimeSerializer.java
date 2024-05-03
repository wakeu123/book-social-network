package com.georges.booknetwork.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateTimeSerializer extends JsonSerializer<Object> {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formattedDate = value.toString();
        if(value instanceof Date) {
            formattedDate = dateFormat.format((Date) value);
        } else if (value instanceof Timestamp) {
            formattedDate = dateFormat.format((Timestamp) value);
        }
        gen.writeString(formattedDate);
    }
}
