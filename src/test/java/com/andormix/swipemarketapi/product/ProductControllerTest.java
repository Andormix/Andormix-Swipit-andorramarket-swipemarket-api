package com.andormix.swipemarketapi.product;

import com.andormix.swipemarketapi.auth.LoginRequest;
import com.andormix.swipemarketapi.auth.RegisterRequest;
import com.andormix.swipemarketapi.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc

class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //@Autowired
    //private ObjectMapper objectMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void cleanDatabase() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateProductWithAuthenticatedUser() throws Exception {
        String token = registerAndGetToken("seller@example.com", "Seller User");

        CreateProductRequest request = new CreateProductRequest(
                "Desk lamp",
                "Lamp in good condition",
                new BigDecimal("25.50"),
                ProductCategory.HOME,
                ProductCondition.GOOD,
                "Escaldes-Engordany"
        );

        mockMvc.perform(post("/api/v1/products")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Desk lamp")))
                .andExpect(jsonPath("$.category", is("HOME")))
                .andExpect(jsonPath("$.condition", is("GOOD")))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.sellerDisplayName", is("Seller User")));
    }

    @Test
    void shouldRejectProductCreationWithoutToken() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "Desk lamp",
                "Lamp in good condition",
                new BigDecimal("25.50"),
                ProductCategory.HOME,
                ProductCondition.GOOD,
                "Escaldes-Engordany"
        );

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReadProductsWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void shouldNotAllowAnotherUserToModifyProduct() throws Exception {
        String sellerToken = registerAndGetToken(
                "seller-owner@example.com",
                "Owner"
        );

        String otherUserToken = registerAndGetToken(
                "other-user@example.com",
                "Other User"
        );

        CreateProductRequest createRequest = new CreateProductRequest(
                "Chair",
                "Wooden chair",
                new BigDecimal("40.00"),
                ProductCategory.FURNITURE,
                ProductCondition.GOOD,
                "Encamp"
        );

        String productResponse = mockMvc.perform(post("/api/v1/products")
                .header("Authorization", "Bearer " + sellerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long productId = objectMapper
                .readTree(productResponse)
                .get("id")
                .asLong();

        UpdateProductRequest updateRequest = new UpdateProductRequest(
                "Modified chair",
                "Modified description",
                new BigDecimal("35.00"),
                ProductCategory.FURNITURE,
                ProductCondition.FAIR,
                "Encamp"
        );

        String responseBody = mockMvc.perform(put("/api/v1/products/" + productId)
                .header("Authorization", "Bearer " + otherUserToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden())
                .andReturn()                      // Captura el resultado MvcResult
                .getResponse()                    // Obtiene la respuesta HTTP simulada
                .getContentAsString();

        //Para ver que esta vacío.
        System.out.println("Cuerpo de la respuesta: " + responseBody);
    }

    @Test
    void shouldChangeProductStatus() throws Exception {
        String token = registerAndGetToken(
                "status-owner@example.com",
                "Status Owner"
        );

        CreateProductRequest createRequest = new CreateProductRequest(
                "Book",
                "Programming book",
                new BigDecimal("10.00"),
                ProductCategory.BOOKS,
                ProductCondition.LIKE_NEW,
                "Ordino"
        );

        String productResponse = mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long productId = objectMapper
                .readTree(productResponse)
                .get("id")
                .asLong();

        ChangeProductStatusRequest statusRequest =
                new ChangeProductStatusRequest(ProductStatus.SOLD);

        mockMvc.perform(patch("/api/v1/products/" + productId + "/status")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SOLD")));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        String token = registerAndGetToken(
                "delete-owner@example.com",
                "Delete Owner"
        );

        CreateProductRequest createRequest = new CreateProductRequest(
                "Old phone",
                "Phone for parts",
                new BigDecimal("15.00"),
                ProductCategory.ELECTRONICS,
                ProductCondition.FAIR,
                "Canillo"
        );

        String productResponse = mockMvc.perform(post("/api/v1/products")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long productId = objectMapper
                .readTree(productResponse)
                .get("id")
                .asLong();

        mockMvc.perform(delete("/api/v1/products/" + productId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/products/" + productId))
                .andExpect(status().isNotFound());
    }

    private String registerAndGetToken(String email, String displayName
    ) throws Exception {

        RegisterRequest request = new RegisterRequest(email, "password123", displayName);

        String response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))) // Takes RegisterRequest object and serializes it into a raw JSON string
                .andExpect(status().isCreated())//Asserts
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        return json.get("token").asText();
    }

    @Test
    void shouldSearchProductsByTitle() throws Exception {
        String token = registerAndGetToken(
                "search@example.com",
                "Search User"
        );

        CreateProductRequest lamp = new CreateProductRequest(
                "Desk lamp",
                "Lamp for studying",
                new BigDecimal("25.00"),
                ProductCategory.HOME,
                ProductCondition.GOOD,
                "Encamp"
        );

        CreateProductRequest chair = new CreateProductRequest(
                "Office chair",
                "Comfortable chair",
                new BigDecimal("80.00"),
                ProductCategory.FURNITURE,
                ProductCondition.GOOD,
                "Encamp"
        );

        createProduct(token, lamp);
        createProduct(token, chair);

        mockMvc.perform(get("/api/v1/products")
                        .param("q", "lamp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Desk lamp")));
    }

    @Test
    void shouldFilterProductsByCategoryAndPrice() throws Exception {
        String token = registerAndGetToken(
                "filter@example.com",
                "Filter User"
        );

        createProduct(token, new CreateProductRequest(
                "Cheap electronics",
                "Electronic device",
                new BigDecimal("20.00"),
                ProductCategory.ELECTRONICS,
                ProductCondition.GOOD,
                "Ordino"
        ));

        createProduct(token, new CreateProductRequest(
                "Expensive electronics",
                "Electronic device",
                new BigDecimal("500.00"),
                ProductCategory.ELECTRONICS,
                ProductCondition.NEW,
                "Ordino"
        ));

        createProduct(token, new CreateProductRequest(
                "Book",
                "Programming book",
                new BigDecimal("20.00"),
                ProductCategory.BOOKS,
                ProductCondition.GOOD,
                "Ordino"
        ));

        mockMvc.perform(get("/api/v1/products")
                        .param("category", "ELECTRONICS")
                        .param("minPrice", "10")
                        .param("maxPrice", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Cheap electronics")));
    }

    @Test
    void shouldPaginateProducts() throws Exception {
        String token = registerAndGetToken(
                "pagination@example.com",
                "Pagination User"
        );

        for (int i = 1; i <= 3; i++) {
            createProduct(token, new CreateProductRequest(
                    "Product " + i,
                    "Product description " + i,
                    new BigDecimal(i * 10L),
                    ProductCategory.OTHER,
                    ProductCondition.GOOD,
                    "Canillo"
            ));
        }

        mockMvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(2)))
                .andExpect(jsonPath("$.size", is(2)))
                .andExpect(jsonPath("$.number", is(0)));
    }


    // ---------------------- HELPERS -----------------------------

    private String createProduct(
            String token,
            CreateProductRequest request
    ) throws Exception {
        return mockMvc.perform(post("/api/v1/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void shouldRejectInvalidPriceRange() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .param("minPrice", "100")
                        .param("maxPrice", "20"))
                .andExpect(status().isBadRequest());
    }


}