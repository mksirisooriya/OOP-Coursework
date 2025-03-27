# Real-Time Ticket Management System

This is a Real-Time Ticket Management System built with a Spring Boot backend, a React.js frontend, and a CLI (Command Line Interface). The system allows vendors to release tickets, customers to purchase tickets, and administrators to configure and monitor system activities in real-time.

## Features

### CLI

*   Command-line interface for managing tickets, starting/stopping the system, and viewing system logs.
*   Supports multi-threaded operations for Vendors and Customers.

### Backend

*   Built with Spring Boot to manage ticket operations, configuration, and logging.
*   Provides RESTful API endpoints for configuration, ticket management, and system logs.
*   Logs system, vendor, and customer events for easy monitoring.

### Frontend

*   React.js web dashboard to visualize ticket availability, logs, and system control options.
*   Real-time updates on ticket availability and system status.
*   Pie chart visualization for available, sold, and remaining tickets.
*   Responsive design to support mobile and desktop views.

## Technologies Used

*   **Backend:** Spring Boot, Java
*   **Frontend:** React.js, Material-UI, Nivo
*   **Database:** MySQL
*   **CLI:** Java

## Installation and Setup

Follow these instructions to get the project running on your local machine.

### Prerequisites

*   Java 17+
*   Maven
*   Node.js (v16 or higher)
*   npm

### 1. Clone the Repository

```bash
git clone https://github.com/mksirisooriya/OOP-Coursework.git
cd OOP-Coursework 
```

### 2. CLI Setup

1.  Navigate to the CLI directory.

    ```bash
    cd CLI
    ```
2.  Compile the CLI files:

    ```bash
    javac TicketingSystemCLI.java
    ```

### 3. Backend Setup

1.  Navigate to the backend directory.

    ```bash
    cd Backend/Ticketing\ System
    ```
2.  Configure the database connection in `src/main/resources/application.properties`.  Make sure to set up the MYSQL and make sure it is running.

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/ticketing_system
    spring.datasource.username=ticketuser
    spring.datasource.password=password
    ```
3.  Build and run the Spring Boot application:

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

### 4. Frontend Setup

1.  Navigate to the frontend directory.

    ```bash
    cd ../../Frontend/ticketing-frontend
    ```
2.  Install dependencies:

    ```bash
    npm install
    npm install @mui/material @emotion/react @emotion/styled @mui/icons-material
    npm install axios
    npm install react-toastify
    npm install @nivo/core @nivo/pie
    ```
3.  Start the React application:

    ```bash
    npm start
    ```

## Usage

### CLI Usage

1.  Run the CLI:

    ```bash
    cd CLI   # If you are not in this directory
    java TicketingSystemCLI
    ```
2.  Use commands to start/stop the system, view logs, and update system configuration.

### Frontend Usage

1.  Start the React frontend and access the dashboard at `http://localhost:3000`.
2.  Configure the system using the Configuration Panel.
3.  Use the Control Panel to start/stop the system.
4.  Monitor system status, ticket availability, and system logs in real-time.

## API Endpoints

### Configuration

*   `POST /api/configuration`: Save system configuration.
*   `GET /api/configuration`: Get the current system configuration.

### System Control

*   `POST /api/tickets/vendor/{vendorId}`: Add a ticket for a vendor.
*   `POST /api/tickets/customer/{customerId}`: Purchase a ticket for a customer.
*   `POST /api/system/reset`: Reset the system.
*   `GET /api/system/health`: Get system health information.

### Logs

*   `GET /api/logs`: Get all system logs.
*   `GET /api/logs/{eventType}`: Get logs by event type (SYSTEM, VENDOR, CUSTOMER).

## Troubleshooting

*   `npm start` fails: Run `npm install` to install missing dependencies.
*   Cannot connect to MySQL: Check `application.properties` for correct credentials and ensure MySQL is running.
*   CLI not starting: Compile using `javac TicketingSystemCLI.java`.
*   API `404 Not Found`: Start the backend using `mvn spring-boot:run`.
*   Log not updating: Ensure polling requests to `/api/logs` are successful.
*   Missing dependencies: Run the following commands in frontend directory:

    ```bash
    npm install @mui/material @emotion/react @emotion/styled @mui/icons-material
    npm install axios
    npm install react-toastify
    npm install @nivo/core @nivo/pie
    ```

```