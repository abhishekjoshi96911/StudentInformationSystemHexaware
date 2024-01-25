package dao;

import model.Course;
import model.Enrollment;
import model.Student;
import exception.StudentNotFoundException;
import exception.CourseNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import util.DBConnUtil;

public class EnrollmentDao {

    private Connection connection = DBConnUtil.getConnection()  ;

    public Student getStudent(Enrollment enrollment) {
        try {
            // Retrieve the student associated with the enrollment
            String sql = "SELECT * FROM Students WHERE student_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, enrollment.getStudentId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int studentId = resultSet.getInt("student_id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                         Date dateOfBirth =resultSet.getDate("dateOfBirth");
                         String email = resultSet.getString("email") ;
                         String phoneNumber = resultSet.getString("phoneNumber");
                        return new Student(studentId, firstName, lastName, dateOfBirth, email, phoneNumber);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    public Course getCourse(Enrollment enrollment) {
        try {
            // Retrieve the course associated with the enrollment
            String sql = "SELECT * FROM Courses WHERE course_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, enrollment.getCourseId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int courseId = resultSet.getInt("course_id");
                      //  String  = resultSet.getString("course_code");
                        String courseName = resultSet.getString("course_name");
                        // You may need to retrieve more information about the course
                        // and create a Course object here
                        return new Course(courseId,courseName);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    private boolean isStudentExists(int studentId) throws SQLException {
        String sql = "SELECT * FROM Students WHERE student_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean isCourseExists(int courseId) throws SQLException {
        String sql = "SELECT * FROM Courses WHERE course_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, courseId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
