# Balance Calculation System

## Overview
This is an example project about balance calculation. It provides get API and transfer API to handle balance.

- GET /api/balance/{account}: get an account balance

output:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "account": "ACCOUNT_101",
    "balance": 1000,
    "createdTime": "2024-12-29 16:30:52",
    "modifiedTime": "2024-12-29 16:30:52"
  }
}
```
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 2,
    "account": "ACCOUNT_102",
    "balance": 1000,
    "createdTime": "2024-12-29 16:30:52",
    "modifiedTime": "2024-12-29 16:30:52"
  }
}
```

- POST /api/balance/transfer: transfer balance

input:
```json
{
   "transactionId": "T001",
   "amount": 500,
   "sourceAccount": "ACCOUNT_101",
   "destinationAccount": "ACCOUNT_102"
 }
```
output:
```json
{
    "code": 0,
    "message": "success",
    "data": null
}
```

- GET /api/balance/{account}: get new balance
  output:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "account": "ACCOUNT_101",
    "balance": 500,
    "createdTime": "2024-12-29 16:30:52",
    "modifiedTime": "2024-12-29 16:40:52"
  }
}
```
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 2,
    "account": "ACCOUNT_102",
    "balance": 1500,
    "createdTime": "2024-12-29 16:30:52",
    "modifiedTime": "2024-12-29 16:40:52"
  }
}
```

## How to Run this Project
- Run this project in an IDEA.

