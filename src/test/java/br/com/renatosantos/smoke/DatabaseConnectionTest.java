package br.com.renatosantos.smoke;

import br.com.renatosantos.quarkusocial.rest.dto.User;
import io.quarkus.test.junit.QuarkusTest;


import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class DatabaseConnectionTest {

    @Inject
    EntityManager entityManager;

    @Test
    @Transactional
    public void testEntityManagerInjection() {
        // Create and persist a user entity
        User user = new User();
        user.setName("John Doe");
        user.setAge(30);

        entityManager.persist(user);

        // Retrieve the user entity by id
        User persistedUser = entityManager.find(User.class, user.getId());

        // Assertions or further operations
        Assertions.assertNotNull(persistedUser);
        Assertions.assertEquals("John Doe", persistedUser.getName());
        Assertions.assertEquals(30, persistedUser.getAge());
    }
}
