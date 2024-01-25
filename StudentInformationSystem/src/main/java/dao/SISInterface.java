/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import model.Course;
import model.Student;
import model.Teacher;
import model.Payment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
* @author Abhi
*
*/
public interface SISInterface {
        
    void enrollStudentInCourse(int studentID, int courseID, Date enrollmentDate);
  
    void assignTeacherToCourse(int teacherID, int courseID);
    
    void recordPayment(int studentID, BigDecimal amount, Date paymentDate);
    
    List<Student> generateEnrollmentReport(int courseId);
    
    List<Payment> generatePaymentReport(int studentId);
    
    //void calculateCourseStatistics(Course course);
}
