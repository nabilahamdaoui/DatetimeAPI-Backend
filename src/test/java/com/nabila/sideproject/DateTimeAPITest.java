package com.nabila.sideproject;


import com.nabila.sideproject.datetimeapi.DateTimeAPI;
import com.nabila.sideproject.datetimeapi.exceptions.InvalidInputException;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.nabila.sideproject.datetimeapi.DateTimeAPI.INPUT_INVALID_MESSAGE;
import static com.nabila.sideproject.datetimeapi.DateTimeAPI.INPUT_VALID_REGEX;
import static org.assertj.core.api.Assertions.*;

public class DateTimeAPITest {

    @Test
    public void parseStringDateShouldFailIfInvalidInput() {
        // Given
        String date = "now-133j/y";

        // When
        Throwable throwable = catchThrowable(() -> DateTimeAPI.parse(date));

        // Then
        assertThat(throwable).isInstanceOf(InvalidInputException.class)
                .hasMessage(INPUT_INVALID_MESSAGE + INPUT_VALID_REGEX);

    }

    @Test
    public void shouldParseStringDate() {
        // Given
        String date = "now-1d+2h";
        LocalDateTime expectedDate = LocalDateTime.now().minusDays(1).plusHours(2);

        // When
        LocalDateTime parsedDate = DateTimeAPI.parse(date);

        // Then
        assertThat(parsedDate).isCloseTo(expectedDate, within(2, ChronoUnit.SECONDS));
    }

    @Test
    public void shouldParseStringDateAndRoundSecond() {
        // Given
        String date = "now+1d/s";
        LocalDateTime now = LocalDateTime.parse("2021-07-01T15:00:01.655");
        LocalDateTime expectedDate = LocalDateTime.parse("2021-07-02T15:00:02");

        // When
        LocalDateTime parsedDate = DateTimeAPI.parse(date, now);

        // Then
        assertThat(parsedDate).isEqualTo(expectedDate);
    }

    @Test
    public void shouldParseStringDateAndRoundMinute() {
        // Given
        String date = "now+1d/m";
        LocalDateTime now = LocalDateTime.parse("2021-07-01T15:00:40");
        LocalDateTime expectedDate = LocalDateTime.parse("2021-07-02T15:01:00");

        // When
        LocalDateTime parsedDate = DateTimeAPI.parse(date, now);

        // Then
        assertThat(parsedDate).isEqualTo(expectedDate);
    }

    @Test
    public void shouldParseStringDateAndRoundHour() {
        // Given
        String date = "now+60s/h";
        LocalDateTime now = LocalDateTime.parse("2021-07-01T15:40");
        LocalDateTime expectedDate = LocalDateTime.parse("2021-07-01T16:01:00");

        // When
        LocalDateTime parsedDate = DateTimeAPI.parse(date, now);

        // Then
        assertThat(parsedDate).isEqualTo(expectedDate);
    }

    @Test
    public void shouldParseStringDateAndRoundDay() {
        // Given
        String date = "now+20s/d";
        LocalDateTime now = LocalDateTime.parse("2021-07-01T15:00");
        LocalDateTime expectedDate = LocalDateTime.parse("2021-07-02T00:00:20");

        // When
        LocalDateTime parsedDate = DateTimeAPI.parse(date, now);

        // Then
        assertThat(parsedDate).isEqualTo(expectedDate);
    }


    @Test
    public void shouldParseStringDateAndRoundMonth() {
        // Given
        String date = "now+1d/M";
        LocalDateTime now = LocalDateTime.parse("2021-07-20T15:00");
        LocalDateTime expectedDate = LocalDateTime.parse("2021-08-02T00:00");

        // When
        LocalDateTime parsedDate = DateTimeAPI.parse(date, now);

        // Then
        assertThat(parsedDate).isEqualTo(expectedDate);
    }

    @Test
    public void shouldParseStringDateAndRoundToPreviousYear() {
        // Given
        String date = "now+1d/y";
        LocalDateTime now = LocalDateTime.parse("2021-05-01T00:00");
        LocalDateTime expectedDate = LocalDateTime.parse("2021-01-02T00:00");

        // When
        LocalDateTime parsedDate = DateTimeAPI.parse(date, now);

        // Then
        assertThat(parsedDate).isEqualTo(expectedDate);
    }

    @Test
    public void shouldParseStringDateAndRoundToNextYear() {
        // Given
        String date = "now+1d/y";
        LocalDateTime now = LocalDateTime.parse("2021-07-01T00:00");
        LocalDateTime expectedDate = LocalDateTime.parse("2022-01-02T00:00");

        // When
        LocalDateTime parsedDate = DateTimeAPI.parse(date, now);

        // Then
        assertThat(parsedDate).isEqualTo(expectedDate);
    }

    @Test
    public void shouldStringifyDate() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateToStringify = now.minusMonths(1).plusYears(5).plusHours(1).plusMinutes(5).plusSeconds(20);
        String expectedOutput = "now+4y+11M+1h+5m+20s";

        //When
        String stringifiedDate = DateTimeAPI.stringify(dateToStringify, now);

        // Then
        assertThat(stringifiedDate).isEqualTo(expectedOutput);
    }
}

