/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Abhi
 */
public class DbCheckingUtil {
    
    
    
     public static boolean isStudentExists(int studentId) throws SQLException {
    Connection connection = DBConnUtil.getConnection() ;
         String sql = "SELECT * FROM Students WHERE student_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean isCourseExists(int courseId) throws SQLException {
        Connection connection = DBConnUtil.getConnection() ;
        String sql = "SELECT * FROM Courses WHERE course_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, courseId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean isEnrollmentExists(int studentId, int courseId) throws SQLException {
        Connection connection = DBConnUtil.getConnection() ;
        String sql = "SELECT * FROM Enrollments WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
