# Energy Consumption Monitor

A full-stack energy monitoring web application that helps users track and optimize their household energy consumption.

## Features

- **User Authentication**: Secure JWT-based authentication with memory-only token storage
- **Appliance Management**: Add, customize, and track household appliances
- **Consumption Logging**: Log daily energy usage with physics-accurate calculations
- **Weekly Statistics**: View weekly consumption trends with interactive pie and bar charts
- **Monthly Statistics**: Analyze monthly patterns with line and bar charts
- **AI Suggestions**: Get personalized energy-saving recommendations based on percentile rankings
- **Real-time Alerts**: Receive WebSocket notifications for high consumption appliances
- **Responsive Design**: Beautiful glassmorphism UI with black-yellow gradient theme

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL
- **Authentication**: JWT (JSON Web Tokens)
- **Real-time**: WebSocket (SockJS + STOMP)
- **Build Tool**: Maven
- **Java Version**: 17

### Frontend
- **Framework**: React 18.2.0
- **Build Tool**: Vite 5.0.8
- **Routing**: React Router DOM 6.20.0
- **Charts**: Recharts 2.10.3
- **HTTP Client**: Axios 1.6.2
- **WebSocket**: SockJS-client 1.6.1, @stomp/stompjs 7.0.0
- **Styling**: CSS Modules with Glassmorphism

## Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher
- npm or yarn

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/krishnarg4321-sudo/EnergySaver.git
cd EnergySaver
```

### 2. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE energymonitor;
```

Update database credentials in `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/energymonitor
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Backend Setup

Navigate to the backend directory and build:

```bash
cd backend
mvn clean install
```

Run the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

The database will be automatically initialized with:
- Schema creation (tables, constraints, indexes)
- Seed data (35+ common household appliances)

### 4. Frontend Setup

Navigate to the frontend directory:

```bash
cd ../frontend
npm install
```

Run the development server:

```bash
npm run dev
```

The frontend will start on `http://localhost:5173`

## Usage

### Register and Login

1. Navigate to `http://localhost:5173`
2. Click "Register" and create an account
3. Login with your credentials

### Add Appliances

1. Go to the "Appliances" page
2. Browse available appliances
3. Click "Add to Collection" on any appliance
4. Optionally customize the name and rated watts

### Log Consumption

1. On the "Appliances" page, scroll to "Log Consumption"
2. Select an appliance from your collection
3. Enter the date and hours used
4. Click "Log Consumption"

### View Statistics

- **Weekly Stats**: View current week's consumption breakdown and trends
- **Monthly Stats**: Analyze monthly patterns and compare with previous month

### Get Suggestions

- Navigate to "Suggestions" to see personalized energy-saving recommendations
- Top consumers (75th percentile) get reduction suggestions
- Low consumers (25th percentile) get positive feedback
- Unused appliances trigger cleanup suggestions

### Respond to Alerts

- High consumption alerts appear at the top of the Appliances page
- Click "I reduced usage" and adjust the percentage slider
- The system automatically adjusts your consumption records

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Appliances
- `GET /api/appliances/` - Get all available appliances (public)
- `GET /api/appliances/user` - Get user's appliances (protected)
- `POST /api/appliances/user` - Add appliance to collection (protected)
- `DELETE /api/appliances/user/{id}` - Remove appliance (protected)

### Consumption
- `POST /api/consumption/log` - Log daily consumption (protected)
- `GET /api/consumption/user` - Get consumption history (protected)

### Statistics
- `GET /api/stats/weekly` - Get weekly statistics (protected)
- `GET /api/stats/monthly` - Get monthly statistics (protected)

### Suggestions
- `GET /api/suggestions` - Get AI suggestions (protected)

### Alerts
- `GET /api/alerts/` - Get user alerts (protected)
- `PUT /api/alerts/{id}/read` - Mark alert as read (protected)
- `POST /api/alerts/respond` - Respond to alert (protected)

### WebSocket
- Endpoint: `/ws`
- Topic: `/topic/alerts/{userId}`

## Energy Calculations

All energy calculations follow physics principles:

