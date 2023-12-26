# Clustered Data Warehouse

## Introduction
Welcome.

Clustered Data Warehouse is an application designed to accept details of fx deals, validate them and persist them to the database.

## Prerequisites

Before you start, ensure that you have the following installed:
- Docker


## Installation

To install and run the application, follow these steps:

1. Clone the repository:

    ```bash
    git clone https://git@github.com:Win2som/data-warehouse.git
    cd clusteredDataWarehouse
    ```

2. Build and start the application:

    ```bash
    make run
    ```

3. Access the application at [http://localhost:8080](http://localhost:8080).
##### Swagger url http://localhost:8080/swagger-ui/index.html

## Endpoints

### `POST /api/deals/json`

Sends a json request to process deals.

#### Request

- Method: `POST`
- Path: `/api/deals/json`
- ## Request Body:

| Field Name    | Data Type     |
| ------------- | ------------- |
|   dealUniqueId    |    String     |
| fromCurrencyISOCode  |    String     |
|  toCurrencyISOCode   |    String     |
|    dealAmount     |   BigDecimal  |


#### Response

- Status: `201 Created` on success
- Status: `400 Bad Request` on failure


### `POST /api/deals/csv`

Upload a CSV file to process deals.

#### Request

- Method: `POST`
- Path: `/api/deals/csv`
- Parameters:
  - `file`: CSV file

#### Response

- Status: `201 Created` on success
- Status: `400 Bad Request` on failure

