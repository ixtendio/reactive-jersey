package com.example.reactive.jersey.domain.model;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class PeriodTest {

    private static LocalDate REF_DATE = LocalDate.of(2017, 6, 14);

    @Test
    public void testFromString() {
        //GIVEN
        Map<String, Period> expectedValues = new HashMap<>();
        expectedValues.put("1w", Period.OneWeek);
        expectedValues.put("1W", Period.OneWeek);
        expectedValues.put("1m", Period.OneMonth);
        expectedValues.put("1M", Period.OneMonth);
        expectedValues.put("1y", Period.OneYear);
        expectedValues.put("1Y", Period.OneYear);

        //THEN
        expectedValues.forEach((value, expectedValue) -> assertThat(Period.fromString(value)).isEqualTo(expectedValue));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringWhenValueIsNotSupported() {
        //GIVEN
        String value = "1d";

        //WHEN
        Period.fromString(value);
    }

    @Test
    public void testToDate() {
        //GIVEN
        Map<Period, Date> expectedValues = new HashMap<>();
        expectedValues.put(Period.OneWeek, toDate(REF_DATE.minus(1, ChronoUnit.WEEKS)));
        expectedValues.put(Period.OneMonth, toDate(REF_DATE.minus(1, ChronoUnit.MONTHS)));
        expectedValues.put(Period.OneYear, toDate(REF_DATE.minus(1, ChronoUnit.YEARS)));

        //THEN
        for (Period period : Period.values()) {
            assertThat(period.toDate()).isEqualTo(expectedValues.get(period));
        }
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
