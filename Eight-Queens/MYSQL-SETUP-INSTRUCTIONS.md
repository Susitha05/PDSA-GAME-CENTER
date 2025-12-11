# MySQL Setup Instructions for Eight Queens (Port 4306)

## Current Configuration

The application is now configured to use **MySQL on port 4306**:

```properties
Database: eight_queens
Host: localhost
Port: 4306
Username: eq_user
Password: strong_password
```

## Setup Steps

### Option 1: Automated Setup (Recommended)

1. **Run the setup script:**

   ```batch
   cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens"
   mysql-setup.bat
   ```

2. **Enter your MySQL root password** when prompted

3. **Done!** The database, tables, and user will be created automatically.

### Option 2: Manual Setup

1. **Connect to MySQL on port 4306:**

   ```bash
   mysql -h localhost -P 4306 -u root -p
   ```

2. **Run the SQL commands:**

   ```sql
   CREATE DATABASE IF NOT EXISTS eight_queens;
   USE eight_queens;

   -- Create tables
   CREATE TABLE IF NOT EXISTS games (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       board TEXT NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       INDEX idx_board (board(255)),
       INDEX idx_name (name),
       INDEX idx_created_at (created_at)
   );

   CREATE TABLE IF NOT EXISTS computation_results (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       computation_type VARCHAR(50) NOT NULL,
       total_solutions INT NOT NULL,
       time_taken_ms BIGINT NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       INDEX idx_computation_type (computation_type),
       INDEX idx_created_at (created_at)
   );

   -- Create user
   CREATE USER IF NOT EXISTS 'eq_user'@'%' IDENTIFIED BY 'strong_password';
   GRANT ALL PRIVILEGES ON eight_queens.* TO 'eq_user'@'%';
   FLUSH PRIVILEGES;
   ```

3. **Verify the setup:**
   ```bash
   mysql -h localhost -P 4306 -u eq_user -p
   # Enter password: strong_password
   USE eight_queens;
   SHOW TABLES;
   ```

## Verify MySQL is Running on Port 4306

Check if MySQL is listening on port 4306:

```powershell
netstat -ano | Select-String ":4306"
```

You should see output showing MySQL is LISTENING on port 4306.

## Start the Application

After MySQL setup is complete:

```powershell
cd "f:\HNDSE232\PDSA 1\PDSA-GAME-CENTER\Eight-Queens\Eight-Queens"
$env:JAVA_HOME="C:\Program Files\Java\jdk-21"
mvn spring-boot:run
```

Or run the compiled JAR:

```powershell
java -jar target/Eight-Queens-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### Connection Refused Error

- Verify MySQL is running: `Get-Service -Name "MySQL*"`
- Check the port: `netstat -ano | Select-String ":4306"`
- Ensure firewall allows connections on port 4306

### Access Denied Error

- Verify username/password in `application.properties`
- Recreate the user:
  ```sql
  DROP USER IF EXISTS 'eq_user'@'%';
  CREATE USER 'eq_user'@'%' IDENTIFIED BY 'strong_password';
  GRANT ALL PRIVILEGES ON eight_queens.* TO 'eq_user'@'%';
  FLUSH PRIVILEGES;
  ```

### Database Not Found

- Create the database: `CREATE DATABASE eight_queens;`
- Run the setup script again

### Wrong Port

If your MySQL is on a different port, update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:YOUR_PORT/eight_queens?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
```

## Configuration Changes Made

### application.properties

✅ Changed from H2 in-memory database to MySQL
✅ Updated port from 3306 to **4306**
✅ Changed `spring.jpa.hibernate.ddl-auto` from `create-drop` to `update` (data persists)
✅ Changed Hibernate dialect to `MySQLDialect`

### Benefits of MySQL vs H2

- **Data Persistence**: Your game data survives application restarts
- **Production Ready**: MySQL is suitable for production deployment
- **Better Performance**: Optimized for real-world usage
- **Data Inspection**: Use MySQL Workbench or CLI to view/manage data

## Testing the Connection

After setup, test the application endpoints:

1. **Health Check:**

   ```
   http://localhost:8080/api/eight-queens/health
   ```

2. **Statistics:**

   ```
   http://localhost:8080/api/eight-queens/statistics
   ```

3. **Frontend:**
   ```
   http://localhost:3000
   ```

## Data Persistence

With MySQL, your data is now **persistent**:

- Player solutions are saved permanently
- Computation results are stored
- Statistics survive application restarts
- You can backup and restore your data

To backup:

```bash
mysqldump -h localhost -P 4306 -u eq_user -p eight_queens > backup.sql
```

To restore:

```bash
mysql -h localhost -P 4306 -u eq_user -p eight_queens < backup.sql
```
