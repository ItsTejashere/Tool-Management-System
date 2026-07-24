# 🛠️ Enterprise Tool Management System (TMS)

A full-stack, enterprise-grade inventory management system designed for manufacturing and industrial environments. This system tracks the lifecycle, location, and movement of physical tools (like CNC end mills, drill bits, and inserts) using **Serialized Inventory Architecture** and an immutable transaction ledger.

## 🚀 Key Features

* **Serialized Tracking:** Graduates from basic "bulk quantity" math to tracking exact physical assets. Every tool gets a unique serial number (e.g., `EM10-001`) to track its exact state (Available, In Use, Sharpening, Damaged).
* **Immutable Movement Ledger:** Every transaction (Issue, Return, Stock In, Send to Sharpening) is recorded in a tamper-proof history ledger with timestamps, specific serials involved, and associated projects/machines.
* **Smart Dashboard:** Real-time calculation of available stock directly from physical instance counts, rather than relying on static database values.
* **Workspace Isolation:** Inventory managers can dynamically create, manage, and delete specific projects/workspaces isolated by department.
* **Role-Based Access Control (RBAC):** UI routing and actions are protected based on user roles (e.g., restricted `INVENTORY` manager capabilities).

## 💻 Tech Stack

**Frontend**
* React.js (Vite)
* React Router DOM
* Axios (API Integration)
* Bootstrap 5 (UI/UX)

**Backend**
* Java 17+
* Spring Boot (REST APIs, CORS configuration)
* JDBC (Custom transactional queries for complex database operations)

**Database**
* MySQL (Relational architecture with Foreign Key constraints and Cascade Deletions)

## 🗄️ Database Architecture Highlights

This system utilizes a dual-table architecture for inventory to mimic enterprise standards:
1. **`tool` (The Catalog Blueprint):** Stores the universal definitions of a tool type (Code, Name, Min Qty).
2. **`tool_instance` (The Physical Objects):** Stores the individual, serial-numbered physical objects, linked back to the blueprint.
3. **`tool_movement` (The Ledger):** Logs every state change in a transactional history block.

## ⚙️ Installation & Setup

### Prerequisites
* Node.js & npm installed
* Java Development Kit (JDK) 17+ installed
* MySQL Server running locally on port 3306

### 1. Database Setup
1. Create a MySQL database named `tms_db`.
2. Run the provided SQL scripts in the `/database` folder to generate the schema for `tool`, `tool_instance`, `tool_movement`, `project`, and `department` tables.

### 2. Backend Setup (Spring Boot)
1. Open the backend project in IntelliJ IDEA or Eclipse.
2. Navigate to `src/main/resources/application.properties` and update your database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/tms_db
   spring.datasource.username=YOUR_MYSQL_USERNAME
   spring.datasource.password=YOUR_MYSQL_PASSWORD
