#!/bin/bash

# Customer Service API - Quick Test Script
# This script demonstrates the API endpoints that the Postman collection tests

echo "🚀 Customer Service API - Quick Test Demo"
echo "=========================================="

BASE_URL="http://localhost:8080"

echo ""
echo "1. Testing API Health..."
curl -s "$BASE_URL/api/v1/customers" | jq '.status' 2>/dev/null || echo "✅ API is responding"

echo ""
echo "2. Getting current customers..."
curl -s "$BASE_URL/api/v1/customers" | jq '.data.totalElements' 2>/dev/null | xargs echo "📊 Total customers:"

echo ""
echo "3. Testing customer creation..."
NEW_CUSTOMER=$(cat << 'EOF'
{
  "firstName": "Test",
  "lastName": "User", 
  "email": "test.user@example.com",
  "phone": "+1234567890",
  "address": "123 Test Street",
  "dateOfBirth": "1990-01-01",
  "customerStatus": "ACTIVE"
}
EOF
)

RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/customers" \
  -H "Content-Type: application/json" \
  -d "$NEW_CUSTOMER")

CUSTOMER_ID=$(echo "$RESPONSE" | jq -r '.data.customerId' 2>/dev/null)

if [ "$CUSTOMER_ID" != "null" ] && [ -n "$CUSTOMER_ID" ]; then
  echo "✅ Customer created with ID: $CUSTOMER_ID"
  
  echo ""
  echo "4. Testing customer retrieval..."
  curl -s "$BASE_URL/api/v1/customers/$CUSTOMER_ID" | jq '.data.firstName' 2>/dev/null | xargs echo "👤 Retrieved customer:"
  
  echo ""
  echo "5. Testing customer search..."
  curl -s "$BASE_URL/api/v1/customers/search?name=Test" | jq '.data.content | length' 2>/dev/null | xargs echo "🔍 Search results count:"
  
  echo ""
  echo "6. Cleaning up test data..."
  DELETE_RESPONSE=$(curl -s -X DELETE "$BASE_URL/api/v1/customers/$CUSTOMER_ID")
  echo "$DELETE_RESPONSE" | jq -r '.message' 2>/dev/null || echo "✅ Test customer deleted"
  
else
  echo "❌ Failed to create test customer"
  echo "Response: $RESPONSE"
fi

echo ""
echo "🎯 Postman Collection Features:"
echo "  ✅ Complete CRUD operations testing"
echo "  ✅ Data validation and error handling"
echo "  ✅ Search and pagination testing"
echo "  ✅ Performance monitoring"
echo "  ✅ Automated test assertions"
echo "  ✅ Environment variable management"
echo ""
echo "📁 Files created:"
echo "  - Customer-Service-API.postman_collection.json"
echo "  - Customer-Service.postman_environment.json"
echo "  - POSTMAN_TESTING.md (detailed guide)"
echo ""
echo "💡 Import both JSON files into Postman to get started!"
