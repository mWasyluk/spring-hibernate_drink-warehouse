# Spring Hibernate - Drink Warehouse
Drink Warehouse is the project built with (and to learn) Spring's Hibernate support. Most of the features used
in the project are related with Hibernate, but there is few of others.

## Contents
* [Classes](https://github.com/mWasyluk/spring-hibernate_drink-warehouse/tree/main/src/main/java/com/example/hibernatetest)
* [Properties file](https://github.com/mWasyluk/spring-hibernate_drink-warehouse/blob/main/src/main/resources/application.properties)
* [Static resources](https://github.com/mWasyluk/spring-hibernate_drink-warehouse/tree/main/src/main/resources/static)


## Getting started
For a proper startup, the application needs a database to persist data related to the warehouse, such as drinks, brands, clients, orders, etc.
The application uses PostgreSQL by default, but it can be changed anytime within the application.properties file to support other databases.
If the PostgreSQL database will be used to support persisting the application data, it can be run with Docker by the following command:
<p style="background-color: #ddd; color: black; border-radius:3px; padding: 10px; word-break: break-all; font-family:'Consolas';">
run --name drink-warehouse-postgres -d -e POSTGRES_USER=drink_warehouse-database -e POSTGRES_PASSWORD=PoOOfn3@t@jjjn#../ -e POSTGRES_DB=drink-warehouse -p 5432:5432 postgres
</p>
Above environment variables can be change if needed, but also application.properties file needs to be changed accordingly.
 

## Available endpoints examples:
* GET localhost:8080/brands
* GET localhost:8080/brands/1
* GET localhost:8080/brands/1/drinks
* POST localhost:8080/brands (brand)
* POST localhost:8080/brands/1 (drink)
<br></br>
* GET localhost:8080/drinks
* GET localhost:8080/drinks/10001
* POST localhost:8080/drinks (drink)
<br></br>
* GET localhost:8080/clients
* GET localhost:8080/clients/70000002
* GET localhost:8080/clients/70000002/orders
* GET localhost:8080/clients/70000002/addresses
<br></br>
* GET localhost:8080/orders
* GET localhost:8080/orders/10502
* PATCH localhost:8080/orders/10502/change-status?status=AWAITING_PAYMENT
