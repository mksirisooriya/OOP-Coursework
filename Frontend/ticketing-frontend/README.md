
# Real-Time Ticket Management System

This is a real-time ticket management system built with a **Spring Boot** backend and a **React.js** frontend. The system allows vendors to release tickets, customers to retrieve tickets, and administrators to monitor system activities in real-time.

## Features

### Backend
- Built with **Spring Boot**.
- Provides RESTful APIs for configuration, system control, and ticket management.
- Real-time updates via **WebSocket**.
- Logging functionality to monitor system activities.
- Uses **JPA** for data persistence.

### Frontend
- Developed with **React.js**.
- Provides an intuitive user interface for system configuration, control, and monitoring.
- Displays real-time logs and ticket availability using **WebSocket**.
- Fully responsive design.

---

## Installation Instructions

### Backend
1. Ensure you have **Java 11+** and **Maven** installed.
2. Clone the repository and navigate to the backend directory.
3. Run the following commands:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
4. The backend will start on `http://localhost:8080`.

### Frontend
1. Ensure you have **Node.js** installed.
2. Navigate to the frontend directory.
3. Run the following commands:
   ```bash
   npm install
   npm start
   ```
4. The frontend will start on `http://localhost:3000`.

---

## Usage

### Configuration
1. Navigate to the **System Configuration** section on the frontend.
2. Enter the following fields:
   - Total Tickets
   - Ticket Release Rate (in milliseconds)
   - Customer Retrieval Rate (in milliseconds)
   - Maximum Ticket Capacity
3. Save the configuration.

### Starting the System
1. Go to the **Control Panel** section.
2. Click "Start System" to begin ticket operations.

### Monitoring
- View real-time ticket availability in the **Ticket Availability** section.
- Monitor system logs in the **System Log** section.

### Stopping the System
1. Go to the **Control Panel** section.
2. Click "Stop System" to halt operations.

---

## API Endpoints

### Configuration
- `POST /api/ticket/configure`: Save system configuration.
- `GET /api/ticket/status`: Get current system status.

### System Control
- `POST /api/ticket/start`: Start the ticketing system.
- `POST /api/ticket/stop`: Stop the ticketing system.

### Logs
- `GET /api/ticket/logs`: Retrieve system logs.

---

## WebSocket Topics
- `/topic/logs`: Real-time system logs.
- `/topic/ticketAvailability`: Real-time ticket availability updates.

---

## Technologies Used

### Backend
- **Spring Boot**
- **Spring WebSocket**
- **Spring Data JPA**

### Frontend
- **React.js**
- **SockJS** and **@stomp/stompjs**

---

## Contributing
1. Fork the repository.
2. Create a feature branch: `git checkout -b feature-name`.
3. Commit your changes: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin feature-name`.
5. Open a pull request.

---
