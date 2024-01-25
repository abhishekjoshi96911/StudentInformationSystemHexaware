/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package main;

import dao.SISImplementation;
import dao.StudentDao;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;
import model.* ;
import java.text.SimpleDateFormat;
import java.util.List;
/**
 *
 * @author Abhi
 */
public class StudentInformationSystem {

     private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SISImplementation sisImplementation = new SISImplementation();

        while (true) {
            System.out.println("Main Menu:");
            System.out.println("1. Update Student Information");
            System.out.println("2. Enroll Student in a Course");
            System.out.println("3. Assign Teacher to a Course");
            System.out.println("4. Record Payment");
            System.out.println("5. Generate Enrollment Report");
            System.out.println("6. Generate Payment Report");
            System.out.println("7. Exit");

            System.out.print("Enter your choice (1-7): ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    updateStudentInformation(sisImplementation);
                    break;
                case 2:
                    enrollStudentInCourse(sisImplementation);
                    break;
                case 3:
                    assignTeacherToCourse(sisImplementation);
                    break;
                case 4:
                    recordPayment(sisImplementation);
                    break;
                case 5:
                    generateEnrollmentReport(sisImplementation);
                    break;
                case 6:
                    generatePaymentReport(sisImplementation);
                    break;
                case 7:
                    System.out.println("Exiting the program.");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 7.");
            }
        }
    }

    private static void updateStudentInformation(SISImplementation sisImplementation) {
        try {
            System.out.print("Enter student ID: ");
            int studentId = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            System.out.print("Enter new first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter new last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter new date of birth (yyyy-MM-dd): ");
            Date dateOfBirth =new SimpleDateFormat("yyyy/dd/MM").parse(scanner.nextLine());

            System.out.print("Enter new email: ");
            String email = scanner.nextLine();

            System.out.print("Enter new phone number: ");
            String phoneNumber = scanner.nextLine();

            //sisImplementation.updateStudentInfo(new Student(studentId, firstName, lastName, dateOfBirth, email, phoneNumber));
            Student student= new Student(studentId, firstName, lastName, dateOfBirth, email, phoneNumber) ;
            StudentDao studentDao = new StudentDao() ;
            studentDao.updateStudentInfo(student, firstName, lastName, dateOfBirth, email, phoneNumber);
             SISImplementation sis = new SISImplementation() ;
             
             
            System.out.println("Student information updated successfully.");
        } catch (Exception e) {
            System.out.println("Error: Invalid date format. Please enter the date in yyyy-MM-dd format.");
        }
    }

    private static void enrollStudentInCourse(SISImplementation sisImplementation) {
         try {
            System.out.print("Enter student ID: ");
            int studentId = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            System.out.print("Enter course ID: ");
            int courseId = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            System.out.print("Enter enrollment date (yyyy-MM-dd): ");
            String date = scanner.nextLine();
            Date enrollmentDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            
             SISImplementation sis = new SISImplementation() ;
             sis.enrollStudentInCourse(studentId, courseId, enrollmentDate);

            //sisImplementation.enrollStudentInCourse(new Student(studentId), new Course(courseId), enrollmentDate);
            System.out.println("Student enrolled in the course successfully.");
        }catch (Exception e) {
            System.out.println("Error: Invalid date format. Please enter the date in yyyy-MM-dd format.");
        }
    }

    private static void assignTeacherToCourse(SISImplementation sisImplementation) {
 try {
            System.out.print("Enter teacher ID: ");
            int teacherId = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            System.out.print("Enter course ID: ");
            int courseId = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

             SISImplementation sis = new SISImplementation() ;
                    sis.assignTeacherToCourse(teacherId,courseId);
            System.out.println("Teacher assigned to the course successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void recordPayment(SISImplementation sisImplementation) {
try {
            System.out.print("Enter student ID: ");
            int studentId = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            System.out.print("Enter payment amount: ");
            BigDecimal amount = scanner.nextBigDecimal();
            scanner.nextLine();  // Consume the newline character

            System.out.print("Enter payment date (yyyy-MM-dd): ");
              String date = scanner.nextLine();
            Date PaymentDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);

            sisImplementation.recordPayment(studentId, amount, PaymentDate);
            System.out.println("Payment recorded successfully.");
            
        } catch (ParseException e) {
            System.out.println("Error: Invalid date format. Please enter the date in yyyy-MM-dd format.");
        }
    }

    private static void generateEnrollmentReport(SISImplementation sisImplementation) {
              try {
            System.out.print("Enter course ID: ");
            int courseId = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            List<Student> enrolledStudents = sisImplementation.generateEnrollmentReport(courseId);
            System.out.println("Enrolled Students in Course:");
            for (Student student : enrolledStudents) {
                System.out.println(student);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
}

    private static void generatePaymentReport(SISImplementation sisImplementation) {
           try {
            System.out.print("Enter student ID: ");
            int studentId = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            List<Payment> payments = sisImplementation.generatePaymentReport(studentId);
            System.out.println("Payments for Student:");
            for (Payment payment : payments) {
                System.out.println(payment);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
