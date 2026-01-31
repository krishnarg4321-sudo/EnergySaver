# Energy Monitor - Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 12+
- Maven 3.6+

### Step 1: Database Setup (1 minute)
```bash
# Create database
psql -U postgres
CREATE DATABASE energymonitor;
\q
```

### Step 2: Backend Setup (2 minutes)
```bash
cd backend

# Update database credentials in src/main/resources/application.properties
# spring.datasource.username=your_username
# spring.datasource.password=your_password

# Run backend
mvn spring-boot:run
```

Backend will start at **http://localhost:8080**

### Step 3: Frontend Setup (2 minutes)
```bash
cd frontend

# Install dependencies
npm install

# Run development server
npm run dev
```

Frontend will start at **http://localhost:5173**

### Step 4: Use the Application

1. **Register**: Navigate to http://localhost:5173 and click "Register"
2. **Add Appliances**: Browse and add appliances to your collection
3. **Log Consumption**: Enter daily usage for your appliances
4. **View Stats**: Check weekly and monthly statistics
5. **Get Suggestions**: See AI-powered energy-saving tips

## 📊 Sample Data

The application comes with **35+ pre-loaded appliances**:
- Kitchen (8): Refrigerator, Microwave, Stove, Dishwasher, etc.
- Entertainment (5): TV, Gaming Console, Sound System, etc.
- Climate Control (5): AC, Heater, Fan, etc.
- Computing (5): Desktop, Laptop, Monitor, etc.
- And more!

## 🔑 Key Features to Try

### 1. Appliance Management
- Browse available appliances
- Add to your collection with custom names
- Override default rated watts

### 2. Energy Tracking
- Log daily consumption (date + hours used)
- View consumption history
- See energy calculations in Wh and kWh

### 3. Statistics
- **Weekly**: Pie chart (breakdown) + Bar chart (daily totals)
- **Monthly**: Line chart (trend) + Bar chart (comparison)
- Period-over-period comparisons

### 4. AI Suggestions
- Get ranked by percentile
- Top consumers get reduction tips
- Low consumers get praise
- Remove unused appliances

### 5. Real-time Alerts
- Receive notifications for high consumption
- Respond with "I reduced usage"
- Auto-adjustment of records

## 🎨 UI Preview

The application features:
- **Black-yellow gradient theme** with glassmorphism
- **Top-left vertical navbar** for navigation
- **Interactive charts** with Recharts
- **Smooth animations** and hover effects
- **Responsive design** for all screen sizes

## 📝 API Endpoints

### Authentication
- `POST /api/auth/register` - Create account
- `POST /api/auth/login` - Get JWT token

### Appliances
- `GET /api/appliances/` - Browse all
- `POST /api/appliances/user` - Add to collection

### Consumption
- `POST /api/consumption/log` - Log usage
- `GET /api/consumption/user` - View history

### Statistics
- `GET /api/stats/weekly` - Weekly stats
- `GET /api/stats/monthly` - Monthly stats

### Suggestions
- `GET /api/suggestions` - Get AI tips

### Alerts
- `GET /api/alerts/` - View alerts
- `POST /api/alerts/respond` - Respond to alert

## 🔧 Configuration

### Backend (application.properties)
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/energymonitor
spring.datasource.username=postgres
spring.datasource.password=postgres

# JWT
jwt.secret=your_secret_key
jwt.expiration=86400000

# Server
server.port=8080
```

### Frontend (vite.config.js)
```javascript
server: {
  port: 5173,
  proxy: {
    '/api': 'http://localhost:8080',
    '/ws': 'http://localhost:8080'
  }
}
```

## 🐛 Troubleshooting

### Backend won't start
- ✅ Check PostgreSQL is running: `sudo service postgresql status`
- ✅ Verify database credentials in application.properties
- ✅ Ensure port 8080 is available

### Frontend won't connect
- ✅ Confirm backend is running on port 8080
- ✅ Check browser console for errors
- ✅ Try clearing browser cache

### WebSocket not working
- ✅ Verify backend WebSocket endpoint: http://localhost:8080/ws
- ✅ Check CORS configuration in SecurityConfig.java
- ✅ Ensure user is logged in

## 📚 Documentation

- **README.md** - Complete setup and API documentation
- **IMPLEMENTATION_SUMMARY.md** - Detailed implementation overview

## 💡 Pro Tips

1. **Energy Formula**: E (Wh) = P (watts) × t (hours), kWh = Wh ÷ 1000
2. **Week Boundaries**: Uses ISO week calculation (Mon-Sun)
3. **Percentile Ranking**: ((N-1-i)/(N-1)) × 100
4. **JWT Storage**: Tokens stored in memory only, not localStorage
5. **Alert Threshold**: 75th percentile triggers high consumption alert

## 🎯 Next Steps

1. ✅ Register and login
2. ✅ Add 3-5 appliances to your collection
3. ✅ Log consumption for a few days
4. ✅ Check weekly and monthly stats
5. ✅ Review AI suggestions
6. ✅ Respond to any alerts

## 🤝 Support

For issues or questions:
- Check README.md for detailed documentation
- Review IMPLEMENTATION_SUMMARY.md for technical details
- Create an issue in the GitHub repository

## 🎉 Enjoy!

Start tracking your energy consumption and save money today!
