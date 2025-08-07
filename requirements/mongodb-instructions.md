
# 🗃️ MongoDB Instructions for Customer Microservice (MCP Tool Context)

## 📦 Database Name

```
customerdb
```

## 📂 Collection

```
customers
```

## 🧬 Schema

```json
{
  "customerId": "UUID (String)",
  "firstName": "String (max 50)",
  "lastName": "String (max 50)",
  "email": "String (valid, unique)",
  "phone": "String (E.164 format: +1234567890)",
  "address": "String (optional, max 200)",
  "dateOfBirth": "Date (YYYY-MM-DD)",
  "customerStatus": "Enum (ACTIVE | INACTIVE | SUSPENDED)",
  "createdAt": "ISODate (set by system)",
  "updatedAt": "ISODate (set by system)"
}
```

## ✅ Constraints

- `email` must be **unique**
- `phone` must match regex: `^\+?[0-9]{10,15}$`
- `customerStatus` defaults to `ACTIVE` if not specified
- `createdAt`, `updatedAt` are **system-managed**, not user-editable

## 📑 Indexes

Create indexes for:

```json
[
  { "email": 1, "unique": true },
  { "phone": 1 },
  { "customerStatus": 1 }
]
```

## 🧪 Test Data Examples

```json
[
  {
    "customerId": "a1b2c3d4-e5f6-7890-abcd-1234567890ab",
    "firstName": "Alice",
    "lastName": "Smith",
    "email": "alice.smith@example.com",
    "phone": "+14155552671",
    "address": "123 Elm Street",
    "dateOfBirth": "1991-05-15",
    "customerStatus": "ACTIVE",
    "createdAt": "2025-08-06T12:00:00Z",
    "updatedAt": "2025-08-06T12:00:00Z"
  },
  {
    "customerId": "b2c3d4e5-f6a7-8901-bcde-2345678901bc",
    "firstName": "Bob",
    "lastName": "Taylor",
    "email": "bob.taylor@example.com",
    "phone": "+14155552672",
    "customerStatus": "INACTIVE",
    "createdAt": "2025-08-06T12:00:00Z",
    "updatedAt": "2025-08-06T12:00:00Z"
  }
]
```

## 🧪 Sample Queries

### 🔍 Get all ACTIVE customers

```javascript
db.customers.find({ customerStatus: "ACTIVE" })
```

### 🔍 Find by Email

```javascript
db.customers.findOne({ email: "alice.smith@example.com" })
```

### ✅ Validate Email Format (pseudo-check using regex)

```javascript
db.customers.find({ email: { $not: { $regex: /^[\w.-]+@[\w.-]+\.\w+$/ } } })
```

### 🧼 Remove test records

```javascript
db.customers.deleteMany({ email: /@example.com$/ })
```

## ⚙️ Collections to Be Created by MCP

The MongoDB MCP tools should:

- Ensure the `customerdb.customers` collection exists
- Apply validation and indexing
- Seed the test data (if asked)
- Allow generation of mocks, sample queries, or tests

## 🧠 Notes for Copilot Agent

- All MongoDB tasks (create schema, validate constraints, run queries) must target the `customerdb.customers` collection
- Use this schema to validate incoming REST payloads in Spring Boot
- When generating integration tests or test data, use the provided sample documents and indexes
