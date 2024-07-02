package com.georges.booknetwork.controllers;

import com.georges.booknetwork.domains.Scientist;
import com.georges.booknetwork.exceptions.BookException;
import com.georges.booknetwork.services.ScientistService;
import com.georges.booknetwork.utils.QrCodeGenerator;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestController
@RequestMapping("scientists")
@RequiredArgsConstructor
@Tag(name = "Scientists")
public class ScientistController {

    private final MessageSource messageSource;
    private final ScientistService scientistService;

    @PostMapping("/save")
    public ResponseEntity<?> register(@RequestBody Scientist request, @RequestHeader("Accept-Language") Locale locale) {
        log.debug("Call of save scientist: {}", request);
        try {
            request = scientistService.save(request, locale);
            log.debug("entity : {}", request);
            return ResponseEntity.ok().body(request);
        }
        catch (BookException e) {
            log.debug(e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while save entity : {}", request, e);
            String message = messageSource.getMessage("unable.to.save.entity", new Object[] { request }, locale);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @GetMapping("/by-name")
    public ResponseEntity<?> authenticate(@RequestParam String name, @RequestHeader("Accept-Language") Locale locale) {
        log.debug("Call of retrieve with name: {}", name);
        try {
            Scientist scientist = scientistService.getByName(name, locale);
            log.debug("Scientist successfully retrieve");
            return ResponseEntity.ok().body(scientist);
        } catch (BookException ex) {
            log.debug(ex.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while retrieve scientist by name : {}", name, e);
            String message = messageSource.getMessage("unable.to.retrieve.scientist.with.name", new Object[]{ name }, locale);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestHeader("Accept-Language") Locale locale) throws IOException, WriterException {
        List<Scientist> items = scientistService.search(locale);
        if(items.size() > 0) {
            for (var item : items) {
                QrCodeGenerator.generateQrCode(item);
            }

        }
        return ResponseEntity.ok(items);
    }
}
