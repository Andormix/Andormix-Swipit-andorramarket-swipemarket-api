package com.andormix.swipemarketapi.interaction;

import com.andormix.swipemarketapi.auth.RegisterRequest;
import com.andormix.swipemarketapi.product.CreateProductRequest;
import com.andormix.swipemarketapi.product.ProductCategory;
import com.andormix.swipemarketapi.product.ProductCondition;
import com.andormix.swipemarketapi.product.ProductRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class InteractionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /*
     * Se utiliza el mismo enfoque que en ProductControllerTest.
     * En este proyecto ObjectMapper de Jackson 2 está disponible
     * mediante las dependencias actuales.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSwipeRepository productSwipeRepository;

    @Autowired
    private ProductFavoriteRepository productFavoriteRepository;

    @BeforeEach
    void cleanDatabase() {
        productFavoriteRepository.deleteAll();
        productSwipeRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldLikeProduct() throws Exception {
        String sellerToken = registerAndGetToken(
                "seller-like@example.com",
                "Seller Like"
        );

        String buyerToken = registerAndGetToken(
                "buyer-like@example.com",
                "Buyer Like"
        );

        Long productId = createProductAndGetId(
                sellerToken,
                "Desk lamp"
        );

        mockMvc.perform(post("/api/v1/products/" + productId + "/swipe")
                .header("Authorization", "Bearer " + buyerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "action": "LIKE"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(productId.intValue())))
                .andExpect(jsonPath("$.action", is("LIKE")))
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void shouldDislikeProduct() throws Exception {
        String sellerToken = registerAndGetToken(
                "seller-dislike@example.com",
                "Seller Dislike"
        );

        String buyerToken = registerAndGetToken(
                "buyer-dislike@example.com",
                "Buyer Dislike"
        );

        Long productId = createProductAndGetId(
                sellerToken,
                "Office chair"
        );

        mockMvc.perform(post("/api/v1/products/" + productId + "/swipe")
                .header("Authorization", "Bearer " + buyerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "action": "DISLIKE"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(productId.intValue())))
                .andExpect(jsonPath("$.action", is("DISLIKE")));
    }

    @Test
    void shouldChangePreviousSwipeAction() throws Exception {
        String sellerToken = registerAndGetToken(
                "seller-change-swipe@example.com",
                "Seller Change"
        );

        String buyerToken = registerAndGetToken(
                "buyer-change-swipe@example.com",
                "Buyer Change"
        );

        Long productId = createProductAndGetId(
                sellerToken,
                "Programming book"
        );

        mockMvc.perform(post("/api/v1/products/" + productId + "/swipe")
                .header("Authorization", "Bearer " + buyerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "action": "LIKE"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action", is("LIKE")));

        mockMvc.perform(post("/api/v1/products/" + productId + "/swipe")
                .header("Authorization", "Bearer " + buyerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "action": "DISLIKE"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(productId.intValue())))
                .andExpect(jsonPath("$.action", is("DISLIKE")));

        mockMvc.perform(get("/api/v1/swipes/interested")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    void shouldFindInterestedProducts() throws Exception {
        String sellerToken = registerAndGetToken(
                "seller-interested@example.com",
                "Seller Interested"
        );

        String buyerToken = registerAndGetToken(
                "buyer-interested@example.com",
                "Buyer Interested"
        );

        Long productId = createProductAndGetId(
                sellerToken,
                "Interested product"
        );

        mockMvc.perform(post("/api/v1/products/" + productId + "/swipe")
                .header("Authorization", "Bearer " + buyerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "action": "LIKE"
                        }
                        """))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/swipes/interested")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(productId.intValue())))
                .andExpect(jsonPath("$[0].title", is("Interested product")));
    }

    @Test
    void shouldNotAllowUserToSwipeOwnProduct() throws Exception {
        String sellerToken = registerAndGetToken(
                "seller-own-swipe@example.com",
                "Seller Own Swipe"
        );

        Long productId = createProductAndGetId(
                sellerToken,
                "Own product"
        );

        mockMvc.perform(post("/api/v1/products/" + productId + "/swipe")
                .header("Authorization", "Bearer " + sellerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "action": "LIKE"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddAndRemoveFavorite() throws Exception {
        String sellerToken = registerAndGetToken(
                "seller-favorite@example.com",
                "Seller Favorite"
        );

        String buyerToken = registerAndGetToken(
                "buyer-favorite@example.com",
                "Buyer Favorite"
        );

        Long productId = createProductAndGetId(
                sellerToken,
                "Favorite product"
        );

        mockMvc.perform(post("/api/v1/products/" + productId + "/favorite")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/favorites")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(productId.intValue())))
                .andExpect(jsonPath("$[0].title", is("Favorite product")));

        mockMvc.perform(delete("/api/v1/products/" + productId + "/favorite")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/favorites")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    void shouldNotCreateDuplicateFavorite() throws Exception {
        String sellerToken = registerAndGetToken(
                "seller-duplicate-favorite@example.com",
                "Seller Duplicate"
        );

        String buyerToken = registerAndGetToken(
                "buyer-duplicate-favorite@example.com",
                "Buyer Duplicate"
        );

        Long productId = createProductAndGetId(
                sellerToken,
                "Duplicate favorite product"
        );

        mockMvc.perform(post("/api/v1/products/" + productId + "/favorite")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/products/" + productId + "/favorite")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/favorites")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void shouldNotAllowUserToFavoriteOwnProduct() throws Exception {
        String sellerToken = registerAndGetToken(
                "seller-own-favorite@example.com",
                "Seller Own Favorite"
        );

        Long productId = createProductAndGetId(
                sellerToken,
                "Own favorite product"
        );

        mockMvc.perform(post("/api/v1/products/" + productId + "/favorite")
                .header("Authorization", "Bearer " + sellerToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectFavoritesWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/favorites"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectSwipeWithoutToken() throws Exception {
        String sellerToken = registerAndGetToken(
                "seller-no-token@example.com",
                "Seller No Token"
        );

        Long productId = createProductAndGetId(
                sellerToken,
                "Protected swipe product"
        );

        mockMvc.perform(post("/api/v1/products/" + productId + "/swipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "action": "LIKE"
                        }
                        """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectFavoriteForNonExistingProduct() throws Exception {
        String buyerToken = registerAndGetToken(
                "buyer-missing-product@example.com",
                "Buyer Missing"
        );

        mockMvc.perform(post("/api/v1/products/999999/favorite")
                .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectSwipeForNonExistingProduct() throws Exception {
        String buyerToken = registerAndGetToken(
                "buyer-missing-swipe@example.com",
                "Buyer Missing Swipe"
        );

        mockMvc.perform(post("/api/v1/products/999999/swipe")
                .header("Authorization", "Bearer " + buyerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "action": "LIKE"
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    private String registerAndGetToken(
            String email,
            String displayName
    ) throws Exception {
        RegisterRequest request = new RegisterRequest(
                email,
                "password123",
                displayName
        );

        String response = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);

        return json.get("token").asText();
    }

    private Long createProductAndGetId(
            String sellerToken,
            String title
    ) throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                title,
                "Product description for " + title,
                new BigDecimal("25.00"),
                ProductCategory.HOME,
                ProductCondition.GOOD,
                "Andorra la Vella"
        );

        String response = mockMvc.perform(post("/api/v1/products")
                .header("Authorization", "Bearer " + sellerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);

        return json.get("id").asLong();
    }
}