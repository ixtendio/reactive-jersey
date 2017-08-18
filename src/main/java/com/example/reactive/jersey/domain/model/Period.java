package com.example.reactive.jersey.domain.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public enum Period {

    OneWeek,
    OneMonth,
    OneYear;

    public static Period fromString(String value) {
        if ("1w".equalsIgnoreCase(value)) {
            return OneWeek;
        } else if ("1m".equalsIgnoreCase(value)) {
            return OneMonth;
        } else if ("1y".equalsIgnoreCase(value)) {
            return OneYear;
        } else {
            throw new IllegalArgumentException("Period time not supported");
        }
    }

    public Date toDate() {
        LocalDate now = LocalDate.of(2017, 6, 14);
        LocalDate localDate = null;
        switch (this) {
            case OneWeek:
                localDate = now.minus(1, ChronoUnit.WEEKS);
                break;
            case OneMonth:
                localDate = now.minus(1, ChronoUnit.MONTHS);
                break;
            case OneYear:
                localDate = now.minus(1, ChronoUnit.YEARS);
                break;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    }

}
