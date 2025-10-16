# Spring Boot VNPay Integration

This is a simple Spring Boot project that demonstrates how to integrate the VNPay payment gateway.

## Features

- Create a payment URL to redirect users to the VNPay gateway.
- Handle the payment callback from VNPay to update the payment status.

## Prerequisites

- Java 17
- Maven

## Configuration

Update the VNPay configuration in the `src/main/resources/application.yml` file with your credentials:

```yaml
vnpay:
  tmn-code: YOUR_TMN_CODE
  secret-key: YOUR_SECRET_KEY
  pay-url: https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
  return-url: http://localhost:8080/api/payment/payment-callback
```

## Running the application

1.  Clone the repository.
2.  Navigate to the project directory.
3.  Run the application using Maven:
    ```bash
    ./mvnw spring-boot:run
    ```

The application will be running at `http://localhost:8080`.

## API Endpoints

- `GET /api/payment/vnpay?amount={amount}`: Creates a VNPay payment URL.
- `GET /api/payment/payment-callback`: Handles the callback from VNPay after a payment transaction.
