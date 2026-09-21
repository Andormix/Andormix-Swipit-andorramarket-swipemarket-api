package com.andormix.swipemarketapi.product;

import com.andormix.swipemarketapi.security.AppUserPrincipal;
import com.andormix.swipemarketapi.user.User;
import com.andormix.swipemarketapi.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductService(
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public ProductResponse create(
            CreateProductRequest request,
            AppUserPrincipal principal
    ) {
        User seller = getUser(principal.getUserId());

        Product product = new Product(
                seller,
                normalize(request.title()),
                normalize(request.description()),
                request.price(),
                request.category(),
                request.condition(),
                normalize(request.parish())
        );

        return toResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(
            ProductStatus status,
            Pageable pageable
    ) {
        Page<Product> products = status == null
                ? productRepository.findAll(pageable)
                : productRepository.findByStatus(status, pageable);

        return products.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long productId) {
        return toResponse(getProduct(productId));
    }

    public ProductResponse update(
            Long productId,
            UpdateProductRequest request,
            AppUserPrincipal principal
    ) {
        Product product = getProduct(productId);
        checkOwnership(product, principal);

        product.update(
                normalize(request.title()),
                normalize(request.description()),
                request.price(),
                request.category(),
                request.condition(),
                normalize(request.parish())
        );

        return toResponse(productRepository.save(product));
    }

    public ProductResponse changeStatus(
            Long productId,
            ChangeProductStatusRequest request,
            AppUserPrincipal principal
    ) {
        Product product = getProduct(productId);
        checkOwnership(product, principal);

        product.changeStatus(request.status());

        return toResponse(productRepository.save(product));
    }

    public void delete(
            Long productId,
            AppUserPrincipal principal
    ) {
        Product product = getProduct(productId);
        checkOwnership(product, principal);

        productRepository.delete(product);
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found"
                ));
    }

    private void checkOwnership(
            Product product,
            AppUserPrincipal principal
    ) {
        if (!product.getSeller().getId().equals(principal.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You do not own this product"
            );
        }
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSeller().getId(),
                product.getSeller().getDisplayName(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getCondition(),
                product.getParish(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    private String normalize(String value) {
        return value.trim();
    }
}