package dao;

import model.Payment;
import model.Student;
import exception.StudentNotFoundException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import util.DBConnUtil;

public class PaymentDao {

 private Connection connection = DBConnUtil.getConnection()  ; 

    public Student getStudent(int paymentId) {
        try {
            // Retrieve student associated with the payment
            String sql = "SELECT Students.* FROM Students " +
                    "INNER JOIN Payments ON Students.student_id = Payments.student_id " +
                    "WHERE Payments.payment_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, paymentId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int studentId = resultSet.getInt("student_id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        Date dateOfBirth = resultSet.getDate("date_of_birth");
                        String email = resultSet.getString("email");
                        String phoneNumber = resultSet.getString("phone_number");
                       
                        return new Student(studentId, firstName, lastName, dateOfBirth, email, phoneNumber);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    public BigDecimal getPaymentAmount(int paymentId) {
        try {
            // Retrieve payment amount
            String sql = "SELECT amount FROM Payments WHERE payment_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, paymentId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getBigDecimal("amount");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    public Date getPaymentDate(int paymentId) {
        try {
            // Retrieve payment date
            String sql = "SELECT payment_date FROM Payments WHERE payment_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, paymentId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDate("payment_date");
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
}
