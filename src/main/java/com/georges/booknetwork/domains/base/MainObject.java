package com.georges.booknetwork.domains.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.georges.booknetwork.utils.JsonDateTimeDeserializer;
import com.georges.booknetwork.utils.JsonDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.sql.Timestamp;

@MappedSuperclass
@Getter
@Setter
public abstract class MainObject extends Persistent{

    @Serial
    private static final long serialVersionUID = 8618687456823021763L;

    private String name;

    private String description;

    /**
     * <p style="margin-top: 0">
     * Creation date time.
     * </p>
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    @Column(nullable = false)
    private Timestamp creationDate;

    /**
     * <p style="margin-top: 0">
     * Last modification date time.
     * </p>
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    @Column(nullable = false)
    private Timestamp modificationDate;

    /**
     * Default constructor.
     */
    public MainObject() {
        creationDate = new Timestamp(System.currentTimeMillis());
        modificationDate = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return name;
    }
}
