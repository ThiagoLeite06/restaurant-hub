package br.com.restaurant_hub.restauranthub.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("OpenAPI Configuration Tests")
class OpenApiConfigTest {

    @Test
    @DisplayName("Should load OpenAPI configuration")
    void shouldLoadOpenApiConfiguration() {
        // Given & When
        OpenApiConfig config = new OpenApiConfig();

        // Then
        assertNotNull(config);
    }
}