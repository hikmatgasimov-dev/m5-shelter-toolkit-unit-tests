package lv.bootcamp.shelter.stretch;

import lv.bootcamp.shelter.model.Animal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;


/**
 * Stretch goal: Testing file output
 *
 * Practice:
 * - Writing to temp files and reading them back
 * - String content assertions
 * - Cleanup with Files.deleteIfExists
 *
 * Instructions:
 * These tests verify that AnimalReportWriter produces correct output.
 * This task is optional — attempt it after completing tasks 1–6.
 */
@DisplayName("AnimalReportWriter (stretch)")
class AnimalReportWriterTest {

    private final AnimalReportWriter writer = new AnimalReportWriter();

    @Test
    @DisplayName("writes report file that contains total count")
    void shouldWriteTotalCount() throws IOException {
        List<Animal> animals = List.of(
                new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15)),
                new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 16)),
                new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 18)));
        Path output = Files.createTempFile("report-test", ".txt");

        writer.writeReport(animals, output);

        String content = Files.readString(output, StandardCharsets.UTF_8);
        assertThat(content).contains("Total animals: 3");

        Files.deleteIfExists(output);
    }

    @Test
    @DisplayName("writes per-species breakdown in alphabetical order")
    void shouldWriteSpeciesBreakdown() throws IOException {
        List<Animal> animals = List.of(
                new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15)),
                new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 16)),
                new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 18)));
        Path output = Files.createTempFile("report-test", ".txt");

        writer.writeReport(animals, output);

        List<String> lines = Files.readAllLines(output, StandardCharsets.UTF_8);
        assertThat(lines).containsSubsequence(
                "Cat: 1 total, 1 vaccinated",
                "Dog: 2 total, 1 vaccinated");

        Files.deleteIfExists(output);
    }

    @Test
    @DisplayName("writes oldest animal per species")
    void shouldWriteOldestPerSpecies() throws IOException {
        List<Animal> animals = List.of(
                new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15)),
                new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 18)),
                new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 16)));
        Path output = Files.createTempFile("report-test", ".txt");

        writer.writeReport(animals, output);

        String content = Files.readString(output, StandardCharsets.UTF_8);
        assertThat(content).contains("Dog: Max (age 5)");

        Files.deleteIfExists(output);
    }
    @Test
    @DisplayName("species lines are grouped and listed alphabetically")
    void shouldGroupSpeciesLinesAlphabetically() throws IOException {
        List<Animal> animals = List.of(
                new Animal("Buddy", "Dog", 3, true, LocalDate.of(2026, 1, 15)),
                new Animal("Luna", "Cat", 2, true, LocalDate.of(2026, 1, 16)),
                new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 18)));
        Path output = Files.createTempFile("report-test", ".txt");

        writer.writeReport(animals, output);

        List<String> speciesLines = Files.readAllLines(output, StandardCharsets.UTF_8).stream()
                .filter(line -> line.contains(" total, "))
                .toList();
        assertThat(speciesLines)
                .hasSize(2)
                .allMatch(line -> line.endsWith(" vaccinated"));
        assertThat(speciesLines.stream()
                .map(line -> line.substring(0, line.indexOf(':')))
                .toList())
                .containsExactly("Cat", "Dog");

        Files.deleteIfExists(output);
    }


    @Test
    @DisplayName("spy: writeReport is called and still writes the real file")
    void shouldCallWriteReportOnSpy() throws IOException {
        AnimalReportWriter spyWriter = spy(new AnimalReportWriter());
        List<Animal> animals = List.of(
                new Animal("Max", "Dog", 5, false, LocalDate.of(2026, 1, 18)));
        Path output = Files.createTempFile("report-test", ".txt");

        spyWriter.writeReport(animals, output);

        verify(spyWriter).writeReport(animals, output);
        assertThat(Files.readString(output, StandardCharsets.UTF_8)).contains("Total animals: 1");

        Files.deleteIfExists(output);
    }
}
