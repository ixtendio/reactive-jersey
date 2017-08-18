package com.example.reactive.jersey.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StockExchangeEvent {

    @JsonIgnore
    private final Date period;
    private final String company;
    private final String market;
    private final String date;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double adjClose;
    private final long volume;

    public StockExchangeEvent(String company, String market, String line) {
        String[] parts = line.split(",");
        if (parts.length < 7) {
            throw new IllegalArgumentException("Unparseable line");
        }
        this.company = company;
        this.market = market;
        this.date = parts[0];
        double exchangeRate = getExchangeRate(market);
        this.open = exchangeRate * Double.parseDouble(parts[1]);
        this.high = exchangeRate * Double.parseDouble(parts[2]);
        this.low = exchangeRate * Double.parseDouble(parts[3]);
        this.close = exchangeRate * Double.parseDouble(parts[4]);
        this.adjClose = exchangeRate * Double.parseDouble(parts[5]);
        this.volume = Integer.parseInt(parts[6]);
        this.period = parseDate(this.date);
    }

    public String getCompany() {
        return company;
    }

    public String getMarket() {
        return market;
    }

    public String getDate() {
        return date;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getAdjClose() {
        return adjClose;
    }

    public long getVolume() {
        return volume;
    }

    @JsonIgnore
    private double getExchangeRate(String market) {
        if ("mx".equalsIgnoreCase(market)) {
            return 0.055;
        } else {
            return 1;
        }
    }

    @JsonIgnore
    public boolean isAfter(Date date) {
        return date.before(period);
    }

    private Date parseDate(String value) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(value);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String toString() {
        return "StockExchangeEvent{" +
                "company='" + company + '\'' +
                ", date='" + date + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", adjClose=" + adjClose +
                ", volume=" + volume +
                '}';
    }
}
