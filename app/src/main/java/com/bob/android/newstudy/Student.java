package com.bob.android.newstudy;

import java.io.Serializable;

/**
 * @package com.bob.android.newstudy
 * @fileName Student
 * @Author Bob on 2018/5/11 22:26.
 * @Describe TODO
 */

public class Student implements Serializable{

    private String studentName;
    private String age;
    private String name;


    public Student(){

    }

    public Student(String studentName, String age) {
        this.studentName = studentName;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
