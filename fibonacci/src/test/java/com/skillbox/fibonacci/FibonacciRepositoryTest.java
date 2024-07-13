package com.skillbox.fibonacci;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FibonacciRepositoryTest extends PostgresTestContainerInitializer {

    @Autowired
    FibonacciRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EntityManager entityManager;

    @BeforeAll
    public static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    public void fillingDataBase() {
        FibonacciNumber fibonacciNumber = new FibonacciNumber(10, 55);
        repository.save(fibonacciNumber);
        entityManager.flush();
        entityManager.detach(fibonacciNumber);
        FibonacciNumber fibonacciNumber2 = new FibonacciNumber(10, 55);
        repository.save(fibonacciNumber2);
        entityManager.flush();
        entityManager.detach(fibonacciNumber2);
    }

    @AfterEach
    public void clearDataBase() {
        repository.deleteAll();
    }

    @Test
    public void testFibonacciNumber() {
        List<FibonacciNumber> actual = jdbcTemplate.query(
                "SELECT * FROM fibonacci_number WHERE index = 10",
                (rs, rowNum) -> new FibonacciNumber(rs.getInt("index"),
                        rs.getInt("value"))
        );
        assertEquals(1, actual.size());
        assertEquals(10, actual.get(0).getIndex());
        assertEquals(55, actual.get(0).getValue());
    }

}
