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
        ResponseEntity<UserAccessResponseDTO> authResponse =
                restTemplate.postForEntity("/auth", UserFactory.getUserCredentials(), UserAccessResponseDTO.class);
        assertNotNull(authResponse.getBody());
        headers.add("Authorization", "Bearer " + authResponse.getBody().getToken());
    }

    @Test
    void save_AssertPurchaseWasSaved_WhenNoOneExceptionWasThrown() {
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<CustomerResponseDTO> savePurchaseResponse =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );
        assertNotNull(savePurchaseResponse);
        assertNotNull(savePurchaseResponse.getBody());
        assertEquals(HttpStatus.CREATED, savePurchaseResponse.getStatusCode());
    }

    @Test
    void save_AssertAnExceptionWasThrown_WhenCustomersIdIsNotFound() {
        ResponseEntity<ErrorResponseDTO> savePurchaseResponse =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        -1L
                );
        assertNotNull(savePurchaseResponse);
        assertEquals(HttpStatus.NOT_FOUND, savePurchaseResponse.getStatusCode());
    }

    @Test
    void save_AssertAnExceptionWasThrown_WhenTokenIsNull() {
        headers = new HttpHeaders();
        ResponseEntity<ErrorResponseDTO> savePurchaseResponse =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        -1L
                );
        assertNotNull(savePurchaseResponse);
        assertEquals(HttpStatus.FORBIDDEN, savePurchaseResponse.getStatusCode());
    }

    @Test
    void save_AssertAnExceptionWasThrown_WhenTokenIsInvalid() {
        generateInvalidToken();
        ResponseEntity<ErrorResponseDTO> savePurchaseResponse =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        -1L
                );
        assertNotNull(savePurchaseResponse);
        assertEquals(HttpStatus.FORBIDDEN, savePurchaseResponse.getStatusCode());
    }

    @Test
    void findAllByCustomer_AssertThatArePurchase_WhenHaveStoredPurchases() {
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<PurchaseResponseDTO> savePurchaseResponse =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );

        ResponseEntity<List<PurchaseResponseDTO>> findPurchasesByCustomerResponse =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );
        assertNotNull(findPurchasesByCustomerResponse);
        assertNotNull(findPurchasesByCustomerResponse.getBody());
        assertNotNull(findPurchasesByCustomerResponse.getBody().get(0).getId());
        assertEquals(1, findPurchasesByCustomerResponse.getBody().size());
        assertEquals(HttpStatus.OK, findPurchasesByCustomerResponse.getStatusCode());
    }

    @Test
    void findAllByCustomer_AssertThatAreNotPurchase_WhenHaveNotStoredPurchases() {
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<List<PurchaseResponseDTO>> findPurchasesByCustomerResponse =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );
        assertNotNull(findPurchasesByCustomerResponse);
        assertNotNull(findPurchasesByCustomerResponse.getBody());
        assertTrue(findPurchasesByCustomerResponse.getBody().isEmpty());
        assertEquals(HttpStatus.OK, findPurchasesByCustomerResponse.getStatusCode());
    }

    @Test
    void findAllByCustomer_AssertThatAnExceptionWasThrown_WhenCustomerIdIsNotFound() {
        ResponseEntity<ErrorResponseDTO> findPurchasesByCustomerResponse =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        -1L
                );
        assertNotNull(findPurchasesByCustomerResponse);
        assertNotNull(findPurchasesByCustomerResponse.getBody());
        assertEquals(HttpStatus.NOT_FOUND, findPurchasesByCustomerResponse.getStatusCode());
    }

    @Test
    void delete_AssertPurchaseWasDeleted_WhenNoOneExceptionWasThrown() {
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<PurchaseResponseDTO> savePurchaseResponse =
                restTemplate.exchange("/purchases/{customerId}",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );
        assertNotNull(savePurchaseResponse.getBody());

        ResponseEntity<Void> deletePurchaseResponse =
                restTemplate.exchange("/purchases/{customerId}/{purchaseId}",
                        HttpMethod.DELETE,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId(),
                        savePurchaseResponse.getBody().getId()
                );
        assertNotNull(deletePurchaseResponse);
        assertEquals(HttpStatus.NO_CONTENT, deletePurchaseResponse.getStatusCode());
    }

    @Test
    void delete_AssertAnExceptionWasThrow_WhenPurchaseIdIsNotFound() {
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<ErrorResponseDTO> deletePurchaseResponse =
                restTemplate.exchange("/purchases/{customerId}/{purchaseId}",
                        HttpMethod.DELETE,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId(),
                        -1L
                );
        assertNotNull(deletePurchaseResponse);
        assertNotNull(deletePurchaseResponse.getBody());
        assertEquals(HttpStatus.NOT_FOUND, deletePurchaseResponse.getStatusCode());
    }

    private void generateInvalidToken() {
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + "12345");
    }
}