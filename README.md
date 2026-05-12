# StockFlow

StockFlow is an inventory and order management application built with Java 21 and Spring Boot. It helps businesses manage products, track stock levels, process customer orders, generate invoices, record payments, and monitor key business metrics from a single dashboard.

The application implements a real-world order workflow where stock is reserved when an order is created, updated as the order progresses, and automatically restored if the order is cancelled.

---

## Features

### Inventory Management
- Add and manage products with name, category, price, and stock quantity
- Filter products by category
- Restock products directly from the dashboard
- Automatically load starter products when the database is empty

### Low-Stock Monitoring
- Highlight products with low inventory
- Quick restock controls
- Low-stock counts displayed in dashboard analytics

### Order Management
- Create customer orders from available products
- Reserve stock automatically
- Search orders by customer name
- Filter orders by status

### Order Workflow

```text
RESERVED → PAID → INVOICED
RESERVED → CANCELLED
```

### Analytics Dashboard
- Revenue
- Total orders
- Average order value
- Best-selling product
- Low-stock products
- Order counts by status

### REST API
- Products
- Orders
- Payments
- Invoices
- Notifications
- Analytics

---

## Technologies Used

### Backend
- Java 21
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Hibernate
- Jakarta Validation
- Maven

### Database
- H2 Database (default)
- PostgreSQL (optional)
- Docker Compose

### Frontend
- HTML5
- CSS3
- Vanilla JavaScript
- Fetch API

### Testing
- JUnit 5
- Spring Boot Test

### Development Tools
- IntelliJ IDEA / VS Code
- Git
- GitHub

---

## Project Structure

```text
StockFlow/
├── src/main/java/com/portfolio/stockflow/
│   ├── controllers/
│   ├── services/
│   ├── entities/
│   ├── repositories/
│   ├── request/
│   └── response/
├── src/main/resources/
│   ├── static/
│   │   ├── index.html
│   │   ├── app.js
│   │   └── styles.css
│   ├── application.properties
│   └── application-postgres.properties
├── src/test/
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## Main Domain Models

- **Product** – Stores product information and stock levels
- **CustomerOrder** – Represents customer orders and workflow status
- **OrderItem** – Individual products within an order
- **Payment** – Payment records for orders
- **Invoice** – Generated invoices
- **Notification** – Order-related notifications

---

## Getting Started

### Requirements
- Java 21 or newer
- Maven
- Optional: Docker (for PostgreSQL)

### Run with H2 Database

```bash
mvn spring-boot:run
```

Open:
- Application: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`

### Run with PostgreSQL

```bash
docker compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

---

## Running Tests

```bash
mvn test
```

---

## Example API Endpoints

### Products
- `GET /api/products`
- `POST /api/products`
- `POST /api/products/{id}/restock`

### Orders
- `GET /api/orders`
- `POST /api/orders`
- `POST /api/orders/{id}/pay`
- `POST /api/orders/{id}/invoice`
- `POST /api/orders/{id}/cancel`

### Analytics
- `GET /api/analytics/dashboard`

---

## Business Rules

- Stock is reserved when an order is created
- Orders cannot exceed available stock
- Only reserved orders can be paid or cancelled
- Only paid orders can be invoiced
- Cancelling an order restores reserved stock

---

## Future Improvements

- Authentication and user roles
- PDF invoice export
- Sales charts and reporting
- Pagination
- Docker deployment

---

## Author

Alsedi Berdufi

