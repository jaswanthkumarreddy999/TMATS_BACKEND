# Railway Database Setup Instructions

## Required Environment Variables

Make sure these variables are set in your Railway project:

| Variable | Description |
|----------|-------------|
| `DATABASE_URL` | Complete JDBC URL including credentials |
| `MYSQLUSER` | MySQL username (usually 'root') |
| `MYSQLPASSWORD` | MySQL password |
| `MYSQLHOST` | MySQL host address |
| `MYSQLPORT` | MySQL port (usually 3306) |
| `MYSQLDATABASE` | MySQL database name (usually 'railway') |

## How to Link the MySQL Service

1. In your Railway project, go to "New Service" and add a MySQL database
2. Once created, click on your web service
3. Go to "Variables" tab
4. Click "Link from other service" 
5. Select your MySQL service
6. This will automatically add all required MySQL variables to your web service

## Checking Connection

You can verify your database connection by checking the logs for successful Hibernate initialization messages. 