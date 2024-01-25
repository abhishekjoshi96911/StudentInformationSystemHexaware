package dao;

import model.Course;
import model.Enrollment;
import model.Teacher;
import exception.CourseNotFoundException;
import exception.TeacherNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Course;
import model.Enrollment;
import model.Teacher;
import util.DBConnUtil;

public class CourseDao {

    private Connection connection = DBConnUtil.getConnection()  ; 

    

    public void assignTeacherToCourse(Course course, Teacher teacher) {
        try {
            // Check if the course and teacher exist
            if (!isCourseExists(course.getCourseId())) {
                throw new CourseNotFoundException("Course not found with ID: " + course.getCourseId());
            }

            if (!isTeacherExists(teacher.getTeacherId())) {
                throw new TeacherNotFoundException("Teacher not found with ID: " + teacher.getTeacherId());
            }

            // Assign the teacher to the course
            String sql = "UPDATE Courses SET teacher_id = ? WHERE course_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, teacher.getTeacherId());
                preparedStatement.setInt(2, course.getCourseId());

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateCourseInfo(String courseCode, String courseName, String instructor) {
        try {
            // Update course information
            String sql = "UPDATE Courses SET course_name = ?, instructor = ? WHERE course_code = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, courseName);
                preparedStatement.setString(2, instructor);
                preparedStatement.setString(3, courseCode);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void displayCourseInfo(Course course) {
        try {
            // Display detailed information about the course
            String sql = "SELECT * FROM Courses WHERE course_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, course.getCourseId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        System.out.println("Course Code: " + resultSet.getString("course_code"));
                        System.out.println("Course Name: " + resultSet.getString("course_name"));
                        System.out.println("Instructor: " + resultSet.getString("instructor"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<Enrollment> getEnrollments(Course course) {
        List<Enrollment> enrollments = new ArrayList<>();

        try {
            // Retrieve student enrollments for the course
            String sql = "SELECT * FROM Enrollments WHERE course_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, course.getCourseId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int studentId = resultSet.getInt("student_id");
                        // You may need to retrieve more information about the student
                        // and create a Student object here
                        enrollments.add(new Enrollment(studentId, course.getCourseId()));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return enrollments;
    }

    public Teacher getTeacher(Course course) {
        try {
            // Retrieve the assigned teacher for the course
            String sql = "SELECT * FROM Teachers WHERE teacher_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, course.getTeacher().getTeacherId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int teacherId = resultSet.getInt("teacher_id");
                        //String teacherName = resultSet.getString("teacher_name");
                        // You may need to retrieve more information about the teacher
                        // and create a Teacher object here
                        return new Teacher(teacherId);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
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
