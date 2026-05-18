package com.learning.learning;

public class AzampayPaymentService implements PaymentService {
    @Override
    public void processPayment(double amount){
        System.out.println("AZAM PAY PAYMENT");

        System.out.println("amount: " + amount);
    }
}
