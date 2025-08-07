# Customer Service API - Postman Collection

## 📋 Overview

This Postman collection provides comprehensive API testing for the Customer Service Microservice. It includes automated tests, data validation, and error handling scenarios.

## 🚀 Quick Start

### 1. Import the Collection

1. Open Postman
2. Click "Import" button
3. Import both files:
   - `Customer-Service-API.postman_collection.json`
   - `Customer-Service.postman_environment.json`

### 2. Set Up Environment

1. Select "Customer Service Environment" from the environment dropdown
2. Ensure `baseUrl` is set to `http://localhost:8080`
3. Start your Customer Service application

### 3. Run the Tests

**Option A: Run Individual Requests**
- Navigate through folders and run requests one by one
- Check test results in the "Test Results" tab

**Option B: Run Collection with Collection Runner**
1. Click "Run" button on the collection
2. Select all requests or specific folders
3. Click "Start Run"
4. View detailed test results

## 📁 Collection Structure

### 1. **Health Check**
- Application health status
- API documentation endpoint
- **Purpose**: Verify the service is running

### 2. **Customer CRUD**
- ✅ Create Customer (Valid)
- ❌ Create Customer (Duplicate Email)
- ❌ Create Customer (Invalid Data)
- ✅ Get Customer by ID
- ❌ Get Customer by ID (Not Found)
- ✅ Update Customer
- ✅ Delete Customer

### 3. **Customer Listing & Search**
- ✅ Get All Customers (Default Pagination)
- ✅ Get Customers (Custom Pagination)
- ✅ Filter by Status
- ✅ Search by Name
- ✅ Search with No Results

### 4. **Test Data Setup**
- Create Alice Brown (for search testing)
- Create Bob Wilson (for status filtering)

### 5. **Performance Tests**
- Bulk customer listing (100 records)
- Response time validation

## 🧪 Automated Tests

Each request includes automated tests that verify:

### ✅ **HTTP Status Codes**
```javascript
pm.test('Status code is 201', function () {
    pm.response.to.have.status(201);
});
```

### ✅ **Response Structure**
```javascript
pm.test('Response has customer ID', function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson.data).to.have.property('customerId');
});
```

### ✅ **Data Validation**
```javascript
pm.test('Customer details are correct', function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson.data.firstName).to.eql('John');
    pm.expect(responseJson.data.email).to.eql('john.doe@example.com');
});
```

### ✅ **Error Handling**
```javascript
pm.test('Error message mentions duplicate email', function () {
    const responseJson = pm.response.json();
    pm.expect(responseJson.message).to.include('already exists');
});
```

### ✅ **Performance Testing**
```javascript
pm.test('Response time is less than 1000ms', function () {
    pm.expect(pm.response.responseTime).to.be.below(1000);
});
```

## 🔧 Environment Variables

The collection uses these environment variables:

| Variable | Description | Example |
|----------|-------------|---------|
| `baseUrl` | API base URL | `http://localhost:8080` |
| `customerId` | ID from created customer | Auto-set by tests |
| `aliceId` | Alice's customer ID | Auto-set by tests |
| `bobId` | Bob's customer ID | Auto-set by tests |

## 📊 Test Coverage

### **Functional Testing**
- ✅ CRUD operations
- ✅ Data validation
- ✅ Business rules (email uniqueness)
- ✅ Pagination and filtering
- ✅ Search functionality
- ✅ Error handling

### **Non-Functional Testing**
- ✅ Response time validation
- ✅ Status code verification
- ✅ JSON structure validation
- ✅ Large dataset handling

## 🎯 Test Scenarios

### **Positive Test Cases**
1. **Customer Creation**: Valid data, proper response
2. **Customer Retrieval**: Existing customer, correct data
3. **Customer Update**: Valid changes, updated response
4. **Customer Search**: Name-based search, relevant results
5. **Pagination**: Page navigation, size limits

### **Negative Test Cases**
1. **Duplicate Email**: Conflict detection, error message
2. **Invalid Data**: Validation errors, proper status codes
3. **Non-existent Customer**: 404 responses, error handling
4. **Invalid Search**: Empty results, graceful handling

## 🚀 Running Tests

### **Prerequisites**
1. Customer Service application running on `localhost:8080`
2. MongoDB connected and accessible
3. Postman application installed

### **Execution Order**
1. **Health Check** → Verify service is running
2. **Test Data Setup** → Create test customers
3. **Customer CRUD** → Test core functionality
4. **Listing & Search** → Test query operations
5. **Performance Tests** → Validate response times

### **Collection Runner Setup**
```
Iterations: 1
Delay: 100ms between requests
Data: None (uses inline data)
Environment: Customer Service Environment
```

## 📈 Expected Results

### **Successful Run Results**
- ✅ **Total Tests**: ~35 automated tests
- ✅ **Pass Rate**: 100% (when service is healthy)
- ✅ **Response Times**: < 1000ms for all requests
- ✅ **Error Handling**: Proper status codes and messages

### **Failure Scenarios**
- ❌ Service not running → Health check fails
- ❌ MongoDB not connected → Database errors
- ❌ Port conflicts → Connection refused
- ❌ Invalid test data → Validation errors

## 🔍 Troubleshooting

### **Common Issues**

1. **Connection Refused**
   ```
   Error: connect ECONNREFUSED 127.0.0.1:8080
   ```
   **Solution**: Start the Customer Service application

2. **MongoDB Errors**
   ```
   MongoException: Connection timeout
   ```
   **Solution**: Ensure MongoDB is running on port 27017

3. **Test Failures**
   ```
   AssertionError: expected 500 to equal 200
   ```
   **Solution**: Check application logs for errors

4. **Environment Issues**
   ```
   ReferenceError: customerId is not defined
   ```
   **Solution**: Run "Create Customer" request first to set variables

## 💡 Pro Tips

### **Best Practices**
1. **Run Health Check First**: Always verify service status
2. **Use Collection Runner**: For comprehensive testing
3. **Check Environment Variables**: Ensure they're set correctly
4. **Monitor Response Times**: Watch for performance issues
5. **Review Test Results**: Check both pass/fail and response data

### **Customization**
1. **Add Custom Tests**: Extend existing test scripts
2. **Modify Environment**: Change URLs for different environments
3. **Add New Scenarios**: Create additional test cases
4. **Performance Tuning**: Adjust delay and iteration settings

## 🔗 Integration with CI/CD

### **Newman (Postman CLI)**
```bash
# Install Newman
npm install -g newman

# Run collection
newman run Customer-Service-API.postman_collection.json \
  -e Customer-Service.postman_environment.json \
  --reporters cli,junit \
  --reporter-junit-export results.xml
```

### **Docker Integration**
```bash
# Run tests in Docker
docker run -t postman/newman:latest \
  run Customer-Service-API.postman_collection.json \
  -e Customer-Service.postman_environment.json
```

## 📝 Notes

- **Test Data**: Collection creates and cleans up test data
- **Environment Specific**: Easily switch between dev/staging/prod
- **Automated Validation**: No manual verification needed
- **Comprehensive Coverage**: Tests all major API functionality
- **Performance Monitoring**: Built-in response time validation

This collection complements your existing unit tests (24/24 passing) by providing comprehensive API-level testing and integration validation.
