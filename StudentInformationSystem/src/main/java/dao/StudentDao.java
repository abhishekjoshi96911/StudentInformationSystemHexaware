package dao;

import exception.CourseNotFoundException;
import exception.DuplicateEnrollmentException;
import exception.PaymentValidationException;
import exception.StudentNotFoundException;
import model.Course;
import model.Student;
import model.Payment;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.DBConnUtil;
import util.DbCheckingUtil ;
public class StudentDao {
 
    
    private Connection connection = DBConnUtil.getConnection()  ; 


    public void updateStudentInfo(Student student, String firstName, String lastName, Date dateOfBirth, String email, String phoneNumber) {
        try {
            // Check if the student exists
            if (!isStudentExists(student.getStudentId())) {
                throw new StudentNotFoundException("Student not found with ID: " + student.getStudentId());
            }

            // Update student information
            String sql = "UPDATE Students SET first_name = ?, last_name = ?, date_of_birth = ?, email = ?, phone_number = ? WHERE student_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setDate(3, new java.sql.Date(dateOfBirth.getTime()));
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, phoneNumber);
                preparedStatement.setInt(6, student.getStudentId());

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void makePayment(Student student, BigDecimal amount, Date paymentDate) {
        try {
            // Check if the student exists
            if (!isStudentExists(student.getStudentId())) {
                throw new StudentNotFoundException("Student not found with ID: " + student.getStudentId());
            }

            // Validate payment amount and date
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new PaymentValidationException("Invalid payment amount.");
            }

            // Record the payment
            String sql = "INSERT INTO Payments (student_id, amount, payment_date) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, student.getStudentId());
                preparedStatement.setBigDecimal(2, amount);
                preparedStatement.setDate(3, new java.sql.Date(paymentDate.getTime()));

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void displayStudentInfo(Student student) {
        try {
            // Check if the student exists
            if (!isStudentExists(student.getStudentId())) {
                throw new StudentNotFoundException("Student not found with ID: " + student.getStudentId());
            }

            // Retrieve and display student information
            String sql = "SELECT * FROM Students WHERE student_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, student.getStudentId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        System.out.println("Student ID: " + resultSet.getInt("student_id"));
                        System.out.println("First Name: " + resultSet.getString("first_name"));
                        System.out.println("Last Name: " + resultSet.getString("last_name"));
                        System.out.println("Date of Birth: " + resultSet.getDate("date_of_birth"));
                        System.out.println("Email: " + resultSet.getString("email"));
                        System.out.println("Phone Number: " + resultSet.getString("phone_number"));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public List<Course> getEnrolledCourses(Student student) {
        List<Course> enrolledCourses = new ArrayList<>();

        try {
            // Check if the student exists
            if (!isStudentExists(student.getStudentId())) {
                throw new StudentNotFoundException("Student not found with ID: " + student.getStudentId());
            }

            // Retrieve enrolled courses for the student
            String sql = "SELECT Courses.* FROM Courses " +
                    "INNER JOIN Enrollments ON Courses.course_id = Enrollments.course_id " +
                    "WHERE Enrollments.student_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, student.getStudentId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int courseId = resultSet.getInt("course_id");
                        String courseName = resultSet.getString("course_name");
                        // Retrieve other course details as needed

                        enrolledCourses.add(new Course(courseId, courseName));
                    }
                }
            }
        } catch (StudentNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return enrolledCourses;
    }

    public List<Payment> getPaymentHistory(Student student) {
        List<Payment> paymentHistory = new ArrayList<>();

        try {
            // Check if the student exists
            if (!isStudentExists(student.getStudentId())) {
                throw new StudentNotFoundException("Student not found with ID: " + student.getStudentId());
            }

            // Retrieve payment history for the student
            String sql = "SELECT * FROM Payments WHERE student_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, student.getStudentId());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int paymentId = resultSet.getInt("payment_id");
                        BigDecimal amount = resultSet.getBigDecimal("amount");
                        Date paymentDate = resultSet.getDate("payment_date");

                        paymentHistory.add(new Payment(paymentDate, student, paymentId, amount));
                    }
                }
            }
        } catch (StudentNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return paymentHistory;
    }

    public Student getStudentById(int studentId) {
    try {
        // Check if the student exists
        if (!isStudentExists(studentId)) {
            throw new StudentNotFoundException("Student not found with ID: " + studentId);
        }

        // Retrieve student information
        String sql = "SELECT * FROM Students WHERE student_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Date dateOfBirth = resultSet.getDate("date_of_birth");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phone_number");

                    return new Student(studentId, firstName, lastName, dateOfBirth, email, phoneNumber);
                }
            }
        }
    } catch (StudentNotFoundException | SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }

    return null; // Return null if the student is not found or an error occurs
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


    private boolean isEnrollmentExists(int studentId, int courseId) throws SQLException {
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
