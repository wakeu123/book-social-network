package com.georges.booknetwork.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateSerializer extends JsonSerializer<Object> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void serialize(Object object, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formattedDate = object.toString();

        try {
            if (object instanceof Date) {
                formattedDate = dateFormat.format((Date) object);
            } else if (object instanceof Timestamp) {
                formattedDate = dateFormat.format((Timestamp) object);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            if (object instanceof Date) {
                formattedDate = dateFormat2.format((Date) object);
            } else if (object instanceof Timestamp) {
                formattedDate = dateFormat2.format((Timestamp) object);
            }
        }

        gen.writeString(formattedDate);
    }
}