```
Power (P) → Energy (E) = P × t → Watt-hours (Wh) → Kilowatt-hours (kWh)
```

**Formula:**
- E (Wh) = P (watts) × t (hours)
- kWh = Wh ÷ 1000

The `EnergyCalculator` utility class is the single source of truth for all energy calculations.

## Database Schema

### Tables
- **users**: User accounts with authentication
- **appliances**: Available appliances catalog
- **user_appliances**: User's appliance collection
- **daily_consumption**: Daily energy consumption logs
- **alert_notifications**: High consumption alerts
- **alert_responses**: User responses to alerts

### Key Features
- Unique constraint on (user_appliance_id, log_date)
- Indexed columns for performance
- Rated watts snapshot captured at insert time
- ISO week calculation for weekly aggregations

## Architecture Highlights

### Backend
- **Layered Architecture**: Controllers → Services → Repositories
- **DTOs**: Clean separation between entities and API contracts
- **Exception Handling**: Global exception handler with proper HTTP status codes
- **Security**: JWT authentication with BCrypt password hashing
- **Scheduled Jobs**: Daily alert detection at midnight
- **WebSocket**: Real-time push notifications

### Frontend
- **Component-Based**: Reusable React components
- **Context API**: Memory-only authentication state
- **Service Layer**: Axios-based API client with interceptors
- **Protected Routes**: Route guards for authenticated pages
- **Glassmorphism**: Consistent UI theme with backdrop blur
- **Real-time**: WebSocket integration for live alerts

## Configuration

### Backend Configuration

Edit `backend/src/main/resources/application.properties`:

```properties
# Server Port
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/energymonitor
spring.datasource.username=postgres
spring.datasource.password=postgres

# JWT
jwt.secret=your_secret_key_here
jwt.expiration=86400000

# CORS
cors.allowed-origins=http://localhost:5173,http://localhost:3000
```

### Frontend Configuration

Edit `frontend/vite.config.js` for proxy settings:

```javascript
server: {
  port: 5173,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
    '/ws': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      ws: true,
    }
  }
}
```

## Building for Production

### Backend

```bash
cd backend
mvn clean package
java -jar target/energy-monitor-1.0.0.jar
```

### Frontend

```bash
cd frontend
npm run build
```

Serve the `dist` folder with any static file server.

## Testing

### Backend Tests

```bash
cd backend
mvn test
```

### Frontend Tests

```bash
cd frontend
npm test
```

## Design Principles

### UI/UX
- **Theme**: Black-to-yellow gradient with glow-in-darkness effect
- **Glassmorphism**: All components use glass effect with backdrop blur
- **Typography**: Montserrat font (all weights)
- **No Emojis**: Clean, professional interface
- **Navigation**: Vertical menu at top-left corner (non-generic layout)
- **Interactions**: Elevated buttons, smooth transitions, shadow effects

### Code Quality
- **Physics-Accurate**: EnergyCalculator for all energy calculations
- **Null-Safe**: Proper null handling in queries
- **ISO Standards**: ISO week calculation for boundaries
- **Snapshots**: Rated watts captured at insert time
- **Real Data**: Charts use live API data, no mocks
- **Transaction Management**: @Transactional for data consistency

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running: `sudo service postgresql status`
- Verify credentials in application.properties
- Check firewall settings

### Frontend Not Loading
- Ensure backend is running on port 8080
- Check proxy configuration in vite.config.js
- Clear browser cache and restart dev server

### WebSocket Connection Failed
- Verify WebSocket endpoint is accessible
- Check CORS configuration
- Ensure SockJS is properly configured

### Build Errors
- Backend: Run `mvn clean install` to refresh dependencies
- Frontend: Delete `node_modules` and run `npm install` again

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit changes: `git commit -am 'Add your feature'`
4. Push to branch: `git push origin feature/your-feature`
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Author

Created by krishnarg4321-sudo

## Acknowledgments

- Spring Boot for the robust backend framework
- React and Vite for the modern frontend tooling
- Recharts for beautiful data visualizations
- PostgreSQL for reliable data storage
