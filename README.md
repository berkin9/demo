# E-Commerce Application

This project is a Spring Boot & Thymeleaf-based e-commerce application. Users can browse products, add them to their cart, and manage their orders. Admins can manage products and categories through a dedicated admin panel.

---

## Features

### User Features
- User login and session management (email & password)
- Product listing with category display
- Add products to cart and create orders
- User-specific order panel
- Order table shows product name, category, quantity, price, and order date
- Recommended products (other products in the same category)

### Admin Panel
- Add, list, and edit products
- Add, list, and edit categories
- Select category when adding products
- Filter products by category
- Admin panel URLs: `/admin/products` and `/admin/categories`

### Database
- PostgreSQL as the database
- JPA / Hibernate for entity management
- Tables: `User`, `Product`, `Category`, `Order`
- Many-to-one relationships between orders, users, and products
- Many-to-one relationship between products and categories
- Proper use of Lazy and Eager fetching

### Frontend
- Thymeleaf template engine
- Dynamic form binding using `th:field`, `th:each`, `th:text`
- Dropdowns for selecting products and categories
- Tables for cart and order panel display

### Backend
- Spring Boot 3.x
- Spring Web (REST + MVC)
- Spring Data JPA
- Spring Security (optional, for login/logout)
- Lombok to reduce boilerplate
- Proper layered architecture: Controller, Service, Repository

### API / Testing
- CRUD operations via REST endpoints:
  - `/orders/panel` - user order panel
  - `/admin/products/*` - product management
  - `/admin/categories/*` - category management
- JSON or form-data requests supported
- Exception handling for errors

---

## Installation

1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE demo;
   ```
2. Configure `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/demo
   spring.datasource.username=postgres
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.thymeleaf.cache=false
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

---

## Usage

- User panel: `http://localhost:8080/orders/panel`
- Admin panel: `http://localhost:8080/admin/products` and `http://localhost:8080/admin/categories`
- Users can place orders via dropdown selection of product and quantity.

---

## Technologies
- Java 17+
- Spring Boot 3.x
- Spring Web MVC & REST
- Spring Data JPA / Hibernate
- Thymeleaf
- PostgreSQL
- Lombok
- Maven

---

## Future Improvements
- Spring Security with roles and permissions
- Order status tracking
- Cart item update and removal
- Improved recommended products algorithm
- Responsive frontend design (CSS / Bootstrap)

---

## License
This project is licensed under the MIT License.
