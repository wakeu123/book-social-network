package com.georges.booknetwork.services;

import com.georges.booknetwork.domains.Scientist;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

public interface ScientistService {

    Scientist getByName(String name, Locale locale);

    Scientist getById(Long id, Locale locale);

    List<Scientist> search(Locale locale);

    Scientist save(Scientist scientist, Locale locale);

    List<Scientist> upload(MultipartFile file);
}
