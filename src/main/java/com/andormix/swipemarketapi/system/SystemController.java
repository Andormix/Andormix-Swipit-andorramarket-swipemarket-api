package com.andormix.swipemarketapi.system;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    @GetMapping("/status")
    public ResponseEntity<SystemStatusResponse> getStatus() {
        SystemStatusResponse response = new SystemStatusResponse(
                "UP",
                "swipe-market-api",
                Instant.now()
        );

        return ResponseEntity.ok(response);
    }
}