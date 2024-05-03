package com.georges.booknetwork.domains.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@MappedSuperclass
@Getter
@Setter
public abstract class Persistent implements IPersistent{

    public abstract Long getId();

    public abstract void setId(Long value);

    public abstract Object copy();

    @JsonIgnore
    public boolean isPersistent() {
        return getId() != null;
    }

    @Override
    public String toString() {
        return "ID = " + getId();
    }
}
