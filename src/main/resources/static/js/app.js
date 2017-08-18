$(window).on("load", function () {
    $.ready.then(function () {

        var baseURL = 'http://localhost:8080/api/finance/history';
        var appleLineChartElement = $('#apple-line-chart');
        var amazonLineChartElement = $('#amazon-line-chart');
        var chartColors = {'US': '#3e95cd', 'MX': '#3cba9f'};

        var buildFiltersStream = function (defaultMarket, defaultPeriod) {
            var marketSelectionClickStream = Rx.Observable.fromEvent($('a.market-selector'), 'click')
                .startWith({})
                .map(function (evt) {
                    return evt.target ? $(evt.target).data('market') : defaultMarket;
                });

            var periodSelectionClickStream = Rx.Observable.fromEvent($('a.period-selector'), 'click')
                .startWith({})
                .map(function (evt) {
                    return evt.target ? $(evt.target).data('period') : defaultPeriod;
                });

            return marketSelectionClickStream
                .combineLatest(periodSelectionClickStream, function (market, period) {
                    return market + '/' + period;
                });
        };

        var buildResponseStream = function (company, filtersClickStream) {
            return filtersClickStream
                .combineLatest(Rx.Observable.just(baseURL + '/' + company), function (filters, url) {
                    return url + '/' + filters;
                })
                .flatMap(function (requestUrl) {
                    return Rx.Observable.fromPromise($.getJSON(requestUrl));
                })
                .map(function (jsonResponse) {
                    return buildChartData(jsonResponse);
                });
        };

        var filtersClickStream = buildFiltersStream('ALL', '1m');

        var appleResponseStream = buildResponseStream('AAPL', filtersClickStream);
        appleResponseStream.subscribe(function (chartData) {
            renderChart(appleLineChartElement, chartData);
        });

        var amazonResponseStream = buildResponseStream('AMZN', filtersClickStream);
        amazonResponseStream.subscribe(function (chartData) {
            renderChart(amazonLineChartElement, chartData);
        });

        var buildChartData = function (jsonResponse) {
            var groupedByMarket = _.groupBy(jsonResponse, function (item) {
                return item.market;
            });

            return {
                labels: _.uniq(_.map(jsonResponse, function (item) {
                    return item.date;
                })),
                datasets: _.map(groupedByMarket, function (stocks, market) {
                    return {
                        data: _.map(stocks, function (item) {
                            return item.close;
                        }),
                        label: market,
                        borderColor: chartColors[market],
                        fill: true
                    };
                })
            };
        };

        var renderChart = function (chartElement, chartData) {
            var previousChart = chartElement.data('chart');
            if (previousChart) {
                previousChart.destroy();
            }
            chartElement.data('chart', new Chart(chartElement, {
                type: 'line',
                data: chartData,
                options: {
                    title: {
                        display: true,
                        text: 'Stock Exchange - Historical Data'
                    }
                }
            }));
        };

    });
});