package com.georges.booknetwork.services;

import com.georges.booknetwork.domains.Scientist;

import java.util.List;
import java.util.Locale;

public interface ScientistService {

    public Scientist save(Scientist scientist, Locale locale);

    public Scientist getByName(String name, Locale locale);

    public Scientist getById(Long id, Locale locale);

    public List<Scientist> search(Locale locale);
}
