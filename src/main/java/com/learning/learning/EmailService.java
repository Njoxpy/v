package com.learning.learning;

public class EmailService implements EmailServices {
    @Override
    public void sendEmail(String email){
        System.out.println("EMAIL SERVICE");
        System.out.println("Email: " + email);
    }
}
