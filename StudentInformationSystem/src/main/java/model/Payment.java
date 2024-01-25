/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.math.BigDecimal;
import java.util.Date;
//import java.util.logging.Logger;
/**
 *
 * @author Abhi
 */
public class Payment {
        
    private int paymentId;
    private int studentId;
    private BigDecimal amount;
    private Date paymentDate;
    private Student student;

    public Payment(BigDecimal amount, Date paymentDate, Student student) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.student = student;
    }
   public Payment(Date paymentDate, Student student, int paymenttId, BigDecimal amount ) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.student = student;
    }
    public Payment(int studentId, BigDecimal amount, Date paymentDate, Student student) {
        this.studentId = studentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.student = student;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public Student getStudent() {
        return student;
    }
 
}
