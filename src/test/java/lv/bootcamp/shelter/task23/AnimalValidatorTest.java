package lv.bootcamp.shelter.task23;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import lv.bootcamp.shelter.model.Animal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tasks 2 & 3: Parameterized tests and exception tests
 * <p>
 * Practice:
 * - @ParameterizedTest with @CsvSource
 * - @ValueSource and @NullAndEmptySource
 * - assertThrows with message checks
 * - AssertJ assertThatThrownBy
 * <p>
 * Instructions:
 * Write tests for AnimalValidator. Each TODO describes one test to write.
 */
@DisplayName("AnimalValidator")
class AnimalValidatorTest {

    private AnimalValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AnimalValidator();
    }

    // ==================== Task 2: Parameterized tests ====================

    @Nested
    @DisplayName("validateName")
    class ValidateName {

        @ParameterizedTest
        @ValueSource(strings = {"Buddy", "Luna", "Mr. Whiskers", "X"})
        @DisplayName("accepts valid names")
        void shouldAcceptValidNames(String name) {

            assertDoesNotThrow(() -> validator.validateName(name));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("rejects blank or null names")
        void shouldRejectBlankNames(String name) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> validator.validateName(name));

            assertThat(ex.getMessage()).contains("must not be blank");
        }

        @Test
        @DisplayName("rejects name longer than 100 characters")
        void shouldRejectOverlyLongName() {

            String name = "a".repeat(101);
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> validator.validateName(name)
            );
            assertThat(ex.getMessage()).contains("100 characters");
        }
    }

    @Nested
    @DisplayName("validateSpecies")
    class ValidateSpecies {

        @ParameterizedTest
        @ValueSource(strings = {"Dog", "Cat", "Bird", "Rabbit"})
        @DisplayName("accepts valid species")
        void shouldAcceptValidSpecies(String species) {
            assertDoesNotThrow(() -> validator.validateSpecies(species));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t"})
        @DisplayName("rejects blank or null species")
        void shouldRejectBlankSpecies(String species) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> validator.validateSpecies(species)
            );

            assertThat(ex.getMessage()).contains("Species must not be blank");
        }
    }

@Nested
@DisplayName("validateAge")
class ValidateAge {

    @ParameterizedTest
    @CsvSource({
            "0",
            "1",
            "10",
            "50"
    })
    @DisplayName("accepts valid ages")
    void shouldAcceptValidAges(int age) {

        assertDoesNotThrow(() -> validator.validateAge(age));
    }

    @ParameterizedTest
    @CsvSource({
            "-1, must not be negative",
            "-100, must not be negative",
            "51, seems unrealistic",
            "999, seems unrealistic"
    })
    @DisplayName("rejects invalid ages with correct message")
    void shouldRejectInvalidAges(int age, String expectedMessagePart) {

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> validator.validateAge(age)
        );
        assertThat(ex.getMessage()).contains(expectedMessagePart);

    }
}
    @Nested
    @DisplayName("validate (one rule broken at a time)")
    class ValidateInvalidInput {

        @ParameterizedTest
        @CsvSource({
                "'', Dog, 3, Name must not be blank",
                "Buddy, '', 3, Species must not be blank",
                "Buddy, Dog, -1, Age must not be negative"
        })
        @DisplayName("rejects an animal when one field is invalid")
        void shouldRejectInvalidInput(String name, String species, int age, String expectedMessage) {
            Animal animal = new Animal(name, species, age, true, LocalDate.of(2026, 1, 15));

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> validator.validate(animal)
            );

            assertThat(ex.getMessage()).contains(expectedMessage);
        }
    }

}


// ==================== Task 3: Exception tests ====================

@Nested
@DisplayName("validate (full animal)")
class ValidateFullAnimal {

    @Test
    @DisplayName("throws NullPointerException for null animal")
    void shouldThrowForNullAnimal() {
        // TODO: Call validator.validate(null)
        // TODO: Verify it throws NullPointerException
        // TODO: Check message contains "must not be null"
    }

    @Test
    @DisplayName("throws for animal with blank name")
    void shouldThrowForBlankName() {
        // TODO: Create an Animal with blank name
        // TODO: Verify validate() throws IllegalArgumentException
    }

    @Test
    @DisplayName("throws for animal with blank species")
    void shouldThrowForBlankSpecies() {
        // TODO: Create an Animal with valid name but blank species
        // TODO: Verify validate() throws IllegalArgumentException
    }

    @Test
    @DisplayName("throws for animal with negative age")
    void shouldThrowForNegativeAge() {
        // TODO: Create an Animal with negative age
        // TODO: Verify validate() throws IllegalArgumentException
    }

    @Test
    @DisplayName("does not throw for fully valid animal")
    void shouldPassForValidAnimal() {
        // TODO: Create a valid Animal (name="Buddy", species="Dog", age=3, vaccinated=true, date=today)
        // TODO: Call validate() and verify no exception is thrown
    }

}
