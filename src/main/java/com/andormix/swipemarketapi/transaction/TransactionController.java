package com.andormix.swipemarketapi.transaction;

import com.andormix.swipemarketapi.security.AppUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse>  create(
            @Valid @RequestBody TransactionRequest transactionRequest,
            @AuthenticationPrincipal AppUserPrincipal principal
    )
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(principal, transactionRequest));
    }

    @GetMapping
    public  ResponseEntity<List<TransactionResponse>>  findAll(@AuthenticationPrincipal AppUserPrincipal principal) {
        return ResponseEntity.ok(transactionService.findAll(principal));
    }
}
