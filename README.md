# Reactive Jersey
This project shows how to use reactive programming using Jersey and Spring-Boot.

## Prerequisites
Install **Java 8** as described at [https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)

Install **Maven** as described at [https://maven.apache.org/install.html](https://maven.apache.org/install.html)

## Program Execution

First, make sure that you are located under the project folder. Here, compile the project by running:

```bash
mvn clean package
```    

After that, launch the program by executing: 

```bash
./local_run.sh
```

To see the result, load the following URL: [http://localhost:8080](http://localhost:8080) in your browser. The server port can be changed in **local_run.sh**.

## Implementation

The reactive code can be found in the following files: 

1. **StockExchangeEventsResource.java** - java implementation using *RXJava* and Jersey *AsyncResponse*
2. **/src/main/resources/static/js/app.js** - JavaScript implementation using *RXJS*
