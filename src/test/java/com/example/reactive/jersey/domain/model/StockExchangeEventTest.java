package com.example.reactive.jersey.domain.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StockExchangeEventTest {

    @Test
    public void testConstructor() {
        //GIVEN
        String company = "company";
        String market = "market";
        String line = "2016-06-15,95.981949,96.560860,95.206787,97.139999,95.314720,29445200";

        //WHEN
        StockExchangeEvent event = new StockExchangeEvent(company, market, line);

        //THEN
        assertThat(event.getCompany()).isEqualTo("company");
        assertThat(event.getMarket()).isEqualTo("market");
        assertThat(event.getDate()).isEqualTo("2016-06-15");
        assertThat(event.getOpen()).isEqualTo(95.981949);
        assertThat(event.getHigh()).isEqualTo(96.560860);
        assertThat(event.getLow()).isEqualTo(95.206787);
        assertThat(event.getClose()).isEqualTo(97.139999);
        assertThat(event.getAdjClose()).isEqualTo(95.314720);
        assertThat(event.getVolume()).isEqualTo(29445200);
    }

    @Test
    public void testConstructorWhenMarketIsMX() {
        //GIVEN
        String company = "company";
        String market = "mx";
        String line = "2016-06-15,95.981949,96.560860,95.206787,97.139999,95.314720,29445200";

        //WHEN
        StockExchangeEvent event = new StockExchangeEvent(company, market, line);

        //THEN
        assertThat(event.getCompany()).isEqualTo("company");
        assertThat(event.getMarket()).isEqualTo("mx");
        assertThat(event.getDate()).isEqualTo("2016-06-15");
        assertThat(event.getOpen()).isEqualTo(0.055 * 95.981949);
        assertThat(event.getHigh()).isEqualTo(0.055 * 96.560860);
        assertThat(event.getLow()).isEqualTo(0.055 * 95.206787);
        assertThat(event.getClose()).isEqualTo(0.055 * 97.139999);
        assertThat(event.getAdjClose()).isEqualTo(0.055 * 95.314720);
        assertThat(event.getVolume()).isEqualTo(29445200);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenDateIsIncorrect() {
        //GIVEN
        String company = "company";
        String market = "mx";
        String line = "2016.06.15,95.981949,96.560860,95.206787,97.139999,95.314720,29445200";

        //WHEN
        new StockExchangeEvent(company, market, line);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenCSVLineIsIncorrect() {
        //GIVEN
        String company = "company";
        String market = "mx";
        String line = "2016-06-15,95.981949,96.560860,95.206787,97.139999,95.314720";

        //WHEN
        new StockExchangeEvent(company, market, line);
    }
}
