package com.georges.booknetwork.services.impls;

import com.georges.booknetwork.domains.Scientist;
import com.georges.booknetwork.exceptions.BookException;
import com.georges.booknetwork.repositories.ScientistRepository;
import com.georges.booknetwork.services.ScientistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScientistServiceImpl implements ScientistService {

    private final MessageSource messageSource;
    private final ScientistRepository scientistRepository;

    @Override
    public Scientist save(Scientist scientist, Locale locale) {
        log.debug("Try to Save entity : {}", scientist);
        if (this.scientistRepository == null) {
            return scientist;
        }
        try {
            if (scientist == null) {
                String message = this.messageSource.getMessage("unable.to.save.null.object", new Object[] { null },
                        locale);
                throw new BookException(message);
            }
            Scientist toSaved = new Scientist();
            toSaved.setName(scientist.getName());
            toSaved.setPhotoUrl(scientist.getPhotoUrl());
            toSaved.setDescription(scientist.getDescription());

            scientist.setId(this.scientistRepository.save(toSaved).getId());
            log.debug("entity successfully saved : {} ", scientist);
            return scientist;
        } catch (BookException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while save entity : {}", scientist, e);
            String message = this.messageSource.getMessage("unable.to.save.model", new Object[] { scientist }, locale);
            throw new BookException(INTERNAL_SERVER_ERROR.value(), message);
        }
    }

    @Override
    public List<Scientist> upload(MultipartFile file) {

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheet("scientist");
            List<Scientist> items = new ArrayList<>();
            int indexRow = 0;
            for (Row row : sheet) {
                if(indexRow == 0) {
                    indexRow++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int indexCell = 0;
                Scientist scientist = new Scientist();
                while(cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (indexCell){
                        case 0: scientist.setId((long) cell.getNumericCellValue());
                        case 1: scientist.setName(cell.getStringCellValue());
                        case 3: scientist.setDescription(cell.getStringCellValue());
                        case 4: scientist.setPhotoUrl(cell.getStringCellValue());
                        default: {};
                    }
                    indexCell++;
                }
                items.add(scientist);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public Scientist getByName(String name, Locale locale) {
        log.debug("Try to retrieve scientist by name : {}", name);
        try {
            if (!StringUtils.hasText(name)) {
                String message = this.messageSource.getMessage("unable.to.retrieve.by.null.name", new Object[] { null },
                        locale);
                throw new BookException(message);
            }
            Scientist scientist = this.scientistRepository.findByName(name)
                            .orElseThrow(() -> {
                                String message = this.messageSource.getMessage("scientist.not.found", new Object[] { name },
                                        locale);
                                return new BookException(message);
                            });

            log.debug("scientist successfully retrieve : {} ", scientist);
            return scientist;
        } catch (BookException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while retrieve scientist with name : {}", name, e);
            String message = this.messageSource.getMessage("unable.to.retrieve.scientist", new Object[] { name }, locale);
            throw new BookException(INTERNAL_SERVER_ERROR.value(), message);
        }
    }

    @Override
    public Scientist getById(Long id, Locale locale) {
        log.debug("Try to retrieve scientist by id : {}", id);
        try {
            if (!StringUtils.hasText(String.valueOf(id))) {
                String message = this.messageSource.getMessage("unable.to.retrieve.by.null.id", new Object[] { null },
                        locale);
                throw new BookException(message);
            }
            Scientist scientist = this.scientistRepository.findById(id)
                    .orElseThrow(() -> {
                        String message = this.messageSource.getMessage("scientist.not.found", new Object[] { id },
                                locale);
                        return new BookException(message);
                    });

            log.debug("entity successfully retrieve : {} ", scientist);
            return scientist;
        } catch (BookException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while retrieve scientist with id : {}", id, e);
            String message = this.messageSource.getMessage("unable.to.retrieve.scientist", new Object[] { id }, locale);
            throw new BookException(INTERNAL_SERVER_ERROR.value(), message);
        }
    }

    @Override
    public List<Scientist> search(Locale locale) {
        return scientistRepository.findAll();
    }
}
