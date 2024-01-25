/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import exception.DuplicateEnrollmentException;
import exception.CourseNotFoundException;
import exception.PaymentValidationException;
import exception.StudentNotFoundException;
import exception.TeacherNotFoundException;
import model.Course ;
import model.Student;
import model.Payment;
import model.Teacher;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.DBConnUtil;

/**
 *
 * @author Abhi
 */
public class SISImplementation implements SISInterface {

    @Override
    public void enrollStudentInCourse(int studentID,int courseId, Date enrollmentDate)
         {
        try (Connection connection = DBConnUtil.getConnection()) {
            // Check if the student and course exist
            if (!isStudentExists(studentID, connection)) {
                throw new StudentNotFoundException("Student not found with ID: " + studentID);
            }

            if (!isCourseExists(courseId, connection)) {
                throw new CourseNotFoundException("Course not found with ID: " + courseId);
            }

            // Check if the student is already enrolled in the course
            if (isEnrollmentExists(studentID, courseId, connection)) {
                throw new DuplicateEnrollmentException("Student is already enrolled in the course.");
            }

            // Perform the enrollment
            String sql = "INSERT INTO Enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, studentID);
                preparedStatement.setInt(2, courseId);
                preparedStatement.setDate(3, (java.sql.Date) enrollmentDate);

                preparedStatement.executeUpdate();
            }
        } 
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void assignTeacherToCourse(int teacherId,int courseId) {
            try {
                  try (Connection connection = DBConnUtil.getConnection()) {
            // Check if the teacher and course exist
            if (!isTeacherExists(teacherId, connection)) {
                throw new TeacherNotFoundException("Teacher not found with ID: " + teacherId);
            }

            if (!isCourseExists(courseId, connection)) {
                throw new CourseNotFoundException("Course not found with ID: " + courseId);
            }

            // Assign the teacher to the course
            String sql = "UPDATE Courses SET teacher_id = ? WHERE course_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, teacherId);
                preparedStatement.setInt(2, courseId);

                preparedStatement.executeUpdate();
            }
        }
            }
                  catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            
            
        }
    }
    

    @Override
    public void recordPayment(int studentID, BigDecimal amount, Date paymentDate) {
            try {
             try (Connection connection = DBConnUtil.getConnection()) {
            // Check if the student exists
            if (!isStudentExists(studentID, connection)) {
                throw new StudentNotFoundException("Student not found with ID: " + studentID);
            }

            // Validate payment amount and date
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new PaymentValidationException("Invalid payment amount.");
            }

            // Record the payment
            String sql = "INSERT INTO Payments (student_id, amount, payment_date) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, studentID);
                preparedStatement.setBigDecimal(2, amount);
                preparedStatement.setDate(3, (java.sql.Date)paymentDate);

                preparedStatement.executeUpdate();
            }
        }
            }
            catch (PaymentValidationException | StudentNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    
    @Override
    public List<Student> generateEnrollmentReport(int courseId) {
        
         List<Student> enrolledStudents = new ArrayList<>();

        try (Connection connection = DBConnUtil.getConnection()) {
            // Check if the course exists
            if (!isCourseExists(courseId, connection)) {
                throw new CourseNotFoundException("Course not found with ID: " + courseId);
            }

            // Retrieve enrollment data for the course
            String sql = "SELECT Students.* FROM Students " +
                    "INNER JOIN Enrollments ON Students.student_id = Enrollments.student_id " +
                    "WHERE Enrollments.course_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, courseId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Process and add enrolled students to the list
                    while (resultSet.next()) {
                        int studentId = resultSet.getInt("student_id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        Date dateOfBirth = resultSet.getDate("date_of_birth");
                        String email = resultSet.getString("email");
                        String phoneNumber = resultSet.getString("phone_number");

                        enrolledStudents.add(new Student(studentId, firstName, lastName, dateOfBirth, email, phoneNumber));
                    }
                }
            }
        }
        catch (Exception e) {
            
            System.out.println("Error: " + e.getMessage());
        }

        return enrolledStudents;
    }

    
    @Override
    public List<Payment> generatePaymentReport(int studentId) {
   
         List<Payment> payments = new ArrayList<>();

        try (Connection connection = DBConnUtil.getConnection()) {
            // Check if the student exists
            if (!isStudentExists(studentId, connection)) {
                throw new StudentNotFoundException("Student not found with ID: " + studentId);
            }

            // Retrieve payment data for the student
            String sql = "SELECT * FROM Payments WHERE student_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, studentId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Process and add payments to the list
                    while (resultSet.next()) {
                        int paymentId = resultSet.getInt("payment_id");
                        BigDecimal amount = resultSet.getBigDecimal("amount");
                        Date paymentDate = resultSet.getDate("payment_date");
                        StudentDao studentDao = new StudentDao() ;
                      Student student =  studentDao.getStudentById(studentId) ;

                        payments.add(new Payment(paymentDate,student, paymentId, amount));
                    }
                }
            }
        }
          catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return payments ;
    }
    
    // Method not yet Ready

//    @Override
//    public void calculateCourseStatistics(Course course) {
//                // Retrieve enrollment records for the specified course
//        List<Enrollment> enrollments = course.getEnrollments();
//
//        // Calculate total number of enrollments
//        int totalEnrollments = enrollments.size();
//
//        // Calculate total payments for the course
//        BigDecimal totalPayments = calculateTotalPayments(course);
//
//        // Display the calculated statistics
//        System.out.println("Course Statistics for " + course.getCourseName());
//        System.out.println("Total Enrollments: " + totalEnrollments);
//        System.out.println("Total Payments: $" + totalPayments);
//
//   }
    
        // Additional helper method to calculate total payments for a course
//    private BigDecimal calculateTotalPayments(Course course) {
//        BigDecimal totalPayments = BigDecimal.ZERO;
//
//        // Retrieve payment records for the specified course
//        List<Enrollment> enrollments = course.getEnrollments();
//
//        for (Enrollment enrollment : enrollments) {
//            List<Payment> payments = enrollment.getPayments();
//            for (Payment payment : payments) {
//                totalPayments = totalPayments.add(payment.getAmount());
//            }
//        }
//
//        return totalPayments;
//    }
    
    
     // Helper methods for checking existence in the database

    private boolean isStudentExists(int studentId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM Students WHERE student_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean isCourseExists(int courseId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM Courses WHERE course_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, courseId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean isEnrollmentExists(int studentId, int courseId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM Enrollments WHERE student_id = ? AND course_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, courseId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
        private boolean isTeacherExists(int teacherId, Connection connection) throws SQLException {
        String sql = "SELECT * FROM Teacher WHERE teacher_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, teacherId);
         
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
