package com.nabila.sideproject.datetimeapi;

import com.nabila.sideproject.datetimeapi.enums.Operator;
import com.nabila.sideproject.datetimeapi.enums.Unit;
import com.nabila.sideproject.datetimeapi.exceptions.InvalidInputException;
import org.apache.commons.lang3.time.DateUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeAPI {

    public static final String DATE_STRING_REGEX = "[-|/+]{1}[0-9]+[smhdMy](\\/[smhdMy])?";
    public static final String INPUT_VALID_REGEX = "now" + DATE_STRING_REGEX;
    public static final String INPUT_INVALID_MESSAGE = "Input date is invalid, it should match the regular expression: ";

    private static final String SPECIAL_CHARACTER_REGEX = "[-|//+|/]";
    private static final String NUMBER_REGEX = "[0-9]+";
    private static final String UNIT_REGEX = "[smhdMy]";
    private static final String ROUND_REGEX = "\\/[smhdMy]";
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = 3600;

    /**
     * Parse a date string and return a date
     *
     * @param input Date string
     * @return parsed date
     */
    public static LocalDateTime parse(String input) {
        LocalDateTime now = LocalDateTime.now();
        return parse(input, now);
    }

    /**
     * Parse a date string and return a date
     *
     * @param input Date string
     * @param now   Current date-time
     * @return parsed date
     */
    public static LocalDateTime parse(String input, LocalDateTime now) {
        if (!validateInput(input)) {
            throw new InvalidInputException(INPUT_INVALID_MESSAGE + INPUT_VALID_REGEX);
        }
        List<DateOperation> dateOperations = getDateOperations(input);
        for (DateOperation dateOperation : dateOperations) {
            switch (dateOperation.getOperator()) {
                case MINUS:
                    now = now.minus(dateOperation.getValue(), getChronoUnit(dateOperation.getUnit()));
                    break;
                case PLUS:
                    now = now.plus(dateOperation.getValue(), getChronoUnit(dateOperation.getUnit()));
                    break;
                case ROUND:
                    now = roundDate(now, dateOperation.getUnit());
                    break;
            }
        }
        return now;
    }

    /**
     * Stringify and return a shorthand date like 'now-1d/y'
     *
     * @param date Date to stringify
     * @return Stringified Date
     */
    public static String stringify(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();

        return stringify(date, now);
    }

    /**
     * Stringify and return a shorthand date like 'now-1d/y'
     *
     * @param date Date to stringify
     * @param now  Current date-time
     * @return Stringified Date
     */
    public static String stringify(LocalDateTime date, LocalDateTime now) {
        StringBuilder stringifiedDate = new StringBuilder("now");
        Map<ChronoUnit, Long> differences = getDifference(date, now);
        for (Map.Entry<ChronoUnit, Long> diff : differences.entrySet()) {
            if (diff.getValue() != 0) {
                stringifiedDate.append
                        ((diff.getValue() > 0 ? "+" : "") + diff.getValue() + Unit.valueOf(diff.getKey().name()).value);
            }
        }
        return stringifiedDate.toString();
    }


    /**
     * Validate input string date
     *
     * @param input Date string
     * @return Whether the input is valid or not
     */
    private static boolean validateInput(String input) {
        Pattern pattern = Pattern.compile(INPUT_VALID_REGEX);
        Matcher matcher = pattern.matcher(input);

        return matcher.find();
    }

    /**
     * Get date operations from the date string
     *
     * @param date Date string
     * @return List of date operations
     */
    private static List<DateOperation> getDateOperations(String date) {

        List<DateOperation> dateOperations = new ArrayList<>();
        String roundMatch = null;
        if (!findMatch(date, ROUND_REGEX).isEmpty()) {
            roundMatch = findMatch(date, ROUND_REGEX).get(0);
        }

        if (roundMatch != null && !roundMatch.isEmpty()) {
            DateOperation dateOperation = new DateOperation(Operator.ROUND, Unit.fromString(findMatch(roundMatch, UNIT_REGEX).get(0)));
            dateOperations.add(dateOperation);
        }
        List<String> matches = findMatch(date, DATE_STRING_REGEX);
        for (String match : matches) {
            DateOperation dateOperation = new DateOperation();
            dateOperation.setUnit(Unit.fromString(findMatch(match, UNIT_REGEX).get(0))); // OrElseThrow
            dateOperation.setOperator(Operator.fromString(findMatch(match, SPECIAL_CHARACTER_REGEX).get(0))); // OrElseThrow
            dateOperation.setValue(Integer.parseInt(findMatch(match, NUMBER_REGEX).get(0))); // OrElseThrow
            dateOperations.add(dateOperation);
        }
        return dateOperations;
    }

    /**
     * Find all subsequences that match the regular expression
     *
     * @param string            The character sequence to be matched
     * @param regularExpression Regex pattern to find
     * @return List of subsequences matched
     */
    private static List<String> findMatch(String string, String regularExpression) {
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(string);
        List<String> matches = new ArrayList<>();

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
    }


    /**
     * Get difference between to date
     *
     * @param dateTime Input date time
     * @param now      The date to compare against
     * @return Map of differences (chronounit -> value)
     */
    private static Map<ChronoUnit, Long> getDifference(LocalDateTime dateTime, LocalDateTime now) {
        Map<ChronoUnit, Long> dateDifferences = new LinkedHashMap<>();

        Period period = Period.between(now.toLocalDate(), dateTime.toLocalDate());
        long durationInSeconds = Duration.between(now.toLocalTime(), dateTime.toLocalTime()).getSeconds();

        dateDifferences.put(ChronoUnit.YEARS, (long) period.getYears());
        dateDifferences.put(ChronoUnit.MONTHS, (long) period.getMonths());
        dateDifferences.put(ChronoUnit.DAYS, (long) period.getDays());
        dateDifferences.put(ChronoUnit.HOURS, durationInSeconds / SECONDS_PER_HOUR);
        dateDifferences.put(ChronoUnit.MINUTES, ((durationInSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE));
        dateDifferences.put(ChronoUnit.SECONDS, (durationInSeconds % SECONDS_PER_MINUTE));

        return dateDifferences;
    }

    /**
     * Round date to the closest unit
     *
     * @param date Date to round
     * @param unit Unit to round against
     * @return Rounded date
     */
    private static LocalDateTime roundDate(LocalDateTime date, Unit unit) {
        Date dateToRound = java.util.Date
                .from(date.atZone(ZoneId.systemDefault())
                        .toInstant());

        return DateUtils.round(dateToRound, getCalendarValue(unit)).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

    }


    /**
     * Get the ChronoUnit corresponding to a unit
     *
     * @param unit Unit of time
     * @return The corresponding ChronoUnit
     */
    private static ChronoUnit getChronoUnit(Unit unit) {
        ChronoUnit chronoUnit = null;
        switch (unit) {
            case SECONDS:
                chronoUnit = ChronoUnit.SECONDS;
                break;
            case MINUTES:
                chronoUnit = ChronoUnit.MINUTES;
                break;
            case HOURS:
                chronoUnit = ChronoUnit.HOURS;
                break;
            case DAYS:
                chronoUnit = ChronoUnit.DAYS;
                break;
            case MONTHS:
                chronoUnit = ChronoUnit.MONTHS;
                break;
            case YEARS:
                chronoUnit = ChronoUnit.YEARS;
                break;

        }
        return chronoUnit;
    }

    /**
     * Get the Calendar value corresponding to a unit
     *
     * @param unit Unit of time
     * @return The corresponding calendar value
     */
    private static int getCalendarValue(Unit unit) {
        int value = 0;
        switch (unit) {
            case SECONDS:
                value = Calendar.SECOND;
                break;
            case MINUTES:
                value = Calendar.MINUTE;
                break;
            case HOURS:
                value = Calendar.HOUR;
                break;
            case DAYS:
                value = Calendar.DAY_OF_MONTH;
                break;
            case MONTHS:
                value = Calendar.MONTH;
                break;
            case YEARS:
                value = Calendar.YEAR;
                break;

        }
        return value;
    }

}
