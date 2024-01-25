package dao;

import model.Course;
import model.Teacher;
import exception.TeacherNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.DBConnUtil;

public class TeacherDao {

    private Connection connection = DBConnUtil.getConnection()  ;

    public void updateTeacherInfo(Teacher teacher) {
        try {
            // Update teacher information
            String sql = "UPDATE Teachers SET first_name = ?, email = ?, last_name = ? WHERE teacher_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, teacher.getFirstName());
                preparedStatement.setString(2, teacher.getEmail());
                preparedStatement.setString(3, teacher.getLastName());
                preparedStatement.setInt(4, teacher.getTeacherId());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Teacher displayTeacherInfo(int teacherId) {
        try {
            // Retrieve detailed information about the teacher
            String sql = "SELECT * FROM Teachers WHERE teacher_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, teacherId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String FirstName = resultSet.getString("first_name");
                         String LastName = resultSet.getString("Last_name");
                        String email = resultSet.getString("email");
                      // String expertise = resultSet.getString("expertise");

                        return new Teacher(teacherId,FirstName,LastName,email);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    public List<Course> getAssignedCourses(int teacherId) {
        List<Course> assignedCourses = new ArrayList<>();

        try {
            // Retrieve courses assigned to the teacher
            String sql = "SELECT * FROM Courses WHERE teacher_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, teacherId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int courseId = resultSet.getInt("course_id");
                        String courseName = resultSet.getString("course_name");

                        assignedCourses.add(new Course(courseId, courseName));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return assignedCourses;
    }

    private boolean isTeacherExists(int teacherId) throws SQLException {
        String sql = "SELECT * FROM Teachers WHERE teacher_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, teacherId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
