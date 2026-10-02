package com.andormix.swipemarketapi.product;


import com.andormix.swipemarketapi.security.AppUserPrincipal;
import com.andormix.swipemarketapi.user.User;
import com.andormix.swipemarketapi.user.UserRepository;
import com.andormix.swipemarketapi.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest
{
    @Mock
    ProductRepository productRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ProductService productService;

    private Product product;
    private User seller;
    private User buyer;
    private AppUserPrincipal sellerPrincipal;
    private AppUserPrincipal buyerPrincipal;

    @BeforeEach
    void setUp()
    {
        seller = new User(
                "seller@andorra.ad",
                "pasword1234",
                "Eric",
                UserRole.USER
        );
        ReflectionTestUtils.setField(seller, "id", 1L);
        sellerPrincipal = AppUserPrincipal.from(seller);

        buyer = new User(
                "seller@andorra.ad",
                "pasword1234",
                "Eric",
                UserRole.USER
        );
        ReflectionTestUtils.setField(buyer, "id", 2L);
        buyerPrincipal = AppUserPrincipal.from(seller);

        product = new Product(
                seller,
                "Cute Lamp",
                "Nice cute lamp made of gold",
                new BigDecimal( 2000.00),
                ProductCategory.HOME,
                ProductCondition.GOOD,
                "Santju"
        );
        ReflectionTestUtils.setField(product, "id", 100L);

    }

    // ------------------------------ Happy path -----------------------------------------

    @Test
    void shouldChangeStatusFromActiveToReserved()
    {
        //GIVEN
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        // Cuando el servicio guarde el producto, devuelve el mismo producto modificado
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        //WHEN
        ChangeProductStatusRequest changeProductStatusRequest = new ChangeProductStatusRequest(
                ProductStatus.RESERVED
        );

        ProductResponse productResponse = productService.changeStatus(
                product.getId(),
                changeProductStatusRequest,
                sellerPrincipal
        );

        //THEN
        assertNotNull(productResponse);
        assertEquals(ProductStatus.RESERVED, productResponse.status());

        verify(productRepository, times(1)).save(product);
    }

    // ------------------------------ Unhappy path -----------------------------------------

    // Same state

    @Test
    void shouldNotChangeStatusFromActiveToActive()
    {
        // GIVEN
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        //when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        // WHEN & THEN
        ChangeProductStatusRequest changeProductStatusRequest = new ChangeProductStatusRequest(
                ProductStatus.ACTIVE
        );

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> productService.changeStatus(
                product.getId(),
                changeProductStatusRequest,
                sellerPrincipal));

        // THEN
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    // Previous state
    @Test
    void shouldNotChangeStatusFromSoldToActive()
    {
        // GIVEN
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        // WHEN & THEN
        ChangeProductStatusRequest changeProductStatusRequest = new ChangeProductStatusRequest(
                ProductStatus.SOLD
        );

        // To sold
        productService.changeStatus(
                product.getId(),
                changeProductStatusRequest,
                sellerPrincipal
        );

        ChangeProductStatusRequest changeProductStatusRequest2 = new ChangeProductStatusRequest(
                ProductStatus.ACTIVE
        );


        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> productService.changeStatus(
                product.getId(),
                changeProductStatusRequest2,
                sellerPrincipal));

        // THEN
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }
}
