package com.dev.customersbackend.api.controllers;

import com.dev.customersbackend.common.dtos.customer.CustomerResponseDTO;
import com.dev.customersbackend.common.dtos.error.ErrorResponseDTO;
import com.dev.customersbackend.common.dtos.purchase.PurchaseResponseDTO;
import com.dev.customersbackend.common.security.dtos.UserAccessResponseDTO;
import com.dev.customersbackend.domain.factories.CustomerFactory;
import com.dev.customersbackend.domain.factories.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "local")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PurchaseControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;

    @BeforeEach
    void setup() {
        headers = new HttpHeaders();
        ResponseEntity<UserAccessResponseDTO> response =
                restTemplate.postForEntity("/auth", UserFactory.getUserCredentials(), UserAccessResponseDTO.class);
        headers.add("Authorization", "Bearer " + response.getBody().getToken());
    }

    @Test
    void save_AssertPurchaseWasSaved_WhenNoOneExceptionWasThrown() {
        ResponseEntity<CustomerResponseDTO> responseOne =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        ResponseEntity<CustomerResponseDTO> responseTwo =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        responseOne.getBody().getId()
                );
        assertNotNull(responseTwo);
        assertNotNull(responseTwo.getBody());
        assertEquals(HttpStatus.CREATED, responseTwo.getStatusCode());
        deleteCustomer(responseOne.getBody().getId());
    }

    @Test
    void save_AssertAnExceptionWasThrown_WhenCustomersIdIsNotFound() {
        ResponseEntity<ErrorResponseDTO> response =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        -1L
                );
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void save_AssertAnExceptionWasThrown_WhenTokenIsNull() {
        headers = new HttpHeaders();
        ResponseEntity<ErrorResponseDTO> response =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        -1L
                );
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void save_AssertAnExceptionWasThrown_WhenTokenIsInvalid() {
        generateInvalidToken();
        ResponseEntity<ErrorResponseDTO> response =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        -1L
                );
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void findAllByCustomer_AssertThatArePurchase_WhenHaveStoredPurchases() {
        ResponseEntity<CustomerResponseDTO> responseOne =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        ResponseEntity<PurchaseResponseDTO> responseTwo =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        responseOne.getBody().getId()
                );
        ResponseEntity<List<PurchaseResponseDTO>> responseThree =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        responseOne.getBody().getId()
                );
        assertNotNull(responseThree);
        assertNotNull(responseThree.getBody());
        assertNotNull(responseThree.getBody().get(0).getId());
        assertEquals(1, responseThree.getBody().size());
        assertEquals(HttpStatus.OK, responseThree.getStatusCode());
        deleteCustomer(responseOne.getBody().getId());
    }

    @Test
    void findAllByCustomer_AssertThatAreNotPurchase_WhenHaveNotStoredPurchases() {
        ResponseEntity<CustomerResponseDTO> responseOne =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        ResponseEntity<List<PurchaseResponseDTO>> responseTwo =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        responseOne.getBody().getId()
                );
        assertNotNull(responseTwo);
        assertNotNull(responseTwo.getBody());
        assertTrue(responseTwo.getBody().isEmpty());
        assertEquals(HttpStatus.OK, responseTwo.getStatusCode());
        deleteCustomer(responseOne.getBody().getId());
    }

    @Test
    void findAllByCustomer_AssertThatAnExceptionWasThrown_WhenCustomerIdIsNotFound() {
        ResponseEntity<ErrorResponseDTO> response =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        -1L
                );
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void delete_AssertPurchaseWasDeleted_WhenNoOneExceptionWasThrown() {
        ResponseEntity<CustomerResponseDTO> responseOne =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        ResponseEntity<PurchaseResponseDTO> responseTwo =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        },
                        responseOne.getBody().getId()
                );
        ResponseEntity<Void> responseThree =
                restTemplate.exchange("/purchases/{customerId}/{purchaseId}",
                        HttpMethod.DELETE,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        },
                        responseOne.getBody().getId(),
                        responseTwo.getBody().getId()
                );
        assertNotNull(responseThree);
        assertEquals(HttpStatus.NO_CONTENT, responseThree.getStatusCode());
        deleteCustomer(responseOne.getBody().getId());
    }

    @Test
    void delete_AssertAnExceptionWasThrow_WhenPurchaseIdIsNotFound() {
        ResponseEntity<CustomerResponseDTO> responseOne =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        ResponseEntity<ErrorResponseDTO> responseTwo =
                restTemplate.exchange("/purchases/{customerId}/{purchaseId}",
                        HttpMethod.DELETE,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        },
                        responseOne.getBody().getId(),
                        -1L
                );
        assertNotNull(responseTwo);
        assertNotNull(responseTwo.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseTwo.getStatusCode());
        deleteCustomer(responseOne.getBody().getId());
    }

    private void deleteCustomer(long id) {
        ResponseEntity<ErrorResponseDTO> response =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        id
                );
    }

    private void generateInvalidToken() {
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + "12345");
    }
}