# Login and Authentication Guide

## Overview
This guide explains how users can login to VirtualBank and use the JWT token to access Transaction service.

## Architecture Flow

```
1. User registers → VirtualBank (password is hashed)
2. User logs in → VirtualBank returns JWT token
3. User makes transaction → Sends JWT token to Transaction service
4. Transaction service validates JWT token
5. If valid → Transaction is processed
6. If invalid → 401 Unauthorized error
```

---

## Step 1: Create a User Account

**Endpoint:** `POST http://localhost:8081/users`

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "mySecurePassword123"
}
```

**Response:**
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "username": "john_doe",
  "email": "john@example.com",
  "password": null
}
```

**Note:** The password is hashed and stored securely. It will NOT be returned in the response.

---

## Step 2: Login to Get JWT Token

**Endpoint:** `POST http://localhost:8081/auth/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "mySecurePassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1NTBlODQwMC1lMjliLTQxZDQtYTcxNi00NDY2NTU0NDAwMDAiLCJ1c2VybmFtZSI6ImpvaG5fZG9lIiwiZW1haWwiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNjk1MDAwMDAwLCJleHAiOjE2OTUwODY0MDB9.abc123xyz",
  "tokenType": "Bearer",
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "username": "john_doe",
  "email": "john@example.com",
  "message": "Login successful"
}
```

**Important:** Save the `token` value. You'll need it for all Transaction service requests.

---

## Step 3: Use Token to Make Transactions

### Deposit Money

**Endpoint:** `POST http://localhost:8080/api/transactions/deposit`

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1NTBlODQwMC1lMjliLTQxZDQtYTcxNi00NDY2NTU0NDAwMDAiLCJ1c2VybmFtZSI6ImpvaG5fZG9lIiwiZW1haWwiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNjk1MDAwMDAwLCJleHAiOjE2OTUwODY0MDB9.abc123xyz
Content-Type: application/json
```

**Request Body:**
```json
{
  "accountId": 123,
  "amount": 500.00
}
```

### Withdraw Money

**Endpoint:** `POST http://localhost:8080/api/transactions/withdraw`

**Headers:**
```
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json
```

**Request Body:**
```json
{
  "accountId": 123,
  "amount": 100.00
}
```

### Transfer Money

**Endpoint:** `POST http://localhost:8080/api/transactions/transfer`

**Headers:**
```
Authorization: Bearer YOUR_TOKEN_HERE
Content-Type: application/json
```

**Request Body:**
```json
{
  "fromAccountId": 123,
  "toAccountId": 456,
  "amount": 250.00
}
```

---

## Testing with Postman

### 1. Create User
- Method: POST
- URL: `http://localhost:8081/users`
- Body (raw JSON):
  ```json
  {
    "username": "test_user",
    "email": "test@example.com",
    "password": "password123"
  }
  ```

### 2. Login
- Method: POST
- URL: `http://localhost:8081/auth/login`
- Body (raw JSON):
  ```json
  {
    "username": "test_user",
    "password": "password123"
  }
  ```
- Copy the `token` from the response

### 3. Make a Transaction
- Method: POST
- URL: `http://localhost:8080/api/transactions/deposit`
- Headers:
  - Key: `Authorization`
  - Value: `Bearer YOUR_COPIED_TOKEN`
- Body (raw JSON):
  ```json
  {
    "accountId": 1,
    "amount": 1000.00
  }
  ```

---

## Error Scenarios

### 1. Invalid Username or Password
**Response:** `400 Bad Request`
```json
{
  "error": "Invalid username or password"
}
```

### 2. Missing Token
**Response:** `401 Unauthorized`
```json
{
  "error": "Authentication token is required"
}
```

### 3. Invalid or Expired Token
**Response:** `401 Unauthorized`
```json
{
  "error": "Invalid or expired token"
}
```

### 4. Token without "Bearer" prefix
Make sure your Authorization header looks like this:
```
Authorization: Bearer YOUR_TOKEN_HERE
```
NOT like this:
```
Authorization: YOUR_TOKEN_HERE
```

---

## Token Information

- **Expiration:** 24 hours (86400000 milliseconds)
- **Algorithm:** HS256 (HMAC with SHA-256)
- **Contains:**
  - User ID (UUID)
  - Username
  - Email
  - Issued at timestamp
  - Expiration timestamp

---

## Security Notes

1. **Password Storage:** Passwords are hashed using BCrypt before storing in database
2. **Token Secret:** Both services use the same secret key to sign/verify tokens
3. **HTTPS:** In production, always use HTTPS to protect tokens in transit
4. **Token Storage:** Store tokens securely (e.g., localStorage, sessionStorage, or cookies with HttpOnly flag)
5. **Token Refresh:** Currently, tokens expire after 24 hours. Implement refresh token logic if needed

---

## Troubleshooting

### "Invalid or expired token" error
- Your token might have expired (24 hours limit)
- Login again to get a new token

### "Authentication token is required" error
- Make sure you're including the `Authorization` header
- Make sure it starts with "Bearer " (with a space after)

### "Invalid username or password" error
- Double-check your credentials
- Make sure the user exists (create user first)
- Passwords are case-sensitive

---

## Testing Validate Token (Optional)

You can test if your token is valid:

**Endpoint:** `GET http://localhost:8081/auth/validate?token=YOUR_TOKEN`

**Response if valid:**
```
Token is valid
```

**Response if invalid:**
```
Token is invalid
```
