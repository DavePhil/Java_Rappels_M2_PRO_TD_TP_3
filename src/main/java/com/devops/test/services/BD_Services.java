package com.devops.test.services;

import com.devops.test.BD.BD_Connection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BD_Services {

    private final BD_Connection bd_connection = new BD_Connection();
    private final Connection connection = bd_connection.getConnection();

    public BD_Services() {}


    public void storeStudent(String matricule, String name, int age) throws SQLException {
        String query = "INSERT INTO Student (matricule, name, age) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, matricule);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
        }
    }

    public void storeLecture(String code, String name, double coefficient) throws SQLException {
        String query = "INSERT INTO Lecture (code, name, coefficient) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            preparedStatement.setString(2, name);
            preparedStatement.setDouble(3, coefficient);
            preparedStatement.executeUpdate();
        }
    }

    public void addNote(String matricule, String code, double value) throws SQLException {
        String query = "INSERT INTO Notes (student_matricule, lecture_code, value) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, matricule);
            preparedStatement.setString(2, code);
            preparedStatement.setDouble(3, value);
            preparedStatement.executeUpdate();
        }
    }

    public List<String> getStudents() throws SQLException {
        List<String> students = new ArrayList<>();
        String query = "SELECT CONCAT(matricule, ' - ', name) AS student_info FROM Student";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                students.add(rs.getString("student_info"));
            }
        }
        return students;
    }

    public List<String> getLectures() throws SQLException {
        List<String> lectures = new ArrayList<>();
        String query = "SELECT CONCAT(code, ' - ', name) AS lecture_info FROM Lecture";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                lectures.add(rs.getString("lecture_info"));
            }
        }
        return lectures;
    }

    public List<Object[]> getOrderedScores() throws SQLException {
        String query = """
        SELECT s.matricule, s.name, 
               ROUND(AVG(n.value), 2) AS average_score
        FROM Student s
        JOIN Notes n ON s.matricule = n.student_matricule
        GROUP BY s.matricule, s.name
        ORDER BY average_score DESC;
    """;
        List<Object[]> results = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                results.add(new Object[]{rs.getString("matricule"), rs.getString("name"), rs.getDouble("average_score")});
            }
        }
        return results;
    }

    public List<Object[]> getFirstAndLastStudent() throws SQLException {
        String query = """
        (SELECT matricule, name, ROUND(AVG(value), 2) AS avg_score
         FROM Student
         JOIN Notes ON Student.matricule = Notes.student_matricule
         GROUP BY matricule, name
         ORDER BY avg_score DESC
         LIMIT 1)
        UNION
        (SELECT matricule, name, ROUND(AVG(value), 2) AS avg_score
         FROM Student
         JOIN Notes ON Student.matricule = Notes.student_matricule
         GROUP BY matricule, name
         ORDER BY avg_score ASC
         LIMIT 1);
    """;
        List<Object[]> results = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                results.add(new Object[]{rs.getString("matricule"), rs.getString("name"), rs.getDouble("avg_score")});
            }
        }
        return results;
    }

    public List<Object[]> getAdmittedStudents() throws SQLException {
        String query = """
        SELECT s.matricule, s.name, ROUND(AVG(n.value), 2) AS average_score
        FROM Student s
        JOIN Notes n ON s.matricule = n.student_matricule
        GROUP BY s.matricule
        HAVING average_score >= 10
        ORDER BY average_score DESC;
    """;
        List<Object[]> results = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                results.add(new Object[]{rs.getString("matricule"), rs.getString("name"), rs.getDouble("average_score")});
            }
        }
        return results;
    }

    public double getClassAverage() throws SQLException {
        String query = "SELECT ROUND(AVG(value), 2) AS class_average FROM Notes";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                return rs.getDouble("class_average");
            }
        }
        return 0.0;
    }



}
