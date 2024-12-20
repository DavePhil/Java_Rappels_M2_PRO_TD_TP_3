package com.devops.test.BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BD_Connection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/notes_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "00000000";
    private Connection connection;

    public BD_Connection() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTables() throws SQLException {
        String createStudentsTable = """
            CREATE TABLE IF NOT EXISTS Student (
                matricule VARCHAR(10) PRIMARY KEY,
                name VARCHAR(50),
                age INT
            );
        """;

        String createLecturesTable = """
            CREATE TABLE IF NOT EXISTS Lecture (
                code VARCHAR(10) PRIMARY KEY,
                name VARCHAR(50),
                coefficient DOUBLE
            );
        """;

        String createNotesTable = """
            CREATE TABLE IF NOT EXISTS Notes (
                id INT AUTO_INCREMENT PRIMARY KEY,
                student_matricule VARCHAR(10),
                lecture_code VARCHAR(10),
                value DOUBLE,
                FOREIGN KEY (student_matricule) REFERENCES Student(matricule),
                FOREIGN KEY (lecture_code) REFERENCES Lecture(code)
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createStudentsTable);
            stmt.execute(createLecturesTable);
            stmt.execute(createNotesTable);
        }
    }
}
