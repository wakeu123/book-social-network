package com.georges.booknetwork.domains.base;

import java.io.Serializable;

public interface IPersistent extends Serializable {

    public abstract Object getId();
}
