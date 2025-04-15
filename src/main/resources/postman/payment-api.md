# Payment API Postman Guide

This document provides information on how to test the Payment API using Postman.

## Setup

1. Import the collection into Postman
2. Configure environment variables:
   - `BASE_URL`: Your API base URL (e.g., `http://localhost:8080/api/v1`)

## Available Endpoints

### 1. Create Payment Intent

**Endpoint**: `POST {{BASE_URL}}/payments/create-payment-intent`

**Request Body**:
```json
{
    "amount": 1000,
    "currency": "usd",
    "order_id": 1,
    "payment_method_id": "pm_card_visa",
    "payment_method_type": "card"
}
```

**Response**:
```json
{
    "payment_intent_id": "pi_123456789",
    "client_secret": "pi_123456789_secret_123456789",
    "status": "PENDING",
    "message": "PaymentIntent created successfully. Awaiting client confirmation."
}
```

**Notes**:
- `amount` is in cents (1000 = $10.00)
- `order_id` must be a valid order ID in your system
- The client secret is only returned in the response and not stored in the database for security reasons

### 2. Get Payment Status

**Endpoint**: `GET {{BASE_URL}}/payments/{paymentIntentId}`

**Path Parameters**:
- `paymentIntentId`: The ID of the payment intent to retrieve

**Response**:
```json
{
    "payment_intent_id": "pi_123456789",
    "status": "PENDING",
    "message": "Payment status retrieved successfully"
}
```

### 3. Confirm Payment

**Endpoint**: `POST {{BASE_URL}}/payments/confirm/{paymentIntentId}`

**Path Parameters**:
- `paymentIntentId`: The ID of the payment intent to confirm

**Response**:
```json
{
    "payment_intent_id": "pi_123456789",
    "status": "SUCCESS",
    "message": "Payment success"
}
```

**Note**: This endpoint is typically used after the client has completed any required authentication steps.

### 4. Cancel Payment

**Endpoint**: `POST {{BASE_URL}}/payments/cancel/{paymentIntentId}`

**Path Parameters**:
- `paymentIntentId`: The ID of the payment intent to cancel

**Response**:
```json
{
    "payment_intent_id": "pi_123456789",
    "status": "CANCELED",
    "message": "Payment canceled successfully"
}
```

## Testing with Stripe Test Cards

When testing the payment API, you can use Stripe's test cards:

- **Successful payment**: `4242 4242 4242 4242`
- **Authentication required**: `4000 0027 6000 3184`
- **Payment declined**: `4000 0000 0000 0002`

For the full list of test cards and scenarios, refer to [Stripe's Testing documentation](https://stripe.com/docs/testing). 