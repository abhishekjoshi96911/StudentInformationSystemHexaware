/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Abhi
 */
public class Course {
    
    private  int  courseId;
    private String courseName ;
    private Teacher teacher;
   


    public Course(int courseId, String courseName, Teacher teacher) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.teacher = teacher;
    //    this.enrollments = enrollments;
    }

    public Course(int courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }
    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    

}
