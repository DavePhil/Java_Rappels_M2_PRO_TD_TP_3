# Notes Management Application with Swing

## Description
This application is a Java Swing-based project designed for managing and calculating class averages from a database of notes. It uses **Apache Maven** for dependency management and build automation.

---

## Prerequisites
1. **Java Development Kit (JDK)**:
   - Ensure you have JDK 17 (or a compatible version) installed.
   - Verify the installation:
     ```bash
     java -version
     ```

2. **Apache Maven**:
   - Ensure Maven is installed on your system.
   - Verify the installation:
     ```bash
     mvn -version
     ```

3. **Database Setup**:
   - Create a database using the DBMS of your choice.
   - Set up a table named `Notes` with at least a `value` column to store numeric grades.
   - Configure the database connection in the `application.properties` file or directly in the Java code:
     - **URL**: The JDBC URL for your database.
     - **USERNAME**: The database username.
     - **PASSWORD**: The database password.

---

## Steps to Build and Run

### 1. Clone the Repository
Clone the project repository and navigate to the project directory:
```bash
git clone https://github.com/your-repo/notes-management-swing.git
cd notes-management-swing
```
### 1. Build an run the project
```bash
mvn clean install
mvn exec:java -Dexec.mainClass="com.java.learning.NotesManagementWithSwing"
```
