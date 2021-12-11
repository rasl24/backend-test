package com.geekbrains.backend.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты функциональности класса Hello")
class HelloTest extends FunctionalTest {

    //что каждый раз не создавать экземпляр
    // класса строкой Hello hello = new Hello();
    // экземпляр класса будет создаваться перед тем,
    // как каждый тест будет загружаться
    static Hello hello;

    @BeforeAll // перед всеми
    static void beforeAll() {
        hello = new Hello();
    }

    private static Stream<Arguments> paramsForSum() {
        return Stream.of(
                Arguments.of(1, 2, 3),
                Arguments.of(5, 5, 10),
                Arguments.of(-1, 1, 0)
        );
    }

    // в скобках справочная инфа о тесте
    @ParameterizedTest(name = "Тест {index}: {0} + {1} = {2}")
    // указывается имя метода, где будут лежать параметры
    @MethodSource("paramsForSum")
    // название теста
    @DisplayName("Тест суммы двух чисел")
    void testFooWithParams(int a, int b, int expected){
        assertEquals(expected, hello.sum(a, b));
    }

    // exception check (проверки ошибки)
    @Test
    @DisplayName("Тест: классическое деление без ошибок")
    void testDivideCommonCase(){
        assertDoesNotThrow(() -> {
            hello.divide(5, 2);
        });
    }

    @Test
    @DisplayName("Тест на ошибку при делении на 0")
    void testDivideByZero(){
        assertThrows(
                ArithmeticException.class,
                () -> hello.divide(1, 0)
        );
    }

    @Test
    @DisplayName("Тест зачитывания ресурса")
    void testWithResource() throws IOException {
        String resource = getStringResource("data.json");
        // проверка, что ресурс корректно зачитывается
        assertTrue(resource.contains("result"));
    }
}