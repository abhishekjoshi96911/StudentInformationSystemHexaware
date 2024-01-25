/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 *
 * @author Abhi
 */
public class DuplicateEnrollmentException extends Exception {
    
//    String message ;
//
//    public DuplicateEnrollmentException(String message) {
//        this.message = message;
//    }
//
//    @Override
//    public String toString() {
//        return "DuplicateEnrollmentException{" + "message=" + message + '}';
//    }
        public DuplicateEnrollmentException(String message) {
        super(message);
    }
}
