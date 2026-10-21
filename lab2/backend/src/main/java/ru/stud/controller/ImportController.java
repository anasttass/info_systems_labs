package ru.stud.controller;

import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stud.dto.ImportHistoryResponse;
import ru.stud.service.ImportService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/imports")
public class ImportController {
    private final ImportService service;
    public ImportController(ImportService service) { this.service = service; }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImportHistoryResponse upload(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal Jwt jwt) {
        return service.importFile(file, jwt);
    }

    @GetMapping
    public List<ImportHistoryResponse> history(@AuthenticationPrincipal Jwt jwt) {
        return service.history(jwt);
    }

    @ExceptionHandler(ImportService.ImportFailedException.class)
    public ResponseEntity<Map<String, String>> invalid(ImportService.ImportFailedException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
