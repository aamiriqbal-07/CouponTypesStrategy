# Coupon Management Service

## Overview
The **Coupon Management Service** is a RESTful API built with Spring Boot that provides functionalities to manage and apply discount coupons for an e-commerce platform. The service supports various types of coupons, including cart-wise, product-wise, and "Buy X Get Y" (BxGy) offers. It is designed to be extensible, allowing easy addition of new coupon types.

The service uses **MySQL** as the database and follows containerized deployment using **Docker** and **Docker Compose**.

---

## Features
1. **Coupon Management**:
    - Create, update, fetch, and delete coupons.
    - Supports cart-wise, product-wise, and BxGy coupon types.

2. **Coupon Application**:
    - Fetch applicable coupons for a given cart.
    - Apply a specific coupon to a cart and calculate discounts.

3. **Error Handling**:
    - Graceful handling of errors, such as invalid or inactive coupons.

4. **Scalable Design**:
    - Extensible structure to add new coupon types easily.
    - Containerized for deployment consistency.

---

## Instructions to Run Locally

### Prerequisites
- **Java 17** installed.
- **Docker** and **Docker Compose** installed.
- **Maven** installed.

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/coupon-management-service.git
   cd coupon-management-service
   ```

2. Build the project:
   ```bash
   mvn clean package
   ```

3. Run the application using Docker Compose:
   ```bash
   docker-compose up --build
   ```

4. Access the application:
    - The API will be available at `http://localhost:8080`.
    - MySQL is accessible at `localhost:3307` (host) or `db:3306` (container).

---

## Testing Process for All APIs

Below is the testing process for the key APIs:

### **1. Create Coupons**
- **Cart-Wise Coupon**: 10% off on carts above $100.
  ```bash
  curl -X POST http://localhost:8080/api/v1/coupons \
  -H "Content-Type: application/json" \
  -d '{
      "code": "CART10",
      "type": "CART_WISE",
      "discount": 10,
      "threshold": 100,
      "description": "10% off on carts above $100",
      "active": true
  }'
  ```

- **Product-Wise Coupon**: 20% off on Product A.
  ```bash
  curl -X POST http://localhost:8080/api/v1/coupons \
  -H "Content-Type: application/json" \
  -d '{
      "code": "PROD20A",
      "type": "PRODUCT_WISE",
      "discount": 20,
      "productId": "A",
      "description": "20% off on Product A",
      "active": true
  }'
  ```

- **BxGy Coupon**: Buy 2 of Product X, Get 1 of Product Y Free.
  ```bash
  curl -X POST http://localhost:8080/api/v1/coupons \
  -H "Content-Type: application/json" \
  -d '{
      "code": "BXGY2X1Y",
      "type": "BXGY",
      "buyProductId": "X",
      "buyQuantity": 2,
      "getProductId": "Y",
      "getQuantity": 1,
      "description": "Buy 2 of Product X, Get 1 of Product Y Free",
      "active": true
  }'
  ```

---

### **2. Get All Coupons**
```bash
curl -X GET http://localhost:8080/api/v1/coupons
```

---

### **3. Fetch Applicable Coupons**
- **Cart Example**: Cart Total is $200, containing Products A and B.
  ```bash
  curl -X POST http://localhost:8080/api/v1/coupons/applicable-coupons \
  -H "Content-Type: application/json" \
  -d '{
      "items": [
          {"productId": "A", "quantity": 3, "price": 50},
          {"productId": "B", "quantity": 2, "price": 25}
      ],
      "totalAmount": 200
  }'
  ```

---

### **4. Apply a Coupon**
- **Apply `CART10` Coupon**:
  ```bash
  curl -X POST http://localhost:8080/api/v1/coupons/apply-coupon/1 \
  -H "Content-Type: application/json" \
  -d '{
      "items": [
          {"productId": "A", "quantity": 3, "price": 50},
          {"productId": "B", "quantity": 2, "price": 25}
      ],
      "totalAmount": 200
  }'
  ```

---

### **5. Get a Coupon by ID**
```bash
curl -X GET http://localhost:8080/api/v1/coupons/1
```

---

### **6. Update a Coupon**
- **Update `CART10` to 12% off for carts above $120**:
  ```bash
  curl -X PUT http://localhost:8080/api/v1/coupons/1 \
  -H "Content-Type: application/json" \
  -d '{
      "code": "CART12",
      "type": "CART_WISE",
      "discount": 12,
      "threshold": 120,
      "description": "12% off on carts above $120",
      "active": true
  }'
  ```

---

### **7. Delete a Coupon**
```bash
curl -X DELETE http://localhost:8080/api/v1/coupons/1
```

---
