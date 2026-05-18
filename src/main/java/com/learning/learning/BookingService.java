package com.learning.learning;

public class BookingService {
    private EmailServices emailServices;

    // create constructor function and pass dependency
    public BookingService(EmailServices emailServices){
        this.emailServices = emailServices;
    }

    public void bookEvent(){
        // var emailService = new EmailService();
        emailServices.sendEmail("mimi@gmail.com");
    }
}
