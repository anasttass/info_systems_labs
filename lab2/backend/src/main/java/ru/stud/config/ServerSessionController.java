package ru.stud.config;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class ServerSessionController {

    private final String serverInstanceId =
            UUID.randomUUID().toString();

    @GetMapping("/auth/server-instance")
    public ResponseEntity<Map<String, String>> getServerInstance() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(Map.of(
                        "instanceId", serverInstanceId
                ));
    }
}