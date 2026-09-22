package com.andormix.swipemarketapi.product;

import com.andormix.swipemarketapi.security.AppUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(
            @Valid @RequestBody CreateProductRequest request,
            @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        return productService.create(request, principal);
    }

    @GetMapping
    public Page<ProductResponse> findAll(
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);

        PageRequest pageRequest = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return productService.findAll(status, pageRequest);
    }

    @GetMapping("/{productId}")
    public ProductResponse findById(@PathVariable Long productId) {
        return productService.findById(productId);
    }

    @PutMapping("/{productId}")
    public ProductResponse update(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request,
            @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        return productService.update(productId, request, principal);
    }

    @PatchMapping("/{productId}/status")
    public ProductResponse changeStatus(
            @PathVariable Long productId,
            @Valid @RequestBody ChangeProductStatusRequest request,
            @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        return productService.changeStatus(productId, request, principal);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long productId,
            @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        productService.delete(productId, principal);
    }
}