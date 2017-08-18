package com.example.reactive.jersey.infrastructure.resource;


import com.example.reactive.jersey.domain.model.Period;
import com.example.reactive.jersey.domain.model.StockExchangeEvent;
import rx.Emitter;
import rx.Observable;
import rx.schedulers.Schedulers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Path("/finance/history")
@Produces("application/json")
public class StockExchangeEventsResource {

    private static final List<String> ALL_MARKET_LOCATIONS = Arrays.asList("US", "MX");

    @GET
    public Response getStatus() {
        return Response.status(Response.Status.CREATED).entity("{\"status\":\"OK\"}").build();
    }

    @GET
    @Path("{companySymbol}/{market}/{period}")
    public void companyStockExchangeByMarketAndPeriod(@PathParam("companySymbol") final String companySymbol,
                                                      @PathParam("market") final String market,
                                                      @PathParam("period") final String period,
                                                      @Suspended final AsyncResponse async) {

        readEvents(companySymbol, evalMarketLocations(market)).filter(e -> e.isAfter(Period.fromString(period).toDate()))
                .toList()
                .observeOn(Schedulers.io())
                .subscribe(async::resume, async::resume);
    }

    @GET
    @Path("{companySymbol}/{market}")
    public void companyStockExchangeByMarket(@PathParam("companySymbol") String companySymbol,
                                             @PathParam("market") String market,
                                             @Suspended final AsyncResponse async) {

        readEvents(companySymbol, evalMarketLocations(market))
                .toList()
                .observeOn(Schedulers.io())
                .subscribe(async::resume, async::resume);
    }

    @GET
    @Path("{companySymbol}")
    public void companyStockExchange(@PathParam("companySymbol") String companySymbol,
                                     @Suspended final AsyncResponse async) {

        readEvents(companySymbol, ALL_MARKET_LOCATIONS)
                .toList()
                .observeOn(Schedulers.io())
                .subscribe(async::resume, async::resume);
    }

    @GET
    @Path("all")
    public void allStockExchange(@Suspended final AsyncResponse async) {

        readEvents("AAPL", ALL_MARKET_LOCATIONS)
                .mergeWith(readEvents("AMZN", ALL_MARKET_LOCATIONS))
                .toList()
                .observeOn(Schedulers.io())
                .subscribe(async::resume, async::resume);
    }

    private Observable<StockExchangeEvent> readEvents(String companySymbol, List<String> marketLocations) {
        return readEvents(Observable.empty(), companySymbol, marketLocations);
    }

    private Observable<StockExchangeEvent> readEvents(Observable<StockExchangeEvent> acc, String companySymbol, List<String> marketLocations) {
        if (marketLocations.isEmpty()) {
            return acc;
        } else {
            return readEvents(acc.mergeWith(Observable.fromEmitter(emitter -> {
                String dataSetPath = Objects.requireNonNull(System.getProperty("dataset.path"), "dataset.path is not specified as system property");
                String dataSetFileName = companySymbol.toUpperCase() + "." + marketLocations.get(0).toUpperCase() + ".csv";
                try (Stream<String> stream = Files.lines(Paths.get(dataSetPath, dataSetFileName))) {
                    stream.skip(1).map(l -> new StockExchangeEvent(companySymbol, marketLocations.get(0), l)).forEach(emitter::onNext);
                    emitter.onCompleted();
                } catch (Exception e) {
                    emitter.onError(e);
                }

            }, Emitter.BackpressureMode.BUFFER)), companySymbol, marketLocations.subList(1, marketLocations.size()));
        }
    }

    private List<String> evalMarketLocations(String value) {
        if ("all".equalsIgnoreCase(value)) {
            return ALL_MARKET_LOCATIONS;
        } else {
            return Collections.singletonList(value);
        }
    }

}
