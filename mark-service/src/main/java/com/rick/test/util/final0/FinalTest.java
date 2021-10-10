package com.rick.test.util.final0;

public class FinalTest {

    public static final int aFinal = 2;

    public static int b = 3;

    public static void main(String[] args) {

        b = aFinal + b;
        System.out.println(b);
        System.out.println(aFinal);

        Student student = new Student();
        student.setName("小明");
        student.setAge(20);
        String stu = getStudent(student);
        System.out.println(stu);
    }

    public static String getStudent(final Student student) {
        return student.getName() + " : " + student.getAge();
    }

}
