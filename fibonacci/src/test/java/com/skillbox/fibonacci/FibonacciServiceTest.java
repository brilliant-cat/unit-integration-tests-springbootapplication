package com.skillbox.fibonacci;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FibonacciServiceTest {

    private final FibonacciCalculator fibonacciCalculator = Mockito.mock(FibonacciCalculator.class);
    private final FibonacciRepository fibonacciRepository = Mockito.mock(FibonacciRepository.class);
    private final FibonacciService fibonacciService = new FibonacciService(fibonacciRepository, fibonacciCalculator);

    @DisplayName("Return exception if index < 1")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    public void givenLessThanOne_whenIndexLessThanOne_thenException(int index) {
        when(fibonacciCalculator.getFibonacciNumber(index))
                .thenThrow(new IllegalArgumentException("Index should be greater or equal to 1"));
        Throwable exception = assertThrows(
                IllegalArgumentException.class,
                () -> fibonacciCalculator.getFibonacciNumber(index)
        );
        assertEquals("Index should be greater or equal to 1", exception.getMessage());
    }

    @DisplayName("Verify if there is no Fibonacci number in the repository it is calculated and saved in the repository")
    @Test
    public void givenThereIsNoFibonacciNumber_whenIndexMoreThanOne_thenFibonacciCalculator() {
        int index = 10;
        int fibonacci = 55;
        when(fibonacciRepository.findByIndex(index)).thenReturn(Optional.empty());
        when(fibonacciCalculator.getFibonacciNumber(index)).thenReturn(fibonacci);
        fibonacciService.fibonacciNumber(index);
        verify(fibonacciRepository, times(1)).findByIndex(index);
        verify(fibonacciCalculator, times(1)).getFibonacciNumber(index);
        ArgumentCaptor<FibonacciNumber> argumentCaptor = ArgumentCaptor.forClass(FibonacciNumber.class);
        verify(fibonacciRepository, times(1)).save(argumentCaptor.capture());
        FibonacciNumber savedFibonacciNumber = argumentCaptor.getValue();
        assertEquals(index, savedFibonacciNumber.getIndex());
        assertEquals(fibonacci, savedFibonacciNumber.getValue());
    }

    @DisplayName("Verify if there is Fibonacci number in the repository it is not calculated and saved in the repository")
    @Test
    public void givenThereIsFibonacciNumber_whenIndexMoreThanOne_thenFibonacciRepository() {
        int index = 10;
        FibonacciNumber fibonacciNumber = new FibonacciNumber();
        fibonacciNumber.setId(1);
        fibonacciNumber.setIndex(index);
        fibonacciNumber.setValue(55);
        when(fibonacciRepository.findByIndex(index)).thenReturn(Optional.of(fibonacciNumber));
        fibonacciService.fibonacciNumber(index);
        verify(fibonacciRepository, times(1)).findByIndex(index);
        assertEquals(55, fibonacciNumber.getValue());
        verify(fibonacciCalculator, times(0)).getFibonacciNumber(index);
        verify(fibonacciRepository, times(0)).save(fibonacciNumber);
    }

}
