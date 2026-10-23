package lv.bootcamp.shelter.task5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import lv.bootcamp.shelter.model.Animal;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DisplayName("AnimalCsvParser")
class AnimalCsvParserTest {

    private AnimalCsvParser parser;

    @BeforeEach
    void setUp() {
        parser = new AnimalCsvParser();
    }

    // ==================== parseRow tests ====================

    @Nested
    @DisplayName("When parsing valid rows")
    class ValidRowsTests {

        @Test
        @DisplayName("parses a complete row into an Animal")
        void shouldParseCompleteRow() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,true,2026-01-15");

            assertThat(result).isPresent();
            Animal animal = result.get();
            assertThat(animal.getName()).isEqualTo("Buddy");
            assertThat(animal.getSpecies()).isEqualTo("Dog");
            assertThat(animal.getAge()).isEqualTo(3);
            assertThat(animal.isVaccinated()).isTrue();
            assertThat(animal.getIntakeDate()).isEqualTo(LocalDate.of(2026, 1, 15));
        }

        @Test
        @DisplayName("trims whitespace from fields")
        void shouldTrimWhitespace() {
            Optional<Animal> result = parser.parseRow("  Buddy , Dog , 3 , true , 2026-01-15 ");

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Buddy");
            assertThat(result.get().getSpecies()).isEqualTo("Dog");
        }

        @Test
        @DisplayName("parses vaccinated=false correctly")
        void shouldParseFalseVaccination() {
            Optional<Animal> result = parser.parseRow("Max,Dog,5,false,2026-01-18");

            assertThat(result).isPresent();
            assertThat(result.get().isVaccinated()).isFalse();
        }
    }

    @Nested
    @DisplayName("When parsing malformed rows")
    class MalformedRowsTests {

        @Test
        @DisplayName("returns empty for null input")
        void shouldReturnEmptyForNull() {
            Optional<Animal> result = parser.parseRow(null);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty for blank input")
        void shouldReturnEmptyForBlank() {
            Optional<Animal> result = parser.parseRow("   ");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty when row has fewer than 5 fields")
        void shouldReturnEmptyForTooFewFields() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty when name is missing")
        void shouldReturnEmptyForMissingName() {
            Optional<Animal> result = parser.parseRow(",Dog,3,true,2026-01-15");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty when age is not a number")
        void shouldReturnEmptyForBadAge() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,old,true,2026-01-15");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty when age is negative")
        void shouldReturnEmptyForNegativeAge() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,-1,true,2026-01-15");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("returns empty when date is invalid")
        void shouldReturnEmptyForBadDate() {
            Optional<Animal> result = parser.parseRow("Buddy,Dog,3,true,not-a-date");

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("When handling edge cases")
    class EdgeCases {

        @Test
        @DisplayName("handles vaccinated field as any non-true string → false")
        void shouldTreatNonTrueAsFalse() {
            // TODO: Parse a row with vaccinated="maybe"
            // TODO: Assert isVaccinated() returns false (Boolean.parseBoolean behavior)
        }

        @Test
        @DisplayName("handles age 0 as valid")
        void shouldAcceptAgeZero() {
            // TODO: Parse a row with age=0
            // TODO: Assert result isPresent() and age is 0
        }
    }

    // ==================== parseFile tests ====================

    @Nested
    @DisplayName("When parsing a CSV file")
    class ParseFile {

        @Test
        @DisplayName("parses valid rows and counts skipped rows")
        void shouldParseFileAndCountSkipped() throws IOException {
            // TODO: Create a temp file with a header + 3 valid rows + 1 malformed row
            //   Hint: Path tempFile = Files.createTempFile("test-intake", ".csv");
            //         Files.writeString(tempFile, content, StandardCharsets.UTF_8);
            // TODO: Call parser.parseFile(tempFile)
            // TODO: Assert result.animals() has size 3
            // TODO: Assert result.skippedRows() == 1
            // TODO: Clean up: Files.deleteIfExists(tempFile)
        }

        @Test
        @DisplayName("returns empty result for file with only a header")
        void shouldReturnEmptyForHeaderOnly() throws IOException {
            // TODO: Create a temp file with just "name,species,age,vaccinated,intakeDate"
            // TODO: Call parser.parseFile(tempFile)
            // TODO: Assert result.animals() is empty and skippedRows == 0
        }

        @Test
        @DisplayName("throws IOException for non-existent file")
        void shouldThrowForMissingFile() {
            // TODO: Call parser.parseFile(Path.of("does-not-exist.csv"))
            // TODO: Assert it throws IOException
        }
    }
}
