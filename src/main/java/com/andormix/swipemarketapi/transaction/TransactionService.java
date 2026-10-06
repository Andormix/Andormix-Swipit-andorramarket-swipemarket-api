package com.andormix.swipemarketapi.transaction;

import com.andormix.swipemarketapi.product.Product;
import com.andormix.swipemarketapi.product.ProductRepository;
import com.andormix.swipemarketapi.security.AppUserPrincipal;
import com.andormix.swipemarketapi.user.User;
import com.andormix.swipemarketapi.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public TransactionResponse create(AppUserPrincipal principal, TransactionRequest request)
    {
        Product product = productRepository.getReferenceById(request.productId());
        User user = userRepository.getReferenceById(principal.getUserId());

        Transaction transaction = new Transaction(
               user, product, request.transactionType(), request.note(), request.amount()
        );

        return toResponse(transactionRepository.save(transaction));
    }

    public List<TransactionResponse> findAll(AppUserPrincipal principal)
    {
         return transactionRepository.findAllByUserIdOrderByCreatedAtDesc(principal.getUserId()).stream()
                .map(this::toResponse).toList();
    }

    public TransactionResponse toResponse(Transaction t)
    {
        return new TransactionResponse(
                t.getId(),
                t.getUser().getId(),
                t.getProduct().getId(),
                t.getTransactionType(),
                t.getTransactionStatus(),
                t.getAmount(),
                t.getCreatedAt(),
                t.getNote()
        );
    }
}
