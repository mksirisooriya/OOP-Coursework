# Real-Time Ticket Management System

This is a Real-Time Ticket Management System built with a Spring Boot backend, a React.js frontend, and a CLI (Command Line Interface). The system allows vendors to release tickets, customers to purchase tickets, and administrators to configure and monitor system activities in real-time.

---

## Features

### CLI
- Command-line interface for managing tickets, starting/stopping the system, and viewing system logs.
- Supports multi-threaded operations for Vendors and Customers.

### Backend
- Built with Spring Boot to manage ticket operations, configuration, and logging.
- Provides RESTful API endpoints for configuration, ticket management, and system logs.
- Logs system, vendor, and customer events for easy monitoring.

### Frontend
- React.js web dashboard to visualize ticket availability, logs, and system control options.
- Real-time updates on ticket availability and system status.
- Pie chart visualization for available, sold, and remaining tickets.
- Responsive design to support mobile and desktop views.

---

## Installation Instructions

### 1. CLI Setup
1. Ensure Java 17+ is installed.
2. Compile the CLI files:
   ```bash
   javac TicketingSystemCLI.java
   ```
3. Run the CLI application:
   ```bash
   java TicketingSystemCLI
   ```

### 2. Backend Setup
1. Ensure Java 17+ and Maven are installed.
2. Set up the MySQL database and configure application.properties with your MySQL credentials.
3. Run the following commands to start the backend:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### 3. Frontend Setup
1. Ensure Node.js (v16 or higher) is installed.
2. Install the required dependencies:
   ```bash
   npm install
   npm install @mui/material @emotion/react @emotion/styled @mui/icons-material
   npm install axios
   npm install react-toastify
   npm install @nivo/core @nivo/pie
   ```
3. Start the React frontend:
   ```bash
   npm start
   ```

---

## Usage

### CLI Usage
1. Run the CLI:
   ```bash
   java TicketingSystemCLI
   ```
2. Use commands to start/stop the system, view logs, and update system configuration.

### Frontend Usage
1. Start the React frontend and access the dashboard at http://localhost:3000.
2. Configure the system using the Configuration Panel.
3. Use the Control Panel to start/stop the system.
4. Monitor system status, ticket availability, and system logs in real-time.

---

## API Endpoints

### Configuration
- POST /api/configuration: Save system configuration.
- GET /api/configuration: Get the current system configuration.

### System Control
- POST /api/tickets/vendor/{vendorId}: Vendor adds tickets.
- POST /api/tickets/customer/{customerId}: Customer purchases a ticket.
- POST /api/system/reset: Resets the entire system.
- GET /api/system/health: Checks the health of the system.

### Logs
- GET /api/logs: View all system logs.
- GET /api/logs/{eventType}: View logs filtered by event type (SYSTEM, VENDOR, CUSTOMER).

---

## Troubleshooting

- npm start fails: Run `npm install` to install missing dependencies.
- Cannot connect to MySQL: Check application.properties for correct credentials and ensure MySQL is running.
- CLI not starting: Compile using `javac TicketingSystemCLI.java`.
- API 404 Not Found: Start the backend using `mvn spring-boot:run`.
- Log not updating: Ensure polling requests to /api/logs are successful.
- Missing dependencies: Run the following commands:
  ```bash
  npm install @mui/material @emotion/react @emotion/styled @mui/icons-material
  npm install axios
  npm install react-toastify
  npm install @nivo/core @nivo/pie
  ```

---

## Technologies Used

- Spring Boot for the backend.
- React.js for the frontend.
- MySQL for database storage.
- Multi-threaded CLI for ticket management.

