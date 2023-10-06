package com.dev.customersbackend.domain.repositories;

import com.dev.customersbackend.domain.entities.User;
import com.dev.customersbackend.domain.factories.UserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles(value = "local")
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Test
    void save_AssertUserIsSaved_WhenNoOneExceptionWasThrown() {
        repository.save(UserFactory.getOliverWithoutId());
        List<User> models = repository.findAll();
        assertNotNull(models);
        assertFalse(models.isEmpty());
        assertEquals(1, models.size());
        assertEquals("Oliver Bernardo Melo", models.get(0).getCompleteName());
    }

    @Test
    void save_AssertThatAnExceptionWasThrown_WhenUserNameIsInUse() {
        repository.save(UserFactory.getOliverWithoutId());
        User model = UserFactory.getOliverWithoutId();
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(model));
    }

    @Test
    void findByUsername_AssertThatAreUser_WhenUserNameIsFound() {
        repository.save(UserFactory.getOliverWithoutId());
        Optional<User> optional = repository.findByUsername("oliver");
        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals("oliver", optional.get().getUsername());
    }

    @Test
    void findByUsername_AssertThatAreNotUser_WhenUsernameIsNotFound() {
        repository.save(UserFactory.getOliverWithoutId());
        Optional<User> optional = repository.findByUsername("xyz");
        assertNotNull(optional);
        assertTrue(optional.isEmpty());
    }
}