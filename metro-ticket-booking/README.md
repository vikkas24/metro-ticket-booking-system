# Metro Ticket Booking System
Java 17 · Spring Boot 3 · Maven · PostgreSQL · HTML/CSS/Vanilla JS

## Requirements
JDK 17+, Maven 3.8+, PostgreSQL 14+, a browser, VS Code or IntelliJ.

## Step 1 – Database
```
createdb -U postgres metro_ticket_booking
psql -U postgres -d metro_ticket_booking -f database/schema.sql
psql -U postgres -d metro_ticket_booking -f database/data.sql
```
## Step 2 – Environment variables
```
export DATABASE_URL=jdbc:postgresql://localhost:5432/metro_ticket_booking
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=your_password
# optional dev admin (defaults: admin@metro.local / Admin@123)
export ADMIN_EMAIL=admin@metro.local
export ADMIN_PASSWORD=ChangeMe123
```
(Windows PowerShell: `$env:DATABASE_PASSWORD="..."`)
## Step 3 – Backend
```
cd backend
mvn spring-boot:run        # http://localhost:8080
```
The admin account is created automatically on first start if it does not exist. Change its password variables for anything beyond local use.
## Step 4 – Frontend
Serve the `frontend` folder on port 5500 (or 3000), e.g. `cd frontend && python3 -m http.server 5500`, or use the VS Code "Live Server" extension.
If you use another origin, set `CORS_ORIGINS` (comma-separated).
## Step 5 – Open `http://localhost:5500`
Register → Book (Bhayandar → Andheri, 2 passengers = ₹60) → My Tickets → Cancel. Log in as admin to see the dashboard.

## Notes
- Fare rules: `FareService.java` (bands by station count, using `station_order`).
- Login tokens are kept in memory: restart the backend and users log in again. Tickets and users persist in PostgreSQL.
- Cancellation is allowed only before the journey date, for BOOKED tickets.
- Tickets are never marked COMPLETED automatically (status exists in the schema; see "Not included").
- Not included: payments table/simulation, profile page, automated tests.
