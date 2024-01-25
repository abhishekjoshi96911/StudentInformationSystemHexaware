/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import exception.InvalidTeacherDataException;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author Abhi
 */
public class Teacher {
  
    private int teacherId;
    private String firstName;
    private String lastName;
    private String email;
   // private List<Course> assignedCourses;

    public Teacher(int teacherId, String firstName, String lastName, String email) {
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }



    public Teacher(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    

    public int getTeacherId() {
        return teacherId;
    }
    
    
}
