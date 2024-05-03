package com.georges.booknetwork.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.expression.ParseException;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateTimeDeserializer extends JsonDeserializer<Object> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        try {
            if (jsonParser.getCurrentToken() != null) {
                String text = jsonParser.getText();
                Date date = dateFormat.parse(text);
                return new Timestamp(date.getTime());
            }
        } catch (ParseException e) {
            try {
                String text = jsonParser.getText();
                Date date = dateFormat2.parse(text);
                return new Timestamp(date.getTime());
            } catch (ParseException | java.text.ParseException e1) {
                try {
                    String text = jsonParser.getText();
                    return new Timestamp(Long.valueOf(text));
                } catch (Exception e2) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
