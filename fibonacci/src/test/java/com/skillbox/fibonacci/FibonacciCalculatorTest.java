package com.skillbox.fibonacci;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class FibonacciCalculatorTest {

    private FibonacciCalculator fibonacciCalculator;

    @BeforeEach
    public void setUp() {
        fibonacciCalculator = new FibonacciCalculator();
    }

    @DisplayName("Return exception if index < 1")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    public void givenLessThanOne_whenIndexLessThanOne_thenException(int index) {
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> fibonacciCalculator.getFibonacciNumber(index)
        );
        assertEquals("Index should be greater or equal to 1", exception.getMessage());
    }

    @DisplayName("Return 1 if index = 1 or 2")
    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void givenOne_whenIndexOneOrTwo_thenOne(int index) {
        int actualResult = fibonacciCalculator.getFibonacciNumber(index);
        assertEquals(1, actualResult);
    }

    @DisplayName("Return correct Fibonacci number if index > 2")
    @ParameterizedTest
    @ValueSource(ints = {10, 30})
    public void givenSequenceNumber_whenIndexMoreThanTwo_thenFibonacciNumber(int index) {
        int[] expectedResults = {55, 832040};
        assertEquals(expectedResults[index == 10 ? 0 : 1], fibonacciCalculator.getFibonacciNumber(index));
    }
}
