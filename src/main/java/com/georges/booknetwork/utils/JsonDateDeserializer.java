package com.georges.booknetwork.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.expression.ParseException;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateDeserializer extends JsonDeserializer<Object> {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

    public void serialize(Object object, JsonGenerator gen, SerializerProvider provider) throws IOException {

        String formattedDate = object.toString();
        if (object instanceof Date) {
            formattedDate = dateFormat.format((Date) object);
        } else if (object instanceof Timestamp) {
            formattedDate = dateFormat.format((Timestamp) object);
        }

        gen.writeString(formattedDate);
    }

    public static Date deserialize(String text) {
        try {
            if (text != null) {
                return dateFormat.parse(text);
            }
        } catch (ParseException e) {
            try {
                return dateFormat2.parse(text);
            } catch (ParseException | java.text.ParseException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        try {
            if (jsonParser.getCurrentToken() != null) {
                String text = jsonParser.getText();
                return dateFormat.parse(text);
            }
        } catch (ParseException e) {
            try {
                String text = jsonParser.getText();
                return dateFormat2.parse(text);
            } catch (ParseException | java.text.ParseException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
