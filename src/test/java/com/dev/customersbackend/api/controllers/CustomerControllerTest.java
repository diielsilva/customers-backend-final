package com.dev.customersbackend.api.controllers;

import com.dev.customersbackend.common.dtos.customer.CustomerRequestDTO;
import com.dev.customersbackend.common.dtos.customer.CustomerResponseDTO;
import com.dev.customersbackend.common.dtos.error.ErrorResponseDTO;
import com.dev.customersbackend.common.security.dtos.UserAccessResponseDTO;
import com.dev.customersbackend.domain.factories.CustomerFactory;
import com.dev.customersbackend.domain.factories.UserFactory;
import com.dev.customersbackend.helpers.TestHelper;
import com.dev.customersbackend.wrappers.PageableWrapper;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "local")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomerControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;

    @BeforeEach
    void beforeEach() {
        headers = new HttpHeaders();
        String token = "";
        ResponseEntity<UserAccessResponseDTO> authResponse =
                restTemplate.postForEntity("/auth", UserFactory.getUserCredentials(), UserAccessResponseDTO.class);
        if (authResponse != null && authResponse.getBody() != null && authResponse.getBody().getToken() != null) {
            token = authResponse.getBody().getToken();
        }
        headers.add("Authorization", "Bearer " + token);
    }

    @Test
    void save_AssertCustomerWasSaved_WhenNoOneExceptionWasThrown() {
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse);
        assertNotNull(saveCustomerResponse.getBody());
        assertEquals("Vinicius Tiago Nathan Pires", saveCustomerResponse.getBody().getName());
        assertEquals(HttpStatus.CREATED, saveCustomerResponse.getStatusCode());
    }

    @Test
    void save_AssertCustomerWasSaved_WhenCustomersCEPIsNull() {
        CustomerRequestDTO customer = CustomerFactory.getViniciusRequestDTO();
        customer.getAddress().setCep(null);
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(customer, headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse);
        assertNotNull(saveCustomerResponse.getBody());
        assertEquals("Vinicius Tiago Nathan Pires", saveCustomerResponse.getBody().getName());
        assertEquals(HttpStatus.CREATED, saveCustomerResponse.getStatusCode());
    }

    @Test
    void save_AssertCustomerWasSaved_WhenCustomersCEPIsBlank() {
        CustomerRequestDTO customer = CustomerFactory.getViniciusRequestDTO();
        customer.getAddress().setCep("");
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(customer, headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse);
        assertNotNull(saveCustomerResponse.getBody());
        assertEquals("Vinicius Tiago Nathan Pires", saveCustomerResponse.getBody().getName());
        assertEquals(HttpStatus.CREATED, saveCustomerResponse.getStatusCode());
    }

    @Test
    void save_AssertThatAnExceptionWasThrown_WhenTokenIsInvalid() {
        generateInvalidToken();
        ResponseEntity<ErrorResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse);
        assertEquals(HttpStatus.FORBIDDEN, saveCustomerResponse.getStatusCode());
    }

    @Test
    void save_AssertThatAnExceptionWasThrown_WhenTokenIsNull() {
        headers = new HttpHeaders();
        ResponseEntity<ErrorResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse);
        assertEquals(HttpStatus.FORBIDDEN, saveCustomerResponse.getStatusCode());
    }

    @Test
    void save_AssertThatAnExceptionWasThrown_WhenCustomerWithAnInvalidPhoneIsGiven() {
        CustomerRequestDTO customer = CustomerFactory.getAgathaRequestDTO();
        customer.setPhoneNumber("12345");
        ResponseEntity<ErrorResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(customer, headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse);
        assertNotNull(saveCustomerResponse.getBody());
        assertEquals(1, saveCustomerResponse.getBody().getDetails().size());
        assertEquals("O telefone é inválido.", saveCustomerResponse.getBody().getDetails().get(0));
        assertEquals(HttpStatus.BAD_REQUEST, saveCustomerResponse.getStatusCode());
    }

    @Test
    void save_AssertThatAnExceptionWasThrown_WhenACustomerWithAnInvalidCEPIsGiven() {
        CustomerRequestDTO customer = CustomerFactory.getAgathaRequestDTO();
        customer.getAddress().setCep("12345");
        ResponseEntity<ErrorResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(customer, headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse);
        assertNotNull(saveCustomerResponse.getBody());
        assertEquals("Os campos estão ausentes ou inválidos.", saveCustomerResponse.getBody().getMessage());
        assertEquals("O CEP é inválido.", saveCustomerResponse.getBody().getDetails().get(0));
        assertEquals(HttpStatus.BAD_REQUEST, saveCustomerResponse.getStatusCode());
    }

    @Test
    void save_AssertThatAnExceptionWasThrown_WhenAnEmptyCustomerIsGiven() {
        ResponseEntity<ErrorResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getEmptyRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse);
        assertNotNull(saveCustomerResponse.getBody());
        assertEquals("Os campos estão ausentes ou inválidos.", saveCustomerResponse.getBody().getMessage());
        assertFalse(saveCustomerResponse.getBody().getDetails().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, saveCustomerResponse.getStatusCode());
    }

    @Test
    void save_AssertThatAnExceptionWasThrown_WhenACustomerWithAnInvalidBirthDateIsGiven() {
        CustomerRequestDTO customer = CustomerFactory.getViniciusRequestDTO();
        customer.setBirthDate(LocalDate.now().plusDays(1));
        ResponseEntity<ErrorResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(customer, headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse);
        assertNotNull(saveCustomerResponse.getBody());
        assertEquals("Os campos estão ausentes ou inválidos.", saveCustomerResponse.getBody().getMessage());
        assertFalse(saveCustomerResponse.getBody().getDetails().isEmpty());
        assertEquals(1, saveCustomerResponse.getBody().getDetails().size());
        assertEquals(HttpStatus.BAD_REQUEST, saveCustomerResponse.getStatusCode());
    }

    @Test
    void findAllOrderedByName_AssertThatAreCustomers_WhenHaveStoredCustomers() {
        ResponseEntity<CustomerResponseDTO> saveFirstCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveFirstCustomerResponse.getBody());

        ResponseEntity<CustomerResponseDTO> saveSecondCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getAgathaRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveSecondCustomerResponse.getBody());

        ResponseEntity<PageableWrapper<CustomerResponseDTO>> findCustomersOrderedByNameResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(findCustomersOrderedByNameResponse);
        assertNotNull(findCustomersOrderedByNameResponse.getBody());
        assertFalse(findCustomersOrderedByNameResponse.getBody().isEmpty());
        assertEquals(2, findCustomersOrderedByNameResponse.getBody().getContent().size());
        assertEquals("Agatha Hadassa Sebastiana Silva", findCustomersOrderedByNameResponse.getBody().getContent().get(0).getName());
    }

    @Test
    void findAllOrderedByName_AssertThatAreNotCustomers_WhenDoNotHaveStoredCustomers() {
        ResponseEntity<PageableWrapper<CustomerResponseDTO>> findCustomersOrderedByNameResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(findCustomersOrderedByNameResponse);
        assertNotNull(findCustomersOrderedByNameResponse.getBody());
        assertTrue(findCustomersOrderedByNameResponse.getBody().isEmpty());
    }

    @Test
    void findById_AssertThatAreCustomer_WhenCustomerIdIsFound() {
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<CustomerResponseDTO> findCustomerByIdResponse =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );
        assertNotNull(findCustomerByIdResponse);
        assertNotNull(findCustomerByIdResponse.getBody());
        assertEquals(saveCustomerResponse.getBody().getId(), findCustomerByIdResponse.getBody().getId());
        assertEquals(HttpStatus.OK, findCustomerByIdResponse.getStatusCode());
    }

    @Test
    void findById_AssertAnExceptionWasThrown_WhenCustomerIdIsNotFound() {
        ResponseEntity<ErrorResponseDTO> findCustomerByIdResponse =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        TestHelper.getInvalidId()
                );
        assertNotNull(findCustomerByIdResponse);
        assertNotNull(findCustomerByIdResponse.getBody());
        assertEquals(HttpStatus.NOT_FOUND, findCustomerByIdResponse.getStatusCode());
    }

    @Test
    void findByName_AssertThatAreCustomer_WhenCustomersNameIsFound() {
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<PageableWrapper<CustomerResponseDTO>> findCustomerByNameResponse =
                restTemplate.exchange("/customers/search?name=vini",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(findCustomerByNameResponse);
        assertNotNull(findCustomerByNameResponse.getBody());
        assertEquals("Vinicius Tiago Nathan Pires", findCustomerByNameResponse.getBody().getContent().get(0).getName());
    }

    @Test
    void findByName_AssertThatAreNotCustomer_WhenCustomerNameIsNotFound() {
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<PageableWrapper<CustomerResponseDTO>> findCustomerByNameResponse =
                restTemplate.exchange("/customers/search?name=xyz",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(findCustomerByNameResponse);
        assertNotNull(findCustomerByNameResponse.getBody());
        assertTrue(findCustomerByNameResponse.getBody().isEmpty());
    }

    @Test
    void update_AssertCustomerWasUpdated_WhenNoOneExceptionWasThrown() {
        CustomerRequestDTO customer = CustomerFactory.getViniciusRequestDTO();
        customer.setName("Vini");
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<CustomerResponseDTO> updateCustomerResponse =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(customer, headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );
        assertNotNull(updateCustomerResponse.getBody());

        ResponseEntity<CustomerResponseDTO> findCustomerResponse = restTemplate.exchange("/customers/{id}",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                },
                saveCustomerResponse.getBody().getId()
        );
        assertNotNull(findCustomerResponse);
        assertNotNull(findCustomerResponse.getBody());
        assertEquals("Vini", findCustomerResponse.getBody().getName());
        assertEquals(HttpStatus.OK, findCustomerResponse.getStatusCode());
    }

    @Test
    void update_AssertAnExceptionWasThrown_WhenCustomerWithAnInvalidPhoneIsGiven() {
        CustomerRequestDTO customer = CustomerFactory.getViniciusRequestDTO();
        customer.setPhoneNumber("Vini");
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<ErrorResponseDTO> updateCustomerResponse =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(customer, headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );
        assertNotNull(updateCustomerResponse);
        assertNotNull(updateCustomerResponse.getBody());
        assertEquals(1, updateCustomerResponse.getBody().getDetails().size());
        assertEquals("O telefone é inválido.", updateCustomerResponse.getBody().getDetails().get(0));
        assertEquals(HttpStatus.BAD_REQUEST, updateCustomerResponse.getStatusCode());
    }

    @Test
    void update_AssertAnExceptionWasThrown_WhenCustomerWithAnInvalidCEPIsGiven() {
        CustomerRequestDTO customer = CustomerFactory.getViniciusRequestDTO();
        customer.getAddress().setCep("Vini");

        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<ErrorResponseDTO> updateCustomerResponse =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(customer, headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );
        assertNotNull(updateCustomerResponse);
        assertNotNull(updateCustomerResponse.getBody());
        assertEquals(1, updateCustomerResponse.getBody().getDetails().size());
        assertEquals("O CEP é inválido.", updateCustomerResponse.getBody().getDetails().get(0));
        assertEquals(HttpStatus.BAD_REQUEST, updateCustomerResponse.getStatusCode());
    }

    @Test
    void update_AssertAnExceptionWasThrown_WhenCustomerWithAnInvalidBirthDateIsGiven() {
        CustomerRequestDTO customer = CustomerFactory.getViniciusRequestDTO();
        customer.setBirthDate(LocalDate.now().plusDays(1));

        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<ErrorResponseDTO> updateCustomerResponse =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(customer, headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );
        assertNotNull(updateCustomerResponse);
        assertNotNull(updateCustomerResponse.getBody());
        assertEquals(1, updateCustomerResponse.getBody().getDetails().size());
        assertEquals(HttpStatus.BAD_REQUEST, updateCustomerResponse.getStatusCode());
    }

    @Test
    void update_AssertAnExceptionWasThrown_WhenCustomerPhoneIsInUse() {
        CustomerRequestDTO customer = CustomerFactory.getViniciusRequestDTO();
        customer.setPhoneNumber("71998904074");
        ResponseEntity<CustomerResponseDTO> saveFirstCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveFirstCustomerResponse.getBody());

        ResponseEntity<CustomerResponseDTO> saveSecondCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getAgathaRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveSecondCustomerResponse.getBody());

        ResponseEntity<ErrorResponseDTO> updateFirstCustomerResponse =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(customer, headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveFirstCustomerResponse.getBody().getId()
                );
        assertNotNull(updateFirstCustomerResponse);
        assertNotNull(updateFirstCustomerResponse.getBody());
        assertEquals(HttpStatus.CONFLICT, updateFirstCustomerResponse.getStatusCode());
    }

    @Test
    void update_AssertAnExceptionWasThrown_WhenAnEmptyCustomerIsGiven() {
        CustomerRequestDTO customer = CustomerFactory.getEmptyRequestDTO();
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<ErrorResponseDTO> updateCustomerResponse =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(customer, headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );
        assertNotNull(updateCustomerResponse);
        assertNotNull(updateCustomerResponse.getBody());
        assertFalse(updateCustomerResponse.getBody().getDetails().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, updateCustomerResponse.getStatusCode());
    }

    @Test
    void update_AssertAnExceptionWasThrown_WhenCustomerIdIsNotFound() {
        ResponseEntity<ErrorResponseDTO> updateCustomerResponse =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.PUT,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        },
                        1L
                );
        assertNotNull(updateCustomerResponse);
        assertNotNull(updateCustomerResponse.getBody());
        assertEquals(HttpStatus.NOT_FOUND, updateCustomerResponse.getStatusCode());
    }

    @Test
    void delete_AssertThatCustomerWasDeleted_WhenCustomerIdIsFound() {
        ResponseEntity<CustomerResponseDTO> saveCustomerResponse =
                restTemplate.exchange("/customers",
                        HttpMethod.POST,
                        new HttpEntity<>(CustomerFactory.getViniciusRequestDTO(), headers),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertNotNull(saveCustomerResponse.getBody());

        ResponseEntity<Void> deleteCustomerResponse =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        saveCustomerResponse.getBody().getId()
                );
        assertNotNull(deleteCustomerResponse);
        assertEquals(HttpStatus.NO_CONTENT, deleteCustomerResponse.getStatusCode());
    }

    @Test
    void delete_AssertThatAnExceptionWasThrown_WhenCustomerIdIsNotFound() {
        ResponseEntity<ErrorResponseDTO> deleteCustomerResponse =
                restTemplate.exchange("/customers/{id}",
                        HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        new ParameterizedTypeReference<>() {
                        },
                        1L
                );
        assertNotNull(deleteCustomerResponse);
        assertEquals(HttpStatus.NOT_FOUND, deleteCustomerResponse.getStatusCode());
    }

    private void generateInvalidToken() {
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + "12345");
    }
}